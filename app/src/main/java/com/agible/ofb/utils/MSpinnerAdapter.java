package com.agible.ofb.utils;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.agible.ofb.R;

import java.util.List;

/**
 * Created by seth on 5/12/15.
 */
public class MSpinnerAdapter implements SpinnerAdapter {

    List<String>items;
    int resid;
    int dresid;
    Context context;

    public MSpinnerAdapter(Context context, int resid, int dresid, List<String> items){
        this.context = context;
        this.resid = resid;
        this.items = items;
        this.dresid = dresid;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(dresid, parent, false);
        TextView name = (TextView)v.findViewById(R.id.spinner_item_name);
        name.setText(items.get(position));
        return v;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(resid, parent, false);
        TextView name = (TextView)v.findViewById(R.id.spinner_item_name);
        name.setText(items.get(position));
        return v;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return  1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
