package khaanavali.vendor;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by gagan on 11/6/2015.
 */
public class MenuFragment extends Fragment {
    TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter mAdapter;
    private ActionBar actionBar;
    OneFragment one;
    TwoFragment two;
    Toolbar toolbar;
    View rootview;
    ProgressDialog dialog;



        //SessionManager session;
        //EditText eItemName;
        //EditText eItemPrice;
        //CheckBox cBreakfast,cLunch,cDinner;
        //public   int bBreakfast=0,bLunch=0,bDinner=0,itemTiming=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.activity_menu, container, false);

        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar();

        TabLayout tabLayout = (TabLayout) rootview.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Breakfast"));
        tabLayout.addTab(tabLayout.newTab().setText("Lunch"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) rootview.findViewById(R.id.pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setHasOptionsMenu(true);
        return rootview;
    }

    protected void onPostExecute(Boolean result) {
        dialog.cancel();
        one.adapter.notifyDataSetChanged();
        if (result == false)
            Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

    }
        //gagan
}