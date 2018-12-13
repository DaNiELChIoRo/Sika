package com.muuyal.sika.webclient;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.muuyal.sika.MyApplication;
import com.muuyal.sika.R;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.utils.SerializerUtils;

import java.util.Map;

/**
 * Created by Isra on 5/22/2017.
 */

public class Dispacher {

    private static final String TAG = Dispacher.class.getSimpleName();

    /***
     * This method send a simple Request
     *
     * @param request is the Request Interface
     ***/
    public static synchronized void sendRequest(WebClient.RequestInterface request) {
        RequestTask requestTask = new RequestTask(null, request);
        requestTask.execute();
    }

    /***
     * This method send a simple Request
     *
     * @param context is the context of the App
     * @param request is the Request Interface
     ***/
    public static synchronized void sendRequest(Context context, WebClient.RequestInterface request) {
        RequestTask requestTask = new RequestTask(context, request);
        requestTask.execute();
    }

    /***
     * This method send a simple Request
     *
     * @param context        is the context of the App
     * @param loadingMessage is the message to show in a Dialog
     * @param request        is the Request Interface
     ***/
    public static synchronized void sendRequest(Context context, String loadingMessage, WebClient.RequestInterface request) {
        RequestTask requestTask = new RequestTask(context, loadingMessage, request);
        requestTask.execute();
    }

    /***
     * This method send a simple Request
     *
     * @param request is the Request Interface
     * @param tag     is the TAG to get a save response Object
     ***/
    public static synchronized void sendRequest(WebClient.RequestInterface request, String tag) {
        if (tag == null) {
            sendRequest(request);
        } else if (NetworkUtils.isNetworkEnabled(MyApplication.getAppContext())) {
            RequestTask requestTask = new RequestTask(null, request);
            requestTask.execute();
        } else {
            Object object = SerializerUtils.loadSerializedObject(MyApplication.getAppContext(), tag);
            if (object != null) {
                request.onComplete(null, WebClient.CODE_OK, object);
            } else
                request.onNetworkError();
        }
    }

    /***
     * This method send a simple Request
     *
     * @param context is the context of the App
     * @param request is the Request Interface
     * @param tag     is the TAG to get a save response Object
     ***/
    public static synchronized void sendRequest(Context context, WebClient.RequestInterface request, String tag) {
        if (tag == null) {
            sendRequest(context, request);
        } else if (NetworkUtils.isNetworkEnabled(MyApplication.getAppContext())) {
            RequestTask requestTask = new RequestTask(context, request);
            requestTask.execute();
        } else {
            Object object = SerializerUtils.loadSerializedObject(MyApplication.getAppContext(), tag);
            if (object != null) {
                request.onComplete(null, WebClient.CODE_OK, object);
            } else {
                if (context != null)
                    DialogUtils.showAlert(context, context.getString(R.string.title_error_connection), context.getString(R.string.error_connection));
            }
        }
    }

    /***
     * This method send a simple Request
     *
     * @param context        is the context of the App
     * @param loadingMessage is the message to show in a Dialog
     * @param request        is the Request Interface
     * @param tag            is the TAG to get a save response Object
     ***/
    public static synchronized void sendRequest(Context context, String loadingMessage, WebClient.RequestInterface request, String tag) {
        if (tag == null) {
            sendRequest(context, loadingMessage, request);
        } else if (NetworkUtils.isNetworkEnabled(MyApplication.getAppContext())) {
            RequestTask requestTask = new RequestTask(context, loadingMessage, request);
            requestTask.execute();
        } else {
            Object object = SerializerUtils.loadSerializedObject(MyApplication.getAppContext(), tag);
            if (object != null) {
                request.onComplete(null, WebClient.CODE_OK, object);
            } else {
                if (context != null)
                    DialogUtils.showAlert(context, context.getString(R.string.title_error_connection), context.getString(R.string.error_connection));
                request.onNetworkError();
            }
        }
    }

    /***
     * RequestTask is the class to make a request to webservice in specific
     ***/
    private static class RequestTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        private WebClient webClient;
        private WebClient.RequestInterface request;
        private String loadingMessage = null;
        private Dialog dialogLoader;

        RequestTask(Context context, WebClient.RequestInterface request) {
            this.context = context;
            this.request = request;
            this.loadingMessage = null;
        }

        RequestTask(Context context, String loadingMessage, WebClient.RequestInterface request) {
            this.context = context;
            this.loadingMessage = loadingMessage;
            this.request = request;
        }

        @Override
        protected void onPreExecute() {
            if (WebClient.isNetworkEnabled()) {
                debugRequest();
                if (context != null && !TextUtils.isEmpty(loadingMessage)) {
                    dialogLoader = DialogUtils.showProgressLoader(context, loadingMessage);
                    dialogLoader.show();
                }
            } else {
                if (context != null)
                    DialogUtils.showAlert(context, context.getString(R.string.title_error_connection), context.getString(R.string.error_connection));
                request.onNetworkError();
                cancel(true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getWebClient().dispatch(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
            MyApplication.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (dialogLoader != null && dialogLoader.isShowing())
                        dialogLoader.dismiss();
                }
            });
        }

        private WebClient getWebClient() {
            webClient = new WebClient();
            return webClient;
        }

        private void debugRequest() {
            if (LoggerUtils.DEBUG) {
                String headers = "";
                String body = "";
                if (request.getHeaders() != null) {
                    for (Map.Entry<String, String> entrySet : request.getHeaders().entrySet())
                        headers += headers + entrySet.getKey() + ":" + entrySet.getValue();
                }
                if (request.getBody() != null) {
                    body = request.getBody();
                }
                LoggerUtils.logInfo(TAG, "Request URL:\n" + request.getUrl());
                LoggerUtils.logInfo(TAG, "Request HEADERS:\n" + headers);
                LoggerUtils.logInfo(TAG, "Request BODY:\n" + body);
            }
        }
    }
}
