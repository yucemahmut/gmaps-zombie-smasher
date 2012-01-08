package fr.alma.ihm.gmapszombiesmasher.views;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class GameOnTouchListener implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		MapView mV = (MapView) v;

		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {

			// Getting the coordinates of click
			GeoPoint p = mV.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());
			System.out.println(p.getLatitudeE6() / 1E6 + ","
					+ p.getLongitudeE6() / 1E6);
			break;
		}
		}
		return false;

	}
}