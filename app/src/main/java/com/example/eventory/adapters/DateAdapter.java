package com.example.eventory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventory.R;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> date_time_list;
    private int last_selected = 0;
    private int selected_card = 0;


    public DateAdapter(Context context, ArrayList<String> date_time_list) {
        this.context = context;
        this.date_time_list = date_time_list;
    }

    public interface onItemClickListener{
        void onClick(int position);
    }

    DateAdapter.onItemClickListener onItemClickListner;

    public void setOnItemClickListener(DateAdapter.onItemClickListener
                                               onItemClickListner)
    {
        this.onItemClickListner = onItemClickListner;
    }


    @NonNull
    @Override
    public DateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DateAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_time, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.ViewHolder holder, int position) {

        if(position == last_selected){
            holder.dateCard.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.dateCard.setForeground(context.getResources().getDrawable(R.drawable.bg_card_borders));
        }
        else {
            holder.dateCard.setCardBackgroundColor(context.getResources().getColor(R.color.cool_grey));
            holder.dateCard.setForeground(null);
        }
        holder.eventDay.setText(date_time_list.get(position).split(" ")[0]);
        holder.eventMonth.setText(date_time_list.get(position).split(" ")[1]);
        holder.eventWeekday.setText(date_time_list.get(position).split(" ")[2]);
        holder.eventTime.setText(date_time_list.get(position).split(" ")[4]);

        holder.dateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.dateCard.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                holder.dateCard.setForeground(context.getResources().getDrawable(R.drawable.bg_card_borders));
                selected_card = holder.getAdapterPosition();
                notifyItemChanged(last_selected);
                last_selected = selected_card;
                onItemClickListner.onClick(last_selected);
            }
        });
    }

    @Override
    public int getItemCount() {
        return date_time_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventWeekday, eventMonth, eventDay, eventTime;
        private CardView dateCard;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventWeekday = itemView.findViewById(R.id.weekday);
            eventMonth = itemView.findViewById(R.id.month);
            eventDay = itemView.findViewById(R.id.day);
            eventTime = itemView.findViewById(R.id.time);
            dateCard = itemView.findViewById(R.id.date_card);

        }
    }


}

