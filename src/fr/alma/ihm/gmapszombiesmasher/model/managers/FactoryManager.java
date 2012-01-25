package fr.alma.ihm.gmapszombiesmasher.model.managers;

import fr.alma.ihm.gmapszombiesmasher.model.factories.BombFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ChopperFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.CitizenFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.IFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ZombieFactory;

public class FactoryManager {

	public static final int ZOMBIES = 0;
	public static final int CITIZEN = 1;
	public static final int CHOPPER = 2;
	public static final int BOMB = 3;

	/**
	 * Return an instance of the right factory according to a type.
	 * 
	 * @param type
	 *            the type of the factory.
	 * @return the factory.
	 */
	public static IFactory getFactory(int type) {
		switch (type) {
		case ZOMBIES:
			return new ZombieFactory();
		case CITIZEN:
			return new CitizenFactory();
		case CHOPPER:
			return new ChopperFactory();
		case BOMB:
			return new BombFactory();
		}
		return null;
	}

}
