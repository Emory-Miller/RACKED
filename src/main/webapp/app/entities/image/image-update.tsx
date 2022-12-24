import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReview } from 'app/shared/model/review.model';
import { getEntities as getReviews } from 'app/entities/review/review.reducer';
import { IRack } from 'app/shared/model/rack.model';
import { getEntities as getRacks } from 'app/entities/rack/rack.reducer';
import { IImage } from 'app/shared/model/image.model';
import { getEntity, updateEntity, createEntity, reset } from './image.reducer';

export const ImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reviews = useAppSelector(state => state.review.entities);
  const racks = useAppSelector(state => state.rack.entities);
  const imageEntity = useAppSelector(state => state.image.entity);
  const loading = useAppSelector(state => state.image.loading);
  const updating = useAppSelector(state => state.image.updating);
  const updateSuccess = useAppSelector(state => state.image.updateSuccess);

  const handleClose = () => {
    navigate('/image');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReviews({}));
    dispatch(getRacks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...imageEntity,
      ...values,
      review: reviews.find(it => it.id.toString() === values.review.toString()),
      rack: racks.find(it => it.id.toString() === values.rack.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...imageEntity,
          review: imageEntity?.review?.id,
          rack: imageEntity?.rack?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rackedApp.image.home.createOrEditLabel" data-cy="ImageCreateUpdateHeading">
            <Translate contentKey="rackedApp.image.home.createOrEditLabel">Create or edit a Image</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rackedApp.image.imageLocation')}
                id="image-imageLocation"
                name="imageLocation"
                data-cy="imageLocation"
                type="text"
              />
              <ValidatedBlobField
                label={translate('rackedApp.image.image')}
                id="image-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField id="image-review" name="review" data-cy="review" label={translate('rackedApp.image.review')} type="select">
                <option value="" key="0" />
                {reviews
                  ? reviews.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="image-rack" name="rack" data-cy="rack" label={translate('rackedApp.image.rack')} type="select">
                <option value="" key="0" />
                {racks
                  ? racks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/image" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ImageUpdate;
