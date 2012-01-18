package fr.alma.ihm.gmapszombiesmasher.model;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIZombie;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
import fr.alma.ihm.gmapszombiesmasher.model.factories.CitizenFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ZombieFactory;

public class Spawn {
	private CitizenFactory citizenFactory;
	private ZombieFactory zombieFactory;
	
	private List<Entity> zombies;
	private List<Entity> citizens;
	private Entity chopper;
	private int citizenFree = 0;
	private int citizenEated = 0;
	private int zombieKilled = 0;
	
	private List<Overlay> mapOverlays;
	private Activity activity;
	
	private int topLatitude;
	private int botLatitude;
	private int leftLongitude;
	private int rightLongitude;
	
	public Spawn(Activity activity, MapView mapView, int topLatitude, int botLatitude, int leftLongitude,
			int rightLongitude) {
		super();
		
		this.activity = activity;
		
		this.topLatitude = topLatitude;
		this.botLatitude = botLatitude;
		this.leftLongitude = leftLongitude;
		this.rightLongitude = rightLongitude;
		
		this.citizenFactory = new CitizenFactory(topLatitude, botLatitude, leftLongitude, rightLongitude, this);
		this.zombieFactory = new ZombieFactory(topLatitude, botLatitude, leftLongitude, rightLongitude, this);
		
		this.zombies = new LinkedList<Entity>();
		this.citizens = new LinkedList<Entity>();

		mapOverlays = mapView.getOverlays();
	}
	
	public void spawnZombies(int number){
		Entity zombie = null;
		for(int i=0; i<number; i++){
			zombie = zombieFactory.getZombie();
			zombies.add(zombie);
			zombie.addComponent(new CGoal(zombie));
			//putZombieOnMap((CCoordinates)zombie.getComponentMap().get(CCoordinates.class.getName()));
		}
	}

	public void spawnCitizen(int number){
		Entity citizen = null;
		for(int i=0; i<number; i++){
			citizen = citizenFactory.getCitizen();
			citizens.add(citizen);
			citizen.addComponent(new CGoal(citizen));
			//putCitizenOnMap((CCoordinates)citizen.getComponentMap().get(CCoordinates.class.getName()));
		}
	}
	
	private void putOnMap(EntityOverlay entityOverlay, Entity entity) {
		CCoordinates coordinates = ((CCoordinates) entity.getComponentMap().get(CCoordinates.class.getName()));
		GeoPoint point = new GeoPoint(coordinates.getLatitude(), coordinates.getLongitude());
		OverlayItem overlayitem = new OverlayItem(point, null, null);
		entityOverlay.addOverlay(overlayitem);
		mapOverlays.add(entityOverlay);
	}
	
	public void putCitizenOnMap(Entity entity){
		EntityOverlay entityOverlay = new EntityOverlay(activity.getResources().getDrawable(R.drawable.citizens_marker));
		putOnMap(entityOverlay, entity);
	}
	
	public void putZombieOnMap(Entity entity){
		EntityOverlay entityOverlay = new EntityOverlay(activity.getResources().getDrawable(R.drawable.zombies_marker));
		putOnMap(entityOverlay, entity);
	}
	
	public void putChopperOnMap(Entity entity){
		chopper = entity;
		EntityOverlay entityOverlay = new EntityOverlay(activity.getResources().getDrawable(R.drawable.chopper_marker));
		putOnMap(entityOverlay, entity);
	}
	
	public void destroyChopper() {
		chopper = null;
	}

	/**
	 * @return the zombies
	 */
	public List<Entity> getZombies() {
		return zombies;
	}

	/**
	 * @return the citizens
	 */
	public List<Entity> getCitizens() {
		return citizens;
	}
	
	public Entity getChopper() {
		return chopper;
	}
	
	public CCoordinates getRandomPosition(Entity entity){
		int tempLatitude = (int) (botLatitude
				+ Math.random() * (topLatitude - botLatitude));
		int tempLongitude = (int) (leftLongitude
				+ Math.random() * (rightLongitude - leftLongitude));
		
		CCoordinates coordinates = new CCoordinates(entity);
		coordinates.setLatitude(tempLatitude);
		coordinates.setLongitude(tempLongitude);
		
		return coordinates;
	}
	
	/**
	 * Set a random new goal to the entity and set the current position on the
	 * road closer to the goal
	 * 
	 * @param entity
	 *            the entity to set
	 */
	public void setNewGoal(Entity entity) {
		// Get New Goal Coordinates
		Entity newGoal = new Entity();
		newGoal.addComponent(getRandomPosition(newGoal));

		// Put the new goal on the Entity
		CGoal goal = (CGoal) entity.getComponentMap()
				.get(CGoal.class.getName());
		entity.addComponent(goal.setGoal(newGoal));
	}

	/**
	 * Free a citizen
	 * 
	 * @param entity the citizen to free
	 */
	public void freeCitizen(Entity entity) {
		System.out.println("FREE");
		citizens.remove(entity);
		citizenFree++;
	}
	
	/**
	 * Eat a citizen
	 * 
	 * @param entity
	 */
	public void eatCitizen(Entity entity) {
		System.out.println("MANGE");
		citizens.remove(entity);
		// Changement d'IA
		entity.addComponent(new CAIZombie(entity, this));
		zombies.add(entity);
		citizenEated++;
	}
	
	/**
	 * Kill Zombie
	 * 
	 * @param entity
	 */
	public void killZombie(Entity entity) {
		System.out.println("KILL");
		zombies.remove(entity);
		zombieKilled++;
	}

	/**
	 * @return the citizenFree
	 */
	public int getCitizenFree() {
		return citizenFree;
	}

	/**
	 * @return the citizenEated
	 */
	public int getCitizenEated() {
		return citizenEated;
	}

	/**
	 * @return the zombieKilled
	 */
	public int getZombieKilled() {
		return zombieKilled;
	}
}

