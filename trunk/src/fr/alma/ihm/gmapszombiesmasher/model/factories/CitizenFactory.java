package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAICitizen;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMoveSpeed;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;
import fr.alma.ihm.gmapszombiesmasher.utils.ManagePreferences;

public class CitizenFactory extends AFactory{
	
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
		entity.setMoveSpeed(new CMoveSpeed(ManagePreferences.getCitizenSpeed()
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
		int topLatitude = getMapInformationUtilities().getTopLatitude();
		int leftLongitude = getMapInformationUtilities().getLeftLongitude();
		int rightLongitude = getMapInformationUtilities().getRightLongitude();
		int botLatitude = getMapInformationUtilities().getBotLatitude();
		
		int gapLatitude = (topLatitude - botLatitude) / 4;
		int gapLongitude = (leftLongitude - rightLongitude) / 6;
		
		topLatitude = topLatitude - gapLatitude;
		botLatitude = botLatitude + gapLatitude;
		leftLongitude = leftLongitude - gapLongitude;
		rightLongitude = rightLongitude + gapLongitude;
		
		int tempLatitude = (int) (botLatitude
				+ Math.random() * (topLatitude - botLatitude));
		int tempLongitude = (int) (leftLongitude
				+ Math.random() * (rightLongitude - leftLongitude));
		
		return new CCoordinates(tempLatitude, tempLongitude);
	}

	@Override
	public void destroyEntity() {
		// NOTHING
	}

	
}
