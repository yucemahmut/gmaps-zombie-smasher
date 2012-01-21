package fr.alma.ihm.gmapszombiesmasher;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import fr.alma.ihm.gmapszombiesmasher.listeners.ManageLevelsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.SelectWorldClickListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.SelectWorldLongClickListener;
import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;

public class PlayActivity extends Activity {
	
	public static final int EDIT_WORLD_CODE = 1;
	public static final int PLAY_CODE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);

		List<String> worlds = new LinkedList<String>();
		worlds.add("Play Here");
		worlds.addAll(ManageWorlds.getWorldsName());
		
		ListView lv = (ListView)this.findViewById(R.id.list_play);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_level,
						worlds));
		lv.setOnItemClickListener(new SelectWorldClickListener(this));
		lv.setOnItemLongClickListener(new SelectWorldLongClickListener(this));
		
		this.findViewById(R.id.new_world).setOnClickListener(new ManageLevelsButtonListener(this));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case EDIT_WORLD_CODE:
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "No changes done!",Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "World updated!", Toast.LENGTH_LONG).show();
			}
			break;
		case PLAY_CODE:
			if (resultCode == RESULT_OK) {
				Bundle objetbunble = data.getExtras();
				boolean win = objetbunble.getBoolean(GameActivity.END_GAME_WIN);
				double time = objetbunble.getDouble(GameActivity.END_GAME_TIME);
				int citizenSaved = objetbunble.getInt(GameActivity.END_GAME_CITIZEN_SAVED);
				int citizenKilled = objetbunble.getInt(GameActivity.END_GAME_CITIZEN_KILLED);
				int zombieKilled = objetbunble.getInt(GameActivity.END_GAME_ZOMBIES_KILLED);
				
				System.out.println("time: " + time);
				System.out.println("citizenSaved: " + citizenSaved);
				System.out.println("citizenKilled: " + citizenKilled);
				System.out.println("zombieKilled: " + zombieKilled);
				
				//Update Achievements
				AchievementsActivity.updateAchievement(AchievementsActivity.TOTAL_TIME_ACHIEVEMENT, time);
				AchievementsActivity.updateAchievement(AchievementsActivity.SAVED_CITIZEN_ACHIEVEMENT, citizenSaved - citizenKilled);
				AchievementsActivity.updateAchievement(AchievementsActivity.KILLED_ZOMBIE_ACHIEVEMENT, zombieKilled);
			}
			break;
		default:
			break;
		}
	}
}