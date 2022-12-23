package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class AmenityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Amenity.class);
        Amenity amenity1 = new Amenity();
        amenity1.setId(1L);
        Amenity amenity2 = new Amenity();
        amenity2.setId(amenity1.getId());
        assertThat(amenity1).isEqualTo(amenity2);
        amenity2.setId(2L);
        assertThat(amenity1).isNotEqualTo(amenity2);
        amenity1.setId(null);
        assertThat(amenity1).isNotEqualTo(amenity2);
    }
}
