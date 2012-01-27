package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAICitizen;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMoveSpeed;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;

public class CitizenFactory extends AFactory{
	
	private static final double SPEED = 10.0;
	
	public CitizenFactory(EntityManager entityManager,
			MapInformationUtilities mapInformationUtilities) {
		super(entityManager, mapInformationUtilities);
	}


	@Override
	public Entity getEntity() {
		Entity entity = new Entity();

		entity.setCurrentPosition(getRandomSpawnPosition());
		entity.setExist(true);
		entity.setIa(new CAICitizen(entity, getEntityManager()));
		entity.setMarker(new CMarker(CMarker.CITIZEN_MARKER));
		entity.setMoveSpeed(new CMoveSpeed(SPEED
				* (getMapInformationUtilities().getZoomLevelMin() 
						- getMapInformationUtilities().getZoomLevel() + 2)));

		return entity;
	}

	/**
	 * Return a random spawn position on the map.
	 * 
	 * @return a random position.
	 */
	private CCoordinates getRandomSpawnPosition() {
		return getRandomPosition();
	}

	@Override
	public void destroyEntity() {
		// NOTHING
	}

	
}
