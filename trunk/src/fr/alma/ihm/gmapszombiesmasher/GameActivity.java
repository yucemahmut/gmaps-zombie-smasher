package fr.alma.ihm.gmapszombiesmasher;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import fr.alma.ihm.gmapszombiesmasher.listeners.GameOnTouchListener;
import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.Spawn;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAICitizen;
import fr.alma.ihm.gmapszombiesmasher.model.components.CAIZombie;
import fr.alma.ihm.gmapszombiesmasher.sounds.BackgroundMusicService;
import fr.alma.ihm.gmapszombiesmasher.sounds.SoundsManager;
import fr.alma.ihm.gmapszombiesmasher.utils.GPSCoordinate;
import fr.alma.ihm.gmapszombiesmasher.utils.GPSUtilities;
import fr.alma.ihm.gmapszombiesmasher.utils.ManagePreferences;
import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;
import fr.alma.ihm.gmapszombiesmasher.utils.World;

public class GameActivity extends MapActivity {

	public static final int ZOOM_LEVEL_MIN = 18;
	public static final int ZOOM_LEVEL_MAX = 16;
	protected static final long SLEEPING_TIME = 500;
	protected static final long TIME_BEFORE_NEXT_STEP = 100;
	// CHOPPER
	public static final int CHOPPER_LIFE_TIME = 10000;
	public static final long CHOPPER_BUTTON_LIFE_TIME = 15000;

	// BOMB
	public static final int BOMB_LIFE_TIME = 2000;
	public static final long BOMB_BUTTON_LIFE_TIME = 10000;

	private MapController mapController;
	private MapView mapView;
	private Spawn spawn;
	private Handler waittingHandler;
	private ProgressDialog dialog;
	private int selectedButton;
	private long startTime;
	private GameOnTouchListener onTouchListener;

	private static final int GPS_CODE = 1;
	private static final int SETTINGS_CODE = 2;

	public static final int CHOPPER = 0;
	public static final int BOMB = 1;

	protected static final int SPAWN_CODE = 1;
	protected static final int NEXT_STEP_CODE = 2;
	protected static final int WAIT_CODE = 3;
	protected static final int STOP_CODE = 4;
	protected static final int RESUME_CODE = 5;
	protected static final int REFRESH_MAP_CODE = 6;
	protected static final int CLEAR_MAP_CODE = 7;
	protected static final int END_GAME = 8;
	public static final int CHOPPER_BUTTON_SELECTION = 9;
	public static final int BOMB_BUTTON_SELECTION = 10;

	protected static boolean onPause = false;
	protected boolean notSpawn = true;
	protected boolean started = false;
	protected boolean endGame = false;
	private boolean win;

	public static final String END_GAME_TIME = "time";
	public static final String END_GAME_ZOMBIES_KILLED = "zombiesKilled";
	public static final String END_GAME_CITIZEN_SAVED = "citizenSaved";
	public static final String END_GAME_CITIZEN_KILLED = "citizenKilled";
	public static final String END_GAME_WIN = "win";
	public static final String END_GAME_CITIZEN_EATED = "citizenEated";
	public static final String END_GAME_ZOMBIES_TOTAL = "zombiesTotal";

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
		onTouchListener = new GameOnTouchListener(this);
		mapView.setOnTouchListener(onTouchListener);
		mapView.setBuiltInZoomControls(false);

		mapView.setSatellite(ManagePreferences.isSateliteView(this));

