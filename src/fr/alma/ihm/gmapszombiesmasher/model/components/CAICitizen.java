package fr.alma.ihm.gmapszombiesmasher.model.components;

import java.util.LinkedList;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;
import fr.alma.ihm.gmapszombiesmasher.model.factories.ChopperFactory;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Citizen will move to
 * the chopper if one is around
 * 
 */
public class CAICitizen extends Component implements ICAI {

	private ChopperFactory cF = new ChopperFactory();
	private LinkedList<Entity> zombies;

	public CAICitizen(Entity parent) {
		super(parent);
	}

	@Override
	public void update() {
		// TODO implémente algo suivant :

		// Chercher le CGoal du parent
		// Si aucun , alors le créer
		if (!(getParent().getComponentMap().containsKey("CGoal"))) {
			getParent().addComponent(new CGoal(getParent()));
		}

		// Si la valeur du CGoal est null
		CGoal g = (CGoal) getParent().getComponentMap().get("CGoal");
		if (g.getGoal() == null) {
			// Alors chercher si une entité de chopper existe, la mettre dans le
			// CGoal
			g.setGoal(cF.get());
		}

		// Si le chopper n'a pas de destination sur la carte
		if (!(g.getGoal().getComponentMap().containsKey("CGoal"))) {
			// Ne rien faire
			// Autistifier sur place
			// TODO ou fuir zombie proche =)
		} else {

			// TODO Modifier par effet de bord les CCoordinates en fonction de
			// la
			// CMovespeed
			// (Prendre en compte le deltaTime) et se diriger vers l helicoptère
			// grace
			// a la route trouvée par Gmap ( que l on conserve en mémoire au cas
			// où
			// la position
			// de l helico ne change pas a la frame suivante )

		}
	}

	public ChopperFactory getcF() {
		return cF;
	}

	public void setcF(ChopperFactory cF) {
		this.cF = cF;
	}

	public LinkedList<Entity> getZombies() {
		return zombies;
	}

	public void setZombies(LinkedList<Entity> zombies) {
		this.zombies = zombies;
	}

}
