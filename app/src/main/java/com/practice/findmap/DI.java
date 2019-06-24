package com.practice.findmap;

import android.content.Context;

import com.practice.findmap.data.AddNewFindDB;
import com.practice.findmap.data.DBHelper;
import com.practice.findmap.domain.repository.AddNewFindDBInterface;

public class DI {
    private static DI ourInstance = null;

    public static DI getInstance() {
        return ourInstance;
    }

    private final AddNewFindDBInterface addNewFindDBInterface;

    private DI(Context ctx) {
        addNewFindDBInterface = new AddNewFindDB(new DBHelper(ctx));
    }

    static void createInstance(Context ctx) {
        ourInstance = new DI(ctx);
    }
    public AddNewFindDBInterface getAddNewFindDBInterface() {
        return addNewFindDBInterface;
    }
}
