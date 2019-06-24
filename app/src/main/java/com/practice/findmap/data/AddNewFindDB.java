package com.practice.findmap.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.practice.findmap.domain.model.FindData;
import com.practice.findmap.domain.model.MarkerCoordinates;

import java.util.ArrayList;
import java.util.List;

public class AddNewFindDB {

    public static DBHelper dbHelper;

    public AddNewFindDB(DBHelper dbHelper) {

        this.dbHelper = dbHelper;
    }

    public List<? extends FindData> findByComment(String comment) {
        String sql = "SELECT * FROM finds WHERE comment LIKE '%" + comment + "%';";
        return getFindsByQuery(sql);
    }

    public FindData findByCoordinates(MarkerCoordinates coordinates) {
        String sql = "SELECT * FROM finds WHERE latitude = " + coordinates.getLatitude()
                + " and longitude = " + coordinates.getLongitude() + ";";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                int commentColumn = cursor.getColumnIndex("comment");
                int categoryColumn = cursor.getColumnIndex("category");
                int idColumn = cursor.getColumnIndex("id");

                return new FindData (cursor.getInt(idColumn), coordinates,
                        cursor.getString(commentColumn), cursor.getString(categoryColumn));
            }
        }

        return null;
    }


    public void saveFind(FindData find) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        contentValues.put("latitude", find.getCoordinates().getLatitude());
        contentValues.put("longitude", find.getCoordinates().getLongitude());
        contentValues.put("comment", find.getComment());
        contentValues.put("category", find.getCategory());

        db.insert("finds", null, contentValues);

    }


    public void deleteById(int findId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("finds", "id = " + findId, null);
    }


    public void updateFind(int id, String comment, String category) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] forID = new String[] {Integer.toString(id)};
        cv.put("comment", comment);
        cv.put("category", category);
        db.update("finds", cv, "id = ?", forID);
    }

    private List<? extends FindData> getFindsByQuery(String sql) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(sql, null)) {
            List<FindData> finds = new ArrayList<>();
            if (cursor.moveToFirst()) {
                int latColumn = cursor.getColumnIndex("latitude");
                int lngColumn = cursor.getColumnIndex("longitude");
                int commentColumn = cursor.getColumnIndex("comment");
                int idColumn = cursor.getColumnIndex("id");
                int categoryColumn = cursor.getColumnIndex("category");
                do {
                    MarkerCoordinates coord = new MarkerCoordinates(
                            cursor.getDouble(latColumn),
                            cursor.getDouble(lngColumn)
                    );
                    finds.add(
                            new FindData(
                                    cursor.getInt(idColumn),
                                    coord,
                                    cursor.getString(commentColumn),
                                    cursor.getString(categoryColumn)
                            )
                    );
                } while (cursor.moveToNext());
            }
            cursor.close();
            return finds;
        }
    }


}
