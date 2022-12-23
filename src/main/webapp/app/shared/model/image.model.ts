import { IReview } from 'app/shared/model/review.model';
import { IRack } from 'app/shared/model/rack.model';

export interface IImage {
  id?: number;
  imageLocation?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  review?: IReview | null;
  rack?: IRack | null;
}

export const defaultValue: Readonly<IImage> = {};
