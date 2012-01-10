package fr.alma.ihm.gmapszombiesmasher.sounds;

import android.content.Context;

public class SoundsManagerFactory {
		
	public static SoundsManager get(Context c) {

		return new SoundsManager(c);
	}
	

}
