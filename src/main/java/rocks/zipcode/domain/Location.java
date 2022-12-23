package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "distance_from_user")
    private Integer distanceFromUser;

    @Column(name = "gps_coordinates")
    private String gpsCoordinates;

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "reviews", "location" }, allowSetters = true)
    private Set<Rack> racks = new HashSet<>();

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "amenities", "ratings", "image", "rack", "location" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDistanceFromUser() {
        return this.distanceFromUser;
    }

    public Location distanceFromUser(Integer distanceFromUser) {
        this.setDistanceFromUser(distanceFromUser);
        return this;
    }

    public void setDistanceFromUser(Integer distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    public String getGpsCoordinates() {
        return this.gpsCoordinates;
    }

    public Location gpsCoordinates(String gpsCoordinates) {
        this.setGpsCoordinates(gpsCoordinates);
        return this;
    }

    public void setGpsCoordinates(String gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }

    public Set<Rack> getRacks() {
        return this.racks;
    }

    public void setRacks(Set<Rack> racks) {
        if (this.racks != null) {
            this.racks.forEach(i -> i.setLocation(null));
        }
        if (racks != null) {
            racks.forEach(i -> i.setLocation(this));
        }
        this.racks = racks;
    }

    public Location racks(Set<Rack> racks) {
        this.setRacks(racks);
        return this;
    }

    public Location addRack(Rack rack) {
        this.racks.add(rack);
        rack.setLocation(this);
        return this;
    }

    public Location removeRack(Rack rack) {
        this.racks.remove(rack);
        rack.setLocation(null);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setLocation(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setLocation(this));
        }
        this.reviews = reviews;
    }

    public Location reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public Location addReview(Review review) {
        this.reviews.add(review);
        review.setLocation(this);
        return this;
    }

    public Location removeReview(Review review) {
        this.reviews.remove(review);
        review.setLocation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", distanceFromUser=" + getDistanceFromUser() +
            ", gpsCoordinates='" + getGpsCoordinates() + "'" +
            "}";
    }
}
