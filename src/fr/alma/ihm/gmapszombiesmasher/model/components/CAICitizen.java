package fr.alma.ihm.gmapszombiesmasher.model.components;

import java.util.LinkedList;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ChopperFactory;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Citizen will move to
 * the chopper if one is around
 * 
 */
public class CAICitizen extends Component implements ICAI {

	private ChopperFactory cF = new ChopperFactory();
	private LinkedList<Entity> zombies;
	private Spawn spawn;
	private double distanceMin = 50;

	public CAICitizen(Entity parent, Spawn spawn) {
		super(parent);
		this.spawn = spawn;
	}

	@Override
	public void update() {
		CGoal goal = (CGoal) getParent().getComponentMap().get(CGoal.class.getName());
		
		// S'il existe un chopper sur la carte, alors on va vers le chopper
		if (spawn.getChopper() != null) {
			
			if(spawn.getChopper().equals(goal.getGoal())){
				// Si le chopper est atteind ou assez proche alors le citizen est
				// libéré
				if (goal.goalReached()
					|| ((CCoordinates) getParent().getComponentMap().get(
					   CCoordinates.class.getName())).isNearOf(goal.getGoalCoordinates(), distanceMin)) {
					// Liberation du citoyen
					spawn.freeCitizen(getParent());
				} else {
					spawn.setNextPosition(getParent(), goal);
				}
			} else {
				System.out.println("[CITIZEN] New Goal Chopper");
				// On ajoute le chopper comme étant le but à atteindre
				spawn.setGoal(getParent(), spawn.getChopper());
			}
		} else {
			// Si el but est atteind, on cherche un nouveau but
			if(goal.goalReached()){
				System.out.println("[CITIZEN] New Goal");
				spawn.setNewGoal(getParent());
			} else {
				spawn.setNextPosition(getParent(), goal);
			}
		}
	}

	public ChopperFactory getcF() {
		return cF;
	}

	public void setcF(ChopperFactory cF) {
		this.cF = cF;
	}

	public LinkedList<Entity> getZombies() {
		return zombies;
	}

	public void setZombies(LinkedList<Entity> zombies) {
		this.zombies = zombies;
	}

}
