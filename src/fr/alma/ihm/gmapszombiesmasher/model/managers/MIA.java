package fr.alma.ihm.gmapszombiesmasher.model.managers;

import java.util.LinkedList;

import fr.alma.ihm.gmapszombiesmasher.model.components.ICAI;

/**
 * 
 * Manage AIs
 * 
 */
public class MIA implements IManager {

	private LinkedList<ICAI> ais;

	public MIA() {
		ais = new LinkedList<ICAI>();
	}

	public void addAI(ICAI ai) {
		ais.add(ai);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
