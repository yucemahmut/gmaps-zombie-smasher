package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.GameActivity;

public interface ICAI {
	
	double distanceMin = 10 * (GameActivity.getZoom() - GameActivity.ZOOM_LEVEL);
	double distanceBombMin = 50 * (GameActivity.getZoom() - GameActivity.ZOOM_LEVEL);

	/**
	 * Implement the change made by the AI on others components of an entity
	 */
	public void update();
	
}
