package fr.alma.ihm.gmapszombiesmasher.model.components;

public interface ICAI {
	
	double distanceMin = 10;
	double distanceBombMin = 100;

	/**
	 * Implement the change made by the AI on others components of an entity
	 */
	public void update();
	
}
