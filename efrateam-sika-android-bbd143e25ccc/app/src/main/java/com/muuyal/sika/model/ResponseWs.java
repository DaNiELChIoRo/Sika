package com.muuyal.sika.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by isra on 6/22/17.
 */

public class ResponseWs implements Serializable {

    private int code;
    private String status;
    private String method;
    private String url;
    private long timestamp;
    private Metadata metadata;
    private String response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    /***
     * This method parse response form webservice
     * @param jsonObject is the json object response
     *
     * @return ResponseWs object
     * ***/
    public static ResponseWs parseResponseWs(JSONObject jsonObject) {
        if (jsonObject != null) {
            ResponseWs mResponseWs = new ResponseWs();
            mResponseWs.setCode(jsonObject.optInt("code"));
            mResponseWs.setStatus(jsonObject.optString("status"));
            mResponseWs.setMethod(jsonObject.optString("method"));
            mResponseWs.setUrl(jsonObject.optString("url"));
            mResponseWs.setTimestamp(jsonObject.optLong("timestamp"));
            mResponseWs.setMetadata(Metadata.parseMetadata(jsonObject.optJSONObject("metadata")));
            mResponseWs.setResponse(jsonObject.optString("response"));

            return mResponseWs;
        }
        return null;
    }
}
