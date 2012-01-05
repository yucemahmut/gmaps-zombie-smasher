package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * 
 * The aim of an entity. For exemple, Zombies aim Citizens who aim Chopper 
 *
 */
public class CGoal extends Component {

	Entity goal;
	
	public CGoal(Entity parent) {
		super(parent);
		goal = null; // If there ain't no goal
	}

	public Entity getGoal() {
		return goal;
	}
	
	public void setGoal(Entity goal) {
		this.goal = goal;
	}
	
}
