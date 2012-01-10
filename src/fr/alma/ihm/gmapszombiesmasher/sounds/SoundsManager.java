package fr.alma.ihm.gmapszombiesmasher.sounds;

import java.util.HashMap;
import java.util.Map;

import fr.alma.ihm.gmapszombiesmasher.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundsManager {
	
	public static int EXPLOSION = 1;
	public static final int ZOMBIE_NOISE = 2;
	
	private Context context;
	private SoundPool sounds;
	private Map<Integer,Integer> soundMap;
	
	public SoundsManager(Context context) {
		super();
		this.context = context;
		soundMap = new HashMap<Integer,Integer>();
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		// sounds.load return a integer ID for every sound track and we put them in a hashmap
		soundMap.put(EXPLOSION, sounds.load(context, R.raw.explosion, 1));
		soundMap.put(ZOMBIE_NOISE, sounds.load(context, R.raw.zombie_noises, 1));
		
	}
	
	public int playSound(int soundID) {
		
		return sounds.play(soundMap.get(soundID), 1.0f, 1.0f, 1, 0, 1.5f);
	}
	
	

}
