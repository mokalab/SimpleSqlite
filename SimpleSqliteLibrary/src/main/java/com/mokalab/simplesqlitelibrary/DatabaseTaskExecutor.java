package com.mokalab.simplesqlitelibrary;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.jetbrains.annotations.Nullable;

/**
 * Extend this class to define logic for executing a {@link android.database.sqlite.SQLiteDatabase} related operation.
 * This class makes your operation asynchronous. You must implement {@link #onExecuteTask(android.database.sqlite.SQLiteDatabase)}
 * and {@link #onPostExecute(Object)}.
 * <br>
 * For example: your simple CRUD operations can extend from this class.
 *
 * <br><br>
 * Created by Pirdad S. on 2014-05-26.
 *
 * @param <T> the return type for {@link #onExecuteTask(android.database.sqlite.SQLiteDatabase)}. Your subclass must define this.
 * @param <P> the type for the callback listener. Must extend from {@link com.mokalab.simplesqlitelibrary.DatabaseTaskExecutor
 * .OnDbTaskExecutedListener}. Your subclass must define this.
 */
public abstract class DatabaseTaskExecutor<T, P extends DatabaseTaskExecutor.OnDbTaskExecutedListener> extends AsyncTask<Void, Void, T> {

    protected static final String TABLE_NAME_CANT_BE_NULL = "The passed Table Name is null or empty. Please pass a proper Table" +
            " Name.";

    private P mListener;
    private SQLiteDatabase mDb;
    private int mTaskId;

    /**
     * Constructor for setting up for your operation.
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param db your database object
     * @param taskId an id for this task
     * @param listener callback listener
     */
    public DatabaseTaskExecutor(SQLiteDatabase db, int taskId, @Nullable P listener) {

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
    protected abstract T onExecuteTask(@Nullable SQLiteDatabase db);

    /**
     * Called from {@link #onPostExecute(Object)} after the task has been completed in a
     * background thread.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    protected abstract void onTaskExecuted(@Nullable T result);

    /**
     * Opens the Database. If {@link com.mokalab.simplesqlitelibrary.DatabaseController} was never initialized then it will
     * return null for the {@link android.database.sqlite.SQLiteDatabase}.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    @Nullable
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
     * Returns the operation task id (Integer).
     *
     * <br><br>
     * Created by Pirdad S.
     */
    public int getTaskId() {
        return mTaskId;
    }

    /**
     * Returns the callback Listener.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    @Nullable
    public P getListener() {
        return mListener;
    }

    /**
     * This interface is the base definition of the callback Listener. It only defines {@link #onDbTaskFailed(int)} which
     * should only be called whenever a task has failed. You should extend this interface and define your own onSuccess methods
     * depending on it's context.
     *
     * <br><br>
     * Created by Pirdad S.
     */
    protected static interface OnDbTaskExecutedListener {

        public abstract void onDbTaskFailed(int taskId);
    }
}
