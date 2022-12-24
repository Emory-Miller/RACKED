package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.Rack;

/**
 * Spring Data JPA repository for the Rack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {}
