import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/rack">
        <Translate contentKey="global.menu.entities.rack" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/review">
        <Translate contentKey="global.menu.entities.review" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/rating">
        <Translate contentKey="global.menu.entities.rating" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/amenity">
        <Translate contentKey="global.menu.entities.amenity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/location">
        <Translate contentKey="global.menu.entities.location" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/image">
        <Translate contentKey="global.menu.entities.image" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
