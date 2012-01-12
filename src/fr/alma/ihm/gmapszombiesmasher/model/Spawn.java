package fr.alma.ihm.gmapszombiesmasher.model;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.factories.CitizenFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ZombieFactory;

public class Spawn {
	
	private CitizenFactory citizenFactory;
	private ZombieFactory zombieFactory;
	
	private List<Overlay> mapOverlays;
	private Activity activity;
	public Spawn(Activity activity, MapView mapView, int topLatitude, int botLatitude, int leftLongitude,
			int rightLongitude) {
		super();
		
		this.activity = activity;
		
		this.citizenFactory = new CitizenFactory(topLatitude, botLatitude, leftLongitude, rightLongitude);
		this.zombieFactory = new ZombieFactory(topLatitude, botLatitude, leftLongitude, rightLongitude);

		mapOverlays = mapView.getOverlays();
		
	}
	
	public void spawnZombies(int number){
		for(int i=0; i<number; i++){
			putOnMap(zombieFactory.getZombie(), activity.getResources().getDrawable(R.drawable.zombie));
		}
	}

	public void spawnCitizen(int number){
		for(int i=0; i<number; i++){
			putOnMap(citizenFactory.getCitizen(), activity.getResources().getDrawable(R.drawable.citizen));
		}
	}
	
	private void putOnMap(Entity entity, Drawable drawable) {
		EntityOverlay entityOverlay = new EntityOverlay(drawable);
		CCoordinates coordinates = (CCoordinates)entity.getComponentMap().get(CCoordinates.class.getName());
		GeoPoint point = new GeoPoint(coordinates.getLatitude(), coordinates.getLongitude());
		OverlayItem overlayitem = new OverlayItem(point, null, null);
		entityOverlay.addOverlay(overlayitem);
		mapOverlays.add(entityOverlay);
	}
	
	
	
}
