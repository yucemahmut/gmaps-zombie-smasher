package fr.alma.ihm.gmapszombiesmasher.model;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.components.CGoal;
import fr.alma.ihm.gmapszombiesmasher.model.factories.CitizenFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ZombieFactory;

public class Spawn {
	
	private CitizenFactory citizenFactory;
	private ZombieFactory zombieFactory;
	
	private List<Entity> zombies;
	private List<Entity> citizens;
	
	private List<Overlay> mapOverlays;
	private Activity activity;
	public Spawn(Activity activity, MapView mapView, int topLatitude, int botLatitude, int leftLongitude,
			int rightLongitude) {
		super();
		
		this.activity = activity;
		
		this.citizenFactory = new CitizenFactory(topLatitude, botLatitude, leftLongitude, rightLongitude);
		this.zombieFactory = new ZombieFactory(topLatitude, botLatitude, leftLongitude, rightLongitude);
		
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
	
}
