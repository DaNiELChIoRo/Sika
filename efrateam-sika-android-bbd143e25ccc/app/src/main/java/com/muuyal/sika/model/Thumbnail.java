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

public class Thumbnail implements Parcelable {

    private int id;
    private String name;
    private int size;
    private String mime;
    private String group;
    private int width;
    private int height;
    private String createdAt;
    private String updatedAt;
    private String url;

    public Thumbnail() {

    }

    protected Thumbnail(Parcel in) {
        id = in.readInt();
        name = in.readString();
        size = in.readInt();
        mime = in.readString();
        group = in.readString();
        width = in.readInt();
        height = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        url = in.readString();
    }

    public static final Creator<Thumbnail> CREATOR = new Creator<Thumbnail>() {
        @Override
        public Thumbnail createFromParcel(Parcel in) {
            return new Thumbnail(in);
        }

        @Override
        public Thumbnail[] newArray(int size) {
            return new Thumbnail[size];
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(size);
        parcel.writeString(mime);
        parcel.writeString(group);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeString(url);
    }

    /***
     * This method parse the thumbnails WS response
     * @param jsonArray is the json array to parse
     *
     * @return the thumbnails
     * ***/
    public static List<Thumbnail> parseThumbnails(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Thumbnail> mThumbnailList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);

                if (jsonObject != null) {
                    Thumbnail mThumbnail = new Thumbnail();
                    mThumbnail.setId(jsonObject.optInt("id"));
                    mThumbnail.setName(jsonObject.optString("name"));
                    mThumbnail.setSize(jsonObject.optInt("size"));
                    mThumbnail.setMime(jsonObject.optString("mime"));
                    mThumbnail.setGroup(jsonObject.optString("group"));
                    mThumbnail.setWidth(jsonObject.optInt("width"));
                    mThumbnail.setHeight(jsonObject.optInt("height"));
                    mThumbnail.setCreatedAt(jsonObject.optString("created_at"));
                    mThumbnail.setUpdatedAt(jsonObject.optString("updated_at"));
                    mThumbnail.setUrl(jsonObject.optString("url"));

                    mThumbnailList.add(mThumbnail);
                }
            }
            return mThumbnailList;
        }
        return null;
    }
}
