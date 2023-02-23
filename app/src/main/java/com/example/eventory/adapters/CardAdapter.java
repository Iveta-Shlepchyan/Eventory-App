package com.example.eventory.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.google.gson.Gson;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context context;
    private List<CardModel> cardModelList;

    public CardAdapter(Context context, List<CardModel> cardModelList) {
        this.context = context;
        this.cardModelList = cardModelList;
    }


    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            CardModel cardModel = cardModelList.get(position);
            Glide.with(context).load(cardModel.getImg_url()).into(holder.cardImg);
            holder.eventName.setText(cardModel.getName());
            //TODO remove date_time
            holder.eventDateTime.setText(cardModel.getDate_time());
            if (cardModel.getDate_time_list()!=null)
                holder.eventDateTime.setText(cardModel.getDate_time_list().get(0));
            holder.eventPlace.setText(cardModel.getPlace());
            if(cardModel.getMin_price() == null)
                holder.eventPrice.setVisibility(View.GONE);
            holder.eventPrice.setText(cardModelList.get(position).getMin_price());
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
                    holder.likeBtn.setBackgroundResource(R.drawable.ic_heart_card);


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
                    i.putExtra("info", cardModel);
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
