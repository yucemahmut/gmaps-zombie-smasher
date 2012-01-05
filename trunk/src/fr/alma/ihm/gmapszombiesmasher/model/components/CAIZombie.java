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
		// Si ce n'est pas le cas , alors le créer

		// Si c'est le cas, alors
		// Si le but est null, alors trouver une cible appropriée

		// Sinon
		// Signifie qu'un but est déjà fixé

		// CONSTANTE % de chances de prendre une cible aléatoire ( 1 a 10 % en
		// fonction du nombre de zombies )

		// Par effet de bord, changer les variables de CCoordinates en fonction
		// de la CMoveSpeed (ne pas oublier deltaTime => demander a Adrien pour
		// plus de précisions) et de la route trouvée par Gmaps

	}

}
