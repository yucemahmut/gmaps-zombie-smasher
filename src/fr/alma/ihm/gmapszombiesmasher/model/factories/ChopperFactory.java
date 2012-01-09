package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

public class ChopperFactory {

	private static Entity chopper;

	public ChopperFactory() {
	}

	public static Entity get() {
		// Lazy Loading
		if (chopper == null) {
			chopper = new Entity();
			// Does nt have movespeed , or coordinate, or goal yet
		}
		return chopper;
	}
}
