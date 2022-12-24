import { IReview } from 'app/shared/model/review.model';
import { StarRating } from 'app/shared/model/enumerations/star-rating.model';

export interface IRating {
  id?: number;
  attribute?: string | null;
  starRating?: StarRating | null;
  review?: IReview | null;
}

export const defaultValue: Readonly<IRating> = {};
