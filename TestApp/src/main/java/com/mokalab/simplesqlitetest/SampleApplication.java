package com.mokalab.simplesqlitetest;

import android.app.Application;

import com.mokalab.simplesqlitelibrary.DatabaseController;
import com.mokalab.simplesqlitelibrary.DatabaseHelper;
import com.mokalab.simplesqlitelibrary.IDatabase;

/**
 * Created by work on 2014-06-12.
 */
public class SampleApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        initialize();
    }

    private void initialize() {

        initializeDb();
    }

    /**
     * How to use and initialize your Application Database
     * using SimpleSqliteLibrary api.
     */
    private void initializeDb() {

        IDatabase db = new SampleDatabase();
        DatabaseHelper helper = new DatabaseHelper(this, db);
        DatabaseController.initialize(helper);
    }
}
