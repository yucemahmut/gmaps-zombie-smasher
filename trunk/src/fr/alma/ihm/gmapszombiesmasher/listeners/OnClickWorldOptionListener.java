package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import fr.alma.ihm.gmapszombiesmasher.EditWorldInMap;
import fr.alma.ihm.gmapszombiesmasher.PlayActivity;
import fr.alma.ihm.gmapszombiesmasher.R;
import fr.alma.ihm.gmapszombiesmasher.exceptions.AlreadyExistingWorldException;
import fr.alma.ihm.gmapszombiesmasher.utils.ManageWorlds;

public class OnClickWorldOptionListener implements OnClickListener {
	
	private Activity parent;
	private String selectedWorldName;

	public OnClickWorldOptionListener(Activity parent, String selectedWorldName) {
		this.parent = parent;
		this.selectedWorldName = selectedWorldName;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		switch (which) {
		// 0 -> Edit
		case 0:
			Intent intent = new Intent().setClass(parent, EditWorldInMap.class);
			intent.putExtra("worldEditName", this.selectedWorldName);
			parent.startActivityForResult(intent, PlayActivity.EDIT_WORLD_CODE);
			break;

		// 1 -> Rename
		case 1:
			this.newNameDialog(this.selectedWorldName);
			break;
		// 2 -> Delete
		case 2:
			ManageWorlds.deleteWorld(this.selectedWorldName);
			reload();
			break;
		default:
			break;
		}
	}

	private void reload() {
		parent.finish();
		Intent intent = new Intent().setClass(parent, PlayActivity.class);
		parent.startActivity(intent);
	}

	private void newNameDialog(final String selectedWorldName) {

		// On instancie notre layout en tant que View
		LayoutInflater factory = LayoutInflater.from(parent);
		final View alertDialogView = factory
				.inflate(R.layout.alert_world, null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(parent);

		// On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		adb.setView(alertDialogView);

		// On donne un titre à l'AlertDialog
		adb.setTitle("New World Name");

		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(android.R.drawable.ic_dialog_alert);

		// On affecte un bouton "OK" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				// Lorsque l'on cliquera sur le bouton "OK", on récupère
				// l'EditText correspondant à notre vue personnalisée (cad à
				// alertDialogView)
				EditText newName = (EditText) alertDialogView
						.findViewById(R.id.new_world_name);

				try {
					ManageWorlds.renameWorld(selectedWorldName, newName
							.getText().toString());
					reload();
				} catch (AlreadyExistingWorldException e) {
					Toast.makeText(parent, e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//
			}
		});

		adb.show();
	}
}
