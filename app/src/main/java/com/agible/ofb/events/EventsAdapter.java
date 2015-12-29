package com.agible.ofb.events;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agible.ofb.listeners.OnItemClickListener;
import com.agible.ofb.R;
import com.agible.ofb.data.Values;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by seth on 5/8/15.
 */
public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int resid;
    Context context;
    List<Values.Events> items;
    int subtraction = 0;
    boolean active = false;
    boolean completed = false;
    boolean pending = false;
    OnItemClickListener<Values.Events> onItemClickListener;

    public EventsAdapter(Context context, List<Values.Events> items, int resid) {
        this.context = context;

        this.items = items;
        this.resid = resid;
        Collections.sort(this.items, new SortItems());
    }

    public void setOnItemClickListener(OnItemClickListener <Values.Events>onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View item;
        RelativeLayout parent;
        TextView name;
        ImageView image;

        public ViewHolder(View item, RelativeLayout parent, TextView name, ImageView image) {
            super(item);
            this.item = item;
            this.parent = parent;
            this.name = name;
            this.image = image;
        }
    }
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resid, viewGroup, false);

        //find each of the sub views by id.
        RelativeLayout parent = (RelativeLayout)view.findViewById(R.id.event_item);
        TextView name = (TextView)view.findViewById(R.id.event_name);
        ImageView image = (ImageView)view.findViewById(R.id.event_image);

        return new ViewHolder(view, parent, name, image);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        ViewHolder vh = (ViewHolder) viewHolder;

        i = i-subtraction;

        if(i == items.size())
            return;

        switch(items.get(i).Status){
            case Values.STATUS_PENDING:
                if(pending)
                     break;

                pending = true;
                subtraction = 1;
                vh.name.setText("Pending");
                vh.parent.setBackgroundColor(Color.parseColor("#FFFD8F"));
                vh.name.setTextColor(Color.BLACK);
                vh.image.setVisibility(View.GONE);
                vh.name.setTextSize(20f);
                vh.item.setPadding(0, 0, 0, 0);
                return;
            case Values.STATUS_ACTIVE:
                if(active)
                    break;

                active = true;
                subtraction = 2;
                vh.name.setText("Active");
                vh.parent.setBackgroundColor(Color.parseColor("#82FFA8"));
                vh.image.setVisibility(View.GONE);
                vh.name.setTextColor(Color.BLACK);
                vh.name.setTextSize(20f);
                vh.item.setPadding(0, 0, 0, 0);
                return;
            case Values.STATUS_COMPLETED:
                if(completed)
                    break;
                completed = true;
                subtraction = 3;
                vh.name.setText("Completed");
                vh.parent.setBackgroundColor(Color.parseColor("#6BEBFF"));
                vh.image.setVisibility(View.GONE);
                vh.name.setTextColor(Color.BLACK);
                vh.name.setTextSize(20f);
                vh.item.setPadding(0, 0, 0, 0);
                return;
            default:
                break;
        }
        vh.name.setTextSize(16f);
        vh.name.setTextColor(context.getResources().getColor(R.color.text_color));
        vh.name.setText(items.get(i).Name);
        vh.item.setPadding(0, 30, 0, 30);
        final int finalI = i;
        vh.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(items.get(finalI), finalI);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(items.size() != 0)
            return items.size() + items.get(items.size() - 1).Status;
        else
            return 0;


    }

    public class SortItems implements Comparator<Values.Events>{

        @Override
        public int compare(Values.Events events, Values.Events events2) {
           return Integer.signum(events.Status-events2.Status);
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }
}
