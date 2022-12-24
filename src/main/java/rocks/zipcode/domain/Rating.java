package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import rocks.zipcode.domain.enumeration.StarRating;

/**
 * A Rating.
 */
@Entity
@Table(name = "rating")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "attribute")
    private String attribute;

    @Enumerated(EnumType.STRING)
    @Column(name = "star_rating")
    private StarRating starRating;

    @ManyToOne
    @JsonIgnoreProperties(value = { "amenities", "ratings", "image", "rack", "location" }, allowSetters = true)
    private Review review;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rating id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttribute() {
        return this.attribute;
    }

    public Rating attribute(String attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public StarRating getStarRating() {
        return this.starRating;
    }

    public Rating starRating(StarRating starRating) {
        this.setStarRating(starRating);
        return this;
    }

    public void setStarRating(StarRating starRating) {
        this.starRating = starRating;
    }

    public Review getReview() {
        return this.review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Rating review(Review review) {
        this.setReview(review);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rating)) {
            return false;
        }
        return id != null && id.equals(((Rating) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", attribute='" + getAttribute() + "'" +
            ", starRating='" + getStarRating() + "'" +
            "}";
    }
}
