package fr.alma.ihm.gmapszombiesmasher.listeners;

import java.util.Map;

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
	private Map<Integer, Boolean> selected;
	private Spawn spawn;

	private int selectedButton = -1;
	private long lifeTime = -1;
	private int buttonSelection = -1;
	private long buttonLifeTime = -1;

	public GameOnTouchListener(GameActivity parent,
			Map<Integer, Boolean> selected) {
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
			GeoPoint point = mV.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());

			System.out.println("Point: " + point.toString() + " - Selected: "
					+ getSelectedButton());

			switch (getSelectedButton()) {
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
		selectedButton = id;

		switch (id) {
		case GameActivity.CHOPPER:
			entity = ChopperFactory.get();
			spawn.createChopper(entity);

			lifeTime = GameActivity.CHOPPER_LIFE_TIME;
			buttonSelection = GameActivity.CHOPPER_BUTTON_SELECTION;
			buttonLifeTime = GameActivity.CHOPPER_BUTTON_LIFE_TIME;
			break;

		case GameActivity.BOMB:
			entity = BombFactory.get();
			spawn.createBomb(entity);

			lifeTime = GameActivity.BOMB_LIFE_TIME;
			buttonSelection = GameActivity.BOMB_BUTTON_SELECTION;
			buttonLifeTime = GameActivity.BOMB_BUTTON_LIFE_TIME;
			break;
		}

		CCoordinates coordinates = new CCoordinates(entity);
		coordinates.setLatitude(point.getLatitudeE6());
		coordinates.setLongitude(point.getLongitudeE6());
		entity.addComponent(coordinates);

		Runnable LifeTime = new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(lifeTime);
				switch (selectedButton) {
				case GameActivity.CHOPPER:
					spawn.destroyChopper();
					break;
				case GameActivity.BOMB:
					spawn.destroyBomb();
					break;
				}
				selected.put(selectedButton, false);
			}
		};

		Runnable ButtonLifeTime = new Runnable() {
			@Override
			public void run() {
				parent.getWaitingHandler().sendEmptyMessage(buttonSelection);
				SystemClock.sleep(buttonLifeTime);
				parent.getWaitingHandler().sendEmptyMessage(buttonSelection);
				parent.putAllOtherToFalse(-1);
			}
		};

		new Thread(ButtonLifeTime).start();
		new Thread(LifeTime).start();
	}

	/**
	 * Get The selected button: - -1 -> nothing selected - 0 -> CHOPPER - 1 ->
	 * BOMB
	 * 
	 * @return
	 */
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
