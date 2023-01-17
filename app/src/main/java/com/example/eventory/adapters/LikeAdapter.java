package com.example.eventory.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventory.LikeFragment;
import com.example.eventory.R;
import com.example.eventory.models.CardModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        return new LikeAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.like_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(cardModelList.get(position).getImg_url()).into(holder.img);
        holder.eventName.setText(cardModelList.get(position).getName());
        holder.eventDateTime.setText(cardModelList.get(position).getDate_time());
        holder.eventPlace.setText(cardModelList.get(position).getPlace());
        if(cardModelList.get(position).getMin_price() == null)
            holder.eventPrice.setWidth(0);
        else holder.eventPrice.setText(cardModelList.get(position).getMin_price());

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardModelList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView eventName, eventDateTime, eventPlace, eventPrice;
        private ImageButton likeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.eventImageL);
            eventName = itemView.findViewById(R.id.eventNameL);
            eventDateTime = itemView.findViewById(R.id.eventDateTimeL);
            eventPlace = itemView.findViewById(R.id.eventPlaceL);
            eventPrice = itemView.findViewById(R.id.eventMinPriceL);
            likeBtn = itemView.findViewById(R.id.likeBtn);

        }
    }
}