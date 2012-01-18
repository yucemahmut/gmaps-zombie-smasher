package fr.alma.ihm.gmapszombiesmasher.model.components;

import java.util.LinkedList;
import java.util.List;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Zombie will move to
 * the nearest citizen in order to eat him
 * 
 */
public class CAIZombie extends Component implements ICAI {

	private static int RANDOMBOUNDARY = 5; // bound of randomize a new target
	private Spawn spawn;
	private double distanceMin = 50;

	public CAIZombie(Entity parent, Spawn spawn) {
		super(parent);
		this.spawn = spawn;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		// TODO fonction de recherche d'entit� citizen plus proche
		// Chercher dans le parent si il a un CGoal
		// Si ce n'est pas le cas , alors le cr�er

		// Si c'est le cas, alors
		// Si le but est null
		CGoal goal = (CGoal) getParent().getComponentMap().get(
				CGoal.class.getName());

		if (goal.getGoal() == null) {
			spawn.setNewGoal(getParent());
			goal = (CGoal) getParent().getComponentMap().get(
					CGoal.class.getName());
		}

		// /*
		// Prendre le citoyen le plus pres
		Entity citizen = getCloserCitizen();
		if (citizen != null) {

			if (citizen.equals(goal.getGoal())) {
				// Si le citizen est atteind ou assez proche alors le citizen
				// est mange
				if ((goal.goalReached() || ((CCoordinates) getParent()
						.getComponentMap().get(CCoordinates.class.getName()))
						.isNearOf(goal.getGoalCoordinates(), distanceMin))) {
					// Si le zombie est toujours près des véritables coordonnées 
					// du citoyen
					if (((CCoordinates) getParent().getComponentMap().get(
							CCoordinates.class.getName()))
							.isNearOf(((CCoordinates) citizen.getComponentMap()
									.get(CCoordinates.class.getName())),
									distanceMin)) {
						// Liberation du citoyen
						spawn.eatCitizen(goal.getGoal());
					} else {
						// Sinon, je recalibre le goal
						spawn.setGoal(getParent(), citizen);
					}
				}
			} else {
				System.out.println("[ZOMBIE] New Goal Citizen");
				spawn.setGoal(getParent(), citizen);
				// goal.setGoal(citizen);
			}
		}

		// */

		if (goal.goalReached()) {
			System.out.println("[ZOMBIE] New Goal");
			spawn.setNewGoal(getParent());
		} else {
			spawn.setNextPosition(getParent(), goal);
			/*
			 * CCoordinates c = goal.getNextPosition(0); // FIXME Pas de retour
			 * null normalement if(c != null){ this.getParent().addComponent(c);
			 * }
			 */
		}

		/*
		 * 
		 * if (citizens.size() > 0) { // si citoyen il y a int percent = (int)
		 * (Math.random() * 100);
		 * 
		 * if (percent < this.RANDOMBOUNDARY) { // Getting a random citizen for
		 * target goal.setGoal(citizens.get((int) (Math.random() * citizens
		 * .size()))); } else {
		 * 
		 * // TODO calcul des distances
		 * 
		 * } } else { System.out.println("Pouet"); goal.setGoal(null); }
		 * 
		 * if (goal.getGoal() != null) { // Une entit� est cibl�e // Par effet
		 * de bord, changer les variables de CCoordinates en // fonction // de
		 * la CMoveSpeed (ne pas oublier deltaTime => demander a Adrien // pour
		 * // plus de pr�cisions) et de la route trouv�e par Gmaps ( que l on //
		 * conserve en m�moire au cas o� la position // de l citizen ne change
		 * pas a la frame suivante )
		 * 
		 * // Si le chopper est atteind ou assez proche alors le citizen est //
		 * libéré if (goal.goalReached() || ((CCoordinates)
		 * getParent().getComponentMap().get(
		 * CCoordinates.class.getName())).isNearOf(goal .getGoalCoordinates()))
		 * { // Liberation du citoyen spawn.eatCitizen(goal.getGoal()); } }
		 * 
		 * System.out.println("Reached= " + goal.goalReached());
		 * this.getParent().addComponent(goal.getNextPosition(0));
		 * 
		 * // utilisation des distances calcul� pr�cedement pour la //
		 * Verification de proximit� // Si un citizen se trouve proche, il est
		 * transform� en zombi, son icone // devient celle d ' un zombi et son
		 * IA aussi, movespeed recalcul�
		 * 
		 * System.out.println("ZOmbie= " + ((CCoordinates)
		 * getParent().getComponentMap().get( CCoordinates.class.getName())));
		 */
	}

	private Entity getCloserCitizen() {
		double maxDistance = 200;
		double distance = 0;
		Entity closer = null;
		for (Entity citizen : spawn.getCitizen()) {
			distance = ((((CCoordinates) getParent().getComponentMap().get(
					CCoordinates.class.getName())))
					.distanceTo((CCoordinates) citizen.getComponentMap().get(
							CCoordinates.class.getName())));
			if (distance < maxDistance) {
				closer = citizen;
				maxDistance = distance;
			}
		}

		return closer;
	}

}
