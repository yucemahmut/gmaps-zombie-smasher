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

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.factories.IFactory;

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
	private PlacementManager placementManager;

	public EntityManager(Activity activity, MapView mapView) {
		this.entities = new HashMap<Integer, List<Entity>>();
		this.placementManager = new PlacementManager(activity, mapView);

		this.types = new LinkedList<Integer>();
		this.types.add(ZOMBIES);
		this.types.add(CITIZEN);
		this.types.add(CHOPPER);
		this.types.add(BOMB);
	}

	/**
	 * Return all the entities of the type in parameter.
	 * 
	 * @param type
	 *            the type of the entities to return
	 * @return A list of the entities
	 */
	public List<Entity> getEntities(int type) {
		List<Entity> entities = this.entities.get(type);
		if (entities == null) {
			entities = new LinkedList<Entity>();
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
		IFactory factory = null;
		Entity entity = null;
		for (int i = 0; i < types.length; i++) {
			factory = FactoryManager.getFactory(types[i]);
			for (int j = 0; j <= numbers[i]; j++) {
				entity = factory.createEntity();
				getEntities().add(entity);
				this.placementManager.putEntityOnMap(entity);
			}
		}
	}

	/**
	 * Start the activity for all the entity for the type.
	 * 
	 * @param type
	 */
	public void start(int type) {
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
	public void stop(int type) {
		for (Entity entity : getEntities(type)) {
			this.placementManager.removeEntityFromMap(entity);
			entity.stop();
		}
	}
	
	/**
	 * Wait all the entities.
	 */
	public void waitAll(){
		for (Entity entity : getEntities()) {
			try {
				entity.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Resume all the entities.
	 */
	public void resumeAll(){
		notifyAll();
	}
}
