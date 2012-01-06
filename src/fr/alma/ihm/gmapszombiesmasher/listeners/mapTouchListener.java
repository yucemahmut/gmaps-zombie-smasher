package fr.alma.ihm.gmapszombiesmasher.listeners;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class mapTouchListener implements OnTouchListener {

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		System.out.println("Miaaou");
		return false;
	}

}
