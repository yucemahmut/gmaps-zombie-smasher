package fr.alma.ihm.gmapszombiesmasher;

import fr.alma.ihm.gmapszombiesmasher.sounds.BackgroundMusicService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.WindowManager;
import android.widget.Toast;

public class SettingPreferenceActivity extends PreferenceActivity {
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String BACKGROUND_MUSIC = "check_backgroundMusic";
	public static final String SATELLITE_VIEW_IN_MAP = "check_satellite";
	public static final String APPLICATION_SOUNDS = "check_sound";
	public static final String DIFICULTY_LEVEL = "level";
	
	private Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting_preference);   
        context = this;
        
        // background music checkbox action
        CheckBoxPreference backgroundBox = (CheckBoxPreference) findPreference(BACKGROUND_MUSIC);
        backgroundBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
        	@Override
        	public boolean onPreferenceChange(Preference arg0, Object newValue) {
        		Toast.makeText(context, "Background music " +  ((Boolean)newValue?"On":"Off"), Toast.LENGTH_SHORT).show();
    
        		if((Boolean)newValue){
        			context.startService(new Intent(context, BackgroundMusicService.class));
        		} else {
        			context.stopService(new Intent(context, BackgroundMusicService.class));
        		}        		
        		return true;
        	}
        } );
        
        // sound checkbox action
        CheckBoxPreference soundBox = (CheckBoxPreference) findPreference(APPLICATION_SOUNDS);
        soundBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
        	@Override
        	public boolean onPreferenceChange(Preference arg0, Object newValue) {
        		Toast.makeText(context, "Sound " +  ((Boolean)newValue?"On":"Off"), Toast.LENGTH_SHORT).show();
        		
        		// to do: sound control method
        		// ...        
        		
        		return true;
        	}
        } );
        
        // satelitte checkbox action
        CheckBoxPreference satelliteBox = (CheckBoxPreference) findPreference(SATELLITE_VIEW_IN_MAP);
        satelliteBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
        	@Override
        	public boolean onPreferenceChange(Preference arg0, Object newValue) {
        		Toast.makeText(context, "Satelitte " +  ((Boolean)newValue?"On":"Off"), Toast.LENGTH_SHORT).show();
        		
        		// to do: satelitte control method
        		// ...
        		
        		return true;
        	}
        } );
        
        // level action
        ListPreference levelList = (ListPreference) findPreference(DIFICULTY_LEVEL);
        levelList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
        	@Override
        	public boolean onPreferenceChange(Preference arg0, Object newValue) {
        		Toast.makeText(context, "Level " +  newValue + " choosed", Toast.LENGTH_SHORT).show();
        		
        		// to do: level control method
        		// ...
        		
        		return true;
        	}
        } );
    }
    
	@Override
    public void onAttachedToWindow() {
		// disable home button
    	this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    	super.onAttachedToWindow();
    
    }
    
    protected void onStop (){
    	super.onStop();
    	setResult(RESULT_OK);
    }
    

}