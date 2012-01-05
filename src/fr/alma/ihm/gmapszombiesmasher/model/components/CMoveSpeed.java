package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * 
 * Class which contains the movespeed of an entity ( Zombie, citizen, chopper .. )
 *
 */
public class CMoveSpeed extends Component{

	private double moveSpeed;
	
	
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
