package khaanavali.vendor;

/*
 * Copyright (C) 2015, Francesco Azzola
 *
 *(http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 20/10/15
 */


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.splunk.mint.Mint;

import khaanavali.vendor.Utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private DrawerLayout dLayout;
    SessionManager session;
    private boolean ishotelFragmentOpen;
    private  boolean isOtherFragmentOpen=false;
    private boolean onBack=false;
    private String notification;
    private void finishscreen() {
        this.finish();
    }

    public boolean isTodayMenuselected() {
        return isTodayMenuselected;
    }
    public String getNotification() {
        return notification;
    }
    private boolean isTodayMenuselected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        session = new SessionManager(getApplicationContext());
        notification=getIntent().getStringExtra("notificationFragment");
        if(session.checkLogin() && !checkNotificationListenerServiceRunning())
        {
            startService(new Intent(this, NotificationListener.class));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        setContentView(R.layout.activity_main_nav);
        ishotelFragmentOpen = true;


        layout = (RelativeLayout) findViewById(R.id.layout);
        setNavigationDrawer();
        setToolBar();
      /*  if(onBack==false && isOtherFragmentOpen==false) {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, new OrderListFragment());
                    transaction.commit();

                }
            }, 0, 180000);
        }*/
    }

    private void getVendorinfo() {
      //new JSONAsyncTask().execute(Constants.GET_VENDOR_INFO);
    }

    public boolean checkNotificationListenerServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("khaanavali.vendor.NotificationListener"
                    .equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(session.isKill)
        {
            session.isKill = false;
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public void setActionBarTitle(String title) {

        getSupportActionBar().setTitle(title);


    }
    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitleTextColor(getResources().getColor(R.color.gagantextcolor));
        tb.setSubtitleTextColor(getResources().getColor(R.color.gagantextcolor));
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("KHAANAVALI");
        }
    }

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        isTodayMenuselected = true;
        transaction.replace(R.id.frame, new OrderListFragment());
        transaction.commit();
        ishotelFragmentOpen = true;
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment frag = null;
                int itemId = menuItem.getItemId();

                if (itemId == R.id.navigation_order_today_list) {
                    isTodayMenuselected = true;
                    ishotelFragmentOpen = true;
                  //  isOtherFragmentOpen=false;
                   frag = new OrderListFragment();
                }
                else if (itemId == R.id.navigation_order_list) {
                    isTodayMenuselected = false;
                    ishotelFragmentOpen = true;
                    //isOtherFragmentOpen=false;
                    frag = new OrderListFragment();
                }
                else if (itemId == R.id.navigation_menu) {
                    //isOtherFragmentOpen=false;
                    isTodayMenuselected = false;
                    ishotelFragmentOpen = false;
                    frag = new MenuFragment();
                }
                else if (itemId == R.id.navigation_about_me) {
                    //isOtherFragmentOpen=true;
                    ishotelFragmentOpen = false;
                    frag = new AboutMeFragment();
                }
                else if (itemId == R.id.navigation_about_Khaanavali) {
                    //isOtherFragmentOpen=true;
                    ishotelFragmentOpen = false;
                    frag = new AboutKhaanavali();
                }
//                else if (itemId == R.id.navigation_settings) {
//                    ishotelFragmentOpen = false;
//                    //isOtherFragmentOpen=true;
//                    frag = new SettingsFragment();
//                }
                else if (itemId == R.id.navigation_logout) {
                    //isOtherFragmentOpen=true;
                    frag = new LogoutFragment();
                    ishotelFragmentOpen = false;
                }

                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                    dLayout.closeDrawers();
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String btnName = null;

        switch(itemId) {

            case android.R.id.home: {
                dLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return false;
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
          if (getSupportFragmentManager().getBackStackEntryCount() == 1){
                finish();
            }



        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        }
        else
        if (ishotelFragmentOpen == false) {
            dLayout.openDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                    onBack=true;
                }
            }, 2000);

        }
    }
}
