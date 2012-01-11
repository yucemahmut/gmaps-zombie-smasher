package fr.alma.ihm.gmapszombiesmasher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import fr.alma.ihm.gmapszombiesmasher.listeners.AchievementsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.ManageLevelsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.PlayButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.SettingsButtonListener;

public class gMapsZombieSmasher extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		init();

		this.findViewById(R.id.play).setOnClickListener(new PlayButtonListener(this));
		this.findViewById(R.id.level).setOnClickListener(new ManageLevelsButtonListener(this));
		this.findViewById(R.id.achievements).setOnClickListener(new AchievementsButtonListener(this));
		this.findViewById(R.id.settings).setOnClickListener(new SettingsButtonListener(this));
	}

	private void init() {
		// make sure we have a mounted SDCard
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// they don't have an SDCard, give them an error message and quit
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.welcome_dialog_sdcard_error)
					.setCancelable(false)
					.setPositiveButton(R.string.welcome_dialog_sdcard_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									finish();
								}
							});
			final AlertDialog alert = builder.create();
			alert.show();
		} else {

		}

	}
}
