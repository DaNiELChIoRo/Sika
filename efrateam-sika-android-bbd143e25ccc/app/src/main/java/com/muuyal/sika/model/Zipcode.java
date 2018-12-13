package com.muuyal.sika.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 6/22/17.
 */

public class Zipcode {

    private int id;
    private String zipcode;
    private String district;
    private String suburb;
    private String createdAt;
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
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

    /***
     * This method parse zipcodes from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of zipcode
     * ***/
    public static List<Zipcode> parseZipcodes(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Zipcode> mZipcodeList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Zipcode mZipcode = new Zipcode();
                    mZipcode.setId(jsonObject.optInt("id", -1));
                    mZipcode.setZipcode(jsonObject.optString("zipcode"));
                    mZipcode.setDistrict(jsonObject.optString("district"));
                    mZipcode.setSuburb(jsonObject.optString("suburb"));
                    mZipcode.setCreatedAt(jsonObject.optString("created_at"));
                    mZipcode.setUpdatedAt(jsonObject.optString("updated_at"));

                    mZipcodeList.add(mZipcode);
                }
            }

            return mZipcodeList;
        }
        return null;
    }
}
