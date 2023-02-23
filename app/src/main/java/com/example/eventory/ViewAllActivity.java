package com.example.eventory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventory.adapters.TagAdapter;
import com.example.eventory.adapters.ViewAllAdapter;
import com.example.eventory.models.CardModel;
import com.google.android.material.slider.RangeSlider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeSet;


public class ViewAllActivity extends Activity {

    RecyclerView searchRec, tagsRec;
    TextView found;
    ArrayList<CardModel> cardModelList;
    ViewAllAdapter adapter;
    TagAdapter tagAdapter;
    SearchView search;
    ImageButton gridBtn, listBtn, filterBtn;
    static HashSet<CardModel> filteredlist = new HashSet<CardModel>();
    static ArrayList<String> helper_list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        searchRec = findViewById(R.id.search_recycler);
        tagsRec = findViewById(R.id.tags_recycler);
        found = findViewById(R.id.found);
        gridBtn = findViewById(R.id.gridMode);
        listBtn = findViewById(R.id.listMode);
        filterBtn = findViewById(R.id.filter_btn);
        search = findViewById(R.id.search);

        //TODO add tags, filter, card out of layout, add hide tag/found bar,
        // remove if event passed (maybe Room local later), SET SEARCHBAR ACTIVE
        final int COLUMNS = calculate_columns(ViewAllActivity.this);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String json = preferences.getString("Tomsarkgh", "");
        if (!json.isEmpty()) {
            Type listType = new TypeToken<List<CardModel>>() {
            }.getType();
            Gson gson = new Gson();
            cardModelList = gson.fromJson(json, listType);

            adapter = new ViewAllAdapter(this, cardModelList, true);
            searchRec.setAdapter(adapter);
            searchRec.setHasFixedSize(true);
            searchRec.setLayoutManager(new GridLayoutManager(this, COLUMNS));
            found.setText(adapter.getItemCount() + "  founds");
            gridBtn.setSelected(true);

            tagAdapter = new TagAdapter(this, ContainerActivity.tags_set, true);
            tagsRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            tagsRec.setAdapter(tagAdapter);
            tagsRec.setHasFixedSize(true);

            String category = getIntent().getStringExtra("category");
            if(category!=null) {
//                filteredlist.clear();
                TagAdapter.selected_tags.add(category);
                filterByTag(category, true);
                found.setText(adapter.getItemCount() + "  founds");
                tagSwap(tagAdapter.getTags().indexOf(category), true);
            }
//            else search.requestFocus();

            tagAdapter.setOnItemClickListener(new TagAdapter.onItemClickListener() {
                @Override
                public void onClick(int position, String tag, boolean selected) {

                    if (tagAdapter.selected_tags.isEmpty()){
                        filteredlist.clear();
                        adapter.filterList(cardModelList);
                    }
                    else {
                        filterByTag(tag, selected);
                    }
                    tagSwap(position, selected);
                    found.setText(adapter.getItemCount() + "  founds");
                }
            });


        }


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                found.setText(adapter.getItemCount() + "  founds");
                return false;
            }
        });
        
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        gridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gridBtn.setSelected(true);
                listBtn.setSelected(false);

                adapter.isGrid = true;
                searchRec.setAdapter(adapter);
                searchRec.setLayoutManager(new GridLayoutManager(ViewAllActivity.this, COLUMNS));

            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listBtn.setSelected(true);
                gridBtn.setSelected(false);

                adapter.isGrid = false;
                searchRec.setAdapter(adapter);
                searchRec.setLayoutManager(new LinearLayoutManager(ViewAllActivity.this));

            }
        });


    }

    public int calculate_columns(Context context) {
        float px = getWindowManager().getDefaultDisplay().getWidth();
        float dp = px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp / 150;
    }

    public void filter(String text) {
        filteredlist.clear();

        for (CardModel item : cardModelList)
            if (item.getName().toLowerCase().contains(text.toLowerCase()))
                filteredlist.add(item);

        if (filteredlist.isEmpty())
            Toast.makeText(ViewAllActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();

        else adapter.filterList(filteredlist);
    }

    public void filterByTag(String tag, boolean selected) {

        if (selected) {
            for (CardModel item : cardModelList)
                if (item.getTags().contains(tag))
                    filteredlist.add(item);
        }
        else {
            filteredlist.removeIf(item -> item.getTags().contains(tag) &&
                    !TagAdapter.selected_tags.stream()
                            .anyMatch(selectedTag -> item.getTags().contains(selectedTag)));
        }
        if (filteredlist.isEmpty())
            Toast.makeText(ViewAllActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();

        else adapter.filterList(filteredlist);

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog_filter);

        RecyclerView categoryRec = dialog.findViewById(R.id.categoryRec);
        RangeSlider priceSlider = dialog.findViewById(R.id.priceSlider);
        Spinner locationSpinner = dialog.findViewById(R.id.locationSpinner);
        Button resetBtn = dialog.findViewById(R.id.resetBtn);
        Button applyBtn = dialog.findViewById(R.id.applyBtn);
        Button todayBtn = dialog.findViewById(R.id.todayBtn);
        Button tmrBtn = dialog.findViewById(R.id.tomorrowBtn);
        Button wkndBtn = dialog.findViewById(R.id.weekendBtn);
        Button setDateBtn = dialog.findViewById(R.id.setDateBtn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewAllActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(ViewAllActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewAllActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        tmrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewAllActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        wkndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewAllActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewAllActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void tagSwap(int position, boolean selected){
        int new_position = tagAdapter.selected_tags.size();
        new_position = selected ?  new_position -1 : new_position;

        Collections.swap(tagAdapter.getTags(), position, new_position);

//        tagAdapter.notifyItemMoved(position,new_position);
        if(selected) tagsRec.smoothScrollToPosition(new_position);
        tagAdapter.notifyDataSetChanged();
    }

}