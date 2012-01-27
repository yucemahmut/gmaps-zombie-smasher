package fr.alma.ihm.gmapszombiesmasher.activities.play;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.activities.achievements.AchievementsActivity;
import fr.alma.ihm.gmapszombiesmasher.activities.game.WaitingHandler;
import fr.alma.ihm.gmapszombiesmasher.activities.play.listeners.ManageLevelsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.activities.play.listeners.SelectWorldClickListener;
import fr.alma.ihm.gmapszombiesmasher.activities.play.listeners.SelectWorldLongClickListener;
import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;

public class PlayActivity extends Activity {

	public static final int EDIT_WORLD_CODE = 1;
	public static final int PLAY_CODE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);

		List<String> worlds = new LinkedList<String>();
		worlds.add(getString(R.string.play_here));
		worlds.addAll(ManageWorlds.getWorldsName());

		ListView lv = (ListView) this.findViewById(R.id.list_play);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_level,
				worlds));
		lv.setOnItemClickListener(new SelectWorldClickListener(this));
		lv.setOnItemLongClickListener(new SelectWorldLongClickListener(this));

		this.findViewById(R.id.new_world).setOnClickListener(
				new ManageLevelsButtonListener(this));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case EDIT_WORLD_CODE:
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, getString(R.string.no_changes),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, getString(R.string.changes),
						Toast.LENGTH_LONG).show();
			}
			break;
		case PLAY_CODE:
			if (resultCode == RESULT_OK) {
				Bundle objetbunble = data.getExtras();
				boolean win = objetbunble.getBoolean(WaitingHandler.END_GAME_WIN);
				long time = objetbunble.getLong(WaitingHandler.END_GAME_TIME);
				int citizenSaved = objetbunble
						.getInt(WaitingHandler.END_GAME_CITIZEN_SAVED);
				int citizenKilled = objetbunble
						.getInt(WaitingHandler.END_GAME_CITIZEN_KILLED);
				int citizenEated = objetbunble
						.getInt(WaitingHandler.END_GAME_CITIZEN_EATED);
				int zombieKilled = objetbunble
						.getInt(WaitingHandler.END_GAME_ZOMBIES_KILLED);
				int zombieTotal = objetbunble
						.getInt(WaitingHandler.END_GAME_ZOMBIES_TOTAL);

				displayResults(time, citizenSaved + citizenKilled
						+ citizenEated, citizenSaved, zombieTotal,
						zombieKilled, win);

				System.out.println("TIME: " + time);
				AchievementsActivity achievementsActivity = new AchievementsActivity(
						this);

				// Update Achievements
				achievementsActivity.updateAchievement(
						AchievementsActivity.TOTAL_TIME_ACHIEVEMENT, time);
				achievementsActivity.updateAchievement(
						AchievementsActivity.SAVED_CITIZEN_ACHIEVEMENT,
						citizenSaved);
				achievementsActivity.updateAchievement(
						AchievementsActivity.KILLED_CITIZENS_ACHIEVEMENT,
						citizenKilled);
				achievementsActivity.updateAchievement(
						AchievementsActivity.EATED_CITIZENS_ACHIEVEMENT,
						citizenEated);
				achievementsActivity.updateAchievement(
						AchievementsActivity.KILLED_ZOMBIE_ACHIEVEMENT,
						zombieKilled);
			}
			break;
		default:
			break;
		}
	}

	private void displayResults(double time, int citizens, int savedCitizens,
			int zombies, int killedZombies, boolean win) {
		View resultView = getLayoutInflater().inflate(R.layout.game_results,
				null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String playedTime = "  " + time / 1000 + " seconds";
		String citizensStat = "  " + savedCitizens + " / " + citizens;
		String zombiesStat = "  " + killedZombies + " / " + zombies;
		String closeText = (win) ? ":)" : ":(";
		TextView playedTimeView = (TextView) resultView
				.findViewById(R.id.played_time_value);
		TextView citizensView = (TextView) resultView
				.findViewById(R.id.citizens_saved_value);
		TextView zombiesView = (TextView) resultView
				.findViewById(R.id.zombies_killed_value);

		playedTimeView.setText(playedTime);
		citizensView.setText(citizensStat);
		zombiesView.setText(zombiesStat);

		builder.setTitle("Results");
		builder.setView(resultView);
		builder.setPositiveButton(closeText,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		builder.show();
	}
}
