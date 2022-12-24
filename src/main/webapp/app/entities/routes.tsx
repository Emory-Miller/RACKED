import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Rack from './rack';
import Review from './review';
import Rating from './rating';
import Amenity from './amenity';
import Location from './location';
import Image from './image';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="rack/*" element={<Rack />} />
        <Route path="review/*" element={<Review />} />
        <Route path="rating/*" element={<Rating />} />
        <Route path="amenity/*" element={<Amenity />} />
        <Route path="location/*" element={<Location />} />
        <Route path="image/*" element={<Image />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
