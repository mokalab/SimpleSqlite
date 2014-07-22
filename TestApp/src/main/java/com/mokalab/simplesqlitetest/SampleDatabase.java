package com.mokalab.simplesqlitetest;

import com.mokalab.simplesqlitelibrary.IDatabase;
import com.mokalab.simplesqlitelibrary.ITable;

import org.jetbrains.annotations.NotNull;

/**
 * Created by work on 2014-06-12.
 */
public class SampleDatabase implements IDatabase {


    @NotNull
    @Override
    public String getDatabaseName() {

        return "SampleDatabase.db";
    }

    @Override
    public int getDatabaseVersion() {
        return 1;
    }

    @NotNull
    @Override
    public ITable[] getDatabaseTables() {

        return new ITable[] {
                new SampleUsersTable()
        };
    }
}
