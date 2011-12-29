package fr.alma.ihm.gmapszombiesmasher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PlayActivity extends Activity {
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TextView textview = new TextView(this);
    textview.setText("This is the Play tab");
    setContentView(textview);
  }
}

