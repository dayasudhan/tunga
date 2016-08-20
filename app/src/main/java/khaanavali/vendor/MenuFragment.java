package khaanavali.vendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import khaanavali.vendor.Utils.Constants;

/**
 * Created by gagan on 11/6/2015.
 */
public class MenuFragment extends Fragment {

    ArrayList<HotelMenu> menuList;

    MenuAdapter adapter;

    View rootview;
    ListView listView;

    SharedPreferences pref;
    String vendor_email;
    EditText eItemName;
    EditText eItemPrice;
    CheckBox cBreakfast,cLunch,cDinner;
    public   int bBreakfast=0,bLunch=0,bDinner=0,itemTiming=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       rootview=inflater.inflate(R.layout.activity_menu,container,false);
        listView = (ListView) rootview.findViewById(R.id.list);
        menuList = new ArrayList<HotelMenu>();

        ((MainActivity) getActivity())
                .setActionBarTitle("Menu Details");
        pref = getActivity().getSharedPreferences("Khaanavali", 0);
        vendor_email = pref.getString("email", "name");

        bindView();
        adapter = new MenuAdapter(getActivity().getApplicationContext(), R.layout.menu_row2 ,menuList,vendor_email);

        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                    long id) {
//                // TODO Auto-generated method stub
//                Toast.makeText(getActivity().getApplicationContext(), adapter.getCustomerList().get(position).getName(), Toast.LENGTH_LONG).show();
//            }
//        });
             eItemName = (EditText)rootview.findViewById(R.id.editItemName);
             eItemPrice = (EditText)rootview.findViewById(R.id.editItemprice);

            cBreakfast=(CheckBox)rootview.findViewById(R.id.checkBoxBreakfast);
            cLunch=(CheckBox)rootview.findViewById(R.id.checkBoxLunch);
            cDinner=(CheckBox)rootview.findViewById(R.id.checkBoxDinner);

        cBreakfast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if(cBreakfast.isChecked())
                    {
                        bBreakfast=1;
                    }
                    else {
                        bBreakfast=0;
                    }
                }
            });
        cLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(cLunch.isChecked())
                {
                    bLunch=2;
                }
                else {
                    bLunch=0;
                }
            }
        });
        cDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(cDinner.isChecked())
                {
                    bDinner=4;
                }
                else {
                    bDinner=0;
                }
            }
        });


            Button addButton = (Button) rootview.findViewById(R.id.addMenuItemButton);//editItemprice

            addButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    itemTiming=bBreakfast+bDinner+bLunch;
                    if (itemTiming!=0) {
                        AddMenuItem(eItemName.getText().toString(), eItemPrice.getText().toString(), Integer.toString(itemTiming));
                        eItemName.setText("");
                        eItemPrice.setText("");
                    }
                    else
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert")
                                .setMessage("Please Check atleast one Checkbox")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Toast.makeText(getActivity(), "thank you", Toast.LENGTH_SHORT).show();
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();

                    }

                }
                });

        return rootview;
    }
    public void AddMenuItem(String ItemName,String ItemPrice ,String ItemTiming)
    {
        if(ItemName.isEmpty() || ItemPrice.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter Proper MenuItem", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            String order_url = Constants.GET_MENU;
            order_url = order_url.concat(vendor_email);
            new AddJSONAsyncTask().execute(order_url, ItemName, ItemPrice,ItemTiming);
        }
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
                postParameters.add(new BasicNameValuePair("fooditem", urls[1]));
                postParameters.add(new BasicNameValuePair("foodprice", urls[2]));

                postParameters.add(new BasicNameValuePair("timings", urls[3]));


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

                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
                dialog.cancel();

            bindView();
            if (result == false)
                Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

    public void bindView() {
        String order_url = Constants.GET_MENU;
        order_url= order_url.concat(vendor_email);
        new JSONAsyncTask().execute(order_url);
    }
    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        ListView mListView;
        Activity mContex;
        public  JSONAsyncTask()
        {
//            this.mListView=gview;
//            this.mContex=contex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                    JSONArray jarray = new JSONArray(data);
                    menuList.clear();
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        HotelMenu cus = new HotelMenu();

                        cus.setName(object.getString("name"));
                        cus.setAvailability(object.getString("availability"));
                        cus.setid(object.getString("_id"));
                        cus.setPrice(object.getString("price"));
                        cus.setTimings(object.getString("timings"));

                        menuList.add(cus);
                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            adapter.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

}
