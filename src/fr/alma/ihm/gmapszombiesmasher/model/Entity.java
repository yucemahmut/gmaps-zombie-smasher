package fr.alma.ihm.gmapszombiesmasher.model;

import java.util.Calendar;

import fr.alma.ihm.gmapszombiesmasher.model.components.CAI;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMoveSpeed;

/**
 * 
 * An entity is anything displayed on the view ( Zombies, Citizen, Chopper,
 * Explosion ... )
 * 
 */
public class Entity {

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
	 * The last time.s
	 */
	private long pastTimeMillis;

	/**
	 * Update the Entity.
	 */
	public void update() {
		getIa().update();
	}
	
	/**
	 * Start the activity of the Entity.
	 */
	public void start() {
		getIa().start();
	}

	/**
	 * Stop the activity of the Entity.
	 */
	public void stop() {
		getIa().setOnStop(true);
	}
	
	/**
	 * Pause the activity of the Entity.
	 */
	public void pause() {
		getIa().setOnPause(true);
	}
	
	/**
	 * Resume the activity of the Entity.
	 */
	public void resume() {
		getIa().setOnPause(false);
	}

	/**
	 * @return the exist
	 */
	public boolean isExist() {
		return exist;
	}

	/**
	 * @param exist
	 *            the exist to set
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
	 * Set a new goal to the entity. The current position of the entity will be
	 * updated with the position on the road.
	 * 
	 * @param newGoalCoordinates the coordinates of the new goal.
	 */
	public void setNewGoal(CCoordinates newGoalCoordinates) {
		this.goal = new CGoal(this);
		this.setCurrentPosition(this.goal.setGoal(newGoalCoordinates));
	}
	
	public void updateTime(){
		this.pastTimeMillis = Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * Update the currentPosition to the next position (according to the goal).
	 */
	public void goToNextPostion(){
		if (this.pastTimeMillis == -1) {
			this.pastTimeMillis = Calendar.getInstance().getTimeInMillis();
		}

		double thisTimeMillis = Calendar.getInstance().getTimeInMillis();
		double delta = (thisTimeMillis - this.pastTimeMillis) / 1000;
		CCoordinates coordinates = goal.getNextPosition(delta);
		if (coordinates != null) {
			setCurrentPosition(coordinates);
		}
		
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
