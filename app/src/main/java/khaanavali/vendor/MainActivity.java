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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


import khaanavali.vendor.Utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private DrawerLayout dLayout;
    SessionManager session;

    public boolean isTodayMenuselected() {
        return isTodayMenuselected;
    }

    private boolean isTodayMenuselected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initializing firebase

        session = new SessionManager(getApplicationContext());
        // put your code here...
        if(session.checkLogin() && !checkNotificationListenerServiceRunning())
        {
            startService(new Intent(this, NotificationListener.class));
           // stopService(new Intent(this,NotificationListener.class));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // int notificationId = getIntent().getExtras().getInt("notificationID");
        notificationManager.cancelAll();


        setContentView(R.layout.activity_main_nav);
        layout = (RelativeLayout) findViewById(R.id.layout);

        setNavigationDrawer();
        setToolBar();
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

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment frag = null;
                int itemId = menuItem.getItemId();

                if (itemId == R.id.navigation_order_today_list) {
                    isTodayMenuselected = true;
                   frag = new OrderListFragment();
                }
                else if (itemId == R.id.navigation_order_list) {
                    isTodayMenuselected = false;
                    frag = new OrderListFragment();
                }
                else if (itemId == R.id.navigation_menu) {
                    frag = new MenuFragment();
                }
                else if (itemId == R.id.navigation_about_me) {
                    frag = new AboutMeFragment();
                }
                else if (itemId == R.id.navigation_about_Khaanavali) {
                    frag = new AboutKhaanavali();
                }
                else if (itemId == R.id.navigation_logout) {
                    frag = new LogoutFragment();
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
    @Override
    public void onBackPressed() {
        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
