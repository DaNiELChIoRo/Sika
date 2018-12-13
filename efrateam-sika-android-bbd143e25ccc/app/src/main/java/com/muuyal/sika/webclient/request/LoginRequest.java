package com.muuyal.sika.webclient.request;

import com.google.gson.JsonObject;
import com.muuyal.sika.MyApplication;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.webclient.API;
import com.muuyal.sika.webclient.WebClient;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Isra on 5/22/2017.
 */

public class LoginRequest extends WebClient.RequestInterface{

    private static final String TAG = LoginRequest.class.getSimpleName();
    public WebClient.WebClientListener listener;
    public LinkedHashMap<String, String> params;

    public LoginRequest(LinkedHashMap<String, String> params, WebClient.WebClientListener listener) {
        this.listener = listener;
        this.params = params;
    }

    @Override
    public String getUrl() {
        return API.URL_LOGIN;
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.parse(WebClient.CONTENT_TYPE_JSON);
    }

    @Override
    public String getBody() {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : params.entrySet())
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        return jsonObject.toString();
    }

    @Override
    public String getRequestMethod() {
        return WebClient.METHOD_POST;
    }

    @Override
    public void onComplete(final Headers headers, final int code, final Object response) {
        LoggerUtils.logInfo(TAG, "onComplete", code + " :: " + response.toString());
        if (code == WebClient.CODE_OK) {
            //Here parse response
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
