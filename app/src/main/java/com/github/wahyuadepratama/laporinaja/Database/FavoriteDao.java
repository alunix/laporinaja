package com.github.wahyuadepratama.laporinaja.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by wahyu on 19/12/18.
 */

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY updated_at DESC")
    List<Report> getAllFavoriteReport();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Favorite report);
}
