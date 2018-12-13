package com.muuyal.sika.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 6/22/17.
 */

public class Country {

    private int id;
    private String name;
    private String code;
    private String createdAt;
    private String updatedAt;
    private List<State> states;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    /***
     * This method parse countries from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of countries
     * ***/
    public static List<Country> parseCountries(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Country> mCountryList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Country mCountry = new Country();
                    mCountry.setId(jsonObject.optInt("id", -1));
                    mCountry.setName(jsonObject.optString("name"));
                    mCountry.setCode(jsonObject.optString("code"));
                    mCountry.setCreatedAt(jsonObject.optString("created_at"));
                    mCountry.setUpdatedAt(jsonObject.optString("updated_at"));

                    mCountryList.add(mCountry);
                }
            }

            return mCountryList;
        }
        return null;
    }

    /***
     * This method parse a specific country from JsonObject
     * @param jsonObject is the json array to parse
     *
     * @return A country with states
     * ***/
    public static Country parseCountry(JSONObject jsonObject) {
        if (jsonObject != null) {
            Country mCountry = new Country();
            mCountry.setId(jsonObject.optInt("id", -1));
            mCountry.setName(jsonObject.optString("name"));
            mCountry.setCode(jsonObject.optString("code"));
            mCountry.setCreatedAt(jsonObject.optString("created_at"));
            mCountry.setUpdatedAt(jsonObject.optString("updated_at"));
            mCountry.setStates(State.parseStates(jsonObject.optJSONArray("states")));

            return mCountry;
        }
        return null;
    }
}
