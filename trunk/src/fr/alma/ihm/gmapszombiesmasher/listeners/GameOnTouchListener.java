package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class GameOnTouchListener implements OnTouchListener {
	
	private Activity parent;
	
	public GameOnTouchListener(Activity parent) {
		super();
		this.parent = parent;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		MapView mV = (MapView) v;

		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {

			// Getting the coordinates of click
			GeoPoint p = mV.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());
			
			System.out.println("Point: " + p.toString());
			
			/*
			// pixel: x, y
			// TopLeft -> 0,0
			int height = mV.getHeight();
			int width = mV.getWidth();
			
			GeoPoint topLeft = mV.getProjection().fromPixels(0, 0);
			GeoPoint topRight = mV.getProjection().fromPixels(width, 0);
			GeoPoint botLeft = mV.getProjection().fromPixels(0,height);
			GeoPoint botRight = mV.getProjection().fromPixels(width, height);
			*/
			
			break;
		}
		}
		return false;

	}
}
