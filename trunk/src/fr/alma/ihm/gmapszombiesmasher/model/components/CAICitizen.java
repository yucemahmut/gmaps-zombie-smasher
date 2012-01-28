package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Citizen will move to
 * the chopper if one is around
 * 
 */
public class CAICitizen extends CAI {

	public CAICitizen(Entity parent, EntityManager entityManager) {
		super(parent, entityManager);
	}

	@Override
	public void run() {
		while (!isOnStop()) {
			// Si on est en pause, on attend
			if(isOnPause()){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				setOnPause(false);
			}
			
			update();
			
			System.out.println("[CITIZEN] FIN BOUCLE");
		}
	}

	@Override
	public void update() {
		CGoal goal = getParent().getGoal();

		if (goal == null) {
			CCoordinates newGoalCoordinates = getEntityManager()
					.getFactory(EntityManager.CITIZEN).getRandomPosition();
			getParent().setNewGoal(newGoalCoordinates);
			goal = getParent().getGoal();
		}
		
		

		if (goal.goalReached()) {
			CCoordinates newGoalCoordinates = getEntityManager()
					.getFactory(EntityManager.CITIZEN).getRandomPosition();
			getParent().setNewGoal(newGoalCoordinates);
			goal = getParent().getGoal();
		} else {
			getParent().goToNextPostion();
		}
	}
}
