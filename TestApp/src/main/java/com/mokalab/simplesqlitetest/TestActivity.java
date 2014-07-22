package com.mokalab.simplesqlitetest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mokalab.simplesqlitelibrary.DbInsert;
import com.mokalab.simplesqlitelibrary.DbRemove;
import com.mokalab.simplesqlitelibrary.DbSelect;
import com.mokalab.simplesqlitelibrary.DbUpdate;

import java.util.List;

public class TestActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, DbInsert.OnDbInsertTaskListener, DbSelect.OnDbSelectTaskListenerMultiple<SampleUserModel>,TestAdapter.OnBtnRemoveClickListener,
        DbRemove.OnDbRemoveTaskListenerSingle, AdapterView.OnItemLongClickListener, DbUpdate.OnDbUpdateTaskListener {

    private static final int TASK_ID_ADD = 10;
    private static final int TASK_ID_REMOVE = 11;
    private static final int TASK_ID_SELECT_ALL = 12;
    private static final int TASK_ID_UPDATE = 13;

    private ListView mListView;
    private TestAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mAdapter = new TestAdapter(this);
        mAdapter.setBtnRemoveClickListener(this);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        SampleUsersTable.selectAll(TASK_ID_SELECT_ALL, this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        TestAdapter.AdapterItem item = (TestAdapter.AdapterItem) mAdapter.getItem(position);
        if (item.type == TestAdapter.AdapterItem.TYPE_MODEL) {
            showUpdateDialog((SampleUserModel) item.object);
        } else {
            showAdditionDialog();
        }

        return true;
    }

    private void showUpdateDialog(final SampleUserModel originalModel) {

        View rootFieldView = LayoutInflater.from(this).inflate(R.layout.layout_new_user, null);
        final EditText txtName = (EditText) rootFieldView.findViewById(R.id.txt_name);
        final EditText txtAge = (EditText) rootFieldView.findViewById(R.id.txt_age);

        txtName.setText(originalModel.getName());
        txtAge.setText(String.valueOf(originalModel.getAge()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update!");
        builder.setMessage("Update this user's information!");
        builder.setView(rootFieldView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = txtName.getText().toString();
                String age = txtAge.getText().toString();
                SampleUserModel model = new SampleUserModel(name, Integer.parseInt(age));
                model.setDbAssociatedId(originalModel.getDbAssociatedId());
                SampleUsersTable.update(TASK_ID_UPDATE, model, TestActivity.this);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        TestAdapter.AdapterItem item = (TestAdapter.AdapterItem) mAdapter.getItem(i);
        if (item.type == TestAdapter.AdapterItem.TYPE_ADD) {
            showAdditionDialog();
        }
    }

    private void showAdditionDialog() {

        View rootFieldView = LayoutInflater.from(this).inflate(R.layout.layout_new_user, null);
        final EditText txtName = (EditText) rootFieldView.findViewById(R.id.txt_name);
        final EditText txtAge = (EditText) rootFieldView.findViewById(R.id.txt_age);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add!");
        builder.setMessage("Add new User!");
        builder.setView(rootFieldView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = txtName.getText().toString();
                String age = txtAge.getText().toString();
                SampleUserModel model = new SampleUserModel(name, Integer.parseInt(age));
                SampleUsersTable.insert(TASK_ID_ADD, model, TestActivity.this);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDbInsertCompleted(int taskId, long insertedId) {

        Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
        SampleUsersTable.selectAll(TASK_ID_SELECT_ALL, this);
    }

    @Override
    public void onDbTaskFailed(int taskId) {

        if (taskId == TASK_ID_ADD) {
            Toast.makeText(this, "Add failed!", Toast.LENGTH_SHORT).show();
        } else if (taskId == TASK_ID_SELECT_ALL) {
            Toast.makeText(this, "Select All failed!", Toast.LENGTH_SHORT).show();
        } else if (taskId == TASK_ID_REMOVE) {
            Toast.makeText(this, "Remove failed!", Toast.LENGTH_SHORT).show();
        } else if (taskId == TASK_ID_UPDATE) {
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDbSelectCompleted(int taskId, List<SampleUserModel> result) {

        Toast.makeText(this, "Data fetched!", Toast.LENGTH_SHORT).show();
        mAdapter.setData(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRemoveClicked(int index, SampleUserModel model) {
        SampleUsersTable.remove(TASK_ID_REMOVE, model, this);
    }

    @Override
    public void onDbRemoveCompleted(int taskId, long idRemoved) {

        Toast.makeText(this, "Item removed!", Toast.LENGTH_SHORT).show();
        SampleUsersTable.selectAll(TASK_ID_SELECT_ALL, this);
    }

    @Override
    public void onDbUpdateCompleted(int taskId, int numberOfRowsUpdated) {

        Toast.makeText(this, numberOfRowsUpdated + " updated!", Toast.LENGTH_SHORT).show();
        SampleUsersTable.selectAll(TASK_ID_SELECT_ALL, this);
    }
}
