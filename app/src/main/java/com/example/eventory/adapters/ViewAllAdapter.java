package com.example.eventory.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventory.ContainerActivity;
import com.example.eventory.EventPageActivity;
import com.example.eventory.R;
import com.example.eventory.models.CardModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CardModel> cardModelList;
    public boolean isGrid = true;

    public ViewAllAdapter(Context context, ArrayList<CardModel> cardModelList, boolean isGrid) {
        this.context = context;
        this.cardModelList = cardModelList;
        this.isGrid = isGrid;
    }


    public void filterList(Collection<CardModel> filterlist) {
        cardModelList = new ArrayList<>();
        cardModelList.addAll(filterlist);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isGrid) {
            view = LayoutInflater.from(context).inflate(R.layout.item_event_centered, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_like_list, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CardModel cardModel = cardModelList.get(position);
        Glide.with(context).load(cardModel.getImg_url()).into(holder.cardImg);
        holder.eventName.setText(cardModel.getName());
        holder.eventDateTime.setText(cardModel.getDate_time_list().get(0));
        holder.eventPlace.setText(cardModel.getPlace());
        if(cardModel.getMin_price() == null)
            holder.eventPrice.setVisibility(View.GONE);
        else  holder.eventPrice.setText(cardModelList.get(position).getMin_price());

        int heart = isGrid ? R.drawable.ic_heart_card : R.drawable.ic_heart_card_black;

        holder.likeBtn.setBackgroundResource(heart);
        if(cardModel.isLiked()){
            holder.likeBtn.setBackgroundResource(R.drawable.ic_heart_card_pressed);
        }


        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!cardModelList.get(position).isLiked()) {

                    cardModelList.get(position).setLiked(true);
                    holder.likeBtn.setBackgroundResource(R.drawable.ic_heart_card_pressed);

                    ContainerActivity.likedCards.add(cardModelList.get(position));

                }else {
                    cardModelList.get(position).setLiked(false);
                    holder.likeBtn.setBackgroundResource(heart);


                    for (CardModel likedCard: ContainerActivity.likedCards ) {
                        if (likedCard.getName().equals(cardModel.getName())){
                            ContainerActivity.likedCards.remove(likedCard);
                            break;
                        }
                    }
                }

                Gson gson = new Gson();
                String json = gson.toJson(ContainerActivity.likedCards);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("card_models", json);
                editor.commit();


            }
        });


        holder.eventCard.setOnClickListener(new View.OnClickListener() {
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
        private ImageView cardImg;
        private TextView eventName, eventDateTime, eventPlace, eventPrice;
        private ImageButton likeBtn;
        private CardView eventCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImg = itemView.findViewById(R.id.eventImage);
            eventName = itemView.findViewById(R.id.eventName);
            eventDateTime = itemView.findViewById(R.id.eventDateTime);
            eventPlace = itemView.findViewById(R.id.eventPlace);
            eventPrice = itemView.findViewById(R.id.eventMinPrice);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            eventCard = itemView.findViewById(R.id.eventCard);
        }
    }
}
