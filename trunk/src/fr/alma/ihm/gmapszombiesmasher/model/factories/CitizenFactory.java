package fr.alma.ihm.gmapszombiesmasher.model.factories;

import java.util.Random;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAICitizen;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMoveSpeed;

/**
 * 
 * Factory of citizen. Create a new entity between predefined boundaries
 * 
 */
public class CitizenFactory {

	private int topLatitude;
	private int botLatitude;
	private int leftLongitude;
	private int rightLongitude;

	public CitizenFactory(int topLatitude, int botLatitude, int leftLongitude,
			int rightLongitude) {

		this.topLatitude = topLatitude;
		this.botLatitude = botLatitude;
		this.leftLongitude = leftLongitude;
		this.rightLongitude = rightLongitude;
		
		System.out.println("topLatitude:" + topLatitude);
		System.out.println("botLatitude:" + botLatitude);
		System.out.println("leftLongitude:" + leftLongitude);
		System.out.println("rightLongitude:" + rightLongitude);
		
	}

	public Entity getCitizen() {
		// create a citizen
		// TODO Component markerColor for citizen
		Entity citizen = new Entity();
		citizen.addComponent(new CCoordinates(citizen));
		// TODO Cr�er une CMoveSpeed dans une fourchette
		// -> d�finir une unit�
		// -> d�finir une movespeed pour les zombies l�gerement superieur en
		// moyenne a un citoyen
		citizen.addComponent(new CMoveSpeed(citizen));
		citizen.addComponent(new CAICitizen(citizen));

		// Random position :
		//Random random = new Random();

		// TODO Verification de formule aux p�les
		int tempLatitude = (int) (botLatitude
				+ Math.random() * (topLatitude - botLatitude));
		int tempLongitude = (int) (leftLongitude
				+ Math.random() * (rightLongitude - leftLongitude));

		System.out.println("TEMP: " + tempLatitude + " : " + tempLongitude);
		
		CCoordinates coordinates = new CCoordinates(citizen);
		coordinates.setLatitude(tempLatitude);
		coordinates.setLongitude(tempLongitude);
		citizen.addComponent(coordinates);

		return citizen;
	}

}
