package com.practice.findmap.domain.model;

import com.practice.findmap.data.AddNewFindDB;
import com.practice.findmap.data.DBHelper;

public class FindData {

    private final int id;
    private final MarkerCoordinates coordinates;
    private final String comment;
    private final String category;

    public FindData (int id, MarkerCoordinates coordinates, String comment, String category) {
        this.id = id;
        this.coordinates = coordinates;
        this.comment = comment;
        this.category = category;
    }

    public FindData (MarkerCoordinates coordinates, String comment, String category) {
        this(-1, coordinates, comment, category); // -1 or 0, if SQLite starts counting from 1
    }

    public MarkerCoordinates getCoordinates() {
        return coordinates;
    }

    public String getComment() {
        return comment;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

}
