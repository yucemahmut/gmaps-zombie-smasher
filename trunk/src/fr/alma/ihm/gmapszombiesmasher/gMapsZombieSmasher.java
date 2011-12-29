package fr.alma.ihm.gmapszombiesmasher;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;

public class gMapsZombieSmasher extends TabActivity
{
  /** Called when the activity is first created. */
  @Override
    public void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      Resources res = getResources(); // resource object to get Drawables
      TabHost tabHost = getTabHost(); // the activity TabHost
      TabHost.TabSpec spec;           // resusable TabSpec for each tab
      Intent intent;                  // reusable Intent for each tab

      // create an Intent to launch an Activity for the tab (to be reused)
      intent = new Intent().setClass(this, PlayActivity.class);

      // initialize a TabSpec for each tab and add it to the TabHost
      spec = tabHost.newTabSpec("play").setIndicator("Play",
             res.getDrawable(R.drawable.ic_tab_play)).setContent(intent);
      tabHost.addTab(spec);

      // do the same for the other tabs
      intent = new Intent().setClass(this, AchivementsActivity.class);
      spec = tabHost.newTabSpec("achivements").setIndicator("Achivements",
             res.getDrawable(R.drawable.ic_tab_achivements)).setContent(intent);
      tabHost.addTab(spec);

      intent = new Intent().setClass(this, SettingsActivity.class);
      spec = tabHost.newTabSpec("settings").setIndicator("Settings",
             res.getDrawable(R.drawable.ic_tab_settings)).setContent(intent);
      tabHost.addTab(spec);
    }
}

