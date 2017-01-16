package khaanavali.vendor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import khaanavali.vendor.Utils.Constants;
import khaanavali.vendor.order.Address;
import khaanavali.vendor.order.Customer;
import khaanavali.vendor.order.HotelMenuItem;
import khaanavali.vendor.order.Order;
import khaanavali.vendor.order.Tracker;

public class NotificationOrder extends AppCompatActivity {
    Order orderList;
    Intent intent;

    private static final String TAG_CUSTOMER = "customer";
    private static final String TAG_ID = "id";
    private static final String TAG_ID2 = "_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_TRACKER = "tracker";
    private static final String TAG_CURRENT_STATUS = "current_status";
    private static final String TAG_MENU = "menu";
    private static final String TAG_BILL_VALUE = "bill_value";
    private static final String TAG_DELIVERY_CHARGE = "deliveryCharge";
    private static final String TAG_TOTAL_COST = "totalCost";
    private String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_order);
        intent = new Intent(this , orderDetail.class);
        msg=getIntent().getStringExtra("notificationFragment");
        bindView();
    }

    public void bindView() {
        String order_url = Constants.GET_ORDER_BY_ID + msg;
        new JSONAsyncTask(this).execute(order_url);
    }
    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

         Activity mContex;
        public  JSONAsyncTask(Activity contex)
        {
            this.mContex=contex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NotificationOrder.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet request = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpResponse response = httpclient.execute(request);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                    JSONArray jarray = new JSONArray(data);
                    for (int i = jarray.length() - 1 ; i >= 0; i--) {
                        JSONObject object = jarray.getJSONObject(i);

                        Order ordr = new Order();
                        if(object.has(TAG_CUSTOMER)) {
                            Customer cus = new Customer();
                            JSONObject custObj = object.getJSONObject(TAG_CUSTOMER);
                            if (custObj.has(TAG_NAME)) {
                                cus.setName(custObj.getString(TAG_NAME));
                            }
                            if (custObj.has(TAG_PHONE)) {
                                cus.setPhone(custObj.getString(TAG_PHONE));
//                                try {
//                                    int phone = custObj.getInt(TAG_PHONE);
//                                    String phonee = custObj.getString(TAG_PHONE);
//                                    cus.setPhone(Integer.toString(phone));
//                                }catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                            }
                            if (custObj.has(TAG_ADDRESS)) {
                                JSONObject addrObj = custObj.getJSONObject(TAG_ADDRESS);
                                Address address = new Address();
                                if(addrObj.has("addressLine1"))
                                    address.setAddressLine1(addrObj.getString("addressLine1"));
                                if(addrObj.has("addressLine2"))
                                    address.setAddressLine2(addrObj.getString("addressLine2"));
                                if(addrObj.has("areaName"))
                                    address.setAreaName(addrObj.getString("areaName"));
                                if(addrObj.has("city"))
                                    address.setCity(addrObj.getString("city"));
                                if(addrObj.has("LandMark"))
                                    address.setLandMark(addrObj.getString("LandMark"));
                                if(addrObj.has("street"))
                                    address.setStreet(addrObj.getString("street"));
                                if(addrObj.has("zip"))
                                    address.setZip(addrObj.getString("zip"));
                                cus.setAddress(address);
                            }
                            ordr.setCustomer(cus);
                        }


                        if(object.has(TAG_ID))
                        {
                            ordr.setId(object.getString(TAG_ID));
                        }
                        if(object.has(TAG_ID2))
                        {
                            ordr.set_id(object.getString(TAG_ID2));
                        }
                        if(object.has(TAG_CURRENT_STATUS))
                        {
                            ordr.setCurrent_status(object.getString(TAG_CURRENT_STATUS));
                        }

                        if(object.has(TAG_MENU))
                        {
                            ArrayList<HotelMenuItem> hotelMenuItemList = new ArrayList<HotelMenuItem>();
                            JSONArray menuarr =  object.getJSONArray(TAG_MENU);
                            for (int j = 0; j < menuarr.length(); j++) {
                                JSONObject menuobject = menuarr.getJSONObject(j);
                                HotelMenuItem menu = new HotelMenuItem();
                                menu.setName(menuobject.getString("name"));
                                menu.setNo_of_order(menuobject.getString("no_of_order"));
                                hotelMenuItemList.add(menu);
                            }
                            ordr.setHotelMenuItems(hotelMenuItemList);
                        }



                        if(object.has(TAG_TRACKER))
                        {
                            ArrayList<Tracker> trackerDetails = new ArrayList<Tracker>();
                            JSONArray trackerarr =  object.getJSONArray(TAG_TRACKER);
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
                                    //   isTodayOrder = DateUtils.isToday(getDate.getTime());
                                }
                                trackerDetails.add(tracker);
                            }
                            ordr.setTrackerDetail(trackerDetails);
                        }
                        if (object.has(TAG_BILL_VALUE)) {
                            try {
                                int intValue = object.getInt(TAG_BILL_VALUE);
                                ordr.setBill_value(intValue);
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (object.has(TAG_DELIVERY_CHARGE)) {
                            try {
                                int intValue = object.getInt(TAG_DELIVERY_CHARGE);
                                ordr.setDeliveryCharge(intValue);
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (object.has(TAG_TOTAL_COST)) {
                            try {
                                int intValue = object.getInt(TAG_TOTAL_COST);
                                ordr.setTotalCost(intValue);
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        orderList=ordr;
                    }
                    return true;
                }
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
            if(result == true && orderList != null)
            {
                Gson gson = new Gson();
                String order = gson.toJson(orderList);
                intent.putExtra("order", order);
                startActivity(intent);
            }
   else  if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }


    }

}
