package com.pelotheban.hither;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    private TextView txtUserLocationX, txtDistanceX;
    private FloatingActionButton fab;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private DatabaseReference homePageRef;
    private String userID;

    Double lat, longit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = FirebaseAuth.getInstance().getUid();
        homePageRef = FirebaseDatabase.getInstance().getReference().child("my_users").child(userID);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        txtUserLocationX = findViewById(R.id.txtUserLocation);
        txtDistanceX = findViewById(R.id.txtDistance);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                        // get location
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {

                                    lat = location.getLatitude();
                                    longit = location.getLongitude();

                                    txtUserLocationX.setText("Lat: " + lat + "  Long: " + longit);
                                    homePageRef.getRef().child("lastlocation").setValue(location);

                                    calculateDistance();

                                }

                            }
                        });

                    } else {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);

                    }

                }

            }
        });
    }

    private void calculateDistance() {

        Location startPoint=new Location("locationA");
        startPoint.setLatitude(lat);
        startPoint.setLongitude(longit);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(43.364605);
        endPoint.setLongitude(-80.3127999);

        double distance=startPoint.distanceTo(endPoint);

        String distance2 = String.valueOf(distance);
        txtDistanceX.setText(distance2);


    }

}