package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.Amenity;

/**
 * Spring Data JPA repository for the Amenity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {}
