package fr.alma.ihm.gmapszombiesmasher.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import fr.alma.ihm.gmapszombiesmasher.R;

/**
 * Provide utilities to the GPS.
 */
public class GPSUtilities extends Activity {

	private static double latitude = 0;
	private static double longitude = 0;
	private ProgressDialog dialog;
	private LocationManager locationManager;
	private LocationListener locationListener;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getCurrentPosition();
	}

	/**
	 * Put the current position into the returned intent object has
	 * Doubles: latitude and longitude 
	 * 
	 */
	public void getCurrentPosition() {
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		
		// if GPS turned on
		if (isGpsEnabled()) {

			// Recupération des coordonnées GPS actuelles
			locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					// Called when a new location is found by the network
					// location provider.
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				}

				public void onStatusChanged(String provider, int status,
						Bundle extras) {}
				public void onProviderEnabled(String provider) {}
				public void onProviderDisabled(String provider) {}
			};

			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationListener);

			// Create a Runnable witch is used to determine when the first GPS
			// fix was received.
			Runnable showWaitDialog = new Runnable() {
				@Override
				public void run() {
					
					while ((latitude == 0 && longitude == 0)) {
						// Waiting...
					}
					
					// After receiving first GPS Fix dismiss the Progress Dialog
					dialog.dismiss();

					if (!(latitude == 0 && longitude == 0)) {
						Intent intent = new Intent();
						intent.putExtra("latitude", latitude);
						intent.putExtra("longitude", longitude);
						System.out.println("[GPS]  OK");
						GPSUtilities.this.setResult(RESULT_OK, intent);
					} else {
						System.out.println("[GPS]  CANCEL");
						GPSUtilities.this.setResult(RESULT_CANCELED);
					}
					
					GPSUtilities.this.finish();
				}
			};

			// Create a Dialog to let the User know that we're waiting for a GPS
			// Fix
			//protected void onStop ()
			dialog = ProgressDialog.show(this, getString(R.string.wait),
					getString(R.string.receiving), true, true, new DialogInterface.OnCancelListener(){

						@Override
						public void onCancel(DialogInterface arg0) {
							finish();
						}
				
			});

			Thread t = new Thread(showWaitDialog);
			t.start();
		}
	}

	public boolean isGpsEnabled() {
		// Detect if GPS is turned on or not
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createGpsDisabledAlert();
			return false;
		}
		
		return true;
	}

	private void createGpsDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.enable_gps_question))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.enable_gps),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showGpsOptions();
							}
						});
		builder.setNegativeButton(getString(R.string.disable_gps),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void showGpsOptions() {
		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		this.startActivity(gpsOptionsIntent);

		this.finish();
	}
}
