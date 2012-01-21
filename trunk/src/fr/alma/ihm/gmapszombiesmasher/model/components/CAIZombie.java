package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Zombie will move to
 * the nearest citizen in order to eat him
 * 
 */
public class CAIZombie extends ICAI {

	//private static int RANDOMBOUNDARY = 5; // bound of randomize a new target
	private Spawn spawn;

	public CAIZombie(Entity parent, Spawn spawn, int zoomLevel) {
		super(parent, zoomLevel);
		this.spawn = spawn;
	}

	@Override
	public void update() {

		CCoordinates parentCoordinates = ((CCoordinates) getParent()
				.getComponentMap().get(CCoordinates.class.getName()));

		// S'il existe une bomb sur la carte
		if (spawn.getBomb() != null) {
			CCoordinates bombCoordinates = (CCoordinates) spawn.getBomb()
					.getComponentMap().get(CCoordinates.class.getName());
			// If the bomb is near
			if (parentCoordinates.isNearOf(bombCoordinates, getDistanceBombMin())) {
				spawn.killZombie(getParent());
			}
		} else {
			CGoal goal = (CGoal) getParent().getComponentMap().get(
					CGoal.class.getName());

			if (goal.getGoal() == null) {
				spawn.setNewGoal(getParent());
				goal = (CGoal) getParent().getComponentMap().get(
						CGoal.class.getName());
			}

			/*
			 * // Prendre le citoyen le plus pres Entity citizen =
			 * getCloserCitizen(); if (citizen != null) {
			 * 
			 * if (citizen.equals(goal.getGoal())) { // Si le citizen est
			 * atteind ou assez proche alors le citizen // est mange if
			 * ((goal.goalReached() || ((CCoordinates) getParent()
			 * .getComponentMap().get(CCoordinates.class.getName()))
			 * .isNearOf(goal.getGoalCoordinates(), distanceMin))) { // Si le
			 * zombie est toujours près des véritables coordonnées // du citoyen
			 * if (((CCoordinates) getParent().getComponentMap().get(
			 * CCoordinates.class.getName())) .isNearOf(((CCoordinates)
			 * citizen.getComponentMap() .get(CCoordinates.class.getName())),
			 * distanceMin)) { // Liberation du citoyen
			 * spawn.eatCitizen(goal.getGoal()); } else { // Sinon, je recalibre
			 * le goal spawn.setGoal(getParent(), citizen); } } } else {
			 * System.out.println("[ZOMBIE] New Goal Citizen");
			 * spawn.setGoal(getParent(), citizen); } }
			 * 
			 * //
			 */

			eatCitizens();

			if (goal.goalReached()) {
				System.out.println("[ZOMBIE] New Goal");
				spawn.setNewGoal(getParent());
			} else {
				spawn.setNextPosition(getParent(), goal);
			}
		}
	}

	/**
	 * 
	 */
	private void eatCitizens() {
		for (Entity citizen : spawn.getCitizen()) {
			if ((((CCoordinates) getParent().getComponentMap().get(
					CCoordinates.class.getName())).isNearOf(
					((CCoordinates) citizen.getComponentMap().get(
							CCoordinates.class.getName())), getDistanceMin()))) {
				spawn.eatCitizen(citizen);
			}
		}
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
