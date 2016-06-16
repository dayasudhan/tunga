package khaanavali.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import khaanavali.vendor.R;
import khaanavali.vendor.Utils.Constants;
import khaanavali.vendor.order.HotelMenuItem;
import khaanavali.vendor.order.Order;
import khaanavali.vendor.order.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


//import order.orderDetailsAdapter;

public class orderDetail extends AppCompatActivity implements OnItemSelectedListener {
    SharedPreferences pref;
    String vendor_email;
    Order order;
    Button callButton;
 //   orderDetailsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_order_detail);
        Intent i = getIntent();
        // Order order = (Order)i.getSerializableExtra("order");
        pref = getSharedPreferences("Khaanavali", 0);
        vendor_email = pref.getString("email", "name");
        Gson gson = new Gson();
      //  Order order;
        order = gson.fromJson(i.getStringExtra("order"), Order.class);
//        JSONObject object = null;
//        try {
//             object = new JSONObject(i.getStringExtra("order"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
      //  adapter = new orderDetailsAdapter(getApplicationContext(), R.layout.activity_customer_order_detail, order);
        TextView txtViewName = (TextView) findViewById(R.id.customer_name_value);
        TextView txtViewPhone = (TextView) findViewById(R.id.customer_contact_value);
        TextView txtViewAddress = (TextView) findViewById(R.id.address_value);
        TextView txtViewStatus = (TextView) findViewById(R.id.current_status_value);
        TextView txtViewMenu = (TextView) findViewById(R.id.items_value);
        TextView txtViewTracker = (TextView) findViewById(R.id.status_tracker_value);
        TextView txtViewid = (TextView) findViewById(R.id.order_id_value);
        TextView txtViewiBillValue = (TextView) findViewById(R.id.bill_value_value);
        callButton = (Button)findViewById(R.id.buttonCall);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        txtViewName.setText(order.getCustomer().getName());
        txtViewPhone.setText(order.getCustomer().getPhone());
        txtViewid.setText(order.getId());
        txtViewStatus.setText(order.getCurrent_status());
        txtViewiBillValue.setText(String.valueOf(order.getTotalCost()));

        String CustomerAddress = new String();
        if(order.getCustomer().getAddress().getAddressLine1() != null && !order.getCustomer().getAddress().getAddressLine1().isEmpty())
            CustomerAddress =  CustomerAddress.concat(order.getCustomer().getAddress().getAddressLine1()).concat("\n");
        if(order.getCustomer().getAddress().getAddressLine2() != null && !order.getCustomer().getAddress().getAddressLine2().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getAddressLine2()).concat("\n");
        if(order.getCustomer().getAddress().getAreaName() != null && !order.getCustomer().getAddress().getAreaName().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getAreaName()).concat("\n");
        if(order.getCustomer().getAddress().getLandMark() != null && !order.getCustomer().getAddress().getLandMark().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getLandMark().concat("\n"));
        if(order.getCustomer().getAddress().getStreet() != null && !order.getCustomer().getAddress().getStreet().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getStreet()).concat("\n");
        if(order.getCustomer().getAddress().getCity() != null && !order.getCustomer().getAddress().getCity().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getCity());
        txtViewAddress.setText(CustomerAddress);

        ArrayList<HotelMenuItem> items = order.getHotelMenuItems();
        String MenuItemStr = "";
        for(int j = 0 ; j < items.size() ; j++)
        {
            MenuItemStr += items.get(j).getName() + " (" + items.get(j).getNo_of_order() + ")" + '\n';
        }
        txtViewMenu.setText(MenuItemStr);

        ArrayList<Tracker> trackeritems = order.getTrackerDetail();
        String trackerItemStr = "";
        for(int j = 0 ; j < trackeritems.size() ; j++)
        {
            SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date getDate = null;
            try {
                getDate = existingUTCFormat.parse(trackeritems.get(j).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDate);
            cal.add(Calendar.HOUR, 5);
            cal.add(Calendar.MINUTE, 30);
            String newTime = requiredFormat.format(cal.getTime());
            trackerItemStr += trackeritems.get(j).getStatus()+ " (" + newTime + ")" + '\n';
        }
        txtViewTracker.setText(trackerItemStr);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);  // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // add button listener
        callButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String calNumber = new String("tel:").concat(order.getCustomer().getPhone());
                callIntent.setData(Uri.parse(calNumber));
                startActivity(callIntent);

            }

        });
        setToolBar();
    }

    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
       // ab.setIcon(R.drawable.ic_action_call);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Order Detail");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_order_detail, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(position !=0) {
            updateStatusTracker(item, "ok");
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void updateStatusTracker(String status,String reason)
    {
      //  String order_url = "http://10.239.54.58:3000/v1/vendor/order/status/";
        String order_url = Constants.GET_STATUS_URL;
        order_url= order_url.concat(order.get_id());
        new AddJSONAsyncTask().execute(order_url,status,reason);
    }
    public  class AddJSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        public  AddJSONAsyncTask()
        {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(orderDetail.this);
            dialog.setMessage("Adding Item, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                String json = "";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("status", urls[1]);
                    jsonObject.put("reason", urls[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);

                HttpPut request = new HttpPut(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                request.setEntity(se);
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    return true;
                }

                //------------------>>

            } catch (android.net.ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            // adapter.notifyDataSetChanged();

            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }
}
