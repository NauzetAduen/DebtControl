package com.aduen.nauzet.debtcontrol.database;

// SQLite don't allow Date objects
// We use long type to store dates
// We use this annotation to let SQLite know

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}