package com.github.wahyuadepratama.laporinaja.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by wahyu on 17/12/18.
 */

@Database( entities = {Report.class, Favorite.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();
    public abstract  FavoriteDao favoriteDao();
}
