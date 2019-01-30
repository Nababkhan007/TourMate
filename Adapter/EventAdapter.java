package com.example.nabab.tourmate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nabab.tourmate.Activity.EventDetailsActivity;
import com.example.nabab.tourmate.PojoClass.EventPojo;
import com.example.nabab.tourmate.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    List<EventPojo> eventPojoList;

    public EventAdapter(Context context, List<EventPojo> eventPojoList) {
        this.context = context;
        this.eventPojoList = eventPojoList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.model_event_design, viewGroup,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder eventViewHolder, int i) {
        final EventPojo getItemId = eventPojoList.get(i);

        eventViewHolder.showTravelDestinationTv.setText(getItemId.getTravelDestination());
        eventViewHolder.showTravelStartingDateTv.setText(getItemId.getTravelStartingDate());
        eventViewHolder.showTravelEndingDateTv.setText(getItemId.getTravelEndingDate());
        eventViewHolder.showTravelEstimatedBudgetTv.setText(getItemId.getTravelEstimatedBudget()+" Tk");

        eventViewHolder.eventShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                String eventId = getItemId.getEventId();
                String travelEstimatedBudget = getItemId.getTravelEstimatedBudget();
                intent.putExtra("eventId", getItemId.getEventId());
                intent.putExtra("travelEstimatedBudget", getItemId.getTravelEstimatedBudget());
                intent.putExtra("TravelDestination", getItemId.getTravelDestination());
                intent.putExtra("TravelStartingDate", getItemId.getTravelStartingDate());
                intent.putExtra("TravelEndingDate", getItemId.getTravelEndingDate());
                intent.putExtra("TravelEstimatedBudget", getItemId.getTravelEstimatedBudget());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventPojoList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder{
        private TextView showTravelDestinationTv, showTravelStartingDateTv, showTravelEndingDateTv, showTravelEstimatedBudgetTv;
        private CardView eventShowCardView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            showTravelDestinationTv = itemView.findViewById(R.id.showTravelDestinationTvId);
            showTravelStartingDateTv = itemView.findViewById(R.id.showTravelStartingDateTvId);
            showTravelEndingDateTv = itemView.findViewById(R.id.showTravelEndingDateTvId);
            showTravelEstimatedBudgetTv = itemView.findViewById(R.id.showTravelEstimatedBudgetTvId);

            eventShowCardView = itemView.findViewById(R.id.eventShowCardViewId);
        }
    }
}
