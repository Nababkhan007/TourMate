package com.example.nabab.tourmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nabab.tourmate.PojoClass.EventExpensesPojo;
import com.example.nabab.tourmate.R;

import java.util.List;

public class EventExpensesAdapter extends RecyclerView.Adapter<EventExpensesAdapter.EventExpensesViewHolder> {
    private Context context;
    List<EventExpensesPojo> eventExpensesPojoList;

    public EventExpensesAdapter(Context context, List<EventExpensesPojo> eventExpensesPojoList) {
        this.context = context;
        this.eventExpensesPojoList = eventExpensesPojoList;
    }

    @NonNull
    @Override
    public EventExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.model_event_expenses_design, viewGroup, false);
        return new EventExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventExpensesViewHolder eventExpensesViewHolder, int i) {
        EventExpensesPojo getItemId = eventExpensesPojoList.get(i);

        eventExpensesViewHolder.showTravelEventExpensesDetailsTv.setText(getItemId.getTravelExpensesDetails());
        eventExpensesViewHolder.showTravelEventExpensesAmountTv.setText(getItemId.getTravelExpensesAmount()+" Tk");
    }

    @Override
    public int getItemCount() {
        return eventExpensesPojoList.size();
    }

    class EventExpensesViewHolder extends RecyclerView.ViewHolder {
        private TextView showTravelEventExpensesDetailsTv, showTravelEventExpensesAmountTv;
        public EventExpensesViewHolder(@NonNull View itemView) {
            super(itemView);
            showTravelEventExpensesDetailsTv = itemView.findViewById(R.id.showTravelEventExpensesDetailsTvId);
            showTravelEventExpensesAmountTv = itemView.findViewById(R.id.showTravelEventExpensesAmountTvId);
        }
    }
}
