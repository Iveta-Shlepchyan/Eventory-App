package com.example.eventory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eventory.adapters.CategoryAdapter;
import com.example.eventory.models.CardModel;
import com.example.eventory.models.CategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseFirestore db ;

    RecyclerView recContainer;

    public interface Callback {
        void onCallback(List<CardModel> cardModels);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

        recContainer = root.findViewById(R.id.container_list);
        recContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        buildItemList();

        //FIXME home fragment
        TextView search = root.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), ViewAllActivity.class));
            }
        });



        return root;
    }

    private void buildItemList() {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (String path: ContainerActivity.paths) {
            buildSubItemList(path, new Callback() {
                @Override
                public void onCallback(List<CardModel> cardModels) {
                    CategoryModel categoryModel = new CategoryModel(path, cardModels);
                    categoryModelList.add(categoryModel);
                    if(categoryModelList.size() == ContainerActivity.paths.size()){
                        for (int i = categoryModelList.size() - 1; i >= 0; i--) {
                            if (categoryModelList.get(i).getCardModelList().isEmpty()) {
                                categoryModelList.remove(i);
                            }
                        }
                        recContainer.setAdapter(new CategoryAdapter(categoryModelList));
                    }
                }
            });
        }
    }

    private void buildSubItemList(String path, Callback callback) {
        List<CardModel> cardModelList = new ArrayList<>();
        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                    CardModel cardModel = document.toObject(CardModel.class);
                                    if(!ContainerActivity.likedCards.isEmpty()){
                                        for (CardModel likedCard: ContainerActivity.likedCards ) {
                                            if (likedCard.getName().equals(cardModel.getName()))
                                                cardModel.setLiked(true);
                                        }
                                    }
                                    cardModelList.add(cardModel);

                                    if(!cardModel.getTags().isEmpty())
                                        ContainerActivity.tags_set.addAll(cardModel.getTags());
                            }
                            callback.onCallback(cardModelList);
                        }
                        else {
                            Toast.makeText(getActivity(),"Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
