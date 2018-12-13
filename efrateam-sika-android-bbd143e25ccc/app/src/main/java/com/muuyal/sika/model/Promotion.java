package com.muuyal.sika.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/28/17.
 */

public class Promotion {

    private String image;
    private String description;
    private String link;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    /***
     * This method parse Solution from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of solutions
     * ***/
    public static List<Promotion> parsePromotions(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Promotion> mPromotionList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Promotion mPromotion = new Promotion();
                    mPromotion.setImage(jsonObject.optString("image"));
                    mPromotion.setDescription(jsonObject.optString("description"));
                    mPromotion.setLink(jsonObject.optString("link"));

                    mPromotionList.add(mPromotion);
                }
            }

            return mPromotionList;
        }
        return null;
    }
}
