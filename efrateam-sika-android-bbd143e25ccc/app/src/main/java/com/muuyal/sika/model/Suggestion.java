package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.muuyal.sika.utils.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/25/17.
 */

public class Suggestion implements Parcelable {

    private int id;
    private String title;
    private String color;
    private List<Item> items;

    public Suggestion() {

    }

    protected Suggestion(Parcel in) {
        id = in.readInt();
        title = in.readString();
        color = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Suggestion> CREATOR = new Creator<Suggestion>() {
        @Override
        public Suggestion createFromParcel(Parcel in) {
            return new Suggestion(in);
        }

        @Override
        public Suggestion[] newArray(int size) {
            return new Suggestion[size];
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (!TextUtils.isEmpty(color) && !color.startsWith("#"))
            color = "#" + color;
        this.color = color;
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
        dest.writeString(color);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /***
     * This method parse Suggestion from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of suggestions
     * ***/
    public static List<Suggestion> parseSuggestions(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Suggestion> mSuggestionList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Suggestion mSuggestion = new Suggestion();
                    mSuggestion.setId(i);
                    mSuggestion.setTitle(jsonObject.optString("title"));
                    mSuggestion.setColor(jsonObject.optString("color"));
                    mSuggestion.setItems(Item.parseItem(jsonObject.optJSONArray("items")));

                    mSuggestionList.add(mSuggestion);
                }
            }

            return mSuggestionList;
        }
        return null;
    }
}
