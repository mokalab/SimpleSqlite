package com.mokalab.simplesqlitetest;

import android.content.ContentValues;
import android.database.Cursor;

import com.mokalab.simplesqlitelibrary.DbInsert;
import com.mokalab.simplesqlitelibrary.DbRemove;
import com.mokalab.simplesqlitelibrary.DbSelect;
import com.mokalab.simplesqlitelibrary.DbUpdate;
import com.mokalab.simplesqlitelibrary.ITable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by work on 2014-06-12.
 */
public class SampleUsersTable implements ITable {

    @Override
    public String getTableName() {
        return "UsersTbl";
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { "_id", "name", "age" };
    }

    @Override
    public String[] getColumnTypes() {
        return new String[] { "integer", "text", "integer" };
    }

    @Override
    public String[] getColumnOptions() {
        return new String[] { "primary key autoincrement", "not null", "not null" };
    }

    public static synchronized void insert(int taskId, SampleUserModel sampleItem, DbInsert.OnDbInsertTaskListener listener) {

        if (sampleItem == null) {
            if (listener != null) {
                listener.onDbTaskFailed(taskId);
            }
            return;
        }

        ITable table = new SampleUsersTable();

        ContentValues values = new ContentValues();
        values.put(table.getColumnNames()[1], sampleItem.getName());
        values.put(table.getColumnNames()[2], sampleItem.getAge());

        DbInsert insert = new DbInsert(taskId, table.getTableName(), values, listener);
        insert.execute();
    }

    public static synchronized void selectAll(int taskId, DbSelect.OnDbSelectTaskListenerMultiple listener) {

        final ITable table = new SampleUsersTable();

        DbSelect<SampleUserModel> selectAll = new DbSelect<SampleUserModel>(taskId, table.getTableName(), new DbSelect.OnDbSelectDelegate<SampleUserModel>() {

            @Override
            public String[] onGetColumnNames() {
                return null; /* FOR ALL COLUMNS */
                //return new String[] { table.getColumnNames()[1] }; /* OR SELECTIVE COLUMNS */
            }

            @Override
            public String onGetWhereClause() {
                return null;
            }

            @Override
            public String[] onGetSelectionArgs() {
                return null;
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
            public ArrayList<SampleUserModel> onSelectParse(Cursor cursor) {

                return parseCursorAll(cursor);
            }

        }, listener);

        selectAll.execute();
    }

    public static synchronized void remove(int taskId, SampleUserModel model, final DbRemove.OnDbRemoveTaskListenerSingle dbRemoveListener) {

        if (model == null) {
            if (dbRemoveListener != null) {
                dbRemoveListener.onDbTaskFailed(taskId);
            }
            return;
        }

        ITable table = new SampleUsersTable();
        long[] idsToRemove = new long[]{ model.getDbAssociatedId() };
        DbRemove remove = new DbRemove(taskId, table.getTableName(), idsToRemove, new DbRemove.OnDbRemoveTaskListenerMultiple() {

            @Override
            public void onDbRemoveCompleted(int taskId, List<Long> idsRemoved) {

                if (dbRemoveListener == null) return;
                if (idsRemoved != null && idsRemoved.size() > 0) {
                    long idRemoved = idsRemoved.get(0);
                    dbRemoveListener.onDbRemoveCompleted(taskId, idRemoved);
                } else {
                    onDbTaskFailed(taskId);
                }
            }

            @Override
            public void onDbTaskFailed(int taskId) {

                if (dbRemoveListener != null) {
                    dbRemoveListener.onDbTaskFailed(taskId);
                }
            }
        });
        remove.execute();
    }

    public static synchronized void update(int taskId, final SampleUserModel modelUpdatedData, DbUpdate.OnDbUpdateTaskListener
            dbUpdateListener) {

        if (modelUpdatedData == null) {
            if (dbUpdateListener != null) {
                dbUpdateListener.onDbTaskFailed(taskId);
            }
            return;
        }

        final ITable table = new SampleUsersTable();

        ContentValues values = new ContentValues();
        values.put(table.getColumnNames()[1], modelUpdatedData.getName());
        values.put(table.getColumnNames()[2], modelUpdatedData.getAge());

        DbUpdate update = new DbUpdate(taskId, table.getTableName(), values, new DbUpdate.OnDbUpdateDelegate() {

            @Override
            public String onGetWhereClause() {
                /* _id=? */
                return table.getColumnNames()[0] + "=?";
            }

            @Override
            public String[] onGetWhereArgs() {
                /* the value of ? */
                return new String[]{ String.valueOf(modelUpdatedData.getDbAssociatedId()) };
            }

        }, dbUpdateListener);

        update.execute();
    }

    private static ArrayList<SampleUserModel> parseCursorAll(Cursor cursor) {

        ArrayList<SampleUserModel> models = new ArrayList<SampleUserModel>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            long dbAssociatedId = cursor.getLong(0); /* index 0 is item id if onGetColumns() return null */
            String name = cursor.getString(1); /* index 1 is title if onGetColumns() return null */
            int age = cursor.getInt(2);
            //String title = cursor.getString(0); /* index 0 is title if onGetColumns() return only title */

            SampleUserModel model = new SampleUserModel(name, age);
            model.setDbAssociatedId(dbAssociatedId);

            models.add(model);
            cursor.moveToNext();
        }

        return models;
    }
}
