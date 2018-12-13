package com.muuyal.sika.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 11/27/17.
 */

public class SolutionSection {

    private int id;
    private String title;
    private List<Solution> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Solution> getItems() {
        return items;
    }

    public void setItems(List<Solution> items) {
        this.items = items;
    }

    /***
     * This method parse Solution from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of solutions
     * ***/
    public static List<SolutionSection> parseSolutionSections(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<SolutionSection> mSections = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    SolutionSection mSection = new SolutionSection();
                    mSection.setId(i);
                    mSection.setTitle(jsonObject.optString("title"));
                    mSection.setItems(Solution.parseSolutions(jsonObject.optJSONArray("items")));

                    mSections.add(mSection);
                }
            }

            return mSections;
        }
        return null;
    }
}
