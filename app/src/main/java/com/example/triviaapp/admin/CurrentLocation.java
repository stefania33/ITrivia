package com.example.triviaapp.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.triviaapp.R;
import com.example.triviaapp.model.UserLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentLocation extends FragmentActivity implements OnMapReadyCallback {
    DatabaseReference userLocation;
    double longitute, latitute;
    ImageButton prev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        prev = findViewById(R.id.prev);

        userLocation = FirebaseDatabase.getInstance().getReference("UserLocation");
        setMap();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentLocation.this, AdminPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setMap(){
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(CurrentLocation.this);
    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                        latitute = dsp.getValue(UserLocation.class).getLatitude();
                        longitute = dsp.getValue(UserLocation.class).getLongitude();
                        LatLng latLng = new LatLng(latitute, longitute);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("User: " + dsp.getValue(UserLocation.class).getUserId());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                        googleMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


}
