package com.example.eventory;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventory.Scraping.WebScraping;
import com.example.eventory.models.CardModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class ContainerActivity extends AppCompatActivity {

    BottomNavigationView bottomNavMenu;
    HomeFragment homeFragment = new HomeFragment();
    MapFragment mapFragment = new MapFragment();
    TicketFragment ticketFragment = new TicketFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    public  static List<CardModel> likedCards = new ArrayList<>();
    public static final List<String> paths = Arrays.asList("Theater","Clubs","Cinema","Concert","Other","Tomsarkgh");
   /* public static final List<String> paths = Arrays.asList("Theater", "Opera", "Clubs","Cinema","Concert",
            "Entertainment","Tours","Interesting places","Museums","Conference","Other","Tomsarkgh");*/
    public static HashSet<String> tags_set = new HashSet<String>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bottomNavMenu = findViewById(R.id.bottomNavigationView);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String json = preferences.getString("card_models", "");
        if(!json.isEmpty()) {
            Type listType = new TypeToken<List<CardModel>>() {}.getType();
            Gson gson = new Gson();
            likedCards = gson.fromJson(json, listType);
        }
        WebScraping webScraping = new WebScraping();
        webScraping.startScraping();


        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        bottomNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,mapFragment).commit();
                        return true;
                    case R.id.like:
                        LikeFragment likeFragment = new LikeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,likeFragment).commit();
                        return true;
                    case R.id.ticket:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,ticketFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                        return true;
                }
                return false;
            }
        });



    }

}
