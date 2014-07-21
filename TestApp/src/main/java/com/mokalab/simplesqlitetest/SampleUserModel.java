package com.mokalab.simplesqlitetest;

import com.mokalab.simplesqlitelibrary.IDbModel;

/**
 * Created by work on 2014-06-12.
 */
public class SampleUserModel implements IDbModel {

    private long mDbAssociatedId;
    private String mName;
    private int mAge;

    public SampleUserModel(String name, int age) {
        mName = name;
        mAge = age;
    }

    @Override
    public long getDbAssociatedId() {
        return mDbAssociatedId;
    }

    @Override
    public void setDbAssociatedId(long dbAssociatedId) {
        mDbAssociatedId = dbAssociatedId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }
}
