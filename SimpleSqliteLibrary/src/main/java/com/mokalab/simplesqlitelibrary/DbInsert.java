package com.mokalab.simplesqlitelibrary;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * DbInsert provides sql insert operation. It wraps
 * {@link android.database.sqlite.SQLiteDatabase#insert(String, String, android.content.ContentValues)} and makes it asynchronous. <br>
 * <br>
 *
 * {@link com.mokalab.simplesqlitelibrary.DbInsert.OnDbInsertTaskListener} is used to callback with success or failure response
 * so if you need the callback, you must implement it.
 *
 * <br><br>
 * Created by Pirdad S. on 2014-06-04.
 */
public class DbInsert extends DatabaseTaskExecutor<Long, DbInsert.OnDbInsertTaskListener> {

    private static final String CONTENTVALUES_CANT_BE_NULL = "ContentValues is null. Please pass a proper reference to a " +
            "ContentValues.";

    private String mTableName;
    private ContentValues mValues;

    /**
     * Constructs a new insert operation.
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param taskId the operation id
     * @param tableName table name
     * @param values parameter values
     * @param listener callback listener
     */
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
     * Implement this interface to get a call back with the inserted id.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public static interface OnDbInsertTaskListener extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        /**
         * Called when the insert operation is completed and successful.
         *
         * @param taskId the operation id
         * @param insertedId the inserted id of the new row
         */
        public abstract void onDbInsertCompleted(int taskId, Long insertedId);
    }
}
