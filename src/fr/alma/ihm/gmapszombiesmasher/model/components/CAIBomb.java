package fr.alma.ihm.gmapszombiesmasher.model.components;

import java.util.Calendar;

import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

public class CAIBomb extends CAI {

	private static final long LIFE_TIME = 2000;
	private static final int TOTAL_FRAMES = 10;

	private int[] deadlyTypes;
	private long startTime;
	private int currentFrame;
	private long startPause;

	public CAIBomb(Entity parent, EntityManager entityManager) {
		super(parent, entityManager);
		currentFrame = 0;
		deadlyTypes = new int[] { EntityManager.CITIZEN, EntityManager.ZOMBIES };
		startTime = Calendar.getInstance().getTimeInMillis();
		startPause = -1;
	}

	@Override
	public void run() {
		startTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("[BOMB] "
				+ (Calendar.getInstance().getTimeInMillis() - startTime));
		// Tantque la vie de la bombe n'est pas finie
		while (Calendar.getInstance().getTimeInMillis() - startTime < LIFE_TIME) {

			if (isOnPause()) {
				startPause = Calendar.getInstance().getTimeInMillis();
			}
			if (startPause != -1) {
				startTime += Calendar.getInstance().getTimeInMillis()
						- startPause;
			}

			update();
		}
		// Stop the entity
		getEntityManager().stopEntity(getParent(), EntityManager.BOMB);

	}

	@Override
	public void update() {
		// Tantque la vie de la bombe n'est pas finie
		if (Calendar.getInstance().getTimeInMillis() - startTime <= LIFE_TIME) {

			if (isOnPause()) {
				startPause = Calendar.getInstance().getTimeInMillis();
			}
			if (startPause != -1) {
				startTime += Calendar.getInstance().getTimeInMillis()
						- startPause;
			}

			updateFrame();

			killThemAll();
		} else {
			// Stop the entity
			getEntityManager().stopEntity(getParent(), EntityManager.BOMB);
		}
	}

	/**
	 * Kill all the deadly entities in the deadly zone.
	 */
	private void killThemAll() {
		for (int i = 0; i < deadlyTypes.length; i++) {
			for (Entity entity : getEntityManager().getEntities(deadlyTypes[i])) {
				if (entity.getCurrentPosition().isNearOf(
						getParent().getCurrentPosition(),
						getEntityManager().getMapInformations()
								.getDistanceBombMin())) {
					// Delete the citizen if close.
					getEntityManager().stopEntity(entity, EntityManager.CITIZEN);
					// Increment the number of killed for the entity
					getEntityManager().incrementKilledCounter(deadlyTypes[i]);
					// PlaySound
					switch (deadlyTypes[i]) {
					case EntityManager.CITIZEN:
						int rand = (int) (Math.random() * 1);
						if (rand == 0) {
							GMapsZombieSmasher.soundsManager
									.playSound(SoundsManager.MAN_DEAD);
						} else {
							GMapsZombieSmasher.soundsManager
									.playSound(SoundsManager.WOMAN_DEAD);
						}
						break;
					case EntityManager.ZOMBIES:
						GMapsZombieSmasher.soundsManager
								.playSound(SoundsManager.KILL_ZOMBIE);
						break;
					default:
						break;
					}

				}
			}
		}
	}

	/**
	 * Update de la frame de l'image.
	 */
	private void updateFrame() {
		getParent().getMarker().setIdMarker(CMarker.BOMB_MARKER + currentFrame++ % TOTAL_FRAMES);
	}

}
