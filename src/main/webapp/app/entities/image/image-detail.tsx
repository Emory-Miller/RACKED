import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './image.reducer';

export const ImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const imageEntity = useAppSelector(state => state.image.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imageDetailsHeading">
          <Translate contentKey="rackedApp.image.detail.title">Image</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{imageEntity.id}</dd>
          <dt>
            <span id="imageLocation">
              <Translate contentKey="rackedApp.image.imageLocation">Image Location</Translate>
            </span>
          </dt>
          <dd>{imageEntity.imageLocation}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="rackedApp.image.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {imageEntity.image ? (
              <div>
                {imageEntity.imageContentType ? (
                  <a onClick={openFile(imageEntity.imageContentType, imageEntity.image)}>
                    <img src={`data:${imageEntity.imageContentType};base64,${imageEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {imageEntity.imageContentType}, {byteSize(imageEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="rackedApp.image.review">Review</Translate>
          </dt>
          <dd>{imageEntity.review ? imageEntity.review.id : ''}</dd>
          <dt>
            <Translate contentKey="rackedApp.image.rack">Rack</Translate>
          </dt>
          <dd>{imageEntity.rack ? imageEntity.rack.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/image/${imageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImageDetail;
