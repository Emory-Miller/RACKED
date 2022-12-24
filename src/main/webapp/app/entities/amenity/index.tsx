import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Amenity from './amenity';
import AmenityDetail from './amenity-detail';
import AmenityUpdate from './amenity-update';
import AmenityDeleteDialog from './amenity-delete-dialog';

const AmenityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Amenity />} />
    <Route path="new" element={<AmenityUpdate />} />
    <Route path=":id">
      <Route index element={<AmenityDetail />} />
      <Route path="edit" element={<AmenityUpdate />} />
      <Route path="delete" element={<AmenityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AmenityRoutes;
