package rocks.zipcode.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.domain.Amenity;
import rocks.zipcode.repository.AmenityRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.Amenity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AmenityResource {

    private final Logger log = LoggerFactory.getLogger(AmenityResource.class);

    private static final String ENTITY_NAME = "amenity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AmenityRepository amenityRepository;

    public AmenityResource(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    /**
     * {@code POST  /amenities} : Create a new amenity.
     *
     * @param amenity the amenity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new amenity, or with status {@code 400 (Bad Request)} if the amenity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amenities")
    public ResponseEntity<Amenity> createAmenity(@RequestBody Amenity amenity) throws URISyntaxException {
        log.debug("REST request to save Amenity : {}", amenity);
        if (amenity.getId() != null) {
            throw new BadRequestAlertException("A new amenity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Amenity result = amenityRepository.save(amenity);
        return ResponseEntity
            .created(new URI("/api/amenities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amenities/:id} : Updates an existing amenity.
     *
     * @param id the id of the amenity to save.
     * @param amenity the amenity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amenity,
     * or with status {@code 400 (Bad Request)} if the amenity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the amenity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amenities/{id}")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable(value = "id", required = false) final Long id, @RequestBody Amenity amenity)
        throws URISyntaxException {
        log.debug("REST request to update Amenity : {}, {}", id, amenity);
        if (amenity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amenity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amenityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Amenity result = amenityRepository.save(amenity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amenity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /amenities/:id} : Partial updates given fields of an existing amenity, field will ignore if it is null
     *
     * @param id the id of the amenity to save.
     * @param amenity the amenity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amenity,
     * or with status {@code 400 (Bad Request)} if the amenity is not valid,
     * or with status {@code 404 (Not Found)} if the amenity is not found,
     * or with status {@code 500 (Internal Server Error)} if the amenity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/amenities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Amenity> partialUpdateAmenity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Amenity amenity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Amenity partially : {}, {}", id, amenity);
        if (amenity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amenity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amenityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Amenity> result = amenityRepository
            .findById(amenity.getId())
            .map(existingAmenity -> {
                if (amenity.getName() != null) {
                    existingAmenity.setName(amenity.getName());
                }

                return existingAmenity;
            })
            .map(amenityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amenity.getId().toString())
        );
    }

    /**
     * {@code GET  /amenities} : get all the amenities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amenities in body.
     */
    @GetMapping("/amenities")
    public List<Amenity> getAllAmenities() {
        log.debug("REST request to get all Amenities");
        return amenityRepository.findAll();
    }

    /**
     * {@code GET  /amenities/:id} : get the "id" amenity.
     *
     * @param id the id of the amenity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amenity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amenities/{id}")
    public ResponseEntity<Amenity> getAmenity(@PathVariable Long id) {
        log.debug("REST request to get Amenity : {}", id);
        Optional<Amenity> amenity = amenityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(amenity);
    }

    /**
     * {@code DELETE  /amenities/:id} : delete the "id" amenity.
     *
     * @param id the id of the amenity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amenities/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        log.debug("REST request to delete Amenity : {}", id);
        amenityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
