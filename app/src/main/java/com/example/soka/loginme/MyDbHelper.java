package com.example.soka.loginme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by soka on 20/11/16.
 */

public class MyDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "settings.db";

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE phones (number text, name text)");
        db.execSQL("CREATE TABLE settings (" +
                "setting_name text, " +
                "setting_value text)");
        db.execSQL("INSERT INTO settings (setting_name, setting_value) values ('token', 'token_value'), ('phones', 'phones_list'), ('time_slot', 'time_slot_value'), ('block_status', 'start'), ('name', 'name_value')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }


}
