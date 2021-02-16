package com.niamh.sailing3app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.niamh.sailing3app.SafetyCRUD.CreateSafety.Safety;
import com.niamh.sailing3app.Utils.Config;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseQueryClass {

    private final Context context;

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    public DatabaseQueryClass(Context context){
        this.context = context;
    }

    //Inserting into the safety table
    public long insertSafety(Safety safety){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Bitmap imageToStoreBitmap = safety.getImage();
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,50,objectByteArrayOutputStream);
        imageInBytes = objectByteArrayOutputStream.toByteArray();

        contentValues.put(Config.COLUMN_SAFETY_TYPE, safety.getType());
        contentValues.put(Config.COLUMN_SAFETY_DESCRIPTION, safety.getDescription());
        contentValues.put(Config.COLUMN_SAFETY_AVAILABLE, safety.getAvailable());
        contentValues.put(Config.COLUMN_SAFETY_AVAILUSER, safety.getAvailuser());
        contentValues.put(Config.COLUMN_SAFETY_FAULT, safety.getFault());
        contentValues.put(Config.COLUMN_SAFETY_FAULTUSER, safety.getFaultuser());
        contentValues.put(Config.COLUMN_SAFETY_FAULTDES, safety.getFaultdes());
        contentValues.put(Config.COLUMN_SAFETY_IMAGE, imageInBytes);

        //Try catch statement for if the contet is blank and fails a message should post
        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_SAFETY, null, contentValues);
        } catch (SQLiteException e){
            Log.d("***NIAMH_FYP***", "Insert Exception: "+ e.getMessage());
            Toast.makeText(context, "Insert Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return id;
    }

    //Getting all the values of the Safety equipment table
    public List<Safety> getAllSafety(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        List<Safety> safetyList = new ArrayList<>();
        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
             Cursor cursor = sqLiteDatabase.query(Config.TABLE_SAFETY, null, null, null,
                     null, null, null, null)) {
            if (cursor.getCount()!=0){
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SAFETY_ID));
                    String type = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_TYPE));
                    String description = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_DESCRIPTION));
                    String available = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_AVAILABLE));
                    String availuser = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_AVAILUSER));
                    String fault = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULT));
                    String faultuser = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULTUSER));
                    String faultdes = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULTDES));
                    byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(Config.COLUMN_SAFETY_IMAGE));

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    safetyList.add(new Safety(id, type, description, available, availuser, fault, faultuser, faultdes, bitmap));

                }

                return safetyList;
            }
            //Try catch statement for if the contet is blank and fails a message should post
        } catch (Exception e) {
            Log.d("***NIAMH_FYP***", "Exception: " + e.getMessage());
            Toast.makeText(context, "Get All Operation failed", Toast.LENGTH_SHORT).show();
        }

        return Collections.emptyList();
    }

    //Getting each attribute by its ID number
    public Safety getSafetyById(long safetyId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Safety safety = null;
        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Config.TABLE_SAFETY, null,
                    Config.COLUMN_SAFETY_ID + " = ? ", new String[] {String.valueOf(safetyId)},
                    null, null, null);

            if(cursor!=null && cursor.getCount() > 0)
            {
                if (cursor.moveToFirst())
                {
                    do {
                        String type = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_TYPE));
                        String description = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_DESCRIPTION));
                        String available = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_AVAILABLE));
                        String availuser = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_AVAILUSER));
                        String fault = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULT));
                        String faultuser = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULTUSER));
                        String faultdes = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULTDES));
                        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(Config.COLUMN_SAFETY_IMAGE));

                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        safety = new Safety((int) safetyId, type, description, available, availuser, fault, faultuser, faultdes, bitmap);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException e){
            Log.d("***NIAMH_FYP***", "Exception: "+ e.getMessage());
            Toast.makeText(context, "Get Safety By ID Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return safety;
    }

    public List<String> getSType(){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqlLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        String [] sqlSelect = {Config.COLUMN_SAFETY_TYPE};

        try {
            cursor = sqlLiteDatabase.query(Config.TABLE_SAFETY, sqlSelect, null, null, null, null, null);
            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<String> typeList = new ArrayList<>();
                    do {
                        typeList.add(cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_TYPE)));

                    } while (cursor.moveToNext());

                    return typeList;
                }

        } catch (Exception e) {
            Log.d("***NIAMH_FYP_DBQ2***", "Exception: " + e.getMessage());
            Toast.makeText(context, "Get Type operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqlLiteDatabase.close();
        }

        return Collections.emptyList();

    }

    public List<String> getSafetyByType(String safetyType) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Safety safety = null;
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(Config.TABLE_SAFETY, null,
                    Config.COLUMN_SAFETY_TYPE + " LIKE ? ", new String[]{"%" + safetyType + "%"},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SAFETY_ID));
                String description = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_DESCRIPTION));
                String available = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_AVAILABLE));
                String availuser = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_AVAILUSER));
                String fault = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULT));
                String faultuser = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULTUSER));
                String faultdes = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SAFETY_FAULTDES));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(Config.COLUMN_SAFETY_IMAGE));

                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                safety = new Safety (id, safetyType, description, available, availuser, fault, faultuser, faultdes, bitmap);
            }

        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return (List<String>) safety;
    }


    //Updating the info in the safety equipment table
    public long updateSafety(Safety safety){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Bitmap imageToStoreBitmap = safety.getImage();
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,50,objectByteArrayOutputStream);
        imageInBytes = objectByteArrayOutputStream.toByteArray();

        contentValues.put(Config.COLUMN_SAFETY_TYPE, safety.getType());
        contentValues.put(Config.COLUMN_SAFETY_DESCRIPTION, safety.getDescription());
        contentValues.put(Config.COLUMN_SAFETY_AVAILABLE, safety.getAvailable());
        contentValues.put(Config.COLUMN_SAFETY_AVAILUSER, safety.getAvailuser());
        contentValues.put(Config.COLUMN_SAFETY_FAULT, safety.getFault());
        contentValues.put(Config.COLUMN_SAFETY_FAULTUSER, safety.getFaultuser());
        contentValues.put(Config.COLUMN_SAFETY_FAULTDES, safety.getFaultdes());
        contentValues.put(Config.COLUMN_SAFETY_IMAGE, imageInBytes);

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_SAFETY, contentValues,
                    Config.COLUMN_SAFETY_ID + " = ? ",
                    new String[] {String.valueOf(safety.getId())});
        } catch (SQLiteException e){
            Log.d("NIAMH_FYP", "Exception: "+ e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    //Delete each safety attribute individulaly by its id
    public boolean deleteSafetyById(long safetyId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.TABLE_SAFETY,
                Config.COLUMN_SAFETY_ID + " = ? ", new String[]{String.valueOf(safetyId)});

        return row > 0;
    }


    //Delete everything in safety at once
    public boolean deleteAllSafety(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            sqLiteDatabase.delete(Config.TABLE_SAFETY, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_SAFETY);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Log.d("***NIAMH_FYP***", "Exception: "+ e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return deleteStatus;
    }

    //Is not currently used in application but can be used for reporting in th future this counts how much safety equipment there is
    public long getNumberOfSafety(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_SAFETY);
        } catch (SQLiteException e){
            Log.d("NIAMH_FYP", "Exception: "+ e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }



}
