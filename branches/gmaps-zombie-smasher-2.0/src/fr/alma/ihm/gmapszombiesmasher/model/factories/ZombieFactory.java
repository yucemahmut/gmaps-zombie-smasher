package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIZombie;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMoveSpeed;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;

public class ZombieFactory extends AFactory {

	public ZombieFactory(EntityManager entityManager,
			MapInformationUtilities mapInformationUtilities) {
		super(entityManager, mapInformationUtilities);
	}

	private static final double SPEED = 5.0;

	

	@Override
	public Entity getEntity() {
		Entity entity = new Entity();

		entity.setCurrentPosition(getRandomSpawnPosition());
		entity.setExist(true);
		entity.setIa(new CAIZombie(entity, getEntityManager()));
		entity.setMarker(new CMarker(CMarker.ZOMBIE_MARKER));
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
		CCoordinates coordinates = null;
		
		int randomValue = (int) (Math.random() * 4);

		int tempLatitude = 0;
		int tempLongitude = 0;
		
		int topLatitude = getMapInformationUtilities().getTopLatitude();
		int leftLongitude = getMapInformationUtilities().getLeftLongitude();
		int rightLongitude = getMapInformationUtilities().getRightLongitude();
		int botLatitude = getMapInformationUtilities().getBotLatitude();
		
		switch (randomValue) {
		case 0: // topLatitude
			tempLatitude = topLatitude - 100;
			tempLongitude = (int) (leftLongitude
					+ Math.random() * (rightLongitude - leftLongitude));
			break;
		case 1: // botLatitude
			tempLatitude = botLatitude;
			tempLongitude = (int) (leftLongitude
					+ Math.random() * (rightLongitude - leftLongitude));
			break;
		case 2: // leftLongitude
			tempLongitude = leftLongitude;
			tempLatitude = (int) (botLatitude
					+ Math.random() * (topLatitude - botLatitude));
			break;
		default: // rightLongitude
			tempLongitude = rightLongitude - 100;
			tempLatitude = (int) (botLatitude
					+ Math.random() * (topLatitude - botLatitude));
			break;
		}
		
		coordinates = new CCoordinates(tempLatitude, tempLongitude);
		
		return coordinates;
	}

	@Override
	public void destroyEntity() {
		
	}

}
