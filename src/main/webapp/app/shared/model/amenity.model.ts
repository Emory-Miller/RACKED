import { IReview } from 'app/shared/model/review.model';

export interface IAmenity {
  id?: number;
  name?: string | null;
  review?: IReview | null;
}

export const defaultValue: Readonly<IAmenity> = {};
