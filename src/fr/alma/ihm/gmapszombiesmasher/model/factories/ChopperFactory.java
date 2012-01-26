package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

public class ChopperFactory implements IFactory {

	private Entity chopper;
	
	@Override
	public Entity getEntity() {
		if(chopper == null){
			chopper = new Entity();
			
		}
		
		return chopper;
	}

}
