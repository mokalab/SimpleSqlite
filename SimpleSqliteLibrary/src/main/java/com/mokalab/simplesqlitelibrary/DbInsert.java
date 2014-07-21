package com.mokalab.simplesqlitelibrary;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * TODO: JAVADOC
 * Created by work on 2014-06-04.
 */
public class DbInsert extends DatabaseTaskExecutor<Long, DbInsert.OnDbInsertTaskListener> {

    private static final String CONTENTVALUES_CANT_BE_NULL = "ContentValues is null. Please pass a proper reference to a " +
            "ContentValues.";

    private String mTableName;
    private ContentValues mValues;

    public DbInsert(int taskId, String tableName, ContentValues values, OnDbInsertTaskListener listener) {

        super(openDb(), taskId, listener);
        mTableName = tableName;
        mValues = values;
    }

    @Override
    protected Long onExecuteTask(SQLiteDatabase db) {

        if (mValues == null) {
            throw new IllegalArgumentException(CONTENTVALUES_CANT_BE_NULL);
        }

        if (TextUtils.isEmpty(mTableName)) {
            throw new IllegalArgumentException(TABLE_NAME_CANT_BE_NULL);
        }

        long newRowId = db.insert(mTableName, null, mValues);
        return newRowId;
    }

    @Override
    protected void onTaskExecuted(Long result) {

        closeDb();
        if (result == null || result <= 0) {

            if (getListener() != null) {
                getListener().onDbTaskFailed(getTaskId());
            }
            return;
        }

        if (getListener() != null) {
            getListener().onDbInsertCompleted(getTaskId(), result);
        }
    }

    /**
     * TODO: JAVADOC
     */
    public static interface OnDbInsertTaskListener extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        public abstract void onDbInsertCompleted(int taskId, Long insertedId);
    }
}
