package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIZombie;
import fr.alma.ihm.gmapszombiesmasher.model.components.CBoolean;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMoveSpeed;

/**
 * 
 * Factory of zombies. Create a new entity around predefined boundaries
 * 
 */
public class ZombieFactory {

	private int topLatitude;
	private int botLatitude;
	private int leftLongitude;
	private int rightLongitude;
	private Spawn spawn;
	

	public ZombieFactory(int topLatitude, int botLatitude, int leftLongitude,
			int rightLongitude, Spawn spawn) {

		this.topLatitude = topLatitude;
		this.botLatitude = botLatitude;
		this.leftLongitude = leftLongitude;
		this.rightLongitude = rightLongitude;
		this.spawn = spawn;
	}

	public Entity getZombie() {
		// TODO setMovespeedcomponent for zombie
		// TODO Component markerColor for zombie
		Entity zombie = new Entity();
		zombie.addComponent(new CAIZombie(zombie, spawn));
		zombie.addComponent(new CGoal(zombie));
		zombie.addComponent(new CMoveSpeed(zombie));
		// Living
		zombie.addComponent(new CBoolean(zombie));
		CMarker marker = new CMarker(zombie);
		marker.setZombie();
		zombie.addComponent(marker);

		// Getting the random boundary where the zombie will appear

		// 0<=value<nbBoundaries
		// FIXME Verification des formules aux p�les
		int randomValue = (int) (Math.random() * 4);

		int tempLatitude = 0;
		int tempLongitude = 0;
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

		CCoordinates coordinates = new CCoordinates(zombie);
		coordinates.setLatitude(tempLatitude);
		coordinates.setLongitude(tempLongitude);
		zombie.addComponent(coordinates);

		return zombie;
	}

}
