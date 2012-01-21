package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.GameActivity;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;

public abstract class ICAI extends Component{
	
	private int zoomLevel;
	private double distanceMin = 10 ;
	private double distanceBombMin = 50;
	private double distanceChopperMin = 70;

	public ICAI(Entity parent, int zoomLevel){
		super(parent);
		this.zoomLevel = zoomLevel;
	}
	
	/**
	 * Implement the change made by the AI on others components of an entity
	 */
	public abstract void update();
	
	public double getDistanceMin(){
		return distanceMin * (zoomLevel - GameActivity.ZOOM_LEVEL_MIN);
	}
	public double getDistanceBombMin(){
		return distanceBombMin * (zoomLevel - GameActivity.ZOOM_LEVEL_MIN);
	}
	public double getDistanceChopperMin(){
		return distanceChopperMin * (zoomLevel - GameActivity.ZOOM_LEVEL_MIN);
	}
	
}
