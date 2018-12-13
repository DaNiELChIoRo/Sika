package com.muuyal.sika.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/29/17.
 */

public class Glossary {

    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /***
     * This method parse Glossary from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of glossary
     * ***/
    public static List<Glossary> parseGlossary(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Glossary> mGlossaryList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Glossary mGlossary = new Glossary();
                    mGlossary.setTitle(jsonObject.optString("title"));
                    mGlossary.setDescription(jsonObject.optString("description"));

                    mGlossaryList.add(mGlossary);
                }
            }

            return mGlossaryList;
        }
        return null;
    }
}
