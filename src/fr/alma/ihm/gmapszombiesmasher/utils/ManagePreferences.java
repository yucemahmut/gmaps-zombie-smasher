package fr.alma.ihm.gmapszombiesmasher.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import fr.alma.ihm.gmapszombiesmasher.SettingPreferenceActivity;

public class ManagePreferences {

	/**
	 * Get on the preference settings if the satellite mode view is on or not
	 * 
	 * @param activity the current activity
	 * @return true if the satellite mode is on
	 */
	public static boolean isSateliteView(Activity activity) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(activity);
		return settings.getBoolean(
				SettingPreferenceActivity.SATELLITE_VIEW_IN_MAP, false);
	}

	/**
	 * Keep the screen on for the activity
	 * 
	 * @param activity the current activity
	 */
	public static void setKeepScreenOn(Activity activity) {
		Window w = activity.getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	/**
	 * Set the Screen Lock on or not
	 * 
	 * @param activity the current activity
	 * @param bool to lock or not the screen
	 */
	public static void setScreenLock(Activity activity, boolean bool) {
		// To desable screen lock, need to do "lock.reanable()" at the end of
		// the activity
		// Problem if the person kill the process (the screen lock will be
		// disabled)
		KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Activity.KEYGUARD_SERVICE);
		KeyguardLock lock = keyguardManager.newKeyguardLock(Activity.KEYGUARD_SERVICE);
		
		if(bool){
			lock.disableKeyguard();
		} else {
			lock.reenableKeyguard();
		}
	}
	
	

}
