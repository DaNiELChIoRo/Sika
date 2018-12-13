package com.muuyal.sika.webclient.request;

import com.muuyal.sika.MyApplication;
import com.muuyal.sika.model.UserLocation;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.webclient.WebClient;

import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Isra on 5/22/2017.
 */

public class GetRouteRequest extends WebClient.RequestInterface{

    private static final String TAG = GetRouteRequest.class.getSimpleName();
    public WebClient.WebClientListener listener;

    private UserLocation origin, destination;
    //private String origin = "19.467978,-99.190407";
    //private String destination = "19.428448,-99.163264";

    public GetRouteRequest(UserLocation origin, UserLocation destination, WebClient.WebClientListener listener) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
        //this.origin = origin;
        //this.destination = destination;
    }

    @Override
    public String getUrl() {
        String origin = "" + this.origin.getLatitude() + "," + this.origin.getLongitude();
        String destination = "" + this.destination.getLatitude() + "," + this.destination.getLongitude();

        return "http://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&sensor=false";
        //return "https://maps.googleapis.com/maps/api/directions/json?origin=19.467978,-99.190407&destination=19.428448,-99.163264";
        //return "https://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA&sensor=false&key=AIzaSyDlH-bBEig8IdKolkeL7m5O3vDQ6mI30oY";
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.parse(WebClient.CONTENT_TYPE_JSON);
    }

    @Override
    public String getRequestMethod() {
        return WebClient.METHOD_GET;
    }

    @Override
    public void onComplete(final Headers headers, final int code, final Object response) {
        LoggerUtils.logInfo(TAG, "onComplete", code + " :: " + response.toString());
        if (code == WebClient.CODE_OK) {
            //Here parse response
            MyApplication.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    listener.onComplete(headers, code, response);
                }
            });
        } else {
            onError(null, "Bad response code = " + code + "\nResponse: " + response);
        }
    }

    @Override
    public void onError(final Exception e, final String message) {
        if (e != null)
            e.printStackTrace();

        MyApplication.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                listener.onError(e, message);
            }
        });
    }

    @Override
    public void onNetworkError() {
        MyApplication.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                listener.onNetworkError();
            }
        });
    }
}
