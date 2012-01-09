package fr.alma.ihm.gmapszombiesmasher.model.managers;

import java.util.LinkedList;

/**
 * 
 * Processing all changes for each frame
 * 
 */
public class MFrame implements IManager {

	private LinkedList<IManager> managers;

	public MFrame() {
		managers = new LinkedList<IManager>();
	}

	public void addAI(IManager manager) {
		managers.add(manager);
	}

	@Override
	public void update() {
		for (IManager manager : managers) {
			manager.update();
		}

	}

}
