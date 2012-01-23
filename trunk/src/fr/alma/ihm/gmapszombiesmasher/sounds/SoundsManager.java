package fr.alma.ihm.gmapszombiesmasher.sounds;

import java.util.HashMap;
import java.util.Map;

import fr.alma.ihm.gmapszombiesmasher.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundsManager {
	
	public static final int EXPLOSION = 1;
	public static final int VICTORY = 2;
	public static final int BACKGROUND = 3;
	public static final int EXPLOSION_2 = 4;
	public static final int MAN_DEAD = 5;
	public static final int WOMAN_DEAD = 6;
	public static final int ZOMBIE = 7;
	public static final int STANDING_BY = 8;
	public static final int BUILD_FINISHED = 9;
	public static final int SHIOO = 10;
	public static final int HELICOP = 11;
	public static final int FREEDEM = 12;
	public static final int GOGOGO = 13;
	
	
	private SoundPool sounds;
	private Map<Integer,Integer> soundMap;
	private boolean soundOn;
	
	public SoundsManager(Context context) {
		super();
		soundMap = new HashMap<Integer,Integer>();
		sounds = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
		setSoundOn(true);
		
		// sounds.load return a integer ID for every sound track and we put them in a hashmap
		soundMap.put(EXPLOSION, sounds.load(context, R.raw.explosion, 1));
		soundMap.put(VICTORY, sounds.load(context, R.raw.victory, 1));
		soundMap.put(BACKGROUND, sounds.load(context, R.raw.background, 1));
		soundMap.put(EXPLOSION_2, sounds.load(context, R.raw.explosion_2, 1));
		soundMap.put(MAN_DEAD, sounds.load(context, R.raw.man_dead, 1));
		soundMap.put(WOMAN_DEAD, sounds.load(context, R.raw.woman_dead, 1));
		soundMap.put(STANDING_BY, sounds.load(context, R.raw.standing_by, 1));
		soundMap.put(BUILD_FINISHED, sounds.load(context, R.raw.build_finished, 1));
		soundMap.put(SHIOO, sounds.load(context, R.raw.shioo, 1));
		soundMap.put(ZOMBIE, sounds.load(context, R.raw.zombie, 1));
		soundMap.put(HELICOP, sounds.load(context, R.raw.helicopt, 1));
		soundMap.put(FREEDEM, sounds.load(context, R.raw.imgoing, 1));
		soundMap.put(GOGOGO, sounds.load(context, R.raw.gogogo, 1));
		
	}
	
	public int playSound(int soundID) {
		if(isSoundOn())
			return sounds.play(soundMap.get(soundID), 1.0f, 1.0f, 1, 0, 1.0f);
		else
			return 0;
	}
	
	/**
	 * @param soundID sound to play
	 * @param rate 1.0 = normal playback, range 0.5 to 2.0
	 */
	public int playSound(int soundID, float rate) {
		if(isSoundOn())
			return sounds.play(soundMap.get(soundID), 1.0f, 1.0f, 1, 0, rate);
		else
			return 0;
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}
	
	

}
