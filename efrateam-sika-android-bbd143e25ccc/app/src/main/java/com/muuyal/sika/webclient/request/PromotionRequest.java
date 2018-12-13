package com.muuyal.sika.webclient.request;

import com.google.gson.JsonObject;
import com.muuyal.sika.MyApplication;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.webclient.API;
import com.muuyal.sika.webclient.WebClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Isra on 5/22/2017.
 */

public class PromotionRequest extends WebClient.RequestInterface{

    private static final String TAG = PromotionRequest.class.getSimpleName();

    private WebClient.WebClientListener listener;
    private LinkedHashMap<String, String> params;

    public PromotionRequest(LinkedHashMap<String, String> params, WebClient.WebClientListener listener) {
        this.listener = listener;
        this.params = params;
    }

    @Override
    public String getUrl() {
        return API.URL_PROMOTION;
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
        return WebClient.METHOD_GET;
    }

    @Override
    public void onComplete(final Headers headers, final int code, final Object response) {
        LoggerUtils.logInfo(TAG, "onComplete", code + " :: " + response.toString());
        if (code == WebClient.CODE_OK) {
            try {
                final ResponseWs mResponseWs;
                if (response instanceof ResponseWs)
                    mResponseWs = (ResponseWs)response;
                else
                    mResponseWs = ResponseWs.parseResponseWs(new JSONObject(response.toString()));

                if (mResponseWs.getCode() == WebClient.CODE_OK) {
                    MyApplication.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onComplete(headers, code, mResponseWs);
                        }
                    });
                } else {
                    onError(null, null);
                }
            } catch (JSONException e) {
                LoggerUtils.logError(TAG, "Error: " + e);
                onError(null, "Error: " + e);
            }
        } else {
            onError(null, null);
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
