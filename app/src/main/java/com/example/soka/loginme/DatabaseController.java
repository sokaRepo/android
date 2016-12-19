package com.example.soka.loginme;

/**
 * Created by soka on 19/12/16.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import android.util.Log;
import java.util.HashMap;

/**
 * Created by soka on 11/14/16.
 *
 * WARNING: This class has not been tested
 */

public class DatabaseController {

    private MyDbHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseController(Context context) {
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void checkDbisAlive() {
        if (!db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
    }

    public String getSettingByName(String setting_name) {
        checkDbisAlive();
        SQLiteStatement stmt = this.db.compileStatement("SELECT setting_value FROM settings WHERE setting_name = ?");
        stmt.bindString(1, setting_name);
        return stmt.simpleQueryForString();
    }

    public void updateSettingByName(String setting_name, String value) {
        try {
            checkDbisAlive();
            db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("UPDATE settings SET setting_value = ? WHERE setting_name = ?");
            Log.w("SQLITE", " UPDATE OK");
            stmt.bindString(1, value);
            stmt.bindString(2, setting_name);
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            Log.w("SQLITE", "Exception " + e);
        }
        finally {
            db.endTransaction();
        }
    }

    public void addNumber(String num, String name) {
        try {
            checkDbisAlive();
            db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("INSERT INTO phones(number, name) VALUES (?,?)");
            stmt.bindString(1, num);
            stmt.bindString(2, name);
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            Log.w("SQLITE", "Exception " + e);
        }
        finally {
            db.endTransaction();
        }
    }

    public HashMap<String, String> getAllNumbers() {
        checkDbisAlive();
        String[] columnToReturn = {"number", "name"};
        Cursor cur = db.query("phones", columnToReturn, null, null, null, null, null);
        HashMap<String, String> data = new HashMap<String, String>();
        if (cur.moveToFirst()) {
            do {
                data.put(cur.getString(cur.getColumnIndex("number")), cur.getString(cur.getColumnIndex("name")));
                Log.d("GETALLNUMBERS", "Num: " + cur.getString(cur.getColumnIndex("number")) + " Name: " + cur.getString(cur.getColumnIndex("name")));
            } while (cur.moveToNext());
        }
        return data;
    }

    public void removeNumber(String num) {
        try {
            checkDbisAlive();
            db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("DELETE FROM phones WHERE number = ?");
            stmt.bindString(1, num);
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            Log.w("SQLITE", "Exception " + e);
        }
        finally {
            db.endTransaction();
        }
    }

    public void close() {
        try {
            if (db.isOpen())
                db.close();
        }catch (Exception e) {
            Log.w("SQLITE", "Exception on close: " + e);
        }
    }
}
