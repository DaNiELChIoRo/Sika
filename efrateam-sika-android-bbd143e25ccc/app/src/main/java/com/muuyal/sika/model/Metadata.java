package com.muuyal.sika.model;

import org.json.JSONObject;

/**
 * Created by isra on 6/22/17.
 */

public class Metadata {

    private int currentPage;
    private int from;
    private int lastPage;
    private String nextPageUrl;
    private String path;
    private int perPage;
    private String prevPageUrl;
    private int to;
    private int total;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /***
     * This method parse the Metadata WS response
     * @param jsonObject is the json array to parse
     *
     * @return the Metadata
     * ***/
    public static Metadata parseMetadata(JSONObject jsonObject) {
        if (jsonObject != null) {
            Metadata metadata = new Metadata();
            metadata.setCurrentPage(jsonObject.optInt("current_page", 1));
            metadata.setFrom(jsonObject.optInt("from", 1));
            metadata.setLastPage(jsonObject.optInt("last_page", 0));
            metadata.setNextPageUrl(jsonObject.optString("next_page_url"));
            metadata.setPath(jsonObject.optString("path"));
            metadata.setPerPage(jsonObject.optInt("per_page", -1));
            metadata.setPrevPageUrl(jsonObject.optString("prev_page_url"));
            metadata.setTo(jsonObject.optInt("to", -1));
            metadata.setTotal(jsonObject.optInt("total", 0));

            return metadata;
        }
        return null;
    }
}
