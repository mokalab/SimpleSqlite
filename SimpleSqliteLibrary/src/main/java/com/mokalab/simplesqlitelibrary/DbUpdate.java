package com.mokalab.simplesqlitelibrary;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DbUpdate provides sql update operation. It wraps
 * {@link android.database.sqlite.SQLiteDatabase#update(String, android.content.ContentValues, String, String[])}
 * and makes it asynchronous.
 * <br><br>
 * Use {@link OnDbUpdateDelegate} to provide operation specific where clause and where arguments to your Update Operation. And
 * implement {@link OnDbUpdateTaskListener} to get notified of operation success or failure.
 *
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
     * Constructs a new update operation.
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param taskId the operation id
     * @param tableName the name of the table
     * @param values the values that need to be updated
     * @param updateDelegate callback for where clause/arg
     * @param listener callback listener for success/failure
     */
    public DbUpdate(int taskId, @NotNull String tableName, @NotNull ContentValues values,
                    @NotNull OnDbUpdateDelegate updateDelegate, @Nullable OnDbUpdateTaskListener
            listener) {

        super(openDb(), taskId, listener);
        mTableName = tableName;
        mValues = values;
        mUpdateDelegate = updateDelegate;
    }

    @Override
    protected Integer onExecuteTask(@Nullable SQLiteDatabase db) {

        if (db == null) return null;

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
    protected void onTaskExecuted(@Nullable Integer result) {

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
     * Implement this interface to get notified of the update success or failure.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public static interface OnDbUpdateTaskListener extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        /**
         * This method will get called from {@link com.mokalab.simplesqlitelibrary.DbUpdate} to notify the caller of a
         * successful update operation.
         * @param taskId the operation id
         * @param numberOfRowsUpdated the number of rows that was updated
         */
        public abstract void onDbUpdateCompleted(int taskId, int numberOfRowsUpdated);
    }

    /**
     * Implement this interface to provide the where clause and the where arguments.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public static interface OnDbUpdateDelegate {

        /**
         * Return operation specific where clause. Ex. _id = ? & name = ?
         */
        @Nullable
        public abstract String onGetWhereClause();

        /**
         * Return the where clause's arguments. Ex. new String[] { "10", "name" }
         */
        @Nullable
        public abstract String[] onGetWhereArgs();
    }
}
