package materialapp.survivingwithandroid.com.materialapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import materialapp.survivingwithandroid.com.materialapp.Utils.Constants;
import materialapp.survivingwithandroid.com.materialapp.Utils.SessionManager;


import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText un,pw;
    TextView error;
    Button ok;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        un=(EditText)findViewById(R.id.et_un);
        pw=(EditText)findViewById(R.id.et_pw);
        ok=(Button)findViewById(R.id.btn_login);
       // error=(TextView)findViewById(R.id.tv_error);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//                postParameters.add(new BasicNameValuePair("username", un.getText().toString()));
//                postParameters.add(new BasicNameValuePair("password", pw.getText().toString()));
                if (un.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pw.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                String response = null;
                try {


                    //      new JSONAsyncTask().execute("http://10.239.54.7:3000/v1/vendor/order/summary/x@gmail.com");
                       new JSONAsyncTask().execute(Constants.LOGIN_URL, un.getText().toString(),pw.getText().toString());
                  //  new JSONAsyncTask().execute("http://oota.herokuapp.com/v1/m/login", "test2@gmail.com","test2");
                  //  new JSONAsyncTask().execute("http://oota.herokuapp.com/v1/m/login", "abcdef@gmail.com","qwerty");

                } catch (Exception e) {
                    un.setText(e.toString());
                }

            }
        });

    }
    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading, please wait");
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
                postParameters.add(new BasicNameValuePair("email", urls[1]));
                postParameters.add(new BasicNameValuePair("password", urls[2]));
                postParameters.add(new BasicNameValuePair("role", "vendor"));

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
                    if (data.equals("1")) {

                        session.createLoginSession("Knvl", urls[1]);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        //  Toast.makeText(getApplicationContext(), "successfully logged in", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getApplicationContext(), "error in logged in", Toast.LENGTH_LONG).show();
                    }
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
            //adapter.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "successfully logged in", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
