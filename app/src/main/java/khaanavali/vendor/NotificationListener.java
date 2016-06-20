package khaanavali.vendor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import khaanavali.vendor.Utils.Constants;
import khaanavali.vendor.Utils.SessionManager;

/**
 * Created by Belal on 3/18/2016.
 */
//Class extending service as it is a service that will run in background
public class NotificationListener extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Opening sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, MODE_PRIVATE);

        //Getting the firebase id from sharedpreferences
        String id = sharedPreferences.getString(Constants.UNIQUE_ID, null);

        Firebase.setAndroidContext(getApplicationContext());
        //Creating a firebase object
        Firebase firebase = new Firebase(Constants.FIREBASE_APP + '/' + id);

        //Adding a valueevent listener to firebase
        //this will help us to  track the value changes on firebase
        firebase.addValueEventListener(new ValueEventListener() {

            //This method is called whenever we change the value in firebase
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Getting the value from firebase
                //We stored none as a initial value
                if(snapshot.child("msg").exists()) {
                    String msg = snapshot.child("msg").getValue().toString();

                    //So if the value is none we will not create any notification
                    if (msg.equals("none"))
                        return;

                    //If the value is anything other than none that means a notification has arrived
                    //calling the method to show notification
                    //String msg is containing the msg that has to be shown with the notification
                    showNotification(msg);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });

        return START_STICKY;
    }


    private void showNotification(String msg){
        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
       // i.putExtra("notificationID", notificationID);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Khaanavali");
        builder.setContentText(msg);
        builder.setAutoCancel(true);
//        pi = PendingIntent.getBroadcast(context, 0, dailyIntent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(123, builder.build());
    }
}
