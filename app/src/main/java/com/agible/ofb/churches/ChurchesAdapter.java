package com.agible.ofb.churches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.agible.ofb.R;
import com.agible.ofb.data.Values;
import com.agible.ofb.listeners.OnItemClickListener;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.Collections;
import java.util.List;

/**
 * Created by seth on 6/25/15.
 */
public class ChurchesAdapter extends RecyclerView.Adapter<ChurchesAdapter.mViewHolder>{
MobileServiceList<Values.Churches> churches;
int resid;
    Context context;
    Values values;

    OnItemClickListener<Values.Churches> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener <Values.Churches>onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ChurchesAdapter(int resid, Context context, Values values, MobileServiceList<Values.Churches> churches){
        this.churches = churches;
        this.resid = resid;
        this.context = context;
        this.values = values;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(resid, parent, false);
        TextView left = (TextView) v.findViewById(R.id.item_left);
        TextView right = (TextView)v.findViewById(R.id.item_right);
       return new mViewHolder(v, left, right);

    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        holder.left.setText(churches.get(position).ChurchName);
        holder.right.setText(String.format("%s %s", churches.get(position).City, churches.get(position).Country));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(churches.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return churches.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView left;
        TextView right;

        public mViewHolder(View item, TextView left, TextView right){
            super(item);
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

}
