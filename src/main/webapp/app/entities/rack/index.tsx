import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Rack from './rack';
import RackDetail from './rack-detail';
import RackUpdate from './rack-update';
import RackDeleteDialog from './rack-delete-dialog';

const RackRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Rack />} />
    <Route path="new" element={<RackUpdate />} />
    <Route path=":id">
      <Route index element={<RackDetail />} />
      <Route path="edit" element={<RackUpdate />} />
      <Route path="delete" element={<RackDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RackRoutes;
