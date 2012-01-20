package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CBoolean;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;

public class BombFactory {

	private static Entity bomb;

	public BombFactory() {
	}

	public static Entity get() {
		// Lazy Loading
		if (bomb == null) {
			bomb = new Entity();
			bomb.addComponent(new CCoordinates(bomb));
			CBoolean alive = new CBoolean(bomb);
			alive.setExist(false);
			bomb.addComponent(alive);
			CMarker marker = new CMarker(bomb);
			marker.setBomb();
			bomb.addComponent(marker);
		}
		return bomb;
	}
}
