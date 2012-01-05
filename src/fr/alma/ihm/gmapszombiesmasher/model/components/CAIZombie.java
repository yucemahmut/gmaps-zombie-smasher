package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Zombie will move to
 * the nearest citizen in order to eat him
 * 
 */
public class CAIZombie extends Component implements ICAI {

	public CAIZombie(Entity parent) {
		super(parent);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		// Chercher dans le parent si il a un CGoal
		// Si ce n'est pas le cas , alors le cr�er

		// Si c'est le cas, alors
		// Si le but est null, alors trouver une cible appropri�e

		// Sinon
		// Signifie qu'un but est d�j� fix�

		// CONSTANTE % de chances de prendre une cible al�atoire ( 1 a 10 % en
		// fonction du nombre de zombies )

		// Par effet de bord, changer les variables de CCoordinates en fonction
		// de la CMoveSpeed (ne pas oublier deltaTime => demander a Adrien pour
		// plus de pr�cisions) et de la route trouv�e par Gmaps

	}

}
