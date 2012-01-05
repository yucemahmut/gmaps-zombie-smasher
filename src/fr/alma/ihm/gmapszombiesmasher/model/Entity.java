package fr.alma.ihm.gmapszombiesmasher.model;

import java.util.TreeMap;

import fr.alma.ihm.gmapszombiesmasher.model.components.Component;

/**
 * 
 * An entity is anything displayed on the view ( Zombies, Citizen, Chopper,
 * Explosion ... )
 * 
 */
public class Entity {

	private TreeMap<String, Component> componentMap;

	public Entity() {
		componentMap = new TreeMap<String, Component>();
	}

	public TreeMap<String, Component> getComponentMap() {
		return componentMap;
	}

	public void setComponentMap(TreeMap<String, Component> componentMap) {
		this.componentMap = componentMap;
	}

	/**
	 * Add a new component to the entity, which can be a moveSpeed, an IA etc...
	 * 
	 * @param c
	 *            the new component to be added
	 */
	public void addComponent(Component c) {
		System.out.println("New component name : " + c.getClass().getName());
		componentMap.put(c.getClass().getName(), c);
	}

}
