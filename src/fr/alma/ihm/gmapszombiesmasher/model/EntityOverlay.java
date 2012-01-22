package fr.alma.ihm.gmapszombiesmasher.model;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class EntityOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private boolean shadow;
	
	public EntityOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public void addOverlay(OverlayItem overlay, boolean shadow) {
	    mOverlays.add(overlay);
	    this.shadow = shadow;
	    populate();
	}
	
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow)
    {
		if(!shadow){
			super.draw(canvas, mapView, false);
		}
			
    }
}
