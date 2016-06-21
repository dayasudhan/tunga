package khaanavali.vendor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;

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
                    String message  = "New Order Received : " + msg;
                    showNotification(Calendar.getInstance().getTimeInMillis(),message);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });

        return START_STICKY;
    }


    private void showNotification(long when, String msg){
        //Creating a notification
        final String GROUP_KEY_ORDER_IDS = "group_order_ids";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
       // intent.putExtra("notificationID", notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Khaanavali");
        builder.setContentText(msg);
        builder.setAutoCancel(true);
        builder.setWhen(when);
//        builder.setGroup(GROUP_KEY_ORDER_IDS);
//        builder.setGroupSummary(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, builder.build());

//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
//                R.mipmap.ic_launcher);

// Create an InboxStyle notification
//        Notification summaryNotification = new NotificationCompat.Builder(this)
//                .setContentTitle("2 new messages")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(largeIcon)
//                .setStyle(new NotificationCompat.InboxStyle()
//                        .addLine("Alex Faaborg   Check this out")
//                        .addLine("Jeff Chang   Launch Party")
//                        .setBigContentTitle("2 new messages")
//                        .setSummaryText("johndoe@gmail.com"))
//                .setGroup(GROUP_KEY_ORDER_IDS)
//                .setGroupSummary(true)
//                .build();
//        notificationManager.notify((int) when, summaryNotification);
    }
}
