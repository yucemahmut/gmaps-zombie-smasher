package fr.alma.ihm.gmapszombiesmasher.model.components;


/**
 * 
 * Class which contains the movespeed of an entity ( Zombie, citizen, chopper ..
 * ) expressed in m/s
 */
public class CMoveSpeed implements Component {

	private double moveSpeed; // meter per second

	public CMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public double getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

}
