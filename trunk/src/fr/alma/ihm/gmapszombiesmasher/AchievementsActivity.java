package fr.alma.ihm.gmapszombiesmasher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import fr.alma.ihm.gmapszombiesmasher.utils.PersistenceAccess;

public class AchievementsActivity extends Activity{
	
	private static final String ID = "id";
	private static final String IMG = "achievement_img";
	private static final String TITLE = "achievement_title";
	private static final String PROGRESS = "achievement_progress";
	
	public static final String SAVED_CITIZEN_ACHIEVEMENT = "saved_citizen";
	public static final String KILLED_ZOMBIE_ACHIEVEMENT = "killed_zombie";
	public static final String TOTAL_TIME_ACHIEVEMENT = "total_time";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achievements);
		
		//Récupération de la listview créée dans le fichier main.xml
		ListView maListViewPerso = (ListView) findViewById(R.id.achievements_list);
 
        //Création de la ArrayList qui nous permettra de remplire la listView
        List<Map<String, String>> listItem = getItemList();
 
        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.achievements_item,
               new String[] {IMG, TITLE, PROGRESS}, new int[] {R.id.achievement_img, R.id.achievement_title, R.id.achievement_progress});
 
        //On attribut à notre listView l'adapter que l'on vient de créer
        maListViewPerso.setAdapter(mSchedule);
	}
	
	/**
	 * Get the itemList from the file saved.
	 * If the list doesn't exist yet, it will be created
	 * @return le item list
	 */
	private static List<Map<String, String>> getItemList(){
		List<Map<String, String>> listItem = 
        		(List<Map<String, String>>)PersistenceAccess.getObject(PersistenceAccess.ACHIEVEMENTS_LIST);
		if(listItem == null){
			listItem = new ArrayList<Map<String, String>>();
			initList(listItem);
			PersistenceAccess.saveObject(PersistenceAccess.ACHIEVEMENTS_LIST, listItem);
		}
		
		System.out.println("ListItem:" + listItem);
		
		return listItem;
	}

	/**
	 * Initialise the list with achievements
	 * @param listItem the list to initialise
	 */
	private static void initList(List<Map<String, String>> listItem) {
		listItem.add(createAchievement(TOTAL_TIME_ACHIEVEMENT, R.drawable.chopper_marker, "Total Game Time"));
		listItem.add(createAchievement(SAVED_CITIZEN_ACHIEVEMENT, R.drawable.chopper_marker, "Citizens Saved"));
		listItem.add(createAchievement(KILLED_ZOMBIE_ACHIEVEMENT, R.drawable.bomb_marker, "Zombies Killed"));
	}
	
	/**
	 * Create a new achievement (in a map)
	 * @param id the type of the ichievement (static element of this class)
	 * @param img the identifier of the image
	 * @param title the title of the achivement
	 * @return
	 */
	private static Map<String, String> createAchievement(String id, int img, String title){
		Map<String, String> achievement = new HashMap<String, String>();
		achievement.put(ID, id);
		achievement.put(IMG, String.valueOf(img));
		achievement.put(TITLE, String.valueOf(title));
		achievement.put(PROGRESS, String.valueOf(0));
		
		return achievement;
	}
	
	/**
	 * Add the <i>update</i> value to the existing value of the Achievement <i>id</i>
	 * 
	 * @param id the Achievement to update
	 * @param update the value of the update
	 */
	public static void updateAchievement(String id, double update){
		List<Map<String, String>> listItem = getItemList();
		for(Map<String, String> map: listItem){
			if(map.get(ID).equals(id)){
				map.put(PROGRESS, String.valueOf(map.get(PROGRESS) + update));
			}
		}
		
		PersistenceAccess.saveObject(PersistenceAccess.ACHIEVEMENTS_LIST, listItem);
	}
}
