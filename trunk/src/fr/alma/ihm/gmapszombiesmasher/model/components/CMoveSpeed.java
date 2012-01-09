package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * 
 * Class which contains the movespeed of an entity ( Zombie, citizen, chopper ..
 * ) expressed in m/s
 */
public class CMoveSpeed extends Component {

	private double moveSpeed; // meter per second

	public CMoveSpeed(Entity parent) {
		super(parent);
		moveSpeed = 0;
	}

	public double getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

}
