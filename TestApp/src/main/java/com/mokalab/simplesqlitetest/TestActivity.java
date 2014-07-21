package com.mokalab.simplesqlitetest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mokalab.simplesqlitelibrary.DbInsert;
import com.mokalab.simplesqlitelibrary.DbRemove;
import com.mokalab.simplesqlitelibrary.DbSelect;

import java.util.ArrayList;

public class TestActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, DbInsert.OnDbInsertTaskListener, DbSelect.OnDbSelectTaskListenerMultiple<SampleUserModel>,TestAdapter.OnBtnRemoveClickListener,
        DbRemove.OnDbRemoveTaskListenerSingle {

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
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        SampleUsersTable.selectAll(10, this);
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
                SampleUsersTable.insert(22, model, TestActivity.this);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDbInsertCompleted(int taskId, Long insertedId) {

        Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
        SampleUsersTable.selectAll(10, this);
    }

    @Override
    public void onDbTaskFailed(int taskId) {

        if (taskId == 22) {
            Toast.makeText(this, "Add failed!", Toast.LENGTH_SHORT).show();
        } else if (taskId == 10) {
            Toast.makeText(this, "Select All failed!", Toast.LENGTH_SHORT).show();
        } else if (taskId == 11) {
            Toast.makeText(this, "Remove failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDbSelectCompleted(int taskId, ArrayList<SampleUserModel> result) {

        Toast.makeText(this, "Data fetched!", Toast.LENGTH_SHORT).show();
        mAdapter.setData(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRemoveClicked(int index, SampleUserModel model) {

        SampleUsersTable.remove(11, model, this);
    }

    @Override
    public void onDbRemoveCompleted(int taskId, long idRemoved) {

        Toast.makeText(this, "Item removed!", Toast.LENGTH_SHORT).show();
        SampleUsersTable.selectAll(10, this);
    }
}
