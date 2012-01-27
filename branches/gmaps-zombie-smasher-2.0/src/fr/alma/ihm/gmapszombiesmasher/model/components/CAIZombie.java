package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Zombie will move to
 * the nearest citizen in order to eat him
 * 
 */
public class CAIZombie extends CAI {

	public CAIZombie(Entity parent, EntityManager entityManager) {
		super(parent, entityManager);
		System.out.println("[ZOMBIE] IA");
	}

	@Override
	public void run() {
		while (!isOnStop()) {

			// Si on est en pause, on attend
			if (isOnPause()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				setOnPause(false);
			}
			
			update();
		}

	}

	@Override
	public void update() {
		CGoal goal = getParent().getGoal();
		if (goal == null) {
			CCoordinates newGoalCoordinates = getEntityManager()
					.getFactory(EntityManager.ZOMBIES).getRandomPosition();
			getParent().setNewGoal(newGoalCoordinates);
			goal = getParent().getGoal();
		}

		eatCitizens();

		if (goal.goalReached()) {
			CCoordinates newGoalCoordinates = getEntityManager()
					.getFactory(EntityManager.ZOMBIES).getRandomPosition();
			getParent().setNewGoal(newGoalCoordinates);
		} else {
			getParent().goToNextPostion();
		}
	}

	/**
	 * Eat any closed citizen.
	 */
	private void eatCitizens() {
		for (Entity entity : getEntityManager().getEntities(
				EntityManager.CITIZEN)) {
			if (entity.getCurrentPosition().isNearOf(
					getParent().getCurrentPosition(),
					getEntityManager().getMapInformations()
							.getDistanceToEatMin())) {
				// Transform the citizen to a zombie
				getEntityManager().transformEntityTo(entity, EntityManager.ZOMBIES);
				// Incremente eated counter
				getEntityManager().incrementEatedCounter(EntityManager.CITIZEN);
				// PlaySound
				GMapsZombieSmasher.soundsManager
						.playSound(SoundsManager.EAT_CITIZEN);
			}
		}
	}
}
