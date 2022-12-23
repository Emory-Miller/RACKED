import { IAmenity } from 'app/shared/model/amenity.model';
import { IRating } from 'app/shared/model/rating.model';
import { IImage } from 'app/shared/model/image.model';
import { IRack } from 'app/shared/model/rack.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IReview {
  id?: number;
  amenities?: IAmenity[] | null;
  ratings?: IRating[] | null;
  image?: IImage | null;
  rack?: IRack | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<IReview> = {};
