package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Review.
 */
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "review")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "review" }, allowSetters = true)
    private Set<Amenity> amenities = new HashSet<>();

    @OneToMany(mappedBy = "review")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "review" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    @JsonIgnoreProperties(value = { "review", "rack" }, allowSetters = true)
    @OneToOne(mappedBy = "review")
    private Image image;

    @ManyToOne
    @JsonIgnoreProperties(value = { "images", "reviews", "location" }, allowSetters = true)
    private Rack rack;

    @ManyToOne
    @JsonIgnoreProperties(value = { "racks", "reviews" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Review id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Amenity> getAmenities() {
        return this.amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        if (this.amenities != null) {
            this.amenities.forEach(i -> i.setReview(null));
        }
        if (amenities != null) {
            amenities.forEach(i -> i.setReview(this));
        }
        this.amenities = amenities;
    }

    public Review amenities(Set<Amenity> amenities) {
        this.setAmenities(amenities);
        return this;
    }

    public Review addAmenity(Amenity amenity) {
        this.amenities.add(amenity);
        amenity.setReview(this);
        return this;
    }

    public Review removeAmenity(Amenity amenity) {
        this.amenities.remove(amenity);
        amenity.setReview(null);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        if (this.ratings != null) {
            this.ratings.forEach(i -> i.setReview(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setReview(this));
        }
        this.ratings = ratings;
    }

    public Review ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public Review addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setReview(this);
        return this;
    }

    public Review removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.setReview(null);
        return this;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        if (this.image != null) {
            this.image.setReview(null);
        }
        if (image != null) {
            image.setReview(this);
        }
        this.image = image;
    }

    public Review image(Image image) {
        this.setImage(image);
        return this;
    }

    public Rack getRack() {
        return this.rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public Review rack(Rack rack) {
        this.setRack(rack);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Review location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        return id != null && id.equals(((Review) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            "}";
    }
}
