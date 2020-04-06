package com.example.pennyfarthing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class GenderReveal extends Activity implements LocationListener {
    private static final int REQUEST_LOCATION = 1;
    private static final String TAG ="ACTIVITY STUPENDOUS" ;
    Button yes, no;
    LocationManager locationManager;
    String latitude, longitude;
    TextView viws;
    double longi = 20,lati = 120;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_reveal);
        FirebaseAuth mFAuth = FirebaseAuth.getInstance();
        yes = findViewById(R.id.yesbtn);
        no = findViewById(R.id.nobtn);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Yes is clicked" );
                viws = (TextView) findViewById(R.id.Question);
                fStore = FirebaseFirestore.getInstance();
                FirebaseAuth mFAuth = FirebaseAuth.getInstance();
                String userID = mFAuth.getCurrentUser().getUid();
                //viws.setText(userID + "You said Yes");
                if(userID!=null) {
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Log.d(TAG, "Data ready to be put " );
                    Map<String,Object> user = new HashMap<>();
                    user.put("Person1Long",longi);
                    user.put("Person1Lat",lati);
                    documentReference.set(user,SetOptions.merge());
                }
                else{
                    Log.d(TAG, "DocumentSnapshot data is here: ");
                }

                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viws = (TextView) findViewById(R.id.Question);
                fStore = FirebaseFirestore.getInstance();
                FirebaseAuth mFAuth = FirebaseAuth.getInstance();
                String userID = mFAuth.getCurrentUser().getUid();
                //viws.setText(userID + "You said No");
                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("Person2Long",longi);
                user.put("Person2Lat",lati);

                documentReference.set(user, SetOptions.merge());
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });

    }

    public void Loccc(Location location){
        lati = location.getLongitude();
        longi = location.getLongitude();

    }

    @Override
    public void onLocationChanged(Location location) {
        lati = location.getLatitude();
        longi =  location.getLongitude();
        Log.d(TAG, "Location found " );
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

