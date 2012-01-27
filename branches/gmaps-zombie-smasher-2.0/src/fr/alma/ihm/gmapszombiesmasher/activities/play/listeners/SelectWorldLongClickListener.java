package fr.alma.ihm.gmapszombiesmasher.activities.play.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class SelectWorldLongClickListener implements OnItemLongClickListener {

	private static Activity parent;

	public SelectWorldLongClickListener(Activity activity) {
		parent = activity;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterParent, View view,
			int position, long id) {

		if (!adapterParent.getItemAtPosition(position).equals("Play Here")) {
			final CharSequence[] items = { "Edit", "Rename", "Delete" };

			AlertDialog.Builder builder = new AlertDialog.Builder(parent);
			builder.setTitle("Pick an Option");
			builder.setItems(items, new OnClickWorldOptionListener(parent,
					(String) adapterParent.getItemAtPosition(position)));

			AlertDialog alert = builder.create();
			alert.show();
		}
		return false;
	}

}
