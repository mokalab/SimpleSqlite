package com.mokalab.simplesqlitelibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.List;

/**
 * DbSelect provides the Sql Select operation on a table. It wraps
 * {@link android.database.sqlite.SQLiteDatabase#query(String, String[], String, String[], String, String,
 * String)} and makes it asynchronous.<br>
 * <br>
 * Use this class to make select statements and it will return to you an ArrayList of {@link IDbModel}.
 * {@link com.mokalab.simplesqlitelibrary.DbSelect.OnDbSelectDelegate} is required to be implemented in order to provide
 * operation specific parameters. It also calls back
 * {@link com.mokalab.simplesqlitelibrary.DbSelect.OnDbSelectDelegate#onSelectParse(android.database.Cursor)} which you can use
 * to parse the Cursor to your model.<br>
 * <br>
 * Defines two callback listeners. {@link com.mokalab.simplesqlitelibrary.DbSelect.OnDbSelectTaskListenerMultiple} which is the
 * default since the select operations can be done on multiple rows. {@link
 * com.mokalab.simplesqlitelibrary.DbSelect.OnDbSelectTaskListenerSingle} is the other callback listener,
 * however it's not used internally by this class. It's only a
 * definition and you must define the usage logic yourself.
 *
 * <br><br>
 * Created by Pirdad S. on 2014-06-04.
 */
public class DbSelect<T extends IDbModel> extends DatabaseTaskExecutor<List<T>, DbSelect.OnDbSelectTaskListenerMultiple> {

    private static final String SELECT_DELEGATE_MUST_NOT_BE_NULL = "OnDbSelectDelegate is null. Please pass a proper reference" +
            " to OnDbSelectDelegate.";

    protected String mTableName;
    protected OnDbSelectDelegate<T> mSelectDelegate;

    /**
     * Constructs a new select operation.
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param taskId the operation id
     * @param tableName name of the table
     * @param selectDelegate parameters callback listener
     * @param listener success/failure callback listener
     */
    public DbSelect(int taskId, String tableName, OnDbSelectDelegate<T> selectDelegate, OnDbSelectTaskListenerMultiple<T> listener) {

        super(openDb(), taskId, listener);
        mSelectDelegate = selectDelegate;
        mTableName = tableName;
    }

    @Override
    protected List<T> onExecuteTask(SQLiteDatabase db) {

        if (mSelectDelegate == null) {
            throw new IllegalArgumentException(SELECT_DELEGATE_MUST_NOT_BE_NULL);
        }

        if (TextUtils.isEmpty(mTableName)) {
            throw new IllegalArgumentException(TABLE_NAME_CANT_BE_NULL);
        }

        Cursor cursor =
                db.query(mTableName,
                mSelectDelegate.onGetColumnNames(),
                mSelectDelegate.onGetWhereClause(),
                mSelectDelegate.onGetSelectionArgs(),
                mSelectDelegate.onGetGroupByStatement(),
                mSelectDelegate.onGetHavingStatement(),
                mSelectDelegate.onGetOrderByStatement());

        if (cursor == null) {
            return null;
        }

        return mSelectDelegate.onSelectParse(cursor);
    }

    @Override
    protected void onTaskExecuted(List<T> result) {

        closeDb();
        if (result == null) {

            if (getListener() != null) {
                getListener().onDbTaskFailed(getTaskId());
            }
            return;
        }

        if (getListener() != null) {
            /*
            IGNORE LINT ERROR HERE AS THE IMPLICIT CAST WILL
            BE SAFE DUE TO TYPE SAFETY ON THE CONSTRUCTOR...
            */
            getListener().onDbSelectCompleted(getTaskId(), result);
        }
    }

    /**
     * Used by {@link com.mokalab.simplesqlitelibrary.DbSelect} to callback to the caller of a success or failure operation.
     * The callback will return a List of parsed {@link com.mokalab.simplesqlitelibrary.IDbModel}.
     * Implement this interface to get the callback.
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param <K> Is the type for the parsed List {@link com.mokalab.simplesqlitelibrary.IDbModel}.
     */
    public static interface OnDbSelectTaskListenerMultiple<K extends IDbModel> extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        /**
         * Called when a select operation is successful.
         * @param taskId the operation id
         * @param result is the parse List of your Model
         */
        public abstract void onDbSelectCompleted(int taskId, List<K> result);
    }

    /**
     * Implement this interface to get a call back with the Parsed Model. It is not used internally by
     * {@link com.mokalab.simplesqlitelibrary.DbSelect} so you must implement the logic for calling
     * {@link #onDbSelectCompleted(int, IDbModel)} yourself.
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param <K> Is the type for the parsed List {@link com.mokalab.simplesqlitelibrary.IDbModel}.
     */
    public static interface OnDbSelectTaskListenerSingle<K extends IDbModel> extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        /**
         * Called when a select operation is successful for a single row.
         * @param taskId the operation id
         * @param result is the parsed Model
         */
        public abstract void onDbSelectCompleted(int taskId, K result);
    }

    /**
     * Since the parameters for
     * { @link android.database.sqlite.SQLiteDatabase#query(String, String[], String, String[], String, String, String) } are all
     * specific, this delegate will allow the child class of { @link DbSelect} to define all the parameters. Plus it will also
     * call {@link #onSelectParse(android.database.Cursor)} that gives you a chance to parse the Models from the cursor.
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param <K>
     */
    public static interface OnDbSelectDelegate<K extends IDbModel> {

        public abstract String[] onGetColumnNames();
        public abstract String onGetWhereClause();
        public abstract String[] onGetSelectionArgs();
        public abstract String onGetGroupByStatement();
        public abstract String onGetHavingStatement();
        public abstract String onGetOrderByStatement();
        public abstract List<K> onSelectParse(Cursor cursor);
    }
}
