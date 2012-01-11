package fr.alma.ihm.gmapszombiesmasher.exceptions;

public class AlreadyExistingWorldException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3159424174393670056L;
	
	public AlreadyExistingWorldException(){
		super("A world with the same name already exist. Please, try another name.");
	}

}
