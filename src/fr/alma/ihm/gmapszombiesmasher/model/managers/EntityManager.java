/**
 * 
 */
package fr.alma.ihm.gmapszombiesmasher.model.managers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.activities.game.WaitingHandler;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.factories.AFactory;

/**
 * Manage the entities for: - Map placement - IA updates - ...
 */
public class EntityManager {

	public static final int ZOMBIES = FactoryManager.ZOMBIES;
	public static final int CITIZEN = FactoryManager.CITIZEN;
	public static final int CHOPPER = FactoryManager.CHOPPER;
	public static final int BOMB = FactoryManager.BOMB;

	private List<Integer> types;
	private Map<Integer, List<Entity>> entities;
	private Map<Integer, Integer> releasedCounter;
	private Map<Integer, Integer> killedCounter;
	private Map<Integer, Integer> eatedCounter;
	private Map<Integer, Integer> inGameCounter;
	private PlacementManager placementManager;
	private FactoryManager factoryManager;
	private MapInformationUtilities mapInformations;
	private WaitingHandler waittingHandler;

	public EntityManager(Activity activity, MapView mapView, 
			WaitingHandler waittingHandler) {
		this.entities = new HashMap<Integer, List<Entity>>();
		this.releasedCounter = new HashMap<Integer, Integer>();
		this.killedCounter = new HashMap<Integer, Integer>();
		this.eatedCounter = new HashMap<Integer, Integer>();
		this.inGameCounter = new HashMap<Integer, Integer>();
		this.placementManager = new PlacementManager(activity, mapView);

		this.types = new LinkedList<Integer>();
		this.types.add(ZOMBIES);
		this.types.add(CITIZEN);
		this.types.add(CHOPPER);
		this.types.add(BOMB);

		this.mapInformations = new MapInformationUtilities(mapView);
		this.factoryManager = new FactoryManager(this, this.mapInformations);
		this.waittingHandler = waittingHandler;
	}

	/**
	 * @return the mapInformations
	 */
	public MapInformationUtilities getMapInformations() {
		return mapInformations;
	}

	/**
	 * Return all the existing entities of the type in parameter.
	 * 
	 * @param type
	 *            the type of the entities to return
	 * @return A list of the entities
	 */
	public List<Entity> getEntities(int type) {
		
		List<Entity> entities = this.entities.get(type);
		if (entities == null) {
			entities = new LinkedList<Entity>();
		} else {
			for (Entity entity : this.entities.get(type)) {
				if (!entity.isExist()) {
					entities.remove(entity);
				}
			}
		}

		return entities;
	}

	/**
	 * Return all the entities in a list.
	 * 
	 * @return A list of the entities
	 */
	public List<Entity> getEntities() {
		List<Entity> entities = new LinkedList<Entity>();
		for (Integer type : this.types) {
			entities.addAll(getEntities(type));
		}
		
		System.out.println("[ENTITY_MANAGER] " + entities.size());

		return entities;
	}

	/**
	 * Create the <i>number</i> of entity with <i>type</i>.
	 * 
	 * @param types
	 *            An array of all the types to create.
	 * @param number
	 *            An array with the number of entity to create.
	 */
	public void spawn(int[] types, int[] numbers) {
		AFactory factory = null;
		Entity entity = null;
		List<Entity> list = null;
		for (int i = 0; i < types.length; i++) {
			factory = getFactory(types[i]);
			inGameCounter.put(types[i], numbers[i]);
			list = new LinkedList<Entity>();
			for (int j = 0; j <= numbers[i]; j++) {
				entity = factory.getEntity();
				list.add(entity);
			}
			this.entities.put(types[i], list);
		}
		
		draw();
	}
	
	public void draw(){
		this.placementManager.draw(getEntities());
	}

	/**
	 * Return the factory for the type in parameter.
	 * 
	 * @param type
	 *            the type of the factory.
	 * 
	 * @return the factory.
	 */
	public AFactory getFactory(int type) {
		return factoryManager.getFactory(type);
	}

	/**
	 * Update all the entity for the type.
	 * 
	 * @param type
	 */
	public void updateType(int type) {
		for (Entity entity : getEntities(type)) {
			if (entity.isExist()) {
				entity.update();
			}
		}
	}
	
	/**
	 * Start the activity for all the entity for the type.
	 * 
	 * @param type
	 */
	public void startType(int type) {
		for (Entity entity : getEntities(type)) {
			if (entity.isExist()) {
				entity.start();
			}
		}
	}

	/**
	 * Stop the activity for all the entity for the type.
	 * 
	 * @param type
	 */
	public void stopType(int type) {
		for (Entity entity : getEntities(type)) {
			this.placementManager.removeEntityFromMap(entity);
			entity.stop();
		}
	}

