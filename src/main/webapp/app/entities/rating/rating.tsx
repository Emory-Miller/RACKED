import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRating } from 'app/shared/model/rating.model';
import { getEntities } from './rating.reducer';

export const Rating = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const ratingList = useAppSelector(state => state.rating.entities);
  const loading = useAppSelector(state => state.rating.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="rating-heading" data-cy="RatingHeading">
        <Translate contentKey="rackedApp.rating.home.title">Ratings</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rackedApp.rating.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/rating/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rackedApp.rating.home.createLabel">Create new Rating</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ratingList && ratingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="rackedApp.rating.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.rating.attribute">Attribute</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.rating.starRating">Star Rating</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.rating.review">Review</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ratingList.map((rating, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/rating/${rating.id}`} color="link" size="sm">
                      {rating.id}
                    </Button>
                  </td>
                  <td>{rating.attribute}</td>
                  <td>
                    <Translate contentKey={`rackedApp.StarRating.${rating.starRating}`} />
                  </td>
                  <td>{rating.review ? <Link to={`/review/${rating.review.id}`}>{rating.review.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/rating/${rating.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/rating/${rating.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/rating/${rating.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="rackedApp.rating.home.notFound">No Ratings found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Rating;
