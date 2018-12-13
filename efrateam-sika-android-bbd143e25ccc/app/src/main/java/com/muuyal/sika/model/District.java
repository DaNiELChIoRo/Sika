package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/31/17.
 */

public class District implements Parcelable {

    private String id;
    private String name;

    public District() {

    }

    protected District(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<District> CREATOR = new Creator<District>() {
        @Override
        public District createFromParcel(Parcel in) {
            return new District(in);
        }

        @Override
        public District[] newArray(int size) {
            return new District[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }

    /***
     * This method parse the District WS response
     * @param jsonDistricts is the json array to parse
     *
     * @return the district
     * ***/
    public static List<District> parseDistrict(JSONArray jsonDistricts, int idState) {
        if (jsonDistricts != null) {
            List<District> districtList = new ArrayList<>();
            for (int i = 0; i < jsonDistricts.length(); i++) {
                District mDistrict = new District();
                mDistrict.setId(idState + "_" + i);
                mDistrict.setName(jsonDistricts.optString(i));

                districtList.add(mDistrict);
            }

            return districtList;
        }

        return null;
    }
}
