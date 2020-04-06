package com.example.pennyfarthing;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import javax.annotation.Nullable;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "ACTIVITY";
    private GoogleMap mMap;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    double P1lat,P1long,P2lat,P2long;
    LatLng P1,P2,point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        P1long = document.getDouble("Person1Long");
                        P1lat = document.getDouble("Person1Lat");
                        P2long = document.getDouble("Person2Long");
                        P2lat = document.getDouble("Person2Lat");
                        Log.d(TAG, "DocumentSnapshot data: " + P2long + " "+ P2lat);
                        P1 = new LatLng(P1lat, P1long);
                        P2 = new LatLng(P2lat,P2long);
                        Log.d(TAG, "DocumentSnapshot data is here: " + P2long + " "+ P2lat);
                        Log.d(TAG, "DocumentSnapshot data is here: " + P1long + " "+ P1lat);
                        mMap.addMarker(new MarkerOptions().position(P1).title("Your name starts with A"));
                        mMap.addMarker(new MarkerOptions().position(P2).title("Your name does not"));


                        float zoom = 16;
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(P2));

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



    }
}
