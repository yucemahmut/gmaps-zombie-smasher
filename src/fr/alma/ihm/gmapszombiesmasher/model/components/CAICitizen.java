package fr.alma.ihm.gmapszombiesmasher.model.components;

import fr.alma.ihm.gmapszombiesmasher.model.Entity;

/**
 * 
 * Artificial intelligence of an Entity if it do have one. Citizen will move to
 * the chopper if one is around
 * 
 */
public class CAICitizen extends Component implements ICAI {

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
		// Alors chercher si une entité de chopper existe, la mettre dans le
		// CGoal

		// Modifier par effet de bord les CCoordinates en fonction de la
		// CMovespeed
		// (Prendre en compte le deltaTime) et se diriger vers l helicoptère
		// grace
		// a la route trouvée par Gmap ( que l on conserve en mémoire au cas où
		// la position
		// de l helico ne change pas a la frame suivante )

		// Si aucun Chopper trouvé , alors autistifier sur place, ou fuir zombie
		// proche =)
		// -> definir distance de fuite
	}

}
