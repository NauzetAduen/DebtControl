package com.aduen.nauzet.debtcontrol.database;

// This class extends RoomDatabase using the singleton pattern
// We use an object LOCK to synchronization

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {DebtEntry.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "debtControl";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if (sInstance == null)
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }

            return sInstance;
    }

    public abstract DebtDao debtDao();
}
