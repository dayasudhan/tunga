package khaanavali.vendor;

import android.content.SharedPreferences;
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
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by gagan on 11/6/2015.
 */
public class MenuFragment extends Fragment {

    ArrayList<HotelMenu> menuList;

    MenuAdapter adapter;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    View rootview;
    ListView listView;
    //SessionManager session;
    SharedPreferences pref;
    String vendor_email;

    //EditText eItemName;
    //EditText eItemPrice;
    //CheckBox cBreakfast,cLunch,cDinner;
    //public   int bBreakfast=0,bLunch=0,bDinner=0,itemTiming=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        rootview = inflater.inflate(R.layout.activity_menu, container, false);

        toolbar = (Toolbar) rootview.findViewById(R.id.toolbarmenu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) rootview.findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());
        // if (adapter.getCount() == 0) {
            adapter.addFragment(new OneFragment(), "Breakfast");
            adapter.addFragment(new TwoFragment(), "Lunch");
            viewPager.setAdapter(adapter);
      //  }

        tabLayout = (TabLayout) rootview.findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        return rootview;
    }

    @Override
    public void onDestroyView()
    {
        //mSectionAdapter.stopLists();
        super.onDestroyView();

    }


    //gagan
}