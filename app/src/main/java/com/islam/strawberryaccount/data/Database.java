package com.islam.strawberryaccount.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.pojo.Trader;


@androidx.room.Database(entities = {Trader.class, Package.class, Cash.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static final String DATABASE_NAME = "StrawberryAccountDatabase";

    private static Database instance;

    public static synchronized Database getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
//                    .addCallback(callback)
                    .build();

        }

        return instance;
    }


    public abstract Dao dao();

}
