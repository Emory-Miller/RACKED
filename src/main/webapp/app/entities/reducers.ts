import rack from 'app/entities/rack/rack.reducer';
import review from 'app/entities/review/review.reducer';
import rating from 'app/entities/rating/rating.reducer';
import amenity from 'app/entities/amenity/amenity.reducer';
import location from 'app/entities/location/location.reducer';
import image from 'app/entities/image/image.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  rack,
  review,
  rating,
  amenity,
  location,
  image,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
