package com.usu.oneviewer.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.usu.oneviewer.R;
import com.usu.utils.Event;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhld on 12/7/2017.
 */

public class RecyclerEventAdapter extends RecyclerView.Adapter<RecyclerEventAdapter.EventHolder> {
    List<Event> mDataset;
    Context context;
    LayoutInflater inflater;

    public RecyclerEventAdapter(Context context) {
        this.context = context;
        this.mDataset = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void updateEventList(List<Event> events) {
        this.mDataset.addAll(events);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public RecyclerEventAdapter.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.row_event, parent, false);

        EventHolder vh = new EventHolder(view.getRootView());

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerEventAdapter.EventHolder holder, int position) {
        LinearLayout view = (LinearLayout) holder.view;
        Event e = mDataset.get(position);

        ImageView iv = view.findViewById(R.id.bgImage);
        view.removeView(iv);
        iv.setOnClickListener(new DetailClick(e));
        Picasso.with(context).load(e.info).fit().into(iv);

        TextView tv1 = view.findViewById(R.id.typeText);
        view.removeView(tv1);
        tv1.setText(e.type);

        TextView tv2 = view.findViewById(R.id.timeText);
        view.removeView(tv2);
        tv2.setText(e.time);
    }

    class EventHolder extends RecyclerView.ViewHolder {
        View view;
        public EventHolder(View v) {
            super(v);
            this.view = v;
        }
    }

//    @Nullable
//    @Override
//    public Event getItem(int position) {
//        return super.getItem(position);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.row_event, parent, false);
//        }
//
//        Event e = getItem(position);
//
//        ImageView imgView = (ImageView) view.findViewById(R.id.bgImage);
//        imgView.setOnClickListener(new DetailClick(e));
//        Picasso.with(context).load(e.info).fit().into(imgView);
//
//        TextView type = (TextView) view.findViewById(R.id.typeText);
//        type.setText(e.type);
//
//        TextView time = (TextView) view.findViewById(R.id.timeText);
//        time.setText(e.time);
//
//        return view;
//    }

    class DetailClick implements View.OnClickListener {
        Event e;
        public DetailClick(Event e) {
            this.e = e;
        }

        @Override
        public void onClick(View v) {
            // Intent zoomAct = new Intent(context, ZoomActivity.class);
            // zoomAct.putExtra("file", e.info);
            // context.startActivity(zoomAct);
        }
    }
}