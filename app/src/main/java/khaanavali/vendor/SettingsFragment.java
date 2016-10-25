package khaanavali.vendor;

import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
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
 * Created by dganeshappa on 8/9/2016.
 */
public class SettingsFragment extends Fragment {


    View rootview;
    SessionManager session;
    String email;
    private Switch mySwitch;
    private TextView switchStatus;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.settings,container,false);
        session = new SessionManager(getActivity().getApplicationContext());
//        TextView lblName = (TextView) rootview.findViewById(R.id.settingname);

        ((MainActivity) getActivity())
                .setActionBarTitle("Settings");
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);




        mySwitch = (Switch) rootview.findViewById(R.id.switch1);
        switchStatus=(TextView) rootview.findViewById(R.id.switchStatus);
        //set the switch to ON
        if(session.getHotelopen().equals("1")) {
            mySwitch.setChecked(true);
            //switchStatus.setText("Open");
        }
        else
        {
            mySwitch.setChecked(false);
            //switchStatus.setText("Closed");
        }
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    switchStatus.setText("Open");
                    AddIsOpen("1");
                }else{
                    switchStatus.setText("Closed");
                    AddIsOpen("0");
                }

            }
        });

        return rootview;
    }
    @Override
    public void onResume() {
            super.onResume();
        //set the switch to ON
        if(session.getHotelopen().equals("1")) {
            mySwitch.setChecked(true);
            switchStatus.setText("Open");
        }
        else
        {
            mySwitch.setChecked(false);
            switchStatus.setText("Closed");
        }
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
            dialog.setMessage("Adding Item, please wait");
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
