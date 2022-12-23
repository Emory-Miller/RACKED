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
import { IRating } from 'app/shared/model/rating.model';
import { StarRating } from 'app/shared/model/enumerations/star-rating.model';
import { getEntity, updateEntity, createEntity, reset } from './rating.reducer';

export const RatingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reviews = useAppSelector(state => state.review.entities);
  const ratingEntity = useAppSelector(state => state.rating.entity);
  const loading = useAppSelector(state => state.rating.loading);
  const updating = useAppSelector(state => state.rating.updating);
  const updateSuccess = useAppSelector(state => state.rating.updateSuccess);
  const starRatingValues = Object.keys(StarRating);

  const handleClose = () => {
    navigate('/rating');
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
      ...ratingEntity,
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
          starRating: 'ONE',
          ...ratingEntity,
          review: ratingEntity?.review?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rackedApp.rating.home.createOrEditLabel" data-cy="RatingCreateUpdateHeading">
            <Translate contentKey="rackedApp.rating.home.createOrEditLabel">Create or edit a Rating</Translate>
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
                  id="rating-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rackedApp.rating.attribute')}
                id="rating-attribute"
                name="attribute"
                data-cy="attribute"
                type="text"
              />
              <ValidatedField
                label={translate('rackedApp.rating.starRating')}
                id="rating-starRating"
                name="starRating"
                data-cy="starRating"
                type="select"
              >
                {starRatingValues.map(starRating => (
                  <option value={starRating} key={starRating}>
                    {translate('rackedApp.StarRating.' + starRating)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="rating-review" name="review" data-cy="review" label={translate('rackedApp.rating.review')} type="select">
                <option value="" key="0" />
                {reviews
                  ? reviews.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rating" replace color="info">
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

export default RatingUpdate;
