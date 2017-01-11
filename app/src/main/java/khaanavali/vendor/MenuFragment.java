package khaanavali.vendor;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.AsyncTask;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import khaanavali.vendor.Utils.Constants;
import khaanavali.vendor.Utils.SessionManager;

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
    String status="Opening";
    View rootview;
    SessionManager session;
    String email;
    private Switch mySwitch;
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
        session= new SessionManager(getActivity().getApplicationContext());
        mySwitch = (Switch) rootview.findViewById(R.id.switch2);

        if (session.KEY_ISOPEN.equals("1")) {
            mySwitch.setChecked(true);
        } else {
            mySwitch.setChecked(false);
            // switchStatus.setText("Closed");
        }
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        ((MainActivity) getActivity()).setActionBarTitle("Menu");
        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar();

        TabLayout tabLayout = (TabLayout) rootview.findViewById(R.id.tab_layout);

        tabLayout.setTabTextColors(getResources().getColor(R.color.gagantextcolorprim),getResources().getColor(R.color.gagantextcolor));
        tabLayout.addTab(tabLayout.newTab().setText("Breakfast"));
        tabLayout.addTab(tabLayout.newTab().setText("Lunch/Dinner"));
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



        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    //switchStatus.setText("Open");
                    status = "Opening";
                    AddIsOpen("1");
                }else{
                   // switchStatus.setText("Closed");
                    status = "Closing";
                    AddIsOpen("0");

                }

            }
        });


        return rootview;
    }

    protected void onPostExecute(Boolean result) {
        dialog.cancel();
        one.adapter.notifyDataSetChanged();
        if (result == false)
            Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

    }

    public void AddIsOpen(String isOpen)
    {
        String order_url = Constants.POST_ISOPEN;
        order_url = order_url.concat(email);
        new AddJSONAsyncTask().execute(order_url, isOpen);

    }
    public  class AddJSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        public  AddJSONAsyncTask()
        {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Hotel "+status+" please Wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("isopen", urls[1]));

                HttpPost request = new HttpPost(urls[0]);
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = httpclient.execute(request);

                // StatusLine stat = response.getStatusLin;
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    session.setHotelopen(urls[1]);
                    return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();

            if (result == false) {
                Toast.makeText(getActivity().getApplicationContext(), "Unable to posh data to server", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "successfully updated the hotel status", Toast.LENGTH_LONG).show();
            }
        }
    }
}
        //gagan
