package fr.alma.ihm.gmapszombiesmasher.sounds;

import android.content.Context;

public class SoundsManagerFactory {
	
	public static SoundsManager soundsManager;
		
	public static SoundsManager get(Context c) {
		if(soundsManager == null)
			return new SoundsManager(c);
		else
			return soundsManager;
	}
	

}
