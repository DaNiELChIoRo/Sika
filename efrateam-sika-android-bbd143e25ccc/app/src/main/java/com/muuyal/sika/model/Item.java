package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isra on 7/25/17.
 */

public class Item implements Parcelable {

    private int id;
    private String title;
    private String image;
    private String caption;
    private List<ProductItem> products;
    private List<Step> steps;
    private List<String> cause;
    private String glossary;
    private String demo;
    private String type;

    public Item() {

    }

    protected Item(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        caption = in.readString();
        products = in.createTypedArrayList(ProductItem.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        cause = in.createStringArrayList();
        glossary = in.readString();
        demo = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }

    public List<String> getCause() {
        return cause;
    }

    public void setCause(List<String> cause) {
        this.cause = cause;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }


    public String getGlossary() {
        return glossary;
    }

    public void setGlossary(String glossary) {
        this.glossary = glossary;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        dest.writeString(caption);
        dest.writeStringList(cause);
        dest.writeTypedList(steps);
        dest.writeTypedList(products);
        dest.writeString(glossary);
        dest.writeString(demo);
    }

    /***
     * This method parse items from Array Json
     * @param jsonArray is the json array to parse
     *
     * @return List of items
     * ***/
    public static List<Item> parseItem(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Item> mItemList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    Item mItem = new Item();
                    mItem.setId(i);
                    mItem.setTitle(jsonObject.optString("title"));
                    mItem.setImage(jsonObject.optString("image"));
                    mItem.setCaption(jsonObject.optString("caption"));
                    mItem.setProducts(ProductItem.parseProductItem(jsonObject.optJSONArray("products")));

                    JSONArray jsonArrayCause = jsonObject.optJSONArray("cause");

                    if (jsonArrayCause != null) {
                        List<String> causes = new ArrayList<>();
                        for (int j = 0; j < jsonArrayCause.length(); j++) {
                            try {
                                causes.add(jsonArrayCause.getString(j));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mItem.setCause(causes);
                    }

                    mItem.setSteps(Step.parseSteps(jsonObject.optJSONArray("steps")));
                    mItem.setGlossary(jsonObject.optString("glossary"));
                    mItem.setDemo(jsonObject.optString("demo"));
                    mItem.setType(jsonObject.optString("type"));

                    mItemList.add(mItem);
                }
            }

            return mItemList;
        }
        return null;
    }
}
