package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isra on 6/22/17.
 */

public class Product implements Parcelable {

    private int id;
    private String name;
    private String details;
    private String createdAt;
    private String deletedAt;
    private List<String> tools;
    private List<ProductRelated> related;
    private Photo photo;

    private int color;

    public Product() {

    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        details = in.readString();
        createdAt = in.readString();
        deletedAt = in.readString();
        tools = in.createStringArrayList();
        related = in.createTypedArrayList(ProductRelated.CREATOR);
        photo = in.readParcelable(Photo.class.getClassLoader());
        color = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    public List<ProductRelated> getRelated() {
        return related;
    }

    public void setRelated(List<ProductRelated> related) {
        this.related = related;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(details);
        parcel.writeString(createdAt);
        parcel.writeString(deletedAt);
        parcel.writeStringList(tools);
        parcel.writeTypedList(related);
        parcel.writeParcelable(photo, i);
        parcel.writeInt(color);
    }

    /***
     * This method parse products from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of products
     * ***/
    public static List<Product> parseProducts(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Product> mProductList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Product mProduct = new Product();
                    mProduct.setId(jsonObject.optInt("id", -1));
                    mProduct.setName(jsonObject.optString("name"));
                    mProduct.setDetails(jsonObject.optString("details"));
                    mProduct.setCreatedAt(jsonObject.optString("created_at"));
                    mProduct.setDeletedAt(jsonObject.optString("deleted_at"));
                    mProduct.setPhoto(Photo.parsePhoto(jsonObject.optJSONObject("photo")));
                    mProduct.setRelated(ProductRelated.parseRelatedProduct(jsonObject.optJSONArray("related")));

                    List<String> mTools = new ArrayList<>();
                    JSONArray jsonTools = jsonObject.optJSONArray("tools");
                    if (jsonTools != null) {
                        for (int j = 0; j < jsonTools.length(); j++) {
                            mTools.add(jsonTools.optString(j));
                        }
                        mProduct.setTools(mTools);
                    }

                    mProductList.add(mProduct);
                }
            }

            return mProductList;
        }
        return null;
    }

    /***
     * This method parse product from JsonObject
     * @param jsonObject is the json array to parse
     *
     * @return a product
     * ***/
    public static Product parseProduct(JSONObject jsonObject) {
        if (jsonObject != null) {
            Product mProduct = new Product();
            mProduct.setId(jsonObject.optInt("id", -1));
            mProduct.setName(jsonObject.optString("name"));
            mProduct.setDetails(jsonObject.optString("details"));
            mProduct.setCreatedAt(jsonObject.optString("created_at"));
            mProduct.setDeletedAt(jsonObject.optString("deleted_at"));
            mProduct.setPhoto(Photo.parsePhoto(jsonObject.optJSONObject("photo")));
            mProduct.setRelated(ProductRelated.parseRelatedProduct(jsonObject.optJSONArray("related")));

            List<String> mTools = new ArrayList<>();
            JSONArray jsonTools = jsonObject.optJSONArray("tools");
            if (jsonTools != null) {
                for (int j = 0; j < jsonTools.length(); j++) {
                    mTools.add(jsonTools.optString(j));
                }
                mProduct.setTools(mTools);
            }

            return mProduct;
        }
        return null;
    }
}
