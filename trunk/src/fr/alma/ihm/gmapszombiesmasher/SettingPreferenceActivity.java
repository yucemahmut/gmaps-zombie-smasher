package fr.alma.ihm.gmapszombiesmasher;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingPreferenceActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting_preference);
    }   
}