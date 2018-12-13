package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/25/17.
 */

public class Solution implements Parcelable {

    private int id;
    private String title;
    private String image;
    private int color;
    private String titleSection;
    private List<Item> items;

    public Solution() {

    }

    protected Solution(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        color = in.readInt();
        titleSection = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Solution> CREATOR = new Creator<Solution>() {
        @Override
        public Solution createFromParcel(Parcel in) {
            return new Solution(in);
        }

        @Override
        public Solution[] newArray(int size) {
            return new Solution[size];
        }
    };

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitleSection() {
        return titleSection;
    }

    public void setTitleSection(String titleSection) {
        this.titleSection = titleSection;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeInt(color);
        dest.writeString(titleSection);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /***
     * This method parse Solution from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of solutions
     * ***/
    public static List<Solution> parseSolutions(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Solution> mSuggestionList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Solution mSuggestion = new Solution();
                    mSuggestion.setId(i);
                    mSuggestion.setTitle(jsonObject.optString("title"));
                    mSuggestion.setImage(jsonObject.optString("image"));
                    mSuggestion.setItems(Item.parseItem(jsonObject.optJSONArray("items")));

                    mSuggestionList.add(mSuggestion);
                }
            }

            return mSuggestionList;
        }
        return null;
    }
}
