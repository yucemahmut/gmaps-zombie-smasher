package fr.alma.ihm.gmapszombiesmasher.model.managers;

import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.factories.AFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.BombFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ChopperFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.CitizenFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ZombieFactory;

public class FactoryManager {

	private MapInformationUtilities mapInfo;
	private EntityManager entityManager;
	
	public static final int ZOMBIES = 0;
	public static final int CITIZEN = 1;
	public static final int CHOPPER = 2;
	public static final int BOMB = 3;
	
	private ZombieFactory zombieFactory;
	private CitizenFactory citizenFactory;
	private ChopperFactory chopperFactory;
	private BombFactory bombFactory;

	public FactoryManager(EntityManager entityManager, MapInformationUtilities mapInfo){
		this.entityManager = entityManager;
		this.mapInfo = mapInfo;
	}

	/**
	 * Return an instance of the right factory according to a type.
	 * 
	 * @param type
	 *            the type of the factory.
	 * @return the factory.
	 */
	public AFactory getFactory(int type) {
		switch (type) {
		case ZOMBIES:
			if(zombieFactory == null){
				zombieFactory = new ZombieFactory(this.entityManager, this.mapInfo);
			}
			return zombieFactory;
		case CITIZEN:
			if(citizenFactory == null){
				citizenFactory = new CitizenFactory(this.entityManager, this.mapInfo);
			}
			return citizenFactory;
		case CHOPPER:
			if(chopperFactory == null){
				chopperFactory = new ChopperFactory(this.entityManager, this.mapInfo);
			}
			return chopperFactory;
		case BOMB:
			if(bombFactory == null){
				bombFactory = new BombFactory(this.entityManager, this.mapInfo);
			}
			return bombFactory;
		default:
				return null;
		}
	}

}
