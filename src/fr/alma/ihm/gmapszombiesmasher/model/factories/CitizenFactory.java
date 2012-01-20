package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAICitizen;
import fr.alma.ihm.gmapszombiesmasher.model.components.CBoolean;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMoveSpeed;

/**
 * 
 * Factory of citizen. Create a new entity between predefined boundaries
 * 
 */
public class CitizenFactory {
  public static final double SPEED = 10.0;

	private int topLatitude;
	private int botLatitude;
	private int leftLongitude;
	private int rightLongitude;
	private Spawn spawn;

	public CitizenFactory(int topLatitude, int botLatitude, int leftLongitude,
			int rightLongitude, Spawn spawn) {

		this.topLatitude = topLatitude;
		this.botLatitude = botLatitude;
		this.leftLongitude = leftLongitude;
		this.rightLongitude = rightLongitude;
		
		this.spawn = spawn;
	}

	public Entity getCitizen() {
		// create a citizen
		// TODO Component markerColor for citizen
		Entity citizen = new Entity();
    CMoveSpeed speed = new CMoveSpeed(citizen);
		citizen.addComponent(new CCoordinates(citizen));
		citizen.addComponent(new CGoal(citizen));
    speed.setMoveSpeed(SPEED);
		citizen.addComponent(speed);
		citizen.addComponent(new CAICitizen(citizen, spawn));
		// Living
		citizen.addComponent(new CBoolean(citizen));
		CMarker marker = new CMarker(citizen);
		marker.setCitizen();
		citizen.addComponent(marker);

		// Random position :
		//Random random = new Random();

		// TODO Verification de formule aux pôles
		int tempLatitude = (int) (botLatitude
				+ Math.random() * (topLatitude - botLatitude));
		int tempLongitude = (int) (leftLongitude
				+ Math.random() * (rightLongitude - leftLongitude));
		
		CCoordinates coordinates = new CCoordinates(citizen);
		coordinates.setLatitude(tempLatitude);
		coordinates.setLongitude(tempLongitude);
		citizen.addComponent(coordinates);

		return citizen;
	}

}
