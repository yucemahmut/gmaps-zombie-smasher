package fr.alma.ihm.gmapszombiesmasher.activities.game;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.activities.game.listeners.GameOnTouchListener;
import fr.alma.ihm.gmapszombiesmasher.activities.main.GMapsZombieSmasher;
import fr.alma.ihm.gmapszombiesmasher.model.components.MapInformationUtilities;
import fr.alma.ihm.gmapszombiesmasher.model.managers.EntityManager;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;
import fr.alma.ihm.gmapszombiesmasher.utils.GPSCoordinate;
import fr.alma.ihm.gmapszombiesmasher.utils.GPSUtilities;
import fr.alma.ihm.gmapszombiesmasher.utils.ManagePreferences;
import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;
import fr.alma.ihm.gmapszombiesmasher.utils.World;

public class GameActivity extends MapActivity {

	protected static final long SLEEPING_TIME = 500;
	protected static final long TIME_BEFORE_NEXT_STEP = 100;

	private MapController mapController;
	private MapView mapView;
	private WaitingHandler waitingHandler;
	private ProgressDialog dialog;
	private int selectedButton;

	private boolean isSpawned;
	private boolean isStarted;

	private static final int GPS_CODE = 1;
	private static final int SETTINGS_CODE = 2;

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		initButtons();

		// Keep Screen On
		ManagePreferences.setKeepScreenOn(this);

		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();

		// Disable controls and set up the view
		mapView.setClickable(false);
		mapView.setFocusable(false);
		GameOnTouchListener onTouchListener = new GameOnTouchListener(this);
		mapView.setOnTouchListener(onTouchListener);
		mapView.setBuiltInZoomControls(false);

		mapView.setSatellite(ManagePreferences.isSateliteView(this));

		// New Waitting Handler
		waitingHandler = new WaitingHandler(this, mapView);
		waitingHandler.sendEntityManager(onTouchListener);

		// On récupère l'objet Bundle envoyé par l'autre Activity
		Bundle objetbunble = this.getIntent().getExtras();
		String worldName = objetbunble.getString("selectedWorldName");
		World world = null;
		if (worldName.equals(getString(R.string.play_here))) {
			isStarted = false;
			// Intent to get GPS coordinates
			Intent intent = new Intent().setClass(this, GPSUtilities.class);
			this.startActivityForResult(intent, 1);
		} else {
			isStarted = true;
			world = ManageWorlds.getWorld(worldName);
			mapController.setCenter(new GeoPoint(world.getLatitude(), world
					.getLongitude()));

			mapController.setZoom(world.getZoom());
			mapView.invalidate();
		}

	}

	/**
	 * Create a listener on each button and update "selectedButton" if one is
	 * selected
	 */
	private void initButtons() {

		// Listeners on buttons
		this.findViewById(R.id.helicopter_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectedButton = EntityManager.CHOPPER;
					}
				});

		this.findViewById(R.id.bomb_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectedButton = EntityManager.BOMB;

					}
				});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(isStarted)
			startGameProcess();
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("OnPause");
		if(isStarted)
			waitingHandler.sendEmptyMessage(WaitingHandler.WAIT_CODE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("OnResume");
		if(isStarted)
			waitingHandler.sendEmptyMessage(WaitingHandler.RESUME_CODE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("OnStop");
		if(isStarted)
			waitingHandler.sendEmptyMessage(WaitingHandler.STOP_CODE);
	}

	/**
	 * Start the main process that load the map, spawn the citizen and zombies
	 * on the map
	 */
	protected void startGameProcess() {

		Runnable spawLoop = new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(SLEEPING_TIME);
				
				while (!isFullyCharged() && mapView.isShown()) {
					System.out.println("NOT YET");
				}

				
				// Send a message to the handler
				waitingHandler.sendEmptyMessage(WaitingHandler.SPAWN_CODE);

				while (!isSpawned) {
					SystemClock.sleep(SLEEPING_TIME);
					// Waiting...
				}

				// Close waitting dialog
				dialog.dismiss();

				GMapsZombieSmasher.soundsManager
						.playSound(SoundsManager.BUILD_FINISHED);
				
				waitingHandler.sendEmptyMessage(WaitingHandler.START_CODE);
			}

			/**
			 * Return true is the map is charged. To know if the map is charge,
			 * we look to the top coordinates, if they are the same (= the
			 * center) the map is not charged.
			 * 
			 * @return true if the map is charged.
			 */
			private boolean isFullyCharged() {

				GeoPoint topLeft = mapView.getProjection().fromPixels(0, 0);
				GeoPoint topRight = mapView.getProjection().fromPixels(
						mapView.getWidth(), 0);

				return (topRight.getLongitudeE6() != topLeft.getLongitudeE6());
			}
		};

		dialog = ProgressDialog.show(GameActivity.this, "",
				getString(R.string.wait), true, true,
				new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface arg0) {
						finish();
					}

				});

		Thread spawnTread = new Thread(spawLoop);
		spawnTread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		waitingHandler.sendEmptyMessage(WaitingHandler.WAIT_CODE);
		inflater.inflate(R.menu.pause_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		waitingHandler.sendEmptyMessage(WaitingHandler.WAIT_CODE);
		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		waitingHandler.sendEmptyMessage(WaitingHandler.RESUME_CODE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.it_resume:
			waitingHandler.sendEmptyMessage(WaitingHandler.RESUME_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

	/**
	 * 
	 * @return the selected button (CHOPPER or BOMB)
	 */
	public int getSelectedButton() {
		return selectedButton;
	}

	/**
	 * Set that no button is selected
	 */
	public void removeSelectedButton() {
		selectedButton = -1;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("[RESULT] " + requestCode);
		switch (requestCode) {
		case GPS_CODE:
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this,
						getString(R.string.unable_current_location),
						Toast.LENGTH_LONG).show();
				this.finish();
			} else {
				Bundle objetbunble = data.getExtras();
				GPSCoordinate gpsCoordinate = new GPSCoordinate(
						objetbunble.getDouble("latitude"),
						objetbunble.getDouble("longitude"));

				World world = new World("Here",
						gpsCoordinate.getDecimalLongitude(),
						gpsCoordinate.getDecimalLatitude());

				mapController.setCenter(new GeoPoint(world.getLatitude(), world
						.getLongitude()));
				mapController.setZoom(MapInformationUtilities.ZOOM_LEVEL_MAX);
				
				startGameProcess();
			}
			break;
		case SETTINGS_CODE:
			mapView.setSatellite(ManagePreferences.isSateliteView(this));
			break;
		default:
			break;
		}
	}

	/**
	 * Return the WaitingHandler
	 * 
	 * @return the WaitingHandler
	 */
	public WaitingHandler getWaitingHandler() {
		return this.waitingHandler;
	}

	/**
	 * Set if the spawn of the entities is complete.
	 * 
	 * @param bool
	 *            true if the spawn is complete.
	 */
	public void setIsSpawned(boolean bool) {
		this.isSpawned = bool;
	}
}
