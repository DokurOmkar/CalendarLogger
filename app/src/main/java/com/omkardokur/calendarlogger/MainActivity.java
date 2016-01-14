package com.omkardokur.calendarlogger;



import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {
    ViewPager viewPager;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        ActionBar.Tab callsTab = actionBar.newTab();
        callsTab.setText("Calls");
        callsTab.setTabListener(this);

        ActionBar.Tab smsTab = actionBar.newTab();
        smsTab.setText("SMS");
        smsTab.setTabListener(this);

        actionBar.addTab(callsTab);
        actionBar.addTab(smsTab);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        //Log.d("Tabs", "onTabSelected" + "position" + tab.getPosition()+ "name " + tab.getText());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
       // Log.d("Tabs", "onTabUnSelected"+ "position" + tab.getPosition()+ "name " + tab.getText());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
       // Log.d("Tabs", "onTabReSelected"+ "position" + tab.getPosition()+ "name " + tab.getText());
    }
}
