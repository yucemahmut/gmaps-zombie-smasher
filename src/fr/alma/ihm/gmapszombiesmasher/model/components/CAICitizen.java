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

	public CAICitizen(Entity parent, Spawn spawn) {
		super(parent);
		this.spawn = spawn;
	}

	@Override
	public void update() {
		// TODO implemente algo suivant :

		// Chercher le CGoal du parent
		// Si aucun , alors le cr�er
		/*
		 * if (!(getParent().getComponentMap().containsKey("CGoal"))) {
		 * getParent().addComponent(new CGoal(getParent())); }
		 */
		CGoal goal = (CGoal) getParent().getComponentMap().get(CGoal.class.getName());
		
		// S'il existe un chopper sur la carte, alors on va vers le chopper
		if (spawn.getChopper() != null && chopperClose(spawn.getChopper())) {
			// On ajoute le chopper comme étant le but à atteindre
			getParent().addComponent(goal.setGoal(spawn.getChopper()));

			// Si le chopper est atteind ou assez proche alors le citizen est
			// libéré
			if (goal.goalReached()
				|| ((CCoordinates) getParent().getComponentMap().get(
				   CCoordinates.class.getName())).isNearOf(goal.getGoalCoordinates())) {
				// Liberation du citoyen
				spawn.freeCitizen(getParent());
			}
		} else {
			// Si el but est atteind, on cherche un nouveau but
			if(goal.goalReached()){
				spawn.setNewGoal(getParent());
			} else {
				this.getParent().addComponent(goal.getNextPosition(0));
			}
		}
	}

	private boolean chopperClose(Entity chopper) {
		double distanceMax = 200;
		CCoordinates chopperCoord = (CCoordinates)chopper.getComponentMap().get(CCoordinates.class.getName());
		CCoordinates citizenCoord = (CCoordinates)getParent().getComponentMap().get(CCoordinates.class.getName());
		if((chopperCoord.distanceTo(citizenCoord)) <= distanceMax){
			return true;
		}
		return false;
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
