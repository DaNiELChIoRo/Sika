package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/29/17.
 */

public class ProductRelated implements Parcelable {

    private int id;
    private String name;
    private String photo;

    public ProductRelated() {

    }

    protected ProductRelated(Parcel in) {
        id = in.readInt();
        name = in.readString();
        photo = in.readString();
    }

    public static final Creator<ProductRelated> CREATOR = new Creator<ProductRelated>() {
        @Override
        public ProductRelated createFromParcel(Parcel in) {
            return new ProductRelated(in);
        }

        @Override
        public ProductRelated[] newArray(int size) {
            return new ProductRelated[size];
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(photo);
    }

    /***
     * This method parse products from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of related products
     * ***/
    public static List<ProductRelated> parseRelatedProduct(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<ProductRelated> mProductList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    ProductRelated mProduct = new ProductRelated();
                    mProduct.setId(jsonObject.optInt("id", -1));
                    mProduct.setName(jsonObject.optString("name"));
                    mProduct.setPhoto(jsonObject.optString("photo"));

                    mProductList.add(mProduct);
                }
            }

            return mProductList;
        }
        return null;
    }
}
