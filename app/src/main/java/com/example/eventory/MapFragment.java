package com.example.eventory;

        import android.content.Context;
        import android.location.Address;
        import android.location.Geocoder;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import com.example.eventory.models.CardModel;
        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.UiSettings;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.GeoPoint;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.io.IOException;
        import java.util.List;

public class MapFragment extends Fragment {

    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        String[] paths = {"Events/Movies/Movie", "Events/Concerts/Concert", "Theater", "Opera", "Clubs","Other"};

        // Initialize view
        View view=inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);





        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                UiSettings UiSettings = googleMap.getUiSettings();
                UiSettings.setZoomControlsEnabled(true);


                // Add a marker in Yerevan and move the camera
                LatLng yerevan = new LatLng(40.177200, 44.503490);
                googleMap.addMarker(new MarkerOptions().position(yerevan).title("Marker in Yerevan"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(yerevan));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(yerevan,10));

                for (String path:paths) {
                    addMarkersFromDb(path, googleMap);

                }


                // When map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {


//                        GeoPoint geoPoint = value.getGeoPoint("geoPoint");

                        // getting latitude and longitude from geo point
                        // and setting it to our location.
//                        LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                        // When clicked on map
                        // Initialize marker options
//                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
//                        markerOptions.position(latLng);
                        // Set title of marker
//                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
//                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
//                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
        // Return view
        return view;
    }

    public void addMarkersFromDb(String path, GoogleMap googleMap){
        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try {
                                    String address = document.getString("place");
                                    Log.i("test", address);

                                    LatLng place = getLocationFromAddress(address);

                                    googleMap.addMarker(new MarkerOptions().position(place).title(address));


                                }catch (Exception exception){
                                    Log.e("address conversion failed", path + " "+ document.getId());
                                };



                            }
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public LatLng getLocationFromAddress( String strAddress) {

        Geocoder coder = new Geocoder(this.getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}
