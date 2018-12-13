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

public class State implements Parcelable {

    private int id;
    private String name;
    private String createdAt;
    private String updatedAt;
    private List<District> districts;

    public State() {

    }

    protected State(Parcel in) {
        id = in.readInt();
        name = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        districts = in.createTypedArrayList(District.CREATOR);
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };

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

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeTypedList(districts);
    }

    /***
     * This method parse countries from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of countries
     * ***/
    public static List<State> parseStates(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<State> mStateList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    State mState = new State();
                    mState.setId(jsonObject.optInt("id", -1));
                    mState.setName(jsonObject.optString("name"));
                    mState.setCreatedAt(jsonObject.optString("created_at"));
                    mState.setUpdatedAt(jsonObject.optString("updated_at"));

                    mStateList.add(mState);
                }
            }

            return mStateList;
        }
        return null;
    }


    /***
     * This method parse the StateDetail WS response
     * @param jsonObject is the json array to parse
     *
     * @return the state detail
     * ***/
    public static State parseState(JSONObject jsonObject) {
        if (jsonObject != null) {
            State mStateDetail = new State();
            mStateDetail.setId(jsonObject.optInt("id", -1));
            mStateDetail.setName(jsonObject.optString("name"));
            mStateDetail.setCreatedAt(jsonObject.optString("created_at"));
            mStateDetail.setUpdatedAt(jsonObject.optString("updated_at"));
            mStateDetail.setDistricts(District.parseDistrict(jsonObject.optJSONArray("districts"), mStateDetail.getId()));

            return mStateDetail;
        }
        return null;
    }
}
