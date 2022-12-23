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
import rocks.zipcode.domain.Amenity;
import rocks.zipcode.repository.AmenityRepository;

/**
 * Integration tests for the {@link AmenityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AmenityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/amenities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmenityMockMvc;

    private Amenity amenity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Amenity createEntity(EntityManager em) {
        Amenity amenity = new Amenity().name(DEFAULT_NAME);
        return amenity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Amenity createUpdatedEntity(EntityManager em) {
        Amenity amenity = new Amenity().name(UPDATED_NAME);
        return amenity;
    }

    @BeforeEach
    public void initTest() {
        amenity = createEntity(em);
    }

    @Test
    @Transactional
    void createAmenity() throws Exception {
        int databaseSizeBeforeCreate = amenityRepository.findAll().size();
        // Create the Amenity
        restAmenityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amenity)))
            .andExpect(status().isCreated());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeCreate + 1);
        Amenity testAmenity = amenityList.get(amenityList.size() - 1);
        assertThat(testAmenity.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createAmenityWithExistingId() throws Exception {
        // Create the Amenity with an existing ID
        amenity.setId(1L);

        int databaseSizeBeforeCreate = amenityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmenityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amenity)))
            .andExpect(status().isBadRequest());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAmenities() throws Exception {
        // Initialize the database
        amenityRepository.saveAndFlush(amenity);

        // Get all the amenityList
        restAmenityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amenity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAmenity() throws Exception {
        // Initialize the database
        amenityRepository.saveAndFlush(amenity);

        // Get the amenity
        restAmenityMockMvc
            .perform(get(ENTITY_API_URL_ID, amenity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amenity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAmenity() throws Exception {
        // Get the amenity
        restAmenityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAmenity() throws Exception {
        // Initialize the database
        amenityRepository.saveAndFlush(amenity);

        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();

        // Update the amenity
        Amenity updatedAmenity = amenityRepository.findById(amenity.getId()).get();
        // Disconnect from session so that the updates on updatedAmenity are not directly saved in db
        em.detach(updatedAmenity);
        updatedAmenity.name(UPDATED_NAME);

        restAmenityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAmenity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAmenity))
            )
            .andExpect(status().isOk());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
        Amenity testAmenity = amenityList.get(amenityList.size() - 1);
        assertThat(testAmenity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAmenity() throws Exception {
        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();
        amenity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmenityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amenity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amenity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAmenity() throws Exception {
        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();
        amenity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmenityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amenity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAmenity() throws Exception {
        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();
        amenity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmenityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amenity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAmenityWithPatch() throws Exception {
        // Initialize the database
        amenityRepository.saveAndFlush(amenity);

        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();

        // Update the amenity using partial update
        Amenity partialUpdatedAmenity = new Amenity();
        partialUpdatedAmenity.setId(amenity.getId());

        restAmenityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmenity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmenity))
            )
            .andExpect(status().isOk());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
        Amenity testAmenity = amenityList.get(amenityList.size() - 1);
        assertThat(testAmenity.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAmenityWithPatch() throws Exception {
        // Initialize the database
        amenityRepository.saveAndFlush(amenity);

        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();

        // Update the amenity using partial update
        Amenity partialUpdatedAmenity = new Amenity();
        partialUpdatedAmenity.setId(amenity.getId());

        partialUpdatedAmenity.name(UPDATED_NAME);

        restAmenityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmenity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmenity))
            )
            .andExpect(status().isOk());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
        Amenity testAmenity = amenityList.get(amenityList.size() - 1);
        assertThat(testAmenity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAmenity() throws Exception {
        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();
        amenity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmenityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, amenity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amenity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAmenity() throws Exception {
        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();
        amenity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmenityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amenity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAmenity() throws Exception {
        int databaseSizeBeforeUpdate = amenityRepository.findAll().size();
        amenity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmenityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(amenity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Amenity in the database
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAmenity() throws Exception {
        // Initialize the database
        amenityRepository.saveAndFlush(amenity);

        int databaseSizeBeforeDelete = amenityRepository.findAll().size();

        // Delete the amenity
        restAmenityMockMvc
            .perform(delete(ENTITY_API_URL_ID, amenity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Amenity> amenityList = amenityRepository.findAll();
        assertThat(amenityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
