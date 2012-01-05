package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/** 
 * 
 * Artificial intelligence of an Entity if it do have one.
 * Zombie will move to the nearest citizen in order to eat him
 *
 */
public class CAIZombie extends Component implements ICAI{

	public CAIZombie(Entity parent) {
		super(parent);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
