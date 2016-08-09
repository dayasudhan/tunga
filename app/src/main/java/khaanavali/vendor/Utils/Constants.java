package khaanavali.vendor.Utils;

/**
 * Created by dganeshappa on 6/8/2016.
 */
public class Constants {
    public static final String LOCALHOST_URL = "http://10.239.54.38:3000";
    public static final String RELEASE_URL = "http://oota.herokuapp.com";
    public static final String DEBUG_URL = "http://kuruva.herokuapp.com";
    public static final String MAIN_URL = DEBUG_URL;
    public static final String ORDER_URL = MAIN_URL + "/v1/vendor/order/";
    public static final String LOGIN_URL =  MAIN_URL +"/v1/m/login";
    public static final String GET_MENU = MAIN_URL +"/v1/vendor/menu/";
    public static final String DELETE_MENU= MAIN_URL + "/v1/vendor/menu/item/";
    public static final String GET_STATUS_URL = MAIN_URL +"/v1/vendor/order/status/";
    public static final String POST_ISOPEN = MAIN_URL +"/v1/vendor/isopen/";
    public static final String FIREBASE_APP = "https://project-8598805513533999178.firebaseio.com";
    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
}
