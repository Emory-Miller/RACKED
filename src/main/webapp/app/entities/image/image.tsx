import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IImage } from 'app/shared/model/image.model';
import { getEntities } from './image.reducer';

export const Image = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const imageList = useAppSelector(state => state.image.entities);
  const loading = useAppSelector(state => state.image.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="image-heading" data-cy="ImageHeading">
        <Translate contentKey="rackedApp.image.home.title">Images</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rackedApp.image.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/image/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rackedApp.image.home.createLabel">Create new Image</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {imageList && imageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="rackedApp.image.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.image.imageLocation">Image Location</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.image.image">Image</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.image.review">Review</Translate>
                </th>
                <th>
                  <Translate contentKey="rackedApp.image.rack">Rack</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {imageList.map((image, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/image/${image.id}`} color="link" size="sm">
                      {image.id}
                    </Button>
                  </td>
                  <td>{image.imageLocation}</td>
                  <td>
                    {image.image ? (
                      <div>
                        {image.imageContentType ? (
                          <a onClick={openFile(image.imageContentType, image.image)}>
                            <img src={`data:${image.imageContentType};base64,${image.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {image.imageContentType}, {byteSize(image.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{image.review ? <Link to={`/review/${image.review.id}`}>{image.review.id}</Link> : ''}</td>
                  <td>{image.rack ? <Link to={`/rack/${image.rack.id}`}>{image.rack.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/image/${image.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/image/${image.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/image/${image.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="rackedApp.image.home.notFound">No Images found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Image;
