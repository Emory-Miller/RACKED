import { IRack } from 'app/shared/model/rack.model';
import { IReview } from 'app/shared/model/review.model';

export interface ILocation {
  id?: number;
  distanceFromUser?: number | null;
  gpsCoordinates?: string | null;
  racks?: IRack[] | null;
  reviews?: IReview[] | null;
}

export const defaultValue: Readonly<ILocation> = {};
