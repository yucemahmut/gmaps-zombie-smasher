package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CBoolean;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;

public class ChopperFactory {

	private static Entity chopper;

	public ChopperFactory() {
	}

	public static Entity get() {
		// Lazy Loading
		if (chopper == null) {
			chopper = new Entity();
			chopper.addComponent(new CCoordinates(chopper));
			CBoolean alive = new CBoolean(chopper);
			alive.setExist(false);
			chopper.addComponent(alive);
			CMarker marker = new CMarker(chopper);
			marker.setChopper();
			chopper.addComponent(marker);
			// Does nt have movespeed , or coordinate, or goal yet
		}
		return chopper;
	}
}
