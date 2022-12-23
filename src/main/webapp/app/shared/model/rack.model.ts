import { IImage } from 'app/shared/model/image.model';
import { IReview } from 'app/shared/model/review.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IRack {
  id?: number;
  images?: IImage[] | null;
  reviews?: IReview[] | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<IRack> = {};
