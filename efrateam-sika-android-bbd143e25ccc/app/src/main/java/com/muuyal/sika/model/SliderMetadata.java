package com.muuyal.sika.model;

import org.json.JSONObject;

/**
 * Created by isra on 7/28/17.
 */

public class SliderMetadata {

    private String link;
    private String description;
    private int id;
    private String name;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /***
     * This method parse metadata from Array Json
     * @param jsonObject is the json array to parse
     *
     * @return List of slider
     * ***/
    public static SliderMetadata parseMetadata(JSONObject jsonObject) {
        if (jsonObject != null) {
            SliderMetadata metadata = new SliderMetadata();
            metadata.setLink(jsonObject.optString("link"));
            metadata.setDescription(jsonObject.optString("description"));
            metadata.setId(jsonObject.optInt("id"));
            metadata.setName(jsonObject.optString("name"));

            return metadata;
        }
        return null;
    }
}
