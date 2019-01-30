package com.example.nabab.tourmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nabab.tourmate.PojoClass.EventPojo;
import com.example.nabab.tourmate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventCaptureMomentAdapter extends RecyclerView.Adapter<EventCaptureMomentAdapter.EventCaptureMomentViewHolder> {
    private Context context;
    private List<EventPojo> eventPojoList;

    public EventCaptureMomentAdapter(Context context, List<EventPojo> eventPojoList) {
        this.context = context;
        this.eventPojoList = eventPojoList;
    }

    @NonNull
    @Override
    public EventCaptureMomentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.model_event_capture_moment_design, viewGroup, false);
        return new EventCaptureMomentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCaptureMomentViewHolder eventCaptureMomentViewHolder, int i) {
        EventPojo getItemId = eventPojoList.get(i);

        ImageView imageView = eventCaptureMomentViewHolder.showEventCaptureMomentIv;
        Picasso.get().load(getItemId.getDownloadImageUrlLink()).placeholder(R.drawable.selfie_image_icon).into(imageView);
        eventCaptureMomentViewHolder.showTravelEventCaptureMomentDetailsTv.setText(getItemId.getTravelCaptureMomentDetails());
    }

    @Override
    public int getItemCount() {
        return eventPojoList.size();
    }

    class EventCaptureMomentViewHolder extends RecyclerView.ViewHolder{
        private ImageView showEventCaptureMomentIv;
        private TextView showTravelEventCaptureMomentDetailsTv;

        public EventCaptureMomentViewHolder(@NonNull View itemView) {
            super(itemView);
            showEventCaptureMomentIv = itemView.findViewById(R.id.showEventCaptureMomentIvId);
            showTravelEventCaptureMomentDetailsTv = itemView.findViewById(R.id.showTravelEventCaptureMomentDetailsTvId);
        }
    }
}
