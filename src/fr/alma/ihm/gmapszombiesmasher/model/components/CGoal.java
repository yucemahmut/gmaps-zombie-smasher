package fr.alma.ihm.gmapszombiesmasher.model.components;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.os.SystemClock;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

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

	private Entity goal;
	private CCoordinates lastGoalCoordinates;
	private JSONObject roadSteps;
	private int currentStep;

	public CGoal(Entity parent) {
		super(parent);
		goal = null; // If there ain't no goal
		lastGoalCoordinates = null;
		roadSteps = null;
		currentStep = 0;
	}

	public Entity getGoal() {
		return goal;
	}

	/**
	 * Assign a new target to the parent entity.
	 * 
	 * @param goal
	 *            The target entity of the parent entity.
	 * @return The initial coordinates of the parent entity. NULL if the goal
	 *         isn't valide.
	 */
	public CCoordinates setGoal(Entity goal) {
		this.goal = goal;

		if (goal.getComponentMap().containsKey(CCoordinates.class.getName())) {
			lastGoalCoordinates = (CCoordinates) goal.getComponentMap().get(
					CCoordinates.class.getName());
		}

		return updateSteps();
	}

	private CCoordinates updateSteps() {
		CCoordinates startCoordinates = null;
		String jsonString = "";
		
		// get the path from the parent position to the goal
		if (getParent().getComponentMap().containsKey(
				CCoordinates.class.getName())
				&& goal.getComponentMap().containsKey(
						CCoordinates.class.getName())) {
			CCoordinates origin = (CCoordinates) getParent().getComponentMap()
					.get(CCoordinates.class.getName());
			CCoordinates dest = (CCoordinates) this.goal.getComponentMap().get(
					CCoordinates.class.getName());
			double origin_lat = (double) origin.getLatitude() / 1000000.0;
			double origin_lon = (double) origin.getLongitude() / 1000000.0;
			double dest_lat = (double) dest.getLatitude() / 1000000.0;
			double dest_lon = (double) dest.getLongitude() / 1000000.0;
			String directionRequest = DIRECTION_URL + "/" + DIRECTION_FORMAT
					+ "?" + "origin=" + origin_lat + "," + origin_lon
					+ "&destination=" + dest_lat + ","
					+ dest_lon
					+ "&sensor=false" // dunno why
					+ "&mode=" + DIRECTION_MODE + "&alternatives="
					+ (DIRECTION_ALTERNATIVES ? "true" : "false");
			try {
				URL directionURL = new URL(directionRequest);
				URLConnection directionConnection = directionURL
						.openConnection();
				InputStreamReader response = new InputStreamReader(
						directionConnection.getInputStream());
				BufferedReader reader = new BufferedReader(response);
				StringBuffer jsonBuffer = new StringBuffer();
				JSONObject jsonDirection = null;

				for (String l = reader.readLine(); l != null; l = reader
						.readLine()) {
					jsonBuffer.append(l);
				}

				jsonString = jsonBuffer.toString();
				jsonDirection = new JSONObject(jsonString);

				// take the first given road
				roadSteps = jsonDirection.getJSONArray("routes")
						.getJSONObject(0).getJSONArray("legs").getJSONObject(0);
				currentStep = 0;

				// initialize the entity position
				JSONObject firstStep = roadSteps.getJSONArray("steps")
						.getJSONObject(0);
				String firstLat = firstStep.getJSONObject("start_location")
						.optString("lat");
				String firstLon = firstStep.getJSONObject("start_location")
						.optString("lng");

				startCoordinates = new CCoordinates(getParent());
				startCoordinates
						.setLatitude((int) (Float.parseFloat(firstLat) * 1e6));
				startCoordinates
						.setLongitude((int) (Float.parseFloat(firstLon) * 1e6));

			} catch (Exception e) {
				System.out.println("String: " + jsonString);
				e.printStackTrace();
				SystemClock.sleep(250);
				startCoordinates = updateSteps();
			}
		} else {
			// TODO exception
		}

		return startCoordinates;
	}

	/**
	 * Determine the new position of an entity, regarding its current location,
	 * its speed and the delta calculated with the frame rate.
	 * 
	 * @param delta
	 *            Time related variable.
	 * @return The new coordinate of the entity. NULL if there is no valide
	 *         goal.
	 */
	public CCoordinates getNextPosition(double delta) {
		CCoordinates coordinates = null;

		if (roadSteps != null) {

			if (getParent().getComponentMap().containsKey(
					CCoordinates.class.getName())
					&& goal.getComponentMap().containsKey(
							CCoordinates.class.getName())) {
				try {
					JSONObject step = roadSteps.getJSONArray("steps")
							.getJSONObject(currentStep);
					CCoordinates currentCoordinates = (CCoordinates) getParent()
							.getComponentMap()
							.get(CCoordinates.class.getName());
					String lastStepLat = step.getJSONObject("end_location")
							.optString("lat");
					String lastStepLon = step.getJSONObject("end_location")
							.optString("lng");
					CCoordinates lastStepCoordinates = new CCoordinates(
							getParent());
          CMoveSpeed speed = null;
					double distance = 2.0;

					// recalculate the steps if the target has moved
					if (!goal.getComponentMap()
							.get(CCoordinates.class.getName())
							.equals(lastGoalCoordinates)) {
						currentCoordinates = updateSteps();
						lastGoalCoordinates = (CCoordinates) goal
								.getComponentMap().get(
										CCoordinates.class.getName());
					}

					coordinates = new CCoordinates(getParent());

					// calculate the traveled distance using the speed and the delta
          if(getParent().getComponentMap().containsKey(CMoveSpeed.class.getName())) {
            speed = (CMoveSpeed) getParent().getComponentMap().get(CMoveSpeed.class.getName());
            distance = speed.getMoveSpeed() * delta;
          }

          // get the last position of the current step
					lastStepCoordinates.setLatitude((int) (Float
							.parseFloat(lastStepLat) * 1e6));
					lastStepCoordinates.setLongitude((int) (Float
							.parseFloat(lastStepLon) * 1e6));

					if (currentCoordinates.distanceTo(lastStepCoordinates) > distance) {
						// report this distance on the road
						int u = (int) ((distance * (lastStepCoordinates
								.getLatitude() - currentCoordinates
								.getLatitude())) / currentCoordinates
								.distanceTo(lastStepCoordinates));
						int v = (int) ((distance * (lastStepCoordinates
								.getLongitude() - currentCoordinates
								.getLongitude())) / currentCoordinates
								.distanceTo(lastStepCoordinates));
						coordinates.setLatitude(currentCoordinates
								.getLatitude() + u);
						coordinates.setLongitude(currentCoordinates
								.getLongitude() + v);
					} else {
						coordinates = lastStepCoordinates;
						// pass to the next step
						if (this.currentStep < (roadSteps.getJSONArray("steps")
								.length() - 1)) {
							++this.currentStep;
							// the goal is reached
						} else {
							this.goal = null;
							this.lastGoalCoordinates = null;
							this.roadSteps = null;
							this.currentStep = 0;
						}
					}
				} catch (Exception e) {
					//FIXME
					e.printStackTrace();
				}
			}
		}

		return coordinates;
	}

	/**
	 * Indicates if the parent entity as a goal or not.
	 * 
	 * @return True if there is no current goal, else false.
	 */
	public boolean goalReached() {
		return goal == (null);
	}

	/**
	 * @return the lastGoalCoordinates
	 */
	public CCoordinates getGoalCoordinates() {
		return lastGoalCoordinates;
	}
}
