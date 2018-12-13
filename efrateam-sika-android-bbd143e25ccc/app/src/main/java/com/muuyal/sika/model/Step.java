package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/6/17.
 */

public class Step implements Parcelable {

    private String image;
    private String caption;

    public Step() {

    }

    protected Step(Parcel in) {
        image = in.readString();
        caption = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(caption);
    }

    /***
     * This method parse steps from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of steps
     * ***/
    public static List<Step> parseSteps(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Step> mStepList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Step mStep = new Step();
                    mStep.setImage(jsonObject.optString("image"));
                    mStep.setCaption(jsonObject.optString("caption"));

                    mStepList.add(mStep);
                }
            }

            return mStepList;
        }
        return null;
    }
}
