package com.github.wahyuadepratama.laporinaja.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wahyu on 02/12/18.
 */

public class ReportItem implements Parcelable {

    int id;
    int id_owner;
    String address;
    String photo;
    String description;
    Double lat;
    Double lang;
    String updated_at;
    String status;
    String owner;
    String type_report;

    public ReportItem(int id, int id_owner, String address, String photo, String description, Double lat, Double lang,
                        String updated_at, String status, String owner, String type_report) {
        this.id = id;
        this.id_owner = id_owner;
        this.address = address;
        this.photo = photo;
        this.description = description;
        this.lat = lat;
        this.lang = lang;
        this.updated_at = updated_at;
        this.status = status;
        this.owner = owner;
        this.type_report = type_report;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_owner() {
        return id_owner;
    }

    public void setId_owner(int id_owner) {
        this.id_owner = id_owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType_report() {
        return type_report;
    }

    public void setType_report(String type_report) {
        this.type_report = type_report;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeInt(this.id_owner);
        parcel.writeString(this.address);
        parcel.writeString(this.photo);
        parcel.writeString(this.description);
        parcel.writeDouble(this.lat);
        parcel.writeDouble(this.lang);
        parcel.writeString(this.updated_at);
        parcel.writeString(this.status);
        parcel.writeString(this.owner);
        parcel.writeString(this.type_report);
    }

    protected ReportItem(Parcel in) {
        id = in.readInt();
        id_owner = in.readInt();
        address = in.readString();
        photo = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lang = null;
        } else {
            lang = in.readDouble();
        }
        updated_at = in.readString();
        status = in.readString();
        owner = in.readString();
        type_report = in.readString();
    }

    public static final Parcelable.Creator<ReportItem> CREATOR = new Parcelable.Creator<ReportItem>() {
        @Override
        public ReportItem createFromParcel(Parcel source) {
            return new ReportItem(source);
        }

        @Override
        public ReportItem[] newArray(int size) {
            return new ReportItem[size];
        }
    };
}
