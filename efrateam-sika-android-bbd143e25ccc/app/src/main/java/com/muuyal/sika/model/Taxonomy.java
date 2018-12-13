package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 6/22/17.
 */

public class Taxonomy implements Parcelable {

    private int id;
    private String type;
    private String value;
    private String createdAt;
    private String updatedAt;

    private int color;
    private List<Taxonomy> taxonomies;
    private int idParent;

    public Taxonomy() {

    }

    protected Taxonomy(Parcel in) {
        id = in.readInt();
        type = in.readString();
        value = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        color = in.readInt();
        taxonomies = in.createTypedArrayList(Taxonomy.CREATOR);
        idParent = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(value);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(color);
        dest.writeTypedList(taxonomies);
        dest.writeInt(idParent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Taxonomy> CREATOR = new Creator<Taxonomy>() {
        @Override
        public Taxonomy createFromParcel(Parcel in) {
            return new Taxonomy(in);
        }

        @Override
        public Taxonomy[] newArray(int size) {
            return new Taxonomy[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<Taxonomy> getTaxonomies() {
        return taxonomies;
    }

    public void setTaxonomies(List<Taxonomy> taxonomies) {
        this.taxonomies = taxonomies;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    /***
     * This method parse taxonomies from Array Json
     * @param jsonArray is the json array to parse
     * @param idParent is the current idParent (-1 -> IS A PARENT)
     *
     * @return List of taxonomies
     * ***/
    public static List<Taxonomy> parseTaxonomies(JSONArray jsonArray, int idParent) {
        if (jsonArray != null) {
            List<Taxonomy> mTaxonomyList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Taxonomy mTaxonomy = new Taxonomy();
                    mTaxonomy.setId(jsonObject.optInt("id", -1));
                    mTaxonomy.setType(jsonObject.optString("type"));
                    mTaxonomy.setValue(jsonObject.optString("value"));
                    mTaxonomy.setCreatedAt(jsonObject.optString("created_at"));
                    mTaxonomy.setUpdatedAt(jsonObject.optString("updated_at"));
                    mTaxonomy.setTaxonomies(Taxonomy.parseTaxonomies(jsonObject.optJSONArray("taxonomies"), mTaxonomy.getId()));
                    mTaxonomy.setIdParent(idParent);

                    mTaxonomyList.add(mTaxonomy);
                }
            }

            return mTaxonomyList;
        }
        return null;
    }
}
