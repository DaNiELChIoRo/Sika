package com.muuyal.sika.webclient;


import com.muuyal.sika.MyApplication;
import com.muuyal.sika.R;

/**
 * Created by Isra on 5/22/2017.
 */

public class API {

    public static final String URL_BASE = MyApplication.getInstance().getString(R.string.url_server) + "/api/" + MyApplication.getInstance().getString(R.string.api_version);

    public static final String URL_LOGIN = URL_BASE + "api/cuenta/login/";
    public static final String URL_GET_ROUTES = "http://maps.googleapis.com/maps/api/directions/json?origin=\" + origin+ \"&destination=\" + destination+ \"&sensor=false";

    public static final String URL_COUNTRY = URL_BASE + "/country";
    public static final String URL_STATES = URL_BASE + "/country/%d/states";
    public static final String URL_DISTRICTS = URL_BASE + "/state/%d";
    public static final String URL_ZIPCODES = URL_BASE + "/state/%d/zipcodes";
    public static final String URL_COMPANY = URL_BASE + "/company";
    public static final String URL_SUGGESTIONS = URL_BASE + "/suggestions";
    public static final String URL_SOLUTION = URL_BASE + "/solution";
    public static final String URL_SOLUTIONS = URL_BASE + "/solutions";
    public static final String URL_PROMOTION = URL_BASE + "/promotion";
    public static final String URL_SLIDER = URL_BASE + "/module/slider";
    public static final String URL_GLOSSARY = URL_BASE + "/glossary";

    public static final String URL_STORES = URL_BASE + "/store";
    public static final String URL_STORES_BY_STATE_ID = URL_BASE + "/state/%d/stores";
    public static final String URL_STORES_BY_PRODUCT_ID = URL_BASE + "/product/%d/stores";
    public static final String URL_STORES_NEAREST = URL_BASE + "/store";
    public static final String URL_STORES_BY_DISTRICT = URL_BASE + "/store?&district=";

    public static final String URL_PRODUCTS = URL_BASE + "/product";
    public static final String URL_PRODUCTS_BY_TAXONOMY_ID = URL_BASE + "/product?taxonomy=%d";
    public static final String URL_PRODUCTS_RELATED = URL_BASE + "/product/%d";

    public static final String URL_TAXONOMY = URL_BASE + "/taxonomy";
    public static final String URL_TAXONOMY_BY_PRODUCT_ID = URL_BASE + "/product/%d/taxonomies";
    //public static final String URL_TAXONOMY_BY_TYPE = URL_BASE + "/taxonomy?type=category";
    public static final String URL_TAXONOMY_BY_TYPE = URL_BASE + "/taxonomy?type=category&onlyParents=true&withSubtaxonomies=true";
}
