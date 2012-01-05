package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * 
 * Class which describe a new behaviour (or feature) for an entity
 *
 */
public abstract class Component {

	private Entity parent;

	public Component(Entity parent) {
		this.parent = parent;
	}

	public Entity getParent() {
		return parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}
}
