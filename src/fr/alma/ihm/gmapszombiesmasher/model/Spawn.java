package fr.alma.ihm.gmapszombiesmasher.model;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import fr.alma.ihm.gmapszombiesmasher.model.components.CAICitizen;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIZombie;
import fr.alma.ihm.gmapszombiesmasher.model.components.CBoolean;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
import fr.alma.ihm.gmapszombiesmasher.model.components.CMarker;
import fr.alma.ihm.gmapszombiesmasher.model.factories.CitizenFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ZombieFactory;

public class Spawn {
	private CitizenFactory citizenFactory;
	private ZombieFactory zombieFactory;
	
	private List<Entity> entities;
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
		
		this.entities = new LinkedList<Entity>();

		mapOverlays = mapView.getOverlays();
	}
	
	public void spawnZombies(int number){
		Entity zombie = null;
		for(int i=0; i<number; i++){
			zombie = zombieFactory.getZombie();
			entities.add(zombie);
		}
	}

	public void spawnCitizen(int number){
		Entity citizen = null;
		for(int i=0; i<number; i++){
			citizen = citizenFactory.getCitizen();
			entities.add(citizen);
		}
	}
	
	/**
	 * Put all the existing entities on the map
	 */
	public void putOnMap() {
		if(getChopper() != null){
			getEntities().add(getChopper());
		}
		for(Entity entity: getEntities()){
			CBoolean exist = ((CBoolean) entity.getComponentMap().get(CBoolean.class.getName()));
			if(exist.isExist()){
				CCoordinates coordinates = ((CCoordinates) entity.getComponentMap().get(CCoordinates.class.getName()));
				GeoPoint point = new GeoPoint(coordinates.getLatitude(), coordinates.getLongitude());
				OverlayItem overlayitem = new OverlayItem(point, null, null);
				int id = ((CMarker)entity.getComponentMap().get(CMarker.class.getName())).getMarker();
				EntityOverlay entityOverlay = new EntityOverlay(activity.getResources().getDrawable(id));
				entityOverlay.addOverlay(overlayitem);
				mapOverlays.add(entityOverlay);
			}
		}
	}
	
	public void createChopper(Entity entity){
		System.out.println("CREATE CHOPPER");
		((CBoolean)entity.getComponentMap().get(CBoolean.class.getName())).setExist(true);
		//entities.add(entity);
		chopper = entity;
	}
	
	public void destroyChopper() {
		
		((CBoolean)chopper.getComponentMap().get(CBoolean.class.getName())).setExist(false);
		chopper = null;
	}

	/**
	 * @return the entities
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	public List<Entity> getClone() {
		
		List<Entity> entities = new LinkedList<Entity>();
		for(Entity entity: getEntities()){
			entities.add((Entity) entity.clone());
		}	
		return entities;
	}
	
	public List<Entity> getCitizen() {
		
		List<Entity> citizens = new LinkedList<Entity>();
		for(Entity entity: getEntities()){
			if(entity.getComponentMap().containsKey(CAICitizen.class.getName())
			   && ((CBoolean)entity.getComponentMap().get(CBoolean.class.getName())).isExist()){
				citizens.add(entity);
			}
		}
		
		return citizens;
	}
	 
	
	public Entity getChopper() {
		return chopper;
	}
	
	private CCoordinates getRandomPosition(Entity entity){
		int tempLatitude = (int) (botLatitude
				+ Math.random() * (topLatitude - botLatitude));
		int tempLongitude = (int) (leftLongitude
				+ Math.random() * (rightLongitude - leftLongitude));
		
		CCoordinates coordinates = new CCoordinates(entity);
		coordinates.setLatitude(tempLatitude);
		coordinates.setLongitude(tempLongitude);
		
		return coordinates;
	}
	
	public void setGoal(Entity entity, Entity goal) {
		entity.addComponent(((CGoal)entity.getComponentMap().get(CGoal.class.getName())).setGoal(goal));
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
		CBoolean isFree = new CBoolean(entity);
		isFree.setExist(false);
		entity.addComponent(isFree);
		entity.removeComponent(CAICitizen.class.getName());
		citizenFree++;
	}
	
	/**
	 * Eat a citizen
	 * 
	 * @param entity
	 */
	public void eatCitizen(Entity entity) {
		System.out.println("EATED");
		// Changement d'IA
		entity.removeComponent(CAICitizen.class.getName());
		entity.addComponent(new CAIZombie(entity, this));
		// Changement de marker
		((CMarker)entity.getComponentMap().get(CMarker.class.getName())).setZombie();
		citizenEated++;
	}
	
	/**
	 * Kill Zombie
	 * 
	 * @param entity
	 */
	public void killZombie(Entity entity) {
		System.out.println("KILLED");
		CBoolean isKilled = new CBoolean(entity);
		isKilled.setExist(false);
		entity.addComponent(isKilled);
		entity.removeComponent(CAIZombie.class.getName());
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

	public void setNextPosition(Entity parent, CGoal goal) {
		CCoordinates c = goal.getNextPosition(0); 
		if(c != null){
			parent.addComponent(c);
		}
	}
}

