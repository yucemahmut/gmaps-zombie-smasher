package fr.alma.ihm.gmapszombiesmasher.model.factories;

import java.util.Random;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIZombie;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
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

	public ZombieFactory(int topLatitude, int botLatitude, int leftLongitude,
			int rightLongitude) {

		this.topLatitude = topLatitude;
		this.botLatitude = botLatitude;
		this.leftLongitude = leftLongitude;
		this.rightLongitude = rightLongitude;
	}

	public Entity getZombie() {
		// TODO setMovespeedcomponent for zombie
		// TODO Component markerColor for zombie
		Entity zombie = new Entity();
		zombie.addComponent(new CAIZombie(zombie));
		zombie.addComponent(new CGoal(zombie));
		zombie.addComponent(new CMoveSpeed(zombie));

		// Getting the random boundary where the zombie will appear
		Random random = new Random();

		// 0<=value<nbBoundaries
		// FIXME Verification des formules aux pôles
		int randomValue = random.nextInt(4);

		int tempLatitude = 0;
		int tempLongitude = 0;
		switch (randomValue) {
		case 0: // topLatitude
			tempLatitude = topLatitude;
			tempLongitude = leftLongitude
					+ random.nextInt(rightLongitude - leftLongitude);
			break;
		case 1: // botLatitude
			tempLatitude = botLatitude;
			tempLongitude = leftLongitude
					+ random.nextInt(rightLongitude - leftLongitude);
			break;
		case 2: // leftLongitude
			tempLongitude = leftLongitude;
			tempLatitude = botLatitude
					+ random.nextInt(topLatitude - botLatitude);
			break;
		default: // rightLongitude
			tempLongitude = rightLongitude;
			tempLatitude = botLatitude
					+ random.nextInt(topLatitude - botLatitude);
			break;
		}

		CCoordinates coordinates = new CCoordinates(zombie);
		coordinates.setLatitude(tempLatitude);
		coordinates.setLongitude(tempLongitude);
		zombie.addComponent(coordinates);

		return zombie;
	}

}
