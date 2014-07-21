package com.mokalab.simplesqlitelibrary;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * * TODO: JAVADOC
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
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param taskId
     * @param tableName
     * @param idsToRemove
     * @param listener
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
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public interface OnDbRemoveTaskListenerMultiple extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        public abstract void onDbRemoveCompleted(int taskId, ArrayList<Long> idsRemoved);
    }

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public interface OnDbRemoveTaskListenerSingle extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        public abstract void onDbRemoveCompleted(int taskId, long idRemoved);
    }
}
