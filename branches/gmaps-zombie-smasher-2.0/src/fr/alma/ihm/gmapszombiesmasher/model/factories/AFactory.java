package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;

/**
 * Interface representing a factory.
 *
 */
public abstract class AFactory {

	private EntityManager entityManager;
	private MapInformationUtilities mapInformationUtilities;
	
	public AFactory(EntityManager entityManager, 
				    MapInformationUtilities mapInformationUtilities){
		this.entityManager = entityManager;
		this.mapInformationUtilities = mapInformationUtilities;
	}
	
	/**
	 * @return the mapInformationUtilities
	 */
	public MapInformationUtilities getMapInformationUtilities() {
		return mapInformationUtilities;
	}

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Return an entity.
	 * 
	 * @return an entity
	 */
	public abstract Entity getEntity();
	
	/**
	 * Return an entity with the current position at the coordinates in parameters.
	 * 
	 * @return an entity
	 */
	public Entity createEntity(CCoordinates coordinates){
		Entity entity = getEntity();
		entity.setCurrentPosition(coordinates);
		return entity;
	}
	
	/**
	 * Destroy the entity.
	 */
	public abstract void destroyEntity();
	
	/**
	 * Return a random position on the map.
	 * 
	 * @return a random position.
	 */
	public CCoordinates getRandomPosition() {
		int topLatitude = getMapInformationUtilities().getTopLatitude();
		int leftLongitude = getMapInformationUtilities().getLeftLongitude();
		int rightLongitude = getMapInformationUtilities().getRightLongitude();
		int botLatitude = getMapInformationUtilities().getBotLatitude();
		
		int tempLatitude = (int) (botLatitude
				+ Math.random() * (topLatitude - botLatitude));
		int tempLongitude = (int) (leftLongitude
				+ Math.random() * (rightLongitude - leftLongitude));
		
		return new CCoordinates(tempLatitude, tempLongitude);
	}
}
