package com.example.eventory;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventory.adapters.CardAdapter;
import com.example.eventory.models.CardModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;


public class ContainerActivity extends AppCompatActivity {

    BottomNavigationView bottomNavMenu;
    HomeFragment homeFragment = new HomeFragment();
    MapFragment mapFragment = new MapFragment();
    TicketFragment ticketFragment = new TicketFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    public  static List<CardModel> likedCards = new ArrayList<>();
//    CardAdapter cardAdapter = new CardAdapter(likedCards);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_activity);

        bottomNavMenu = findViewById(R.id.bottomNavigationView);



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
                        LikeFragment likeFragment = new LikeFragment(likedCards);
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
