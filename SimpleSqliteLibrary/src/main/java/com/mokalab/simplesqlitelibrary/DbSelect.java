package com.mokalab.simplesqlitelibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Use this class to make select statements on certain table.
 * It will return to you an ArrayList of {@link IDbModel} by going
 * through your callback methods of {@link DbSelect.OnDbSelectDelegate}.
 *
 * <br><br>
 * Created by Pirdad S. on 2014-06-04.
 */
public class DbSelect<T extends IDbModel> extends DatabaseTaskExecutor<ArrayList<T>, DbSelect.OnDbSelectTaskListenerMultiple> {

    private static final String SELECT_DELEGATE_MUST_NOT_BE_NULL = "OnDbSelectDelegate is null. Please pass a proper reference" +
            " to OnDbSelectDelegate.";

    protected String mTableName;
    protected OnDbSelectDelegate<T> mSelectDelegate;

    /**
     * TODO: JAVA DOC
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param taskId
     * @param tableName
     * @param selectDelegate
     * @param listener
     */
    public DbSelect(int taskId, String tableName, OnDbSelectDelegate<T> selectDelegate, OnDbSelectTaskListenerMultiple<T> listener) {

        super(openDb(), taskId, listener);
        mSelectDelegate = selectDelegate;
        mTableName = tableName;
    }

    @Override
    protected ArrayList<T> onExecuteTask(SQLiteDatabase db) {

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
    protected void onTaskExecuted(ArrayList<T> result) {

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
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param <K>
     */
    public static interface OnDbSelectTaskListenerMultiple<K extends IDbModel> extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        public abstract void onDbSelectCompleted(int taskId, ArrayList<K> result);
    }

    /**
     * TODO: JAVADOC
     *
     * <br><br>
     * Created by Pirdad S.
     *
     * @param <K>
     */
    public static interface OnDbSelectTaskListenerSingle<K extends IDbModel> extends DatabaseTaskExecutor.OnDbTaskExecutedListener {

        public abstract void onDbSelectCompleted(int taskId, K result);
    }

    /**
     * TODO: JAVADOC
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
        public abstract ArrayList<K> onSelectParse(Cursor cursor);
    }
}
