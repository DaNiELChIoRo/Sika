package com.muuyal.sika.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/28/17.
 */

public class SliderItem {

    private int id;
    private String image;
    private String type;
    private SliderMetadata metadata;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SliderMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(SliderMetadata metadata) {
        this.metadata = metadata;
    }

    /***
     * This method parse Slider from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of slider
     * ***/
    public static List<SliderItem> parseSliders(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<SliderItem> mSliderList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    SliderItem item = new SliderItem();
                    item.setId(i);
                    item.setImage(jsonObject.optString("image"));
                    item.setType(jsonObject.optString("type"));
                    item.setMetadata(SliderMetadata.parseMetadata(jsonObject.optJSONObject("metadata")));

                    mSliderList.add(item);
                }
            }

            return mSliderList;
        }
        return null;
    }
}
