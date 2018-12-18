package com.github.wahyuadepratama.laporinaja.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by wahyu on 17/12/18.
 */

@Entity(tableName = "reports")
public class Report {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "id_owner")
    public int id_owner;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "photo")
    public String photo;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "lat")
    public double lat;

    @ColumnInfo(name = "lang")
    public double lang;

    @ColumnInfo(name = "updated_at")
    public String updated_at;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "favorite")
    public String favorite;

    @ColumnInfo(name = "owner")
    public String owner;

    @ColumnInfo(name = "type_report")
    public String type_report;
}
