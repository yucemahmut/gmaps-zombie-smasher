package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * 
 * The aim of an entity. For exemple, Zombies aim Citizens who aim Chopper 
 *
 */
public class CGoal extends Component {
  private static final String DIRECTION_URL = "http://maps.googleapis.com/maps/api/directions";
  private static final String DIRECTION_FORMAT = "json";
  private static final String DIRECTION_MODE = "walking";
  private static final boolean DIRECTION_ALTERNATIVES = false;

	Entity goal;
	
	public CGoal(Entity parent) {
		super(parent);
		goal = null; // If there ain't no goal
	}

	public Entity getGoal() {
		return goal;
	}
	
	public void setGoal(Entity goal) {
		this.goal = goal;

    // get the path from the parent position to the goal
    if(getParent().getComponentMap().containsKey(CCoordinates.class.getName())
       && goal.getComponentMap().containsKey(CCoordinates.class.getName())) {
      CCoordinates origin = (CCoordinates) getParent().getComponentMap().get(CCoordinates.class.getName());
      CCoordinates dest = (CCoordinates) this.goal.getComponentMap().get(CCoordinates.class.getName());
      double origin_lat = (double) origin.getLatitude() / 1000000.0;
      double origin_lon = (double) origin.getLongitude() / 1000000.0;
      double dest_lat = (double) dest.getLatitude() / 1000000.0;
      double dest_lon = (double) dest.getLongitude() / 1000000.0;
      String directionRequest = DIRECTION_URL + "/" + DIRECTION_FORMAT + "?"
                                + "origin=" + origin_lat + "," + origin_lon
                                + "&destination=" + dest_lat + "," + dest_lon
                                + "&mode="  + DIRECTION_MODE
                                + "&alternatives="
                                + (DIRECTION_ALTERNATIVES? "true" : "false");
      try {
        URL directionURL = new URL(directionRequest);
        URLConnection directionConnection = directionURL.openConnection();
        InputStreamReader response = new InputStreamReader(directionConnection.getInputStream());
        BufferedReader reader = new BufferedReader(response);
        StringBuffer jsonBuffer = new StringBuffer();
        String jsonDirection = "";

        for(String l = reader.readLine(); l != null; l = reader.readLine()) {
          jsonBuffer.append(l);
        }

        jsonDirection = jsonBuffer.toString();
      } catch(Exception e) {
        // TODO
      }
    } else {
      // TODO exception
    }
	}
	
}