	/**
	 * Stop the activity for an entity.
	 * 
	 * @param entity
	 *            the entity to stop
	 */
	public void stopEntity(Entity entity, int type) {
		this.placementManager.removeEntityFromMap(entity);
		entity.setExist(false);
		entity.stop();
		getFactory(type).destroyEntity();
	}

	/**
	 * Wait all the entities.
	 */
	public void waitAll() {
		for (Entity entity : getEntities()) {
			entity.pause();
		}
	}

	/**
	 * Resume all the entities.
	 */
	public void resumeAll() {
		updateTime();
		for (Entity entity : getEntities()) {
			entity.resume();
		}
	}

	/**
	 * Stop all the entities.
	 */
	public void stopAll() {
		for (Integer type : this.types) {
			stopType(type);
		}
	}

	/**
	 * Update each entity.
	 */
	public void updateAll() {
		for (Integer type : this.types) {
			updateType(type);
		}
	}
	
	/**
	 * Start each activity of all entities.
	 */
	public void startAll() {
		for (Integer type : this.types) {
			startType(type);
		}
	}

	/**
	 * Transform the entity to a <i>type</i> entity.
	 * 
	 * @param entity
	 *            the entity to transform.
	 * @param fromType
	 *            the current entity type.
	 * @param toType
	 *            the new type of the entity.
	 */
	public void transformEntityTo(Entity entity, int fromType, int toType) {
		// remove the entity form the type list
		entities.get(fromType).remove(entity);
		// change the marker
		entity.getMarker().setIdMarker(toType);
		// put the entity to the list
		entities.get(toType).add(entity);
	}

	/**
	 * Return the number of released entities of type in parameter.
	 * 
	 * @param type
	 *            the type to check.
	 * @return the number of released entities.
	 */
	public int getReleasedCounter(int type) {
		if (!releasedCounter.containsKey(type)) {
			releasedCounter.put(type, 0);
		}

		return releasedCounter.get(type);
	}

	/**
	 * Return the number of killed entities of type in parameter.
	 * 
	 * @param type
	 *            the type to check.
	 * @return the number of killed entities.
	 */
	public int getKilledCounter(int type) {
		if (!killedCounter.containsKey(type)) {
			killedCounter.put(type, 0);
		}

		return killedCounter.get(type);
	}
	
	/**
	 * Return the number of entities in game for the type in parameter.
	 * 
	 * @param type
	 *            the type to check.
	 * @return the number of in game entities.
	 */
	public int getInGameCounter(int type) {
		if (!inGameCounter.containsKey(type)) {
			inGameCounter.put(type, 0);
		}

		return inGameCounter.get(type);
	}

	/**
	 * Increment the released counter for the type in parameter.
	 * 
	 * @param type
	 *            the type to increment.
	 */
	public void incrementRealsedCounter(int type) {
		releasedCounter.put(type, getReleasedCounter(type) + 1);
		inGameCounter.put(type, getInGameCounter(type) - 1);
		checkEndOfGame();
	}
	
	/**
	 * Increment the killed counter for the type in parameter.
	 * 
	 * @param type
	 *            the type to increment.
	 */
	public void incrementKilledCounter(int type) {
		killedCounter.put(type, getKilledCounter(type) + 1);
		inGameCounter.put(type, getInGameCounter(type) - 1);
		checkEndOfGame();
	}
	
	/**
	 * Return the number of entities eated for the type in parameter.
	 * 
	 * @param type
	 *            the type to check.
	 * @return the number of eated entities.
	 */
	public int getEatedCounter(int type) {
		if (!eatedCounter.containsKey(type)) {
			eatedCounter.put(type, 0);
		}

		return eatedCounter.get(type);
	}

	/**
	 * Increment the eated counter for the type in parameter.
	 * 
	 * @param type
	 *            the type to increment.
	 */
	public void incrementEatedCounter(int type) {
		eatedCounter.put(type, getEatedCounter(type) + 1);
		inGameCounter.put(type, getInGameCounter(type) - 1);
		checkEndOfGame();
	}

	/**
	 * Check if one the type is empty.
	 */
	private void checkEndOfGame() {
		for(int type: inGameCounter.keySet()){
			if(inGameCounter.get(type) <= 0){
				waittingHandler.sendEmptyMessage(WaitingHandler.END_GAME);
			}
		}
	}

	/**
	 * Create a new entity with the coordinates in parameter.
	 * @param type the type of the entity to create.
	 * @param coordinates the coordinate to place the entity.
	 */
	public void createEntity(int type, CCoordinates coordinates) {
		Entity entity = getFactory(type).createEntity(coordinates);
		List<Entity> list = new LinkedList<Entity>();
		list.add(entity);
		this.entities.put(type, list);
	}

	/**
	 * Change the pastSeconds used to calculate the delta time.
	 */
	public void updateTime() {
		for(Entity entity: getEntities()){
			entity.updateTime();
		}
	}
}
