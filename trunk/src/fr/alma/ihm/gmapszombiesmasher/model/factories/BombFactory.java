package fr.alma.ihm.gmapszombiesmasher.model.factories;

import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIBomb;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

public class BombFactory extends AFactory {

	public BombFactory(EntityManager entityManager,
			MapInformationUtilities mapInformationUtilities) {
		super(entityManager, mapInformationUtilities);
	}

	private Entity bomb;

	@Override
	public Entity getEntity() {
		if (bomb == null) {
			bomb = new Entity();
			bomb.setExist(true);
			bomb.setIa(new CAIBomb(bomb, getEntityManager()));
			bomb.setMarker(new CMarker(CMarker.BOMB_MARKER));
		}
		bomb.setExist(true);
		GMapsZombieSmasher.soundsManager
				.playSound(SoundsManager.BOMB_EXPLOSION);
		return bomb;
	}

	@Override
	public void destroyEntity() {
		this.bomb = null;
	}

}
