package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rack.
 */
@Entity
@Table(name = "rack")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "rack")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "review", "rack" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "rack")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "amenities", "ratings", "image", "rack", "location" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "racks", "reviews" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rack id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Image> getImages() {
        return this.images;
    }

    public void setImages(Set<Image> images) {
        if (this.images != null) {
            this.images.forEach(i -> i.setRack(null));
        }
        if (images != null) {
            images.forEach(i -> i.setRack(this));
        }
        this.images = images;
    }

    public Rack images(Set<Image> images) {
        this.setImages(images);
        return this;
    }

    public Rack addImage(Image image) {
        this.images.add(image);
        image.setRack(this);
        return this;
    }

    public Rack removeImage(Image image) {
        this.images.remove(image);
        image.setRack(null);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setRack(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setRack(this));
        }
        this.reviews = reviews;
    }

    public Rack reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public Rack addReview(Review review) {
        this.reviews.add(review);
        review.setRack(this);
        return this;
    }

    public Rack removeReview(Review review) {
        this.reviews.remove(review);
        review.setRack(null);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Rack location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rack)) {
            return false;
        }
        return id != null && id.equals(((Rack) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rack{" +
            "id=" + getId() +
            "}";
    }
}
