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

public class Store implements Parcelable {

    private int id;
    private String name;
    private String details;
    private String street;
    private String externalNumber;
    private String internalNumber;
    private String visualReference;
    private String phone;
    private Double lat;
    private Double lng;
    private String address;
    private String createdAt;
    private String updatedAt;
    private int stateId;
    private double distance;

    public Store() {

    }

    protected Store(Parcel in) {
        id = in.readInt();
        name = in.readString();
        details = in.readString();
        street = in.readString();
        externalNumber = in.readString();
        internalNumber = in.readString();
        visualReference = in.readString();
        phone = in.readString();
        address = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        stateId = in.readInt();
        distance = in.readDouble();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public void setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getVisualReference() {
        return visualReference;
    }

    public void setVisualReference(String visualReference) {
        this.visualReference = visualReference;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(details);
        dest.writeString(street);
        dest.writeString(externalNumber);
        dest.writeString(internalNumber);
        dest.writeString(visualReference);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(stateId);
        dest.writeDouble(distance);
    }

    /***
     * This method parse stores from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of stores
     * ***/
    public static List<Store> parseStores(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Store> mStoreList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Store mStore = new Store();
                    mStore.setId(jsonObject.optInt("id", -1));
                    mStore.setName(jsonObject.optString("name"));
                    mStore.setDetails(jsonObject.optString("details"));
                    mStore.setStreet(jsonObject.optString("street"));
                    mStore.setExternalNumber(jsonObject.optString("external_number"));
                    mStore.setInternalNumber(jsonObject.optString("internal_number"));
                    mStore.setVisualReference(jsonObject.optString("visual_reference"));
                    mStore.setPhone(jsonObject.optString("phone"));
                    mStore.setLat(jsonObject.optDouble("latitude", -1));
                    mStore.setLng(jsonObject.optDouble("longitude", -1));
                    mStore.setAddress(jsonObject.optString("address"));
                    mStore.setCreatedAt(jsonObject.optString("created_at"));
                    mStore.setUpdatedAt(jsonObject.optString("updated_at"));
                    mStore.setStateId(jsonObject.optInt("state_id", -1));
                    mStore.setDistance(jsonObject.optDouble("distance"));

                    mStoreList.add(mStore);
                }
            }

            return mStoreList;
        }
        return null;
    }
}
