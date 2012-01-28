package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;

/**
 * 
 * Interface representing an IA.
 *
 */
public abstract class CAI extends Thread implements Component  {
	
	private EntityManager entityManager;
	private Entity parent;
	private boolean onPause;
	private boolean onStop;

	public CAI(Entity parent, EntityManager entityManager){
		this.parent = parent;
		this.entityManager = entityManager;
		this.onPause = false;
		this.onStop = false;
	}
	
	public abstract void update();

	/**
	 * @return the parent
	 */
	public Entity getParent() {
		return parent;
	}

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @return the onPause
	 */
	public boolean isOnPause() {
		return onPause;
	}

	/**
	 * @param onPause the onPause to set
	 */
	public void setOnPause(boolean onPause) {
		this.onPause = onPause;
	}

	/**
	 * @return the onStop
	 */
	public boolean isOnStop() {
		return onStop;
	}

	/**
	 * @param onStop the onStop to set
	 */
	public void setOnStop(boolean onStop) {
		this.onStop = onStop;
	}
}
