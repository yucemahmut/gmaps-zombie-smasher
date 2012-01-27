package fr.alma.ihm.gmapszombiesmasher.model.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * Manage all the placement on the map.
 * 
 */
public class PlacementManager {

	private Map<Entity, EntityOverlay> entitiesOverlays;
	private MapView mapView;
	private Activity activity;

	/**
	 * Constructor.
	 * 
	 * @param activity
	 *            The current actitvity.
	 * @param mapView
	 *            the mapview on to display.
	 */
	public PlacementManager(Activity activity, MapView mapView) {
		this.activity = activity;
		this.mapView = mapView;
		this.entitiesOverlays = new HashMap<Entity, EntityOverlay>();
	}
	
	public void draw(List<Entity> entities){
		mapView.getOverlays().clear();
		for(Entity entity: entities){
			putEntityOnMap(entity);
		}
		mapView.invalidate();
		
	}

	/**
	 * Put the entity on the map view.
	 * 
	 * @param entity
	 *            the entity to put on the map.
	 */
	private void putEntityOnMap(Entity entity) {
		if (entity.isExist()) {
			createOverlay(entity);
		} 
		/*
		else {
			removeEntityFromMap(entity);
		}
		*/
	}

	/**
	 * Remove the entity from the map.
	 * 
	 * @param entity
	 *            the entity to remove.
	 */
	public void removeEntityFromMap(Entity entity) {
		removeOverlay(entity);
	}

	/**
	 * Return the EntityOverlay according to the entity. Using singleton
	 * pattern: one overlay per entity.
	 * 
	 * @param entity
	 *            the entity.
	 * @return the overlay occording to this entity.
	 */
	private EntityOverlay createOverlay(Entity entity) {

		EntityOverlay overlay = new EntityOverlay(entity, this.activity);
		this.entitiesOverlays.put(entity, overlay);
		mapView.getOverlays().add(overlay);

		//return this.entitiesOverlays.get(entity);
		return overlay;
	}

	/**
	 * Remove the overlay of the map.
	 * 
	 * @param entity
	 *            the entity to remove.
	 */
	private void removeOverlay(Entity entity) {
		mapView.getOverlays().remove(createOverlay(entity));
		this.entitiesOverlays.remove(entity);
	}

}
