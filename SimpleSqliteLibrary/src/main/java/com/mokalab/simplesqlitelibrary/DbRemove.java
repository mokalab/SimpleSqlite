package com.mokalab.simplesqlitelibrary;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * DbRemove provides row removal operation. It essentially wraps {@link android.database.sqlite.SQLiteDatabase#delete(String,
 * String, String[])} and makes it asynchronous. <br><br>
 *
 * Defines two callback listeners. {@link com.mokalab.simplesqlitelibrary.DbRemove.OnDbRemoveTaskListenerMultiple} which is the
 * default since the remove operations can be done on multiple rows. {@link
 * com.mokalab.simplesqlitelibrary.DbRemove.OnDbRemoveTaskListenerSingle} is the other callback listener,
 * however it's not used internally by this class. It's only a
 * definition and you must define the usage logic yourself.
 *
 * <br><br>
 * Created by Pirdad S. on 2014-06-04.
 */
public class DbRemove extends DatabaseTaskExecutor<ArrayList<Long>, DbRemove.OnDbRemoveTaskListenerMultiple> {

    private static final String ATLEAST_ONE_ID_NEEDED = "No row id found. Please pass at least one row id in order to remove " +
            "it from the table.";

    private long[] mIdsToRemove;
    private String mTableName;

    /**
     * Constructs a new remove operation.
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param taskId operation id
     * @param tableName name of the table
     * @param idsToRemove ids of the rows to remove (must refer to '_id' column)
     * @param listener callback listener
     */
    public DbRemove(int taskId, String tableName, long[] idsToRemove, OnDbRemoveTaskListenerMultiple listener) {

        super(openDb(), taskId, listener);
        mIdsToRemove = idsToRemove;
        mTableName = tableName;
    }

    @Override
    protected ArrayList<Long> onExecuteTask(SQLiteDatabase db) {

        if (TextUtils.isEmpty(mTableName)) {
            throw new IllegalArgumentException(TABLE_NAME_CANT_BE_NULL);
        }

        if (mIdsToRemove.length == 0) {
            throw new IllegalArgumentException(ATLEAST_ONE_ID_NEEDED);
        }

        ArrayList<Long> idsRemoved = new ArrayList<Long>();
        for (int i = 0; i < mIdsToRemove.length; i++) {

            long id = mIdsToRemove[i];
            int rowsAffected = db.delete(mTableName, TablesHelper.DEFAULT_ID_COLUMN_NAME + "=?", new String[] { String.valueOf(id)} );

            if (rowsAffected > 0) {
                idsRemoved.add(id);
            }
        }

        return idsRemoved;
    }

    @Override
    protected void onTaskExecuted(ArrayList<Long> result) {

        closeDb();
        if (result == null || result.size() <= 0) {

            if (getListener() != null) {
                getListener().onDbTaskFailed(getTaskId());
            }
            return;
        }

        if (getListener() != null) {
            getListener().onDbRemoveCompleted(getTaskId(), result);
        }
    }

    /**
     * Used by {@link com.mokalab.simplesqlitelibrary.DbRemove} to callback to the caller of a success or failure operation.
     * The callback will return a List of ids that was removed from the database.
     * Implement this interface to get the callback.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public interface OnDbRemoveTaskListenerMultiple extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        /**
         * Called when a remove operation is successful for a list of ids.
         * @param taskId the operation id
         * @param idsRemoved ids that were removed
         */
        public abstract void onDbRemoveCompleted(int taskId, ArrayList<Long> idsRemoved);
    }

    /**
     * Implement this interface to get a call back of the id that was removed. It is not used internally by
     * {@link com.mokalab.simplesqlitelibrary.DbRemove} so you must implement the logic for calling
     * {@link #onDbRemoveCompleted(int, long)} yourself.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public interface OnDbRemoveTaskListenerSingle extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        /**
         * Called when a remove operation is successful for a single row.
         * @param taskId the operation id
         * @param idRemoved id that was removed
         */
        public abstract void onDbRemoveCompleted(int taskId, long idRemoved);
    }
}
