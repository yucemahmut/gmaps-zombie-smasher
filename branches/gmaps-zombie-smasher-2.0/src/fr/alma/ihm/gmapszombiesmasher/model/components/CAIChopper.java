package fr.alma.ihm.gmapszombiesmasher.model.components;

import java.util.Calendar;

import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

public class CAIChopper extends CAI {

	private static final long CHOPPER_SOUND_TIME = 5000;
	private static final long FRAME_TIME = 100;
	private static final long LIFE_TIME = 10000;
	private static final int TOTAL_FRAMES = 2;

	private long startTime;
	private long currentTime;
	private long soundTime;
	private int currentFrame;

	public CAIChopper(Entity parent, EntityManager entityManager) {
		super(parent, entityManager);
		currentFrame = 0;
		startTime = Calendar.getInstance().getTimeInMillis();
		currentTime = startTime;
		soundTime = startTime;
		System.out.println("CHOPPER");
	}

	@Override
	public void run() {
		startTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("[CHOPPER] "
				+ (Calendar.getInstance().getTimeInMillis() - startTime));
		// Tantque la vie du chopper n'est pas finie
		while (Calendar.getInstance().getTimeInMillis() - startTime < LIFE_TIME) {

			if (isOnPause()) {
				long startPause = Calendar.getInstance().getTimeInMillis();
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				setOnPause(false);
				startTime += Calendar.getInstance().getTimeInMillis()
						- startPause;
			}

			update();
		}

		System.out.println("[CHOPPER] STOP");
		// Stop the entity
		getEntityManager().stopEntity(getParent(), EntityManager.CHOPPER);
	}

	@Override
	public void update() {
		// Tantque la vie du chopper n'est pas finie
		if (Calendar.getInstance().getTimeInMillis() - startTime <= LIFE_TIME) {

			if (isOnPause()) {
				long startPause = Calendar.getInstance().getTimeInMillis();
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				setOnPause(false);
				startTime += Calendar.getInstance().getTimeInMillis()
						- startPause;
			}

			updateFrame();

			notifyCitizens();
			releaseCitizens();
		} else {
			System.out.println("[CHOPPER] STOP");
			// Stop the entity
			getEntityManager().stopEntity(getParent(), EntityManager.CHOPPER);
		}
	}

	/**
	 * Notify all the citizens that the chopper is here.
	 */
	private void notifyCitizens() {
		for (Entity entity : getEntityManager().getEntities(
				EntityManager.CITIZEN)) {
			if (entity.getCurrentPosition().isNearOf(
					getParent().getCurrentPosition(),
					getEntityManager().getMapInformations()
							.getDistanceChopperMin())) {
				// Si le goal du civil n'est pas l'hélico
				if(!entity.getGoal().getGoalCoordinates().equals(getParent().getCurrentPosition())){
					entity.setNewGoal(getParent().getCurrentPosition());
				}
			}
		}
	}

	/**
	 * Release all the nearly citizens.
	 */
	private void releaseCitizens() {
		for (Entity entity : getEntityManager().getEntities(
				EntityManager.CITIZEN)) {
			if (entity.getCurrentPosition().isNearOf(
					getParent().getCurrentPosition(),
					getEntityManager().getMapInformations()
							.getDistanceToGetInChopper())) {
				// Delete the citizen if close.
				getEntityManager().stopEntity(entity, EntityManager.CITIZEN);
				// Increment the release citizen counter
				getEntityManager().incrementRealsedCounter(
						EntityManager.CITIZEN);
				// PlaySound
				GMapsZombieSmasher.soundsManager
						.playSound(SoundsManager.FREE_CITIZEN);
			}
		}
	}

	/**
	 * Update de la frame de l'image.
	 */
	private void updateFrame() {
		int id = getParent().getMarker().getIdMarker();

		if (Calendar.getInstance().getTimeInMillis() - soundTime >= CHOPPER_SOUND_TIME) {
			soundTime = Calendar.getInstance().getTimeInMillis();
			GMapsZombieSmasher.soundsManager
					.playSound(SoundsManager.CHOPPER_SOUND);
		}

		if (Calendar.getInstance().getTimeInMillis() - currentTime >= FRAME_TIME) {
			currentTime = Calendar.getInstance().getTimeInMillis();
			getParent().getMarker().setIdMarker(
					id + (currentFrame++ % TOTAL_FRAMES));
		}
	}

}
