package materialapp.survivingwithandroid.com.materialapp;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
            Button addButton = (Button) rootview.findViewById(R.id.addMenuItemButton);//editItemprice
            addButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    AddMenuItem(eItemName.getText().toString(),eItemPrice.getText().toString());
                }
            });
        return rootview;
    }
    public void AddMenuItem(String ItemName,String ItemPrice)
    {
        String order_url = "http://oota.herokuapp.com/v1/vendor/menu/";
        order_url= order_url.concat(vendor_email);
        new AddJSONAsyncTask().execute(order_url,ItemName,ItemPrice);
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

                //------------------>>
//                HttpGet httppost = new HttpGet(urls[0]);
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpResponse response = httpclient.execute(httppost);

                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("fooditem", urls[1]));
                postParameters.add(new BasicNameValuePair("foodprice", urls[2]));


                HttpPost request = new HttpPost(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = httpclient.execute(request);


                // StatusLine stat = response.getStatusLin;
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
//                    if (data.equals("1")) {
//
//                        session.createLoginSession("Knvl", urls[1]);
//                        Intent intent = new Intent("com.example.gagan.khanavali_main.MainActivity");
//                        startActivity(intent);
//                        //  Toast.makeText(getApplicationContext(), "successfully logged in", Toast.LENGTH_LONG).show();
//                    } else {
//                        //Toast.makeText(getApplicationContext(), "error in logged in", Toast.LENGTH_LONG).show();
//                    }
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
           // adapter.notifyDataSetChanged();
            bindView();
            if (result == false)
                Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }
//    public void deleteButtonClickHandler(View v)
//    {
//        LinearLayout vwParentRow = (LinearLayout)v.getParent();
//        TextView child = (TextView)vwParentRow.getChildAt(0);
//        Toast.makeText(getActivity().getApplicationContext(), child.getText(), Toast.LENGTH_LONG).show();
//    }
    public void bindView() {
        String order_url = "http://oota.herokuapp.com/v1/vendor/menu/";
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
                        // cus.setImage(object.getString("image"));

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
