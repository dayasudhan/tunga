package khaanavali.vendor;

/**
 * Created by dganeshappa on 11/10/2015.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
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

public class MenuAdapter extends ArrayAdapter<HotelMenu> {

    private int breakfast,lunch,dinner;
    public ArrayList<HotelMenu> getCustomerList() {
        return customerList;
    }

    ArrayList<HotelMenu> customerList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    Context mContext;
    String mVendor_email;

    public MenuAdapter(Context context, int resource, ArrayList<HotelMenu> objects, String vendor_email) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        customerList = objects;
        mContext = context;
        mVendor_email = new String(vendor_email);

    //    customerList = new ArrayList<HotelMenu>();

    //    bindView();
    }


    public  void setAvailability(String name, Boolean isChecked)
    {
        if(isChecked==true)
        {
            //avail=1
            String order_url =  Constants.UPDATE_MENU;
            order_url= order_url.concat(mVendor_email);
          //  order_url= order_url.concat("/");
        //    String item = available.replace("1", "%20");
      //      order_url= order_url.concat(item);
            new UpdateJSONAsyncTask().execute(order_url,name,"1");
        }else
        {
           //avail=0
            String order_url =  Constants.UPDATE_MENU;
            order_url= order_url.concat(mVendor_email);
//            order_url= order_url.concat("/");
  //          String item = available.replace("0", "%20");
    //        order_url= order_url.concat(item);
            new UpdateJSONAsyncTask().execute(order_url,name,"0");
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            //holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
         //   holder.itemid = (TextView) v.findViewById(R.id.itemid);
           // holder.itemavailability = (TextView) v.findViewById(R.id.itemavailability);
            holder.itemname = (TextView) v.findViewById(R.id.itemname);
            holder.itemprice = (TextView) v.findViewById(R.id.itemprice);
            holder.onOff = (Switch) v.findViewById(R.id.onoff);
            //holder.itemTimeBreakfast = (TextView) v.findViewById(R.id.textViewBreakfast);
            //holder.itemTimeLunch = (TextView) v.findViewById(R.id.textViewLunch);
            //holder.itemTimeDinner = (TextView) v.findViewById(R.id.textViewDinner);
            if(customerList.get(position).getAvailability().equalsIgnoreCase("1"))
            {
                holder.onOff.setChecked(true);
            }
            else{
                holder.onOff.setChecked(false);
            }

       // Button deleteButton = (Button) v.findViewById(R.id.delete_menu_Item_button);
             //onOff = (Switch) v.findViewById(R.id.onoff);
            holder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    setAvailability(customerList.get(position).getName(),isChecked);
                }
            });
       /* deleteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                deleteMenuItem(customerList.get(position).getName());

            }
        });*/


        v.setTag(holder);
    } else {
        holder = (ViewHolder) v.getTag();
    }
    //  holder.imageview.setImageResource(R.drawable.ic);
    // new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
    holder.itemname.setText(customerList.get(position).getName());

    //   holder.itemid.setText(customerList.get(position).getid());

    holder.itemprice.setText(customerList.get(position).getPrice());

       /* switch(Integer.parseInt(customerList.get(position).getTimings()))
        {
            case 1: holder.itemTimeBreakfast.setTextColor(Color.GREEN);
                    holder.itemTimeLunch.setTextColor(Color.RED);
                    holder.itemTimeDinner.setTextColor(Color.RED);
                    break;
            case  2: holder.itemTimeBreakfast.setTextColor(Color.RED);
                     holder.itemTimeLunch.setTextColor(Color.GREEN);
                     holder.itemTimeDinner.setTextColor(Color.RED);
                       break;
            case 3: holder.itemTimeBreakfast.setTextColor(Color.GREEN);
                    holder.itemTimeLunch.setTextColor(Color.GREEN);
                    holder.itemTimeDinner.setTextColor(Color.RED);
                    break;
            case 4: holder.itemTimeBreakfast.setTextColor(Color.RED);
                    holder.itemTimeLunch.setTextColor(Color.RED);
                    holder.itemTimeDinner.setTextColor(Color.GREEN);
                    break;
            case 5: holder.itemTimeBreakfast.setTextColor(Color.GREEN);
                    holder.itemTimeLunch.setTextColor(Color.RED);
                    holder.itemTimeDinner.setTextColor(Color.GREEN);
                    break;
            case 6: holder.itemTimeBreakfast.setTextColor(Color.RED);
                    holder.itemTimeLunch.setTextColor(Color.GREEN);
                    holder.itemTimeDinner.setTextColor(Color.GREEN);
                    break;
            case 7: holder.itemTimeBreakfast.setTextColor(Color.GREEN);
                    holder.itemTimeLunch.setTextColor(Color.GREEN);
                    holder.itemTimeDinner.setTextColor(Color.GREEN);
                    break;

        }*/
        /*if(breakfast==1)
        {

        }
        if(lunch==1)
        {
            holder.itemTimeBreakfast.setTextColor(Color.GREEN);
        }
        if(dinner==1)
        {
            holder.itemTimeBreakfast.setTextColor(Color.GREEN);
        }*/
        return v;

}
static class ViewHolder {
    //  public ImageView imageview;
        public TextView itemname;
        public TextView itemprice;
        public TextView itemavailability;
        public Switch onOff;
       // public TextView itemTimeBreakfast;
       // public TextView itemTimeLunch;
       // public TextView itemTimeDinner;
    }
    public void deleteMenuItem(String ItemName)
    {
        String order_url =  Constants.UPDATE_MENU;
        order_url= order_url.concat(mVendor_email);
        order_url= order_url.concat("/");
        String item = ItemName.replace(" ", "%20");
        order_url= order_url.concat(item);
        new UpdateJSONAsyncTask().execute(order_url);
    }

    public void bindView() {
        String order_url =  Constants.GET_MENU;
        order_url= order_url.concat(mVendor_email);
        new GetJSONAsyncTask().execute(order_url);
    }
    public  class GetJSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        //        ListView mListView;
//        Activity mContex;
        public  GetJSONAsyncTask()
        {
//            this.mListView=gview;
//            this.mContex=contex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(mContext);
//            dialog.setMessage("Loading, please wait");
//            dialog.setTitle("Connecting server");
//            dialog.show();
//            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet request = new HttpGet(urls[0]);
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(request);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                    JSONArray jarray = new JSONArray(data);
                    customerList.clear();
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        HotelMenu cus = new HotelMenu();

                        cus.setName(object.getString("name"));
                        cus.setAvailability(object.getString("availability"));
                        cus.setid(object.getString("_id"));
                        cus.setPrice(object.getString("price"));
                        cus.setTimings(object.getString("timings"));
                        // cus.setImage(object.getString("image"));

                        customerList.add(cus);
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
            //    dialog.cancel();
            notifyDataSetChanged();
            if (result == false)
                Toast.makeText(mContext, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }
    public  class UpdateJSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        //        ListView mListView;
//        Activity mContex;
        public  UpdateJSONAsyncTask()
        {
//            this.mListView=gview;
//            this.mContex=contex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(mContext);
//            dialog.setMessage("Loading, please wait");
//            dialog.setTitle("Connecting server");
//            dialog.show();
//            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpPost request = new HttpPost(urls[0]);

                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("fooditem", urls[1]));
                postParameters.add(new BasicNameValuePair("availability", urls[2]));

                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = httpclient.execute(request);

                // StatusLine stat = response.getStatusLine();
              int status = response.getStatusLine().getStatusCode();

                if (status == 200) {


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
            //    dialog.cancel();
            bindView();
       //     notifyDataSetChanged();
            if (result == false)
                Toast.makeText(mContext, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

}
