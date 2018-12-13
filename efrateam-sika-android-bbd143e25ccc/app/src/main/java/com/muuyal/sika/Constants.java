package com.muuyal.sika;

import android.graphics.Color;

/**
 * Created by SOFTCO on 1/3/2017.
 */

public class Constants {

    /*** DEFINE COLORS DEFAULT ***/
    public static final int COLOR_PRIMARY = Color.parseColor("#F8AE00");
    public static final int COLOR_PRIMARY_DARK = Color.parseColor("#F38400");
    public static final int COLOR_ACCENT = Color.parseColor("#29b6f6");

    /*** DEFINE REQUESTS ***/
    public static final int REQUEST_PERMISSIONS = 100;
    public static final int REQUEST_FILE_CHOOSER = 200;
    public static final int REQUEST_CAMERA = 201;
    public static final int REQUEST_SEND_EMAIL = 202;

    /*** IMAGE OPTIONS ***/
    public static final int OPTION_TAKE_PHOTO = 0;
    public static final int OPTION_CHOOSE_IMAGE = 1;

    /*** TABS SELECTION ***/
    //public static final int TAB_MY_LOCATION = 0;
    public static final int TAB_MAP = 0;
    public static final int TAB_STATE = 1;

    /*** DEFAULTS ***/
    public static final int DEFAULT_COUNTRY_ID = 148;//MEXICO
    public static final int DEFAULT_STATE_ID = 1;//CD DE MEX
    public static final int TAXONOMY_PARENT = -1;

    /*** ITEM SLIDERS ***/
    public static final String TYPE_PROMO = "promo";
    public static final String TYPE_PRODUCT = "product";

    /*** OTHERS ***/
    public static final int DELAY_SWIFT = 3000;
}
