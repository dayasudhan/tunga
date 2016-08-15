package khaanavali.vendor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.splunk.mint.Mint;

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
import khaanavali.vendor.Utils.StatusTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


//import order.orderDetailsAdapter;

public class orderDetail extends AppCompatActivity implements OnItemSelectedListener {
    SharedPreferences pref;
    String vendor_email;
    Order order;
    Button callButton;
    int current_status = 0 ;
    boolean isStartActivity = false;
    Spinner spinner;
    ArrayAdapter<String> spinnerArrayAdapter;
    private boolean userIsInteracting;
    //   orderDetailsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_order_detail);
        Mint.initAndStartSession(this, "49d903c2");
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

        TextView txtViewMenu = (TextView) findViewById(R.id.items_value);

        TextView txtViewid = (TextView) findViewById(R.id.order_id_value);
        TextView txtViewiBillValue = (TextView) findViewById(R.id.bill_value_value);
        callButton = (Button) findViewById(R.id.buttonCall);

        txtViewName.setText(order.getCustomer().getName());
        txtViewPhone.setText(order.getCustomer().getPhone());
        txtViewid.setText(order.getId());

        String CustomerAddress = new String();
        if (order.getCustomer().getAddress().getAddressLine1() != null && !order.getCustomer().getAddress().getAddressLine1().isEmpty())
            CustomerAddress = CustomerAddress.concat(order.getCustomer().getAddress().getAddressLine1()).concat("\n");
        if (order.getCustomer().getAddress().getAddressLine2() != null && !order.getCustomer().getAddress().getAddressLine2().isEmpty())
            CustomerAddress = CustomerAddress.concat(order.getCustomer().getAddress().getAddressLine2()).concat("\n");
        if (order.getCustomer().getAddress().getAreaName() != null && !order.getCustomer().getAddress().getAreaName().isEmpty())
            CustomerAddress = CustomerAddress.concat(order.getCustomer().getAddress().getAreaName()).concat("\n");
        if (order.getCustomer().getAddress().getLandMark() != null && !order.getCustomer().getAddress().getLandMark().isEmpty())
            CustomerAddress = CustomerAddress.concat(order.getCustomer().getAddress().getLandMark().concat("\n"));
        if (order.getCustomer().getAddress().getStreet() != null && !order.getCustomer().getAddress().getStreet().isEmpty())
            CustomerAddress = CustomerAddress.concat(order.getCustomer().getAddress().getStreet()).concat("\n");
        if (order.getCustomer().getAddress().getCity() != null && !order.getCustomer().getAddress().getCity().isEmpty())
            CustomerAddress = CustomerAddress.concat(order.getCustomer().getAddress().getCity());
        txtViewAddress.setText(CustomerAddress);

        ArrayList<HotelMenuItem> items = order.getHotelMenuItems();
        String MenuItemStr = "";
        for (int j = 0; j < items.size(); j++) {
            MenuItemStr += items.get(j).getName() + " (" + items.get(j).getNo_of_order() + ")" + '\n';
        }
        txtViewMenu.setText(MenuItemStr);
        txtViewiBillValue.setText(String.valueOf(order.getTotalCost()));

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item);
        initStatusTracker();



        // add button listener
        callButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String calNumber = new String("tel:").concat(order.getCustomer().getPhone());
                callIntent.setData(Uri.parse(calNumber));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(orderDetail.this,
                            Manifest.permission.CALL_PHONE)) {

                         new AlertDialog.Builder(orderDetail.this)
                                .setTitle("Permission Required")
                                .setMessage("This permission was denied earlier by you. This permission is required to call from app .So, in order to use this feature please allow this permission by clicking ok.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ActivityCompat.requestPermissions(orderDetail.this,
                                                new String[]{Manifest.permission.CALL_PHONE},
                                                1);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } else {

                        ActivityCompat.requestPermissions(orderDetail.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                1);

                    }
                    return;
                }
                startActivity(callIntent);

            }

        });
        setToolBar();
    }

    private void initStatusTracker() {
        //updatecurrent status
        TextView txtViewStatus = (TextView) findViewById(R.id.current_status_value);
        txtViewStatus.setText(order.getCurrent_status());

        isStartActivity = true;
        //update status tracker
        ArrayList<Tracker> trackeritems = order.getTrackerDetail();
        String trackerItemStr = "";
        for (int j = 0; j < trackeritems.size(); j++) {
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
            String newTime = requiredFormat.format(cal.getTime());
            trackerItemStr += trackeritems.get(j).getStatus() + " (" + newTime + ")" + '\n';
        }
        TextView txtViewTracker = (TextView) findViewById(R.id.status_tracker_value);
        txtViewTracker.setText(trackerItemStr);

        try {
            final StatusTracker current_status = StatusTracker.valueOf(order.getCurrent_status().toUpperCase());
            ArrayList<String> statusArray = new ArrayList<>();
            statusArray.add("UPDATE STATUS");
            for (StatusTracker status : StatusTracker.values()) {
                if (current_status == StatusTracker.ORDERED && status == StatusTracker.ORDERED) {
                    // Hide the second item from Spinner
                    continue;
                } else if (current_status == StatusTracker.ACCEPTED && (status == StatusTracker.ORDERED
                        || status == StatusTracker.REJECTED || status == StatusTracker.ACCEPTED)) {
                    continue;
                } else if (current_status == StatusTracker.DELIVERED || current_status == StatusTracker.REJECTED) {
                    continue;
                }
                statusArray.add(status.toString());
            }

            spinnerArrayAdapter.clear();
            spinnerArrayAdapter.addAll(statusArray);
            spinner.setAdapter(spinnerArrayAdapter);  // Spinner click listener
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }
    /**
     *  This method will be invoked when user allows or deny's a permission from the permission dialog so take actions accordingly.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Call Permission granted", Toast.LENGTH_LONG).show();
                } else {
                    String permission = permissions[0];
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                    if (!showRationale) {
                        Toast.makeText(this, "Call Permission Denied with never show options", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Call Permission Denied", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
        }
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
        if(userIsInteracting && !item.equals("UPDATE STATUS"))
            updateStatusTracker(item, "ok");
        isStartActivity = false;
        userIsInteracting = false;
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
                request.setHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.setHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.setHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                request.setEntity(se);
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    order.setCurrent_status(urls[1]);

                    JSONArray trackerarr = null;
                    try {
                        trackerarr = new JSONArray(data);
                        ArrayList<Tracker> trackerDetails = new ArrayList<Tracker>();
                        for (int j = 0; j < trackerarr.length(); j++) {
                            JSONObject trackerobject = trackerarr.getJSONObject(j);
                            Tracker tracker = new Tracker();
                            tracker.setStatus(trackerobject.getString("status"));
                            tracker.setTime(trackerobject.getString("time"));
                            if(trackerobject.has("reason"))
                            {
                                tracker.setReason(trackerobject.getString("reason"));
                            }
                            if(j ==0)
                            {
                                Date getDate = null;
                                SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                try {
                                    getDate = existingUTCFormat.parse(tracker.getTime());
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            trackerDetails.add(tracker);
                        }
                        order.setTrackerDetail(trackerDetails);
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            initStatusTracker();

            TextView txtViewTracker = (TextView) findViewById(R.id.status_tracker_value);
            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }
}
