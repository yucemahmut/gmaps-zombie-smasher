package fr.alma.ihm.gmapszombiesmasher.activities.game.listeners;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.activities.game.GameActivity;
import fr.alma.ihm.gmapszombiesmasher.activities.game.WaitingHandler;
import fr.alma.ihm.gmapszombiesmasher.model.components.CCoordinates;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;

public class GameOnTouchListener implements OnTouchListener {

	// CHOPPER
	public static final long CHOPPER_BUTTON_LIFE_TIME = 15000;

	// BOMB
	public static final long BOMB_BUTTON_LIFE_TIME = 10000;

	private GameActivity parent;

	private int buttonSelection = -1;
	private long buttonLifeTime = -1;

	private EntityManager entityManager;

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
			case EntityManager.CHOPPER:
				// If the chopper doesn't exist yet
				if (entityManager.getEntities(EntityManager.CHOPPER).size() == 0) {
					createEntity(EntityManager.CHOPPER, point);
				}
				break;
			case EntityManager.BOMB:
				// If the bomb doesn't exist yet
				if (entityManager.getEntities(EntityManager.BOMB).size() == 0) {
					createEntity(EntityManager.BOMB, point);
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
		switch (id) {
		case EntityManager.CHOPPER:
			entityManager.createEntity(EntityManager.CHOPPER, new CCoordinates(point));
			//entityManager.startType(EntityManager.CHOPPER);
			buttonSelection = WaitingHandler.CHOPPER_BUTTON_SELECTION;
			buttonLifeTime = CHOPPER_BUTTON_LIFE_TIME;
			break;

		case EntityManager.BOMB:
			entityManager.createEntity(EntityManager.BOMB, new CCoordinates(point));
			//entityManager.startType(EntityManager.BOMB);
			buttonSelection = WaitingHandler.BOMB_BUTTON_SELECTION;
			buttonLifeTime = BOMB_BUTTON_LIFE_TIME;
			break;
		}

		Runnable ButtonLifeTime = new Runnable() {
			@Override
			public void run() {
				int buttonID = buttonSelection;

				parent.removeSelectedButton();
				parent.getWaitingHandler().sendEmptyMessage(buttonID);
				SystemClock.sleep(buttonLifeTime);
				parent.getWaitingHandler().sendEmptyMessage(buttonID);
			}
		};

		Thread buttonLife = new Thread(ButtonLifeTime);
		buttonLife.start();
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
