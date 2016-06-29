package khaanavali.vendor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import khaanavali.vendor.R;
import khaanavali.vendor.Utils.SessionManager;

/**
 * Created by gagan on 11/6/2015.
 */
public class AboutMeFragment extends Fragment {

    View rootview;
    // Session Manager Class
    SessionManager session;
    // Button Logout
    //Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       rootview=inflater.inflate(R.layout.about_me,container,false);
        // Session class instance
        session = new SessionManager(getActivity().getApplicationContext());
        TextView lblName = (TextView) rootview.findViewById(R.id.lblName);
        TextView lblEmail = (TextView) rootview.findViewById(R.id.lblEmail);
        ((MainActivity) getActivity())
                .setActionBarTitle("About Me");
        // Button logout
        //btnLogout = (Button)rootview.findViewById(R.id.btnLogout);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // displaying user data
        lblName.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
        lblEmail.setText(Html.fromHtml("Email: <b>" + email + "</b>"));


        /**
         * Logout button click event
         * */
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // Clear the session data
//                // This will clear all session data and
//                // redirect user to LoginActivity
//                session.logoutUser();
//            }
//        });

        return rootview;
    }



}
