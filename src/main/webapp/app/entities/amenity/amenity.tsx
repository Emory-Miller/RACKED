import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAmenity } from 'app/shared/model/amenity.model';
import { getEntities } from './amenity.reducer';

export const Amenity = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const amenityList = useAppSelector(state => state.amenity.entities);
  const loading = useAppSelector(state => state.amenity.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="amenity-heading" data-cy="AmenityHeading">
        <Translate contentKey="rackedApp.amenity.home.title">Amenities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rackedApp.amenity.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/amenity/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rackedApp.amenity.home.createLabel">Create new Amenity</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {amenityList && amenityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="rackedApp.amenity.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.amenity.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.amenity.review">Review</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {amenityList.map((amenity, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/amenity/${amenity.id}`} color="link" size="sm">
                      {amenity.id}
                    </Button>
                  </td>
                  <td>{amenity.name}</td>
                  <td>{amenity.review ? <Link to={`/review/${amenity.review.id}`}>{amenity.review.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/amenity/${amenity.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/amenity/${amenity.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/amenity/${amenity.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="rackedApp.amenity.home.notFound">No Amenities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Amenity;
