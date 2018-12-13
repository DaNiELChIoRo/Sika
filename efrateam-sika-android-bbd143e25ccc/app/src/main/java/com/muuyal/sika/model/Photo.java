package com.muuyal.sika.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by isra on 7/29/17.
 */

public class Photo implements Parcelable {

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
    private List<Thumbnail> thumbnails;

    public Photo() {

    }

    protected Photo(Parcel in) {
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
        thumbnails = in.createTypedArrayList(Thumbnail.CREATOR);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
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

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
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
        parcel.writeTypedList(thumbnails);
    }

    /***
     * This method parse the Photo WS response
     * @param jsonObject is the json array to parse
     *
     * @return the Photo
     * ***/
    public static Photo parsePhoto(JSONObject jsonObject) {
        if (jsonObject != null) {
            Photo mPhoto = new Photo();
            mPhoto.setId(jsonObject.optInt("id"));
            mPhoto.setName(jsonObject.optString("name"));
            mPhoto.setSize(jsonObject.optInt("size"));
            mPhoto.setMime(jsonObject.optString("mime"));
            mPhoto.setGroup(jsonObject.optString("group"));
            mPhoto.setWidth(jsonObject.optInt("width"));
            mPhoto.setHeight(jsonObject.optInt("height"));
            mPhoto.setCreatedAt(jsonObject.optString("created_at"));
            mPhoto.setUpdatedAt(jsonObject.optString("updated_at"));
            mPhoto.setUrl(jsonObject.optString("url"));
            mPhoto.setThumbnails(Thumbnail.parseThumbnails(jsonObject.optJSONArray("thumbnails")));

            return mPhoto;
        }
        return null;
    }
}
