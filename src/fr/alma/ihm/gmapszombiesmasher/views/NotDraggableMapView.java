package fr.alma.ihm.gmapszombiesmasher.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.maps.MapView;

public class NotDraggableMapView extends MapView {

	public NotDraggableMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	public void computeScroll(){
		//nothing
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		return false;
	}
	
	@Override
	public boolean	onKeyUp(int keyCode, android.view.KeyEvent event) {
		return false;
	}
}
