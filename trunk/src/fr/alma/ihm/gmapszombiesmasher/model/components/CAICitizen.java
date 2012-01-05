package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/** 
 * 
 * Artificial intelligence of an Entity if it do have one.
 * Citizen will move to the chopper if one is around
 *
 */
public class CAICitizen extends Component implements ICAI{

	public CAICitizen(Entity parent) {
		super(parent);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
