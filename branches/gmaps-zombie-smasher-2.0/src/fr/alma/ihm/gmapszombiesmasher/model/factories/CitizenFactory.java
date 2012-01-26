package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;

public class CitizenFactory implements IFactory{

	
	private MapInformationUtilities mapInfo;

	public CitizenFactory(MapInformationUtilities mapInfo) {
		this.mapInfo = mapInfo;
	}

	@Override
	public Entity getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}
