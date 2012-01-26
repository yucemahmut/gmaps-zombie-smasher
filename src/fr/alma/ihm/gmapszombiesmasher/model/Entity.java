package fr.alma.ihm.gmapszombiesmasher.model;

import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMoveSpeed;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAI;

/**
 * 
 * An entity is anything displayed on the view ( Zombies, Citizen, Chopper,
 * Explosion ... )
 * 
 */
public class Entity {

	private Thread thread;
	
	/**
	 * The entity exist or not.
	 */
	private boolean exist;
	/**
	 * The IA of the entity.
	 */
	private CAI ia;
	/**
	 * The current position of the entity.
	 */
	private CCoordinates currentPosition;
	/**
	 * The goal of the entity (the coordinates to reach).
	 */
	private CGoal goal;
	/**
	 * The marker of the entity.
	 */
	private CMarker marker;
	/**
	 * The speed of the entity.
	 */
	private CMoveSpeed moveSpeed;

	
	/**
	 * Return the thread o create a new one.
	 * 
	 * @return the thread.
	 */
	private Thread getThread(){
		if(thread == null){
			thread = new Thread(getIa());
		}
		
		return thread;
	}
	
	/**
	 * Start the activity of the Entity.
	 */
	public void start() {
		getThread().run();
	}
	
	/**
	 * Stop the activity of the Entity.
	 */
	public void stop() {
		getThread().stop();
	}

	/**
	 * @return the exist
	 */
	public boolean isExist() {
		return exist;
	}
	
	/**
	 * @param exist the exist to set
	 */
	public void setExist(boolean exist) {
		this.exist = exist;
	}

	/**
	 * @return the ia
	 */
	public CAI getIa() {
		return ia;
	}

	/**
	 * @param ia
	 *            the ia to set
	 */
	public void setIa(CAI ia) {
		this.ia = ia;
	}

	/**
	 * @return the currentPosition
	 */
	public CCoordinates getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * @param currentPosition
	 *            the currentPosition to set
	 */
	public void setCurrentPosition(CCoordinates currentPosition) {
		this.currentPosition = currentPosition;
	}

	/**
	 * @return the goal
	 */
	public CGoal getGoal() {
		return goal;
	}

	/**
	 * @param goal
	 *            the goal to set
	 */
	public void setGoal(CGoal goal) {
		this.goal = goal;
	}

	/**
	 * @return the marker
	 */
	public CMarker getMarker() {
		return marker;
	}

	/**
	 * @param marker
	 *            the marker to set
	 */
	public void setMarker(CMarker marker) {
		this.marker = marker;
	}

	/**
	 * @return the moveSpeed
	 */
	public CMoveSpeed getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * @param moveSpeed
	 *            the moveSpeed to set
	 */
	public void setMoveSpeed(CMoveSpeed moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
}
