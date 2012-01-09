package fr.alma.ihm.gmapszombiesmasher.model.components;

import java.util.LinkedList;
import java.util.Random;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Zombie will move to
 * the nearest citizen in order to eat him
 * 
 */
public class CAIZombie extends Component implements ICAI {

	private static int RANDOMBOUNDARY = 5; // bound of randomize a new target
	private LinkedList<Entity> citizens;

	public CAIZombie(Entity parent) {
		super(parent);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		// TODO fonction de recherche d'entit� citizen plus proche
		// Chercher dans le parent si il a un CGoal
		// Si ce n'est pas le cas , alors le cr�er
		if (!(getParent().getComponentMap().containsKey("CGoal"))) {
			getParent().addComponent(new CGoal(getParent()));
		}

		// Si c'est le cas, alors
		// Si le but est null
		CGoal g = (CGoal) getParent().getComponentMap().get("CGoal");

		if (citizens.size() > 0) { // si citoyen il y a
			Random random = new Random();
			int percent = random.nextInt(100);

			if (percent < this.RANDOMBOUNDARY) {
				// Getting a random citizen for target
				g.setGoal(citizens.get(random.nextInt()));
			} else {

				// TODO calcul des distances

			}
		} else {
			g.setGoal(null);
		}

		if (g.getGoal() != null) { // Une entit� est cibl�e
			// Par effet de bord, changer les variables de CCoordinates en
			// fonction
			// de la CMoveSpeed (ne pas oublier deltaTime => demander a Adrien
			// pour
			// plus de pr�cisions) et de la route trouv�e par Gmaps ( que l on
			// conserve en m�moire au cas o� la position
			// de l citizen ne change pas a la frame suivante )

		}

		// utilisation des distances calcul� pr�cedement pour la
		// Verification de proximit�
		// Si un citizen se trouve proche, il est transform� en zombi, son icone
		// devient celle d ' un zombi et son IA aussi, movespeed recalcul�

	}

	public LinkedList<Entity> getCitizens() {
		return citizens;
	}

	public void setCitizens(LinkedList<Entity> citizens) {
		this.citizens = citizens;
	}

}
