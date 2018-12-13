package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/28/17.
 */

public class ProductItem implements Parcelable {

    private int id;
    private String title;
    private String image;

    public ProductItem() {

    }

    protected ProductItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
    }

    /***
     * This method parse product item from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of product list
     * ***/
    public static List<ProductItem> parseProductItem(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<ProductItem> mProductList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    ProductItem mProduct = new ProductItem();
                    mProduct.setId(jsonObject.optInt("id"));
                    mProduct.setTitle(jsonObject.optString("title"));
                    mProduct.setImage(jsonObject.optString("image"));

                    mProductList.add(mProduct);
                }
            }

            return mProductList;
        }
        return null;
    }
}
