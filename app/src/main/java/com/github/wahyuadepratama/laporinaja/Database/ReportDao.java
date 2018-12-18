package com.github.wahyuadepratama.laporinaja.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by wahyu on 18/12/18.
 */

@Dao
public interface ReportDao {

    @Query("SELECT * FROM reports ORDER BY updated_at DESC")
    List<Report> getAllReport();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReport(Report report);

}
