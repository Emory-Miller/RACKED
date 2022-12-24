import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './amenity.reducer';

export const AmenityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const amenityEntity = useAppSelector(state => state.amenity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="amenityDetailsHeading">
          <Translate contentKey="rackedApp.amenity.detail.title">Amenity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{amenityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rackedApp.amenity.name">Name</Translate>
            </span>
          </dt>
          <dd>{amenityEntity.name}</dd>
          <dt>
            <Translate contentKey="rackedApp.amenity.review">Review</Translate>
          </dt>
          <dd>{amenityEntity.review ? amenityEntity.review.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/amenity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/amenity/${amenityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AmenityDetail;