		// On récupère l'objet Bundle envoyé par l'autre Activity
		Bundle objetbunble = this.getIntent().getExtras();
		String worldName = objetbunble.getString("selectedWorldName");
		World world = null;
		if (worldName.equals("Play Here")) {

			// Intent to get GPS coordinates
			Intent intent = new Intent().setClass(this, GPSUtilities.class);
			this.startActivityForResult(intent, 1);

		} else {
			world = ManageWorlds.getWorld(worldName);
			mapController.setCenter(new GeoPoint(world.getLatitude(), world
					.getLongitude()));

			mapController.setZoom(world.getZoom());
			mapView.invalidate();
		}

	}

	/**
	 * 
	 * @return the current zoom level
	 */
	public int getZoom() {
		return mapView.getZoomLevel();
	}

	/**
	 * Thread that look if the game is ended
	 */
	private void startWatchingGame() {
		Runnable watchingGame = new Runnable() {

			@Override
			public void run() {
				while (!endGame) {
					if (GameActivity.this.spawn.getCitizenInGame() == 0
							|| GameActivity.this.spawn.getZombiesInGame() == 0) {
						endGame = true;
						waittingHandler.sendEmptyMessage(END_GAME);
					}
				}
			}
		};

		new Thread(watchingGame).start();
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
						selectedButton = CHOPPER;
					}
				});

		this.findViewById(R.id.bomb_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectedButton = BOMB;

					}
				});
	}

	@Override
	protected void onStart() {
		super.onStart();
		startGameProcess();
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("OnPause");
		waittingHandler.sendEmptyMessage(WAIT_CODE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("OnResume");
		waittingHandler.sendEmptyMessage(RESUME_CODE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("OnStop");
		waittingHandler.sendEmptyMessage(STOP_CODE);
	}

	/**
	 * Start the main process that load the map, spawn the citizen and zombies
	 * on the map
	 */
	protected void startGameProcess() {
		if (!started) {
			mapView.getOverlays().clear();
			started = true;

			Runnable spawLoop = new Runnable() {
				@Override
				public void run() {

					if (notSpawn) {
						while (!mapView.isShown()) {
							SystemClock.sleep(SLEEPING_TIME);
						}

						// Send a message to the handler
						waittingHandler.sendEmptyMessage(SPAWN_CODE);

						while (notSpawn) {
						}

						updatePlacementsEntities();

						// Close waitting dialog
						if (dialog.isShowing()) {
							dialog.dismiss();
						}

						startWatchingGame();
						startTime = Calendar.getInstance().getTimeInMillis();
						gMapsZombieSmasher.soundsManager
								.playSound(SoundsManager.BUILD_FINISHED);
						waittingHandler.sendEmptyMessage(NEXT_STEP_CODE);
					}
				}
			};

			if (notSpawn) {
				dialog = ProgressDialog.show(GameActivity.this, "",
						"Loading. Please wait...", true);
			}

			Thread spawnTread = new Thread(spawLoop);
			spawnTread.start();

			// Handler waitting for spawn
			/**
			 * Waitting handler that allways listen if an incoming message came.
			 * 
			 */
			waittingHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case SPAWN_CODE:
						spawn();
						break;
					case NEXT_STEP_CODE:
						nextStep();
						break;
					case WAIT_CODE:
						onPause = true;
						break;
					case STOP_CODE:
						started = false;
						onPause = false;
						notSpawn = true;
						GameActivity.this.finish();
						break;
					case RESUME_CODE:
						if (onPause) {
							onPause = false;
							waittingHandler.sendEmptyMessage(NEXT_STEP_CODE);
						}
						break;
					case CLEAR_MAP_CODE:
						mapView.getOverlays().clear();
						break;
					case REFRESH_MAP_CODE:
						mapView.invalidate();
					case CHOPPER_BUTTON_SELECTION:
            System.out.println("###########----#############");
						GameActivity.this
								.setSelected((Button) GameActivity.this
										.findViewById(R.id.helicopter_button));
						break;
					case BOMB_BUTTON_SELECTION:
						GameActivity.this
								.setSelected((Button) GameActivity.this
										.findViewById(R.id.bomb_button));
						break;
					case END_GAME:
						started = false;
						notSpawn = true;
						endGame();
						break;
					}
				}
			};
		}
	}

	/**
	 * Create and place citizen and zombies on the map Set a goal for each
	 */
	private void spawn() {
		int height = mapView.getHeight();
		int width = mapView.getWidth();

		GeoPoint topLeft = mapView.getProjection().fromPixels(0, 0);
		GeoPoint topRight = mapView.getProjection().fromPixels(width, 0);
		GeoPoint botLeft = mapView.getProjection().fromPixels(0, height);
		// GeoPoint botRight =
		// mapView.getProjection().fromPixels(width,height);

		spawn = new Spawn(GameActivity.this, mapView, topLeft.getLatitudeE6(),
				botLeft.getLatitudeE6(), topLeft.getLongitudeE6(),
				topRight.getLongitudeE6());
		spawn.spawnCitizen(ManagePreferences
				.getCitizenNumber(GameActivity.this));
		spawn.spawnZombies(ManagePreferences.getZombieNumber(GameActivity.this));

		spawn.putOnMap();
		notSpawn = false;

		onTouchListener.setSpawn(spawn);

	}

	/**
	 * Next step process - Move Citizen and zombie to next step
	 */
	private void nextStep() {

		// Update IA
		updatePlacementsEntities();

		// Put On Map
		spawn.putOnMap();

		// Refresh Map
		mapView.invalidate();

		// Next Step
		spawn.updateSeconds();
		if (!onPause && !endGame) {
			waittingHandler.sendEmptyMessage(NEXT_STEP_CODE);
		}
	}

	/**
	 * Update all the entities placements
	 */
	private void updatePlacementsEntities() {
		for (Entity entity : spawn.getEntities()) {
			if (entity.getComponentMap()
					.containsKey(CAICitizen.class.getName())) {
				((CAICitizen) entity.getComponentMap().get(
						CAICitizen.class.getName())).update();
			} else if (entity.getComponentMap().containsKey(
					CAIZombie.class.getName())) {
				((CAIZombie) entity.getComponentMap().get(
						CAIZombie.class.getName())).update();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		waittingHandler.sendEmptyMessage(WAIT_CODE);
		inflater.inflate(R.menu.pause_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		waittingHandler.sendEmptyMessage(WAIT_CODE);
		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		waittingHandler.sendEmptyMessage(RESUME_CODE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.it_resume:
			waittingHandler.sendEmptyMessage(RESUME_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Get and return the results of the game to the PlayActivity
	 */
	private void endGame() {
		String textName = "";
		String buttonName = "";

		System.out.println("TIME: " + new Date().getTime());
		
		win = (spawn.getCitizenFree() + spawn.getCitizenInGame()) >= ManagePreferences
				.getMinCitizenSavedToWin(this);
		if (win) {
			textName = "You Win !";
			buttonName = "Fu%k Yea";
			// update background music setting
			this.startService(new Intent(this, BackgroundMusicService.class));
		} else {
			textName = "You LOOSE !!!";
			buttonName = "Okay :(";
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(textName)
				.setCancelable(false)
				.setPositiveButton(buttonName,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent();
								intent.putExtra(END_GAME_WIN, win);
								System.out.println("TIIIIIIIIIIIIIIIIME: " +( Calendar
										.getInstance().getTimeInMillis()
										- startTime));

								intent.putExtra(END_GAME_TIME, Calendar
										.getInstance().getTimeInMillis()
										- startTime);
								intent.putExtra(END_GAME_CITIZEN_SAVED,
										spawn.getCitizenFree() + spawn.getCitizenInGame());
								intent.putExtra(END_GAME_CITIZEN_EATED,	spawn.getCitizenEated());
								intent.putExtra(END_GAME_CITIZEN_KILLED,
										spawn.getCitizenKilled());
								intent.putExtra(END_GAME_ZOMBIES_KILLED,
										spawn.getZombieKilled());
								intent.putExtra(END_GAME_ZOMBIES_TOTAL, ManagePreferences.getZombieNumber(GameActivity.this));
								GameActivity.this.setResult(RESULT_OK, intent);
								GameActivity.this.finish();
							}
						}).create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case GPS_CODE:
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Unable to get your current location",
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
				mapController.setZoom(ZOOM_LEVEL_MIN);
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
	 * Set if the button is selected or not
	 * 
	 * @param button
	 */
	public void setSelected(Button button) {
		System.out.println("IsEnabled: " + button.getId() + " -> "
				+ button.isEnabled());
		if (button.isEnabled()) {
			button.setEnabled(false);
		} else {
			button.setEnabled(true);
		}

	}

	/**
	 * 
	 * @return return the handler
	 */
	public Handler getWaitingHandler() {
		return waittingHandler;
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
}
