package fr.alma.ihm.gmapszombiesmasher;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingPreferenceActivity extends PreferenceActivity {
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String SATELLITE_VIEW_IN_MAP = "check_satellite";
	public static final String APPLICATION_SOUNDS = "check_sound";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting_preference);        
    }
    
    protected void onStop (){
    	super.onStop();
    	setResult(RESULT_OK);
    }
}