package fr.alma.ihm.gmapszombiesmasher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import fr.alma.ihm.gmapszombiesmasher.exceptions.AlreadyExistingWorldException;
import fr.alma.ihm.gmapszombiesmasher.utils.GPSCoordinate;
import fr.alma.ihm.gmapszombiesmasher.utils.GPSUtilities;
import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;
import fr.alma.ihm.gmapszombiesmasher.utils.World;

public class SelectWorldInMap extends MapActivity {

	private static final int ZOOM_LEVEL = 16;
	private static MapView mapView;
	private static MapController mapController;
	private static int longitude = 0;
	private static int latitude = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_world);

		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();

		mapView.setBuiltInZoomControls(true);
		mapController.setCenter(new GeoPoint(40769800, -73960500));
		mapController.setZoom(ZOOM_LEVEL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.select_world_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.get_current_position:

			Intent intent = new Intent().setClass(this, GPSUtilities.class);
			this.startActivityForResult(intent, 1);

			return true;
		case R.id.set_zoom:
			mapController.setZoom(ZOOM_LEVEL);

			return true;
		case R.id.select_world_here:
			longitude = mapView.getMapCenter().getLongitudeE6();
			latitude = mapView.getMapCenter().getLatitudeE6();
			World world = new World("", longitude, latitude);
			newNameDialog(world);

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {

			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Unable to get your current location",
						Toast.LENGTH_LONG).show();
			} else {
				Bundle objetbunble = data.getExtras();
				System.out.println("Object: " + objetbunble);
				setCurrentCenter(new GPSCoordinate(
						objetbunble.getDouble("latitude"),
						objetbunble.getDouble("longitude")));
			}
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCurrentCenter(GPSCoordinate gps) {
		mapController.setCenter(new GeoPoint(gps.getDecimalLatitude(), gps
				.getDecimalLongitude()));

	}

	/**
	 * Open a dialog and ask the name of the level choosen. Set the name into
	 * the world in parameter and it to the world manager. When the world is
	 * added, the current activuty is finished and the PlayActivity Called.
	 * 
	 * @param world
	 */
	private void newNameDialog(final World world) {
		// On instancie notre layout en tant que View
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory
				.inflate(R.layout.alert_world, null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(this);

		// On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		adb.setView(alertDialogView);

		// On donne un titre à l'AlertDialog
		adb.setTitle("New World Name");

		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(android.R.drawable.ic_dialog_alert);

		// On affecte un bouton "OK" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				// Lorsque l'on cliquera sur le bouton "OK", on récupère
				// l'EditText correspondant à notre vue personnalisée (cad à
				// alertDialogView)
				EditText newName = (EditText) alertDialogView
						.findViewById(R.id.new_world_name);

				world.setName(newName.getText().toString());
				world.setZoom(mapView.getZoomLevel());

				try {
					ManageWorlds.addWorld(world);

					SelectWorldInMap.this.finish();

					Intent intent = new Intent().setClass(
							SelectWorldInMap.this, PlayActivity.class);
					SelectWorldInMap.this.startActivity(intent);

				} catch (AlreadyExistingWorldException e) {
					Toast.makeText(SelectWorldInMap.this, e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//
			}
		});

		adb.show();
	}
}
