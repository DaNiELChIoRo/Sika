package com.muuyal.sika.webclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.muuyal.sika.MyApplication;
import com.muuyal.sika.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by SOFTCO on 1/3/2017.
 */

public class WebClient {

    private static final int TIMEOUT = 20000;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_POST_MULTIPART = "POST_MULTIPART";

    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    public static final String CONTENT_TYPE_PLAIN = "text/plain; charset=utf-8";
    public static final String CONTENT_TYPE_FORM_DATA = "multipart/form-data";

    public static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static final int CODE_OK = 200;

    //setup cache
    private static final File httpCacheDirectory = new File(MyApplication.getInstance().getCacheDir(), "responses");
    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            if (NetworkUtils.isNetworkEnabled(MyApplication.getAppContext())) {
                int maxAge = 60 * 60 * 4; // read from cache for 4 Hour
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    private Response get(final RequestInterface requestInterface) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(requestInterface.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(requestInterface.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(requestInterface.getTimeout(), TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request original = chain.request();

                        //add cookie
                        Request.Builder builder = original.newBuilder();
                        for (Map.Entry<String, String> entry : requestInterface.getCookies().entrySet())
                            builder.addHeader(entry.getKey(), entry.getValue());

                        final Request authorized = builder.build();

                        return chain.proceed(authorized);
                    }
                })
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();

        Request.Builder builder = new Request.Builder()
                .url(requestInterface.getUrl());

        for (Map.Entry<String, String> entry : requestInterface.getHeaders().entrySet())
            builder.addHeader(entry.getKey(), entry.getValue());

        Response response = client.newCall(builder.build()).execute();
        return response;
    }

    private Response post(final RequestInterface requestInterface) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(requestInterface.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(requestInterface.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(requestInterface.getTimeout(), TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request original = chain.request();

                        //add cookie
                        Request.Builder builder = original.newBuilder();
                        for (Map.Entry<String, String> entry : requestInterface.getCookies().entrySet())
                            builder.addHeader(entry.getKey(), entry.getValue());

                        final Request authorized = builder.build();

                        return chain.proceed(authorized);
                    }
                })
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();

        RequestBody body = RequestBody.create(requestInterface.getMediaType(), requestInterface.getBody());
        Request.Builder builder = new Request.Builder()
                .url(requestInterface.getUrl())
                .post(body);

        for (Map.Entry<String, String> entry : requestInterface.getHeaders().entrySet())
            builder.addHeader(entry.getKey(), entry.getValue());


        Response response = client.newCall(builder.build()).execute();
        return response;
    }

    private Response postMuiltpart(final RequestInterface requestInterface) throws IOException {
        MultipartBody.Builder multiPartyBodyBuilder = requestInterface.getBodyMultiPart();
        multiPartyBodyBuilder.setType(requestInterface.getMediaType());

        int timeoutMultipart = requestInterface.getCountFiles() * requestInterface.getTimeout();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(timeoutMultipart, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutMultipart, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMultipart, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request original = chain.request();

                        //add cookie
                        Request.Builder builder = original.newBuilder();
                        for (Map.Entry<String, String> entry : requestInterface.getCookies().entrySet())
                            builder.addHeader(entry.getKey(), entry.getValue());

                        final Request authorized = builder.build();

                        return chain.proceed(authorized);
                    }
                })
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();

        Request.Builder builder = new Request.Builder()
                .url(requestInterface.getUrl())
                .post(multiPartyBodyBuilder.build());

        for (Map.Entry<String, String> entry : requestInterface.getHeaders().entrySet())
            builder.addHeader(entry.getKey(), entry.getValue());


        Response response = client.newCall(builder.build()).execute();
        return response;
    }

    /***
     * This method creaqte a personal Cookie
     ***/
    public static Cookie createNonPersistentCookie() {
        return new Cookie.Builder()
                .domain("publicobject.com")
                .path("/")
                .name("cookie-name")
                .value("cookie-value")
                .httpOnly()
                .secure()
                .build();
    }

    public void dispatch(RequestInterface requestInterface) {
        try {
            if (requestInterface.getRequestMethod().equalsIgnoreCase(METHOD_POST)) {
                Response response = post(requestInterface);
                requestInterface.onComplete(response.headers(), response.code(), response.body().string());
            } else if (requestInterface.getRequestMethod().equalsIgnoreCase(METHOD_POST_MULTIPART)) {
                Response response = postMuiltpart(requestInterface);
                requestInterface.onComplete(response.headers(), response.code(), response.body().string());
            } else {
                Response response = get(requestInterface);
                requestInterface.onComplete(response.headers(), response.code(), response.body().string());
            }
        } catch (SocketTimeoutException timeoutError) {
            timeoutError.printStackTrace();
            requestInterface.onError(timeoutError, null);
        } catch (IOException e) {
            e.printStackTrace();
            requestInterface.onError(e, "Exception Error: " + e.getMessage());
        }
    }

    public static boolean isNetworkEnabled() {
        if (MyApplication.getAppContext() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getAppContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)) {
                return true;
            }
        }
        return false;
    }

    public static abstract class WebClientListener<T> {
        public void onComplete(Headers headers, int code, T response) {
        }

        public void onError(Exception e, String message) {
        }

        public void onNetworkError() {
        }

        public void onCancel() {
        }
    }

    public static abstract class RequestInterface extends WebClientListener {
        public String getUrl() {
            return "";
        }

        public Map<String, String> getHeaders() {
            return new HashMap<>();
        }

        public String getBody() {
            return "";
        }

        public MediaType getMediaType() {
            return MediaType.parse(CONTENT_TYPE_JSON);
        }

        public String getRequestMethod() {
            return METHOD_GET;
        }

        public Map<String, String> getCookies() {
            return new HashMap<>();
        }

        public MultipartBody.Builder getBodyMultiPart() {
            return null;
        }

        public int getCountFiles() {
            return 0;
        }

        public int getTimeout() {
            return TIMEOUT;
        }
    }
}
