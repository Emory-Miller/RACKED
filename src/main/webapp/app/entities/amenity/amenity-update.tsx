import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReview } from 'app/shared/model/review.model';
import { getEntities as getReviews } from 'app/entities/review/review.reducer';
import { IAmenity } from 'app/shared/model/amenity.model';
import { getEntity, updateEntity, createEntity, reset } from './amenity.reducer';

export const AmenityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reviews = useAppSelector(state => state.review.entities);
  const amenityEntity = useAppSelector(state => state.amenity.entity);
  const loading = useAppSelector(state => state.amenity.loading);
  const updating = useAppSelector(state => state.amenity.updating);
  const updateSuccess = useAppSelector(state => state.amenity.updateSuccess);

  const handleClose = () => {
    navigate('/amenity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReviews({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...amenityEntity,
      ...values,
      review: reviews.find(it => it.id.toString() === values.review.toString()),
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
          ...amenityEntity,
          review: amenityEntity?.review?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rackedApp.amenity.home.createOrEditLabel" data-cy="AmenityCreateUpdateHeading">
            <Translate contentKey="rackedApp.amenity.home.createOrEditLabel">Create or edit a Amenity</Translate>
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
                  id="amenity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('rackedApp.amenity.name')} id="amenity-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                id="amenity-review"
                name="review"
                data-cy="review"
                label={translate('rackedApp.amenity.review')}
                type="select"
              >
                <option value="" key="0" />
                {reviews
                  ? reviews.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/amenity" replace color="info">
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

export default AmenityUpdate;
