package fr.alma.ihm.gmapszombiesmasher.utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.os.Environment;

public class PersistenceAccess {
	
	private static final String SDCARD = Environment.getExternalStorageDirectory() + "/gmapszombiesmasher/";

	public static final String MANAGE_LEVELS_LIST = SDCARD + "manage_levels.alma";
	public static final String ACHIEVEMENTS_LIST = SDCARD + "achievements.alma";

	public static void saveObject(String objectName, Object object) {
		ObjectOutputStream oos = null;

		try {
			File sdcardFolder = new File(SDCARD);
			if(!sdcardFolder.exists()){
				sdcardFolder.mkdir();
			}
			
			File file = new File(objectName);
			// if the file exist, we delete it
			if(file.exists()){
				file.delete();
			}
			
			// Creation of the file
			file.createNewFile();
			
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(object);
			oos.flush();
		} catch (EOFException ex) {
			// nothing
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the ObjectOutputStream
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static Object getObject(String objectName) {
		Object returnedObject = null;
		ObjectInputStream ois = null;
		try {
			File file = new File(objectName);
			// Creation of the file if not exist
			file.createNewFile();
			
			FileInputStream fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			returnedObject = ois.readObject();
		} catch (EOFException ex) {
			// nothing
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Close the ObjectInputStream
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return returnedObject;
	}
}
