package fr.alma.ihm.gmapszombiesmasher;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fr.alma.ihm.gmapszombiesmasher.listeners.ManageLevelsButtonListener;
import fr.alma.ihm.gmapszombiesmasher.listeners.SelectWorldClickListener;

public class PlayActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);

		List<String> worlds = new LinkedList<String>();
		worlds.add("Play Here");
		
		ListView lv = (ListView)this.findViewById(R.id.list_play);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_level,
						worlds));
		lv.setOnItemClickListener(new SelectWorldClickListener(this));
		
		this.findViewById(R.id.new_world).setOnClickListener(new ManageLevelsButtonListener(this));
	}

}