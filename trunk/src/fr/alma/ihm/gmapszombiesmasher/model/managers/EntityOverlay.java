package fr.alma.ihm.gmapszombiesmasher.model.managers;

import android.app.Activity;
import android.graphics.Canvas;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * Redefinition of the OverlayItem.
 */
public class EntityOverlay extends ItemizedOverlay<OverlayItem> {

	private Entity entity;

	public EntityOverlay(Entity entity, Activity activity) {
		super(boundCenter(activity.getResources().getDrawable(
				entity.getMarker().getIdMarker())));
		this.entity = entity;
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return new OverlayItem(entity.getCurrentPosition().getPoint(), null, null);
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (!shadow) {
			super.draw(canvas, mapView, false);
		}

	}
}
