package com.example.eventory.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventory.ContainerActivity;
import com.example.eventory.LikeFragment;
import com.example.eventory.R;
import com.example.eventory.models.CardModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context context;
    private List<CardModel> cardModelList;
//    private static List<CardModel> likedCards;

    public CardAdapter(Context context, List<CardModel> cardModelList) {
        this.context = context;
        this.cardModelList = cardModelList;
    }

//    public CardAdapter(List<CardModel> likedCards) {
//        CardAdapter.likedCards = likedCards;
//    }


    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            CardModel cardModel = cardModelList.get(position);
            Glide.with(context).load(cardModel.getImg_url()).into(holder.cardImg);
            holder.eventName.setText(cardModel.getName());
            holder.eventDateTime.setText(cardModel.getDate_time());
            holder.eventPlace.setText(cardModel.getPlace());
            if(cardModel.getMin_price() == null)
                holder.eventPrice.setBackgroundColor(0x0000000);
            holder.eventPrice.setText(cardModelList.get(position).getMin_price());
            if(cardModel.isLiked()){
                holder.likeBtn.setBackgroundResource(R.drawable.ic_heart_card_pressed);
            }

            holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*LikeFragment likeFragment = new LikeFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("cardModel" ,cardModelList.get(position));
                likeFragment.setArguments(bundle);*/
                if (!cardModelList.get(position).isLiked()) {

                    cardModelList.get(position).setLiked(true);
                    holder.likeBtn.setBackgroundResource(R.drawable.ic_heart_card_pressed);

                    ContainerActivity.likedCards.add(cardModelList.get(position));

                }else {
                    cardModelList.get(position).setLiked(false);
                    holder.likeBtn.setBackgroundResource(R.drawable.navigation_heart_selector);

                    ContainerActivity.likedCards.remove(cardModelList.get(position));
                }

                Gson gson = new Gson();
                String json = gson.toJson(ContainerActivity.likedCards);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("card_models", json);
                editor.commit();

                /*FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, likeFragment).commit();

                BottomNavigationView mBottomNavigationView = (BottomNavigationView) ((AppCompatActivity) context).findViewById(R.id.bottomNavigationView);
                mBottomNavigationView.getMenu().findItem(R.id.like).setChecked(true);*/

            }
        });

    }

    @Override
    public int getItemCount() {
        return cardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImg;
        TextView eventName, eventDateTime, eventPlace, eventPrice;
        private ImageButton likeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardImg = itemView.findViewById(R.id.eventImage);
            eventName = itemView.findViewById(R.id.eventName);
            eventDateTime = itemView.findViewById(R.id.eventDateTime);
            eventPlace = itemView.findViewById(R.id.eventPlace);
            eventPrice = itemView.findViewById(R.id.eventMinPrice);
            likeBtn = itemView.findViewById(R.id.like);

        }
    }
}
