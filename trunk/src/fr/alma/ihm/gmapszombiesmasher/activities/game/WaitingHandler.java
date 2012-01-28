package fr.alma.ihm.gmapszombiesmasher.activities.game;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.activities.game.listeners.GameOnTouchListener;
import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;
import fr.alma.ihm.gmapszombiesmasher.utils.ManagePreferences;

public class WaitingHandler extends Handler {

	// Handler's codes
	public static final int SPAWN_CODE = 1;
	public static final int START_CODE = 2;
	public static final int WAIT_CODE = 3;
	public static final int RESUME_CODE = 4;
	public static final int LOOP_CODE = 5;
	public static final int STOP_CODE = 6;
	public static final int END_GAME = 7;
	public static final int CHOPPER_BUTTON_SELECTION = 8;
	public static final int BOMB_BUTTON_SELECTION = 9;

	// Returned Intent parameters
	public static final String END_GAME_TIME = "time";
	public static final String END_GAME_ZOMBIES_KILLED = "zombiesKilled";
	public static final String END_GAME_CITIZEN_SAVED = "citizenSaved";
	public static final String END_GAME_CITIZEN_KILLED = "citizenKilled";
	public static final String END_GAME_WIN = "win";
	public static final String END_GAME_CITIZEN_EATED = "citizenEated";
	public static final String END_GAME_ZOMBIES_TOTAL = "zombiesTotal";

	private GameActivity activity;
	private EntityManager entityManager;
	private MapView mapView;

	private long startTime;
	private boolean win;
	private boolean onStop;
	private boolean onPause;
	private boolean isStarted;

	public WaitingHandler(GameActivity activity, MapView mapView) {
		this.activity = activity;
		this.mapView = mapView;
		this.entityManager = new EntityManager(activity, mapView, this);
		onStop = false;
		onPause = false;
		isStarted = false;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case SPAWN_CODE:
			int[] types = new int[] { EntityManager.CITIZEN,
					EntityManager.ZOMBIES };
			int[] numbers = new int[] {
					ManagePreferences.getCitizenNumber(this.activity),
					ManagePreferences.getZombieNumber(this.activity) };
			entityManager.spawn(types, numbers);
			mapView.invalidate();
			this.sendEmptyMessage(START_CODE);
			break;
		case START_CODE:
			entityManager.updateAll();
			startTime = Calendar.getInstance().getTimeInMillis();
			mapView.invalidate();
			activity.setIsSpawned(true);
			isStarted = true;
			startRefreshMapLoop();
			break;
		case WAIT_CODE:
			onPause = true;
			entityManager.waitAll();
			break;
		case RESUME_CODE:
			if (isStarted) {
				onPause = false;
				entityManager.resumeAll();
				this.sendEmptyMessage(LOOP_CODE);
			}
			break;
		case LOOP_CODE:
			startRefreshMapLoop();
			break;
		case STOP_CODE:
			onStop = true;
			entityManager.stopAll();
			this.activity.finish();
			break;
		case END_GAME:
			onStop = true;
			entityManager.stopAll();
			endGame();
			break;
		case CHOPPER_BUTTON_SELECTION:
			setSelected((Button) this.activity
					.findViewById(R.id.helicopter_button));
			break;
		case BOMB_BUTTON_SELECTION:
			setSelected((Button) this.activity.findViewById(R.id.bomb_button));
			break;
		}
	}

	/**
	 * Loop for refreshing the mapView.
	 */
	private void startRefreshMapLoop() {
		entityManager.updateAll();
		entityManager.draw();
		entityManager.updateTime();

		if (!onPause && !onStop) {
			this.sendEmptyMessage(LOOP_CODE);
		}
	}

	/**
	 * Get and return the results of the game to the PlayActivity
	 */
	private void endGame() {
		String textName = "";
		String buttonName = "";

		win = (entityManager.getReleasedCounter(EntityManager.CITIZEN) + entityManager
				.getInGameCounter(EntityManager.CITIZEN)) >= ManagePreferences
				.getMinCitizenSavedToWin(activity);
		if (win) {
			textName = activity.getString(R.string.you_win);
			buttonName = activity.getString(R.string.you_win_button);
			// Play victory music
			GMapsZombieSmasher.soundsManager.playSound(SoundsManager.VICTORY);
		} else {
			textName = activity.getString(R.string.you_loose);
			buttonName = activity.getString(R.string.you_loose_button);
			// Play lose music
			GMapsZombieSmasher.soundsManager.playSound(SoundsManager.LOSE);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(textName)
				.setCancelable(false)
				.setPositiveButton(buttonName,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent();
								intent.putExtra(END_GAME_WIN, win);
								intent.putExtra(END_GAME_TIME, Calendar
										.getInstance().getTimeInMillis()
										- startTime);
								intent.putExtra(
										END_GAME_CITIZEN_SAVED,
										entityManager
												.getReleasedCounter(EntityManager.CITIZEN)
												+ entityManager
														.getInGameCounter(EntityManager.CITIZEN));
								intent.putExtra(
										END_GAME_CITIZEN_EATED,
										entityManager
												.getEatedCounter(EntityManager.CITIZEN));
								intent.putExtra(
										END_GAME_CITIZEN_KILLED,
										entityManager
												.getKilledCounter(EntityManager.CITIZEN));
								intent.putExtra(
										END_GAME_ZOMBIES_KILLED,
										entityManager
												.getKilledCounter(EntityManager.ZOMBIES));
								intent.putExtra(
										END_GAME_ZOMBIES_TOTAL,
										ManagePreferences
												.getZombieNumber(activity)
												+ entityManager
														.getEatedCounter(EntityManager.CITIZEN));
								activity.setResult(Activity.RESULT_OK, intent);
								WaitingHandler.this.sendEmptyMessage(STOP_CODE);
							}
						}).create().show();
	}

	public void sendEntityManager(GameOnTouchListener listener) {
		listener.setEntityManager(this.entityManager);
	}

	/**
	 * Set if the button is selected or not
	 * 
	 * @param button
	 */
	public void setSelected(Button button) {
		if (button.isEnabled()) {
			button.setEnabled(false);
		} else {
			button.setEnabled(true);
		}
	}

}
