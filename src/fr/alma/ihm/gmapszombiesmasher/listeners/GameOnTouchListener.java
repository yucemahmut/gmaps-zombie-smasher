package fr.alma.ihm.gmapszombiesmasher.listeners;

import java.util.Map;

import android.app.Activity;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.GameActivity;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ChopperFactory;

public class GameOnTouchListener implements OnTouchListener {

	private Activity parent;
	private Map<Integer, Boolean> selected;
	private Spawn spawn;

	public GameOnTouchListener(Activity parent, Map<Integer, Boolean> selected) {
		super();
		this.parent = parent;
		this.selected = selected;
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

			System.out.println("Point: " + p.toString() + " - Selected: "
					+ getSelectedButton());

			switch (getSelectedButton()) {
			case GameActivity.CHOPPER:
				// If the chopper doesn't exist yet
				if (spawn.getChopper() == null) {
					Entity chopper = ChopperFactory.get();
					CCoordinates coordinates = new CCoordinates(chopper);
					coordinates.setLatitude(p.getLatitudeE6());
					coordinates.setLongitude(p.getLongitudeE6());
					chopper.addComponent(coordinates);
					spawn.createChopper(chopper);

					Runnable chopperLifeTime = new Runnable() {
						@Override
						public void run() {
							SystemClock.sleep(GameActivity.CHOPPER_LIFE_TIME);
							spawn.destroyChopper();
						}
					};

					new Thread(chopperLifeTime).start();
				}
				break;
			case GameActivity.BOMB:

				break;
			default:
				// Nothing
				break;
			}

			break;
		}
		}
		return false;

	}

	private int getSelectedButton() {
		for (int element : this.selected.keySet()) {
			if (this.selected.get(element)) {
				return element;
			}
		}
		return -1;
	}

	public void setSpawn(Spawn spawn) {
		this.spawn = spawn;
	}
}
