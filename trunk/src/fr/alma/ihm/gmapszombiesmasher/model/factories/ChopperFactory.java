package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIChopper;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

public class ChopperFactory extends AFactory {

	public ChopperFactory(EntityManager entityManager,
			MapInformationUtilities mapInformationUtilities) {
		super(entityManager, mapInformationUtilities);
	}

	private Entity chopper;

	@Override
	public Entity getEntity() {
		if (chopper == null) {
			chopper = new Entity();
			chopper.setExist(true);
			chopper.setIa(new CAIChopper(chopper, getEntityManager()));
			chopper.setMarker(new CMarker(CMarker.CHOPPER_MARKER));
		}
		chopper.setExist(true);
		GMapsZombieSmasher.soundsManager.playSound(SoundsManager.CHOPPER_ARRIVE);
		GMapsZombieSmasher.soundsManager.playSound(SoundsManager.CHOPPER_SOUND);
		return chopper;
	}

	@Override
	public void destroyEntity() {
		this.chopper = null;
	}

}
