package com.muuyal.sika.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isra on 6/22/17.
 */

public class Company {

    private int id;
    private String name;
    private String details;
    private String createdAt;
    private String updatedAt;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
     * This method parse companies from Array Json
     *
     * @param jsonArray is the json array to parse
     * @return List of companies
     * ***/
    public static List<Company> parseCompanies(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Company> mCompanyList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Company mCompany = new Company();
                    mCompany.setId(jsonObject.optInt("id", -1));
                    mCompany.setName(jsonObject.optString("name"));
                    mCompany.setDetails(jsonObject.optString("details"));
                    mCompany.setCreatedAt(jsonObject.optString("created_at"));
                    mCompany.setUpdatedAt(jsonObject.optString("updated_at"));

                    mCompanyList.add(mCompany);
                }
            }

            return mCompanyList;
        }
        return null;
    }
}
