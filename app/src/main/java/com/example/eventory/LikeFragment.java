package com.example.eventory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventory.adapters.LikeAdapter;
import com.example.eventory.models.CardModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LikeFragment extends Fragment{

    private RecyclerView recyclerView;
    private static List<CardModel> cardModels;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public LikeFragment(List<CardModel> cardModels) {
        this.cardModels = cardModels;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_like, container, false);
        recyclerView = root.findViewById(R.id.like_recView);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String json = preferences.getString("card_models", "");
        Type listType = new TypeToken<List<CardModel>>(){}.getType();
        Gson gson = new Gson();
        List<CardModel> cardModels = gson.fromJson(json, listType);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LikeAdapter likeAdapter = new LikeAdapter(getContext(), cardModels);
        recyclerView.setAdapter(likeAdapter);
        recyclerView.setRecycledViewPool(viewPool);
        recyclerView.setHasFixedSize(true);


            /*if(getArguments()!= null) {
                CardModel cardModel = (CardModel) getArguments().getSerializable("cardModel");
                cardModels.add(cardModel);
                likeAdapter.notifyDataSetChanged();
            }*/

        return root;
    }

}