package com.mokalab.simplesqlitelibrary;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * TODO: JAVADOC
 *
 * <br><br>
 * Created by Pirdad S. on 2014-05-26.
 */
public abstract class DatabaseTaskExecutor<T, P extends DatabaseTaskExecutor.OnDbTaskExecutedListener> extends AsyncTask<Void, Void, T> {

    protected static final String TABLE_NAME_CANT_BE_NULL = "The passed Table Name is null or empty. Please pass a proper Table" +
            " Name.";

    private P mListener;
    private SQLiteDatabase mDb;
    private int mTaskId;

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param db
     * @param taskId
     * @param listener
     */
    public DatabaseTaskExecutor(SQLiteDatabase db, int taskId, P listener) {

        mListener = listener;
        mDb = db;
        mTaskId = taskId;
    }

    @Override
    protected T doInBackground(Void... voids) {

        return onExecuteTask(mDb);
    }

    @Override
    protected void onPostExecute(T result) {

        onTaskExecuted(result);
    }

    /**
     * Called from {@link #doInBackground(Void...)}. It allows the chance to
     * execute some specific task depending on which Class it is being implemented in.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    protected abstract T onExecuteTask(SQLiteDatabase db);

    /**
     * Called from {@link #onPostExecute(Object)} after the task has been completed in a
     * background thread.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    protected abstract void onTaskExecuted(T result);

    /**
     * Opens the Database. If {@link com.mokalab.simplesqlitelibrary.DatabaseController} was never initialized then it will
     * return null for the {@link android.database.sqlite.SQLiteDatabase}.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    protected synchronized static SQLiteDatabase openDb() {

        DatabaseController dbCtrl = null;
        try {
            dbCtrl = DatabaseController.getInstance();
        } catch (IllegalStateException e) {
            return null;
        }

        return dbCtrl.openDatabase();
    }

    /**
     * Closes the Database if the DatabaseController allows it.<br><br>
     * Note: After performing your CRUD, you must call this instead of
     * {@link android.database.sqlite.SQLiteDatabase#close()} to close the db.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    protected synchronized static void closeDb() {

        DatabaseController dbCtrl = null;
        try {
            dbCtrl = DatabaseController.getInstance();
        } catch (IllegalStateException e) {
            return;
        }

        dbCtrl.closeDatabase();
    }

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public int getTaskId() {
        return mTaskId;
    }

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public P getListener() {
        return mListener;
    }

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     */
    protected static interface OnDbTaskExecutedListener {

        public abstract void onDbTaskFailed(int taskId);
    }
}
