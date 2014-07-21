package com.mokalab.simplesqlitelibrary;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * TODO: JAVADOC
 * <br><br>
 * Created by Pirdad S. on 2014-06-04.
 */
public class DbUpdate extends DatabaseTaskExecutor<Integer, DbUpdate.OnDbUpdateTaskListener> {

    private static final String UPDATE_DELEGATE_MUST_NOT_BE_NULL = "OnDbUpdateDelegate is null. Please pass a proper reference" +
            " to OnDbUpdateDelegate.";

    private static final String CONTENTVALUES_CANT_BE_NULL = "ContentValues is null. Please pass a proper reference to a " +
            "ContentValues.";

    private String mTableName;
    private ContentValues mValues;
    private OnDbUpdateDelegate mUpdateDelegate;

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param taskId
     * @param tableName
     * @param values
     * @param updateDelegate
     * @param listener
     */
    public DbUpdate(int taskId, String tableName, ContentValues values, OnDbUpdateDelegate updateDelegate, OnDbUpdateTaskListener
            listener) {

        super(openDb(), taskId, listener);
        mTableName = tableName;
        mValues = values;
        mUpdateDelegate = updateDelegate;
    }

    @Override
    protected Integer onExecuteTask(SQLiteDatabase db) {

        if (mUpdateDelegate == null) {
            throw new IllegalArgumentException(UPDATE_DELEGATE_MUST_NOT_BE_NULL);
        }

        if (mValues == null) {
            throw new IllegalArgumentException(CONTENTVALUES_CANT_BE_NULL);
        }

        if (TextUtils.isEmpty(mTableName)) {
            throw new IllegalArgumentException(TABLE_NAME_CANT_BE_NULL);
        }

        int numRowsAffected = db.update(mTableName, mValues, mUpdateDelegate.onGetWhereClause(), mUpdateDelegate.onGetWhereArgs());
        return numRowsAffected;
    }

    @Override
    protected void onTaskExecuted(Integer result) {

        closeDb();
        if (result == null || result <= 0) {

            if (getListener() != null) {
                getListener().onDbTaskFailed(getTaskId());
            }
            return;
        }

        if (getListener() != null) {
            getListener().onDbUpdateCompleted(getTaskId(), result);
        }
    }

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public static interface OnDbUpdateTaskListener extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        public abstract void onDbUpdateCompleted(int taskId, int numberOfRowsUpdated);
    }

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public static interface OnDbUpdateDelegate {

        public abstract String onGetWhereClause();
        public abstract String[] onGetWhereArgs();
    }
}
