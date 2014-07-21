package com.mokalab.simplesqlitetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by work on 2014-06-25.
 */
public class TestAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<AdapterItem> mData;
    private OnBtnRemoveClickListener mBtnRemoveClickListener;

    public TestAdapter(Context context) {

        super();
        mLayoutInflater = LayoutInflater.from(context);
        mData = new ArrayList<AdapterItem>();
        addItemAddition();
    }

    private void addItemAddition() {

        if (mData != null) {

            boolean addExists = false;
            for (int i = 0; i < mData.size(); i++) {
                AdapterItem item = mData.get(i);
                if (item.type == AdapterItem.TYPE_ADD) {
                    addExists = true;
                    break;
                }
            }

            if (!addExists) {
                mData.add(new AdapterItem(AdapterItem.TYPE_ADD));
            }
        }
    }

    @Override
    public int getCount() {

        return mData.size();
    }

    @Override
    public Object getItem(int i) {

        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {

        AdapterItem item = (AdapterItem) getItem(i);
        if (item != null && item.type == AdapterItem.TYPE_MODEL) {
            SampleUserModel model = (SampleUserModel) item.object;
            return model.getDbAssociatedId();
        }

        return 0;
    }

    public void setData(ArrayList<SampleUserModel> data) {

        mData.clear();
        for (int i = 0; i < data.size(); i++) {
            SampleUserModel model = data.get(i);
            AdapterItem item = new AdapterItem(AdapterItem.TYPE_MODEL);
            item.object = model;
            mData.add(item);
        }

        addItemAddition();
    }

    @Override
    public int getViewTypeCount() {

        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        AdapterItem item = (AdapterItem) getItem(position);
        if (item.type == AdapterItem.TYPE_MODEL) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        AdapterItem item = (AdapterItem) getItem(i);
        if (view == null) {
            if (item.type == AdapterItem.TYPE_MODEL) {
                view = mLayoutInflater.inflate(R.layout.item_model, null);
            } else {
                view = mLayoutInflater.inflate(R.layout.item_add, null);
            }
        }

        if (item.type == AdapterItem.TYPE_MODEL) {

            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            TextView lblAge = (TextView) view.findViewById(R.id.lbl_age);
            ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btn_remove);

            final SampleUserModel model = (SampleUserModel) item.object;
            lblName.setText(model.getName());
            lblAge.setText(String.valueOf(model.getAge()));

            btnDelete.setFocusable(false);
            btnDelete.setFocusableInTouchMode(false);
            btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (mBtnRemoveClickListener != null) {
                        mBtnRemoveClickListener.onRemoveClicked(i, model);
                    }
                }
            });
        }

        return view;
    }

    public void setBtnRemoveClickListener(OnBtnRemoveClickListener btnRemoveClickListener) {
        mBtnRemoveClickListener = btnRemoveClickListener;
    }

    public static class AdapterItem {

        public static final int TYPE_MODEL = 0;
        public static final int TYPE_ADD = 1;

        public int type = -1;
        public Object object;

        public AdapterItem(int type) {
            this.type = type;
        }
    }

    public static interface OnBtnRemoveClickListener {

        public void onRemoveClicked(int index, SampleUserModel model);
    }
}
