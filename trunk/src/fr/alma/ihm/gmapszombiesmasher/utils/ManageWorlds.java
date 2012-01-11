package fr.alma.ihm.gmapszombiesmasher.utils;

import java.util.LinkedList;
import java.util.List;

import fr.alma.ihm.gmapszombiesmasher.exceptions.AlreadyExistingWorldException;

public class ManageWorlds {

	private final static String DEFAULT_WORLD_NAME = "Manhattan";
	private final static int DEFAULT_WORLD_LONGITUDE = -73960500;
	private final static int DEFAULT_WORLD_LATITUDE = 40769800;
	private final static int DEFAULT_WORLD_ZOOM = 18;
	
	private static List<World> worlds;
	
	@SuppressWarnings("unchecked")
	public ManageWorlds(){
		worlds = (List<World>) PersistenceAccess.getObject(PersistenceAccess.MANAGE_LEVELS_LIST);
		if(worlds == null){
			worlds = new LinkedList<World>();
		}
	}
	
	/**
	 * Return all the current world
	 * 
	 * @return all the currents worlds
	 */
	public static List<World> getWorlds(){
		return worlds;
	}
	
	/**
	 * Delete a world of the list
	 * 
	 * @param world
	 */
	public static void deleteWorld(World world){
		worlds.remove(world);
		PersistenceAccess.saveObject(PersistenceAccess.MANAGE_LEVELS_LIST, worlds);
	}
	
	/**
	 * Delete a world of the list
	 * 
	 * @param world
	 */
	public static void deleteWorld(String worldName){
		if(worlds == null){
			getWorld();
		}
		deleteWorld(getWorld(worldName));
	}
	
	/**
	 * Add a new world to the list
	 * 
	 * @param world
	 * @throws AlreadyExistingWorldException
	 */
	public static void addWorld(World world) throws AlreadyExistingWorldException{
		if(worlds == null){
			getWorld();
		}
		if(getWorld(world.getName()) != null){
			throw new AlreadyExistingWorldException();
		}
		worlds.add(world);
		PersistenceAccess.saveObject(PersistenceAccess.MANAGE_LEVELS_LIST, worlds);
	}
	
	/**
	 * Return the world with the name "name"
	 * 
	 * @param name the name of the world
	 * @return the world with the name (null if the world dosen't exist)
	 */
	public static World getWorld(String name){
		if(worlds == null){
			getWorld();
		}
		for(World world: worlds){
			if(world.getName().equals(name)){
				return world;
			}
		}
		return null;
	}
	
	/**
	 * Return a list of all the worlds' name
	 * 
	 * @return the list
	 */
	public static List<String> getWorldsName(){
		if(worlds == null){
			getWorld();
		}
		List<String> names = new LinkedList<String>();
		for(World world: worlds){
			names.add(world.getName());
		}
		
		return names;
	}
	
	@SuppressWarnings("unchecked")
	private static void getWorld() {
		worlds = (List<World>) PersistenceAccess.getObject(PersistenceAccess.MANAGE_LEVELS_LIST);
		if(worlds == null){
			worlds = new LinkedList<World>();
			World world = new World(DEFAULT_WORLD_NAME, DEFAULT_WORLD_LONGITUDE, DEFAULT_WORLD_LATITUDE);
			world.setZoom(DEFAULT_WORLD_ZOOM);
			worlds.add(world);
		}
	}

	public static void renameWorld(String worldName, String newName) throws AlreadyExistingWorldException{
		if(worlds == null){
			getWorld();
		}
		if(getWorld(newName) != null){
			throw new AlreadyExistingWorldException();
		}
		World world = getWorld(worldName);
		world.setName(newName);
		PersistenceAccess.saveObject(PersistenceAccess.MANAGE_LEVELS_LIST, worlds);
	}
	
	public static World getWorld(int position){
		if(worlds == null){
			getWorld();
		}

		return worlds.get(position - 1);
	}
}
