package fr.alma.ihm.gmapszombiesmasher.model;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

import java.util.Calendar;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
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
	private Entity bomb;
	
	private int citizenInGame = 0;
	private int zombiesInGame = 0;
	private int citizenFree = 0;
	private int citizenEated = 0;
	private int citizenKilled = 0;
	private int zombieKilled = 0;

	private Activity activity;
	
	private int topLatitude;
	private int botLatitude;
	private int leftLongitude;
	private int rightLongitude;
  private double pastMillis;
	private MapView mapView;
	
	public Spawn(Activity activity, MapView mapView, int topLatitude, int botLatitude, int leftLongitude,
			int rightLongitude) {
		super();
		
		this.activity = activity;
		
		this.topLatitude = topLatitude;
		this.botLatitude = botLatitude;
		this.leftLongitude = leftLongitude;
		this.rightLongitude = rightLongitude;
    this.pastMillis = -1;
		
		this.citizenFactory = new CitizenFactory(topLatitude, botLatitude, leftLongitude, rightLongitude, this);
		this.zombieFactory = new ZombieFactory(topLatitude, botLatitude, leftLongitude, rightLongitude, this);
		
		this.entities = new LinkedList<Entity>();

		this.mapView = mapView;
		//mapOverlays = mapView.getOverlays();
	}
	
	public void spawnZombies(int number){
		Entity zombie = null;
		zombiesInGame = number;
		for(int i=0; i<number; i++){
			zombie = zombieFactory.getZombie();
			entities.add(zombie);
		}
	}

	public void spawnCitizen(int number){
		citizenInGame = number;
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
		
		if(getChopper() != null && !getEntities().contains(getChopper())){
			getEntities().add(getChopper());
		}
		
		if(getBomb() != null && !getEntities().contains(getBomb())){
			getEntities().add(getBomb());
		}
		
		mapView.getOverlays().clear();
		
		for(Entity entity: getEntities()){
			CBoolean exist = ((CBoolean) entity.getComponentMap().get(CBoolean.class.getName()));
			if(exist.isExist()){
				CCoordinates coordinates = ((CCoordinates) entity.getComponentMap().get(CCoordinates.class.getName()));
				GeoPoint point = new GeoPoint(coordinates.getLatitude(), coordinates.getLongitude());
				OverlayItem overlayitem = new OverlayItem(point, null, null);
				int id = ((CMarker)entity.getComponentMap().get(CMarker.class.getName())).getMarker();
				EntityOverlay entityOverlay = new EntityOverlay(activity.getResources().getDrawable(id));
				entityOverlay.addOverlay(overlayitem);
				mapView.getOverlays().add(entityOverlay);
			}
		}
		
		//mapView.invalidate();
	}
	
	/**
	 * Create the chopper
	 * @param entity
	 */
	public void createChopper(Entity entity){
		System.out.println("CREATE CHOPPER");
		((CBoolean)entity.getComponentMap().get(CBoolean.class.getName())).setExist(true);
		chopper = entity;
	}
	
	/**
	 * Destroy the chopper
	 */
	public void destroyChopper() {
		((CBoolean)chopper.getComponentMap().get(CBoolean.class.getName())).setExist(false);
		chopper = null;
	}
	
	/**
	 * Create the bomb
	 * @param entity
	 */
	public void createBomb(Entity entity){
		System.out.println("CREATE BOMB");
		((CBoolean)entity.getComponentMap().get(CBoolean.class.getName())).setExist(true);
		bomb = entity;
	}
	
	/**
	 * Destroy the bomb
	 */
	public void destroyBomb() {
		((CBoolean)bomb.getComponentMap().get(CBoolean.class.getName())).setExist(false);
		bomb = null;
	}

	/**
	 * @return the entities
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Return the list of citizens
	 * @return
	 */
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
	
	/**
	 * Return the chopper 
	 * @return the chopper, null if not exist
	 */
	public Entity getChopper() {
		return chopper;
	}
	
	/**
	 * Return the bomb 
	 * @return the bolb, null if not exist
	 */
	public Entity getBomb() {
		return bomb;
	}
	
	/**
	 * Return random position on the map for the entity
	 * @param entity
	 * @return a random position on the map
	 */
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
	
	/**
	 * Set the goal <i>goal</i> for the chosen entity
	 * @param entity the entity
	 * @param goal the goal
	 */
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
		citizenInGame--;
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
		citizenInGame--;
		// Changement d'IA
		entity.removeComponent(CAICitizen.class.getName());
		entity.addComponent(new CAIZombie(entity, this));
		// Changement de marker
		((CMarker)entity.getComponentMap().get(CMarker.class.getName())).setZombie();
		citizenEated++;
	}
	
	/**
	 * Kill a citizen
	 * 
	 * @param entity the citizen to free
	 */
	public void killCitizen(Entity entity) {
		System.out.println("KILL CITIZEN");
		citizenInGame--;
		CBoolean isKilled = new CBoolean(entity);
		isKilled.setExist(false);
		entity.addComponent(isKilled);
		entity.removeComponent(CAICitizen.class.getName());
		citizenKilled++;
	}
	
	/**
	 * Kill Zombie
	 * 
	 * @param entity
	 */
	public void killZombie(Entity entity) {
		System.out.println("KILL ZOMBIE");
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

	/**
	 * @return the citizenInGame
	 */
	public int getCitizenInGame() {
		return citizenInGame;
	}

	/**
	 * @return the zombiesInGame
	 */
	public int getZombiesInGame() {
		return zombiesInGame;
	}

	/**
	 * @return the citizenKilled
	 */
	public int getCitizenKilled() {
		return citizenKilled;
	}

  /**
   * Change the pastSeconds used to calculate the delta time.
   */
  public void updateSeconds() {
    this.pastMillis = Calendar.getInstance().getTimeInMillis();
  }

	/**
	 * Get and set the next position on <i>parent</i> for the goal <i>goal</i>
	 * @param parent the parent to set
	 * @param goal the goal to reach
	 */
	public void setNextPosition(Entity parent, CGoal goal) {
    if(this.pastMillis == -1) {
      this.pastMillis = Calendar.getInstance().getTimeInMillis();
    }

    double pastMillis = Calendar.getInstance().getTimeInMillis();
    double delta = (pastMillis - this.pastMillis) / 1000;
		CCoordinates c = goal.getNextPosition(delta); 
		if(c != null){
			parent.addComponent(c);
		}
	}
}

