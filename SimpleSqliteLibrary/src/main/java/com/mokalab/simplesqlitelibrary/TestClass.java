package com.mokalab.simplesqlitelibrary;

import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by work on 2014-07-11.
 */
public class TestClass extends DbSelect
        implements IDatabase, IDbModel, ITable, Iterable, Comparator, DialogInterface.OnClickListener, View.OnLongClickListener,
        DbSelect.OnDbSelectDelegate, DbRemove.OnDbRemoveTaskListenerMultiple {

    public TestClass(int taskId, String tableName, OnDbSelectDelegate selectDelegate, OnDbSelectTaskListenerMultiple listener) {

        super(taskId, tableName, selectDelegate, listener);
    }

    @Override
    protected void onTaskExecuted(Object result) {

    }

    @Override
    protected Object doInBackground(Object[] params) {
        String a = "x";
        return null;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        return 0;
    }

    @Override
    public String getDatabaseName() {
        return null;
    }

    @Override
    public int getDatabaseVersion() {
        return 0;
    }

    @Override
    public ITable[] getDatabaseTables() {
        return new ITable[0];
    }

    @Override
    public long getDbAssociatedId() {
        return 0;
    }

    @Override
    public void setDbAssociatedId(long dbAssociatedId) {

    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String[] getColumnNames() {
        return new String[0];
    }

    @Override
    public String[] getColumnTypes() {
        return new String[0];
    }

    @Override
    public String[] getColumnOptions() {
        return new String[0];
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onDbRemoveCompleted(int taskId, ArrayList<Long> idsRemoved) {

    }

    @Override
    public String[] onGetColumnNames() {
        return new String[0];
    }

    @Override
    public String onGetWhereClause() {
        return null;
    }

    @Override
    public String[] onGetSelectionArgs() {
        return new String[0];
    }

    @Override
    public String onGetGroupByStatement() {
        return null;
    }

    @Override
    public String onGetHavingStatement() {
        return null;
    }

    @Override
    public String onGetOrderByStatement() {
        return null;
    }

    @Override
    public ArrayList onSelectParse(Cursor cursor) {
        return null;
    }

    @Override
    public void onDbTaskFailed(int taskId) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
