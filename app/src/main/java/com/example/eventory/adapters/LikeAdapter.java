package com.example.eventory.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventory.ContainerActivity;
import com.example.eventory.EventPageActivity;
import com.example.eventory.R;
import com.example.eventory.models.CardModel;

import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder>{

    private Context context;
    private List<CardModel> cardModelList;

    public LikeAdapter(Context context, List<CardModel> cardModelList) {
        this.context = context;
        this.cardModelList = cardModelList;
    }


    @NonNull
    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LikeAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_like_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(cardModelList.get(position).getImg_url()).into(holder.img);
        holder.eventName.setText(cardModelList.get(position).getName());
        holder.eventDateTime.setText(cardModelList.get(position).getDate_time_list().get(0));
        holder.eventPlace.setText(cardModelList.get(position).getPlace());
        if(cardModelList.get(position).getMin_price() == null)
            holder.eventPrice.setVisibility(View.GONE);
        else holder.eventPrice.setText(cardModelList.get(position).getMin_price());

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContainerActivity.likedCards.remove(cardModelList.get(holder.getPosition()));
                notifyItemRemoved(holder.getPosition());
            }
        });

        holder.likedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EventPageActivity.class);
                i.putExtra("info", cardModelList.get(holder.getPosition()));
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView eventName, eventDateTime, eventPlace, eventPrice;
        private ImageButton likeBtn;
        private CardView likedCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            likedCard = itemView.findViewById(R.id.eventCard);
            img = itemView.findViewById(R.id.eventImage);
            eventName = itemView.findViewById(R.id.eventName);
            eventDateTime = itemView.findViewById(R.id.eventDateTime);
            eventPlace = itemView.findViewById(R.id.eventPlace);
            eventPrice = itemView.findViewById(R.id.eventMinPrice);
            likeBtn = itemView.findViewById(R.id.likeBtn);

        }
    }
}