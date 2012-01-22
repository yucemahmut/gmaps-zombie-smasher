package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.GameActivity;
import fr.alma.ihm.gmapszombiesmasher.gMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.factories.BombFactory;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ChopperFactory;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;

public class GameOnTouchListener implements OnTouchListener {

	private GameActivity parent;
	private Spawn spawn;

	private int buttonSelection = -1;
	private long buttonLifeTime = -1;

	public GameOnTouchListener(GameActivity parent) {
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
			GeoPoint point = mV.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());

			switch (parent.getSelectedButton()) {
			case GameActivity.CHOPPER:
				// If the chopper doesn't exist yet
				if (spawn.getChopper() == null) {
					createEntity(GameActivity.CHOPPER, point);
				}
				break;
			case GameActivity.BOMB:
				// If the bomb doesn't exist yet
				if (spawn.getBomb() == null) {
					// Play explosion sound
					gMapsZombieSmasher.soundsManager.playSound(SoundsManager.EXPLOSION);
					createEntity(GameActivity.BOMB, point);
				}
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

	/**
	 * Create the entity
	 * 
	 * @param id
	 *            the is of the entity to create (chopper, bomb, etc ...)
	 * @param point
	 *            the GeoPoint where place the entity
	 */
	private void createEntity(int id, GeoPoint point) {
		Entity entity = null;

		switch (id) {
		case GameActivity.CHOPPER:
			entity = ChopperFactory.get();
			spawn.createChopper(entity);

			buttonSelection = GameActivity.CHOPPER_BUTTON_SELECTION;
			buttonLifeTime = GameActivity.CHOPPER_BUTTON_LIFE_TIME;
			break;

		case GameActivity.BOMB:
			entity = BombFactory.get();
			spawn.createBomb(entity);

			buttonSelection = GameActivity.BOMB_BUTTON_SELECTION;
			buttonLifeTime = GameActivity.BOMB_BUTTON_LIFE_TIME;
			break;
		}

		CCoordinates coordinates = new CCoordinates(entity);
		coordinates.setLatitude(point.getLatitudeE6());
		coordinates.setLongitude(point.getLongitudeE6());
		entity.addComponent(coordinates);

		Runnable ButtonLifeTime = new Runnable() {
			@Override
			public void run() {
        int buttonID = buttonSelection;

				parent.removeSelectedButton();
				parent.getWaitingHandler().sendEmptyMessage(buttonID);
				System.out.println("BEFORE SLEEP: " + buttonID);
				SystemClock.sleep(buttonLifeTime);
				System.out.println("AFTER SLEEP: " + buttonID);
				parent.getWaitingHandler().sendEmptyMessage(buttonID);
			}
		};

		Thread buttonLife = new Thread(ButtonLifeTime);
		buttonLife.start();
	}

	public void setSpawn(Spawn spawn) {
		this.spawn = spawn;
	}
}
