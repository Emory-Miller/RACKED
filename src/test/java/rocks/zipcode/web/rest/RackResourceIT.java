package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.Rack;
import rocks.zipcode.repository.RackRepository;

/**
 * Integration tests for the {@link RackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RackResourceIT {

    private static final String ENTITY_API_URL = "/api/racks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RackRepository rackRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRackMockMvc;

    private Rack rack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rack createEntity(EntityManager em) {
        Rack rack = new Rack();
        return rack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rack createUpdatedEntity(EntityManager em) {
        Rack rack = new Rack();
        return rack;
    }

    @BeforeEach
    public void initTest() {
        rack = createEntity(em);
    }

    @Test
    @Transactional
    void createRack() throws Exception {
        int databaseSizeBeforeCreate = rackRepository.findAll().size();
        // Create the Rack
        restRackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isCreated());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeCreate + 1);
        Rack testRack = rackList.get(rackList.size() - 1);
    }

    @Test
    @Transactional
    void createRackWithExistingId() throws Exception {
        // Create the Rack with an existing ID
        rack.setId(1L);

        int databaseSizeBeforeCreate = rackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRacks() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList
        restRackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rack.getId().intValue())));
    }

    @Test
    @Transactional
    void getRack() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get the rack
        restRackMockMvc
            .perform(get(ENTITY_API_URL_ID, rack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rack.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRack() throws Exception {
        // Get the rack
        restRackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRack() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        int databaseSizeBeforeUpdate = rackRepository.findAll().size();

        // Update the rack
        Rack updatedRack = rackRepository.findById(rack.getId()).get();
        // Disconnect from session so that the updates on updatedRack are not directly saved in db
        em.detach(updatedRack);

        restRackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRack.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRack))
            )
            .andExpect(status().isOk());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
        Rack testRack = rackList.get(rackList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rack.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRackWithPatch() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        int databaseSizeBeforeUpdate = rackRepository.findAll().size();

        // Update the rack using partial update
        Rack partialUpdatedRack = new Rack();
        partialUpdatedRack.setId(rack.getId());

        restRackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRack))
            )
            .andExpect(status().isOk());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
        Rack testRack = rackList.get(rackList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateRackWithPatch() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        int databaseSizeBeforeUpdate = rackRepository.findAll().size();

        // Update the rack using partial update
        Rack partialUpdatedRack = new Rack();
        partialUpdatedRack.setId(rack.getId());

        restRackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRack))
            )
            .andExpect(status().isOk());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
        Rack testRack = rackList.get(rackList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRack() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        int databaseSizeBeforeDelete = rackRepository.findAll().size();

        // Delete the rack
        restRackMockMvc
            .perform(delete(ENTITY_API_URL_ID, rack.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
