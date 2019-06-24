package com.practice.findmap.domain.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.practice.findmap.data.DBHelper;
import com.practice.findmap.domain.model.FindData;
import com.practice.findmap.domain.model.MarkerCoordinates;

import java.util.ArrayList;
import java.util.List;

public interface AddNewFindDBInterface {

    List<? extends FindData> findByComment(String comment);

    FindData findByCoordinates(MarkerCoordinates coordinates);

    void saveFind(FindData find);

    void deleteById(int findId);

    void updateFind(int id, String comment, String category);

    List<? extends FindData> getFindsByQuery(String sql);
}
