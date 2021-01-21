package com.example.hikersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager ;
    LocationListener locationlistener ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            StartListening();
        }

    }

    public void  updateLocationInfo(Location location){
        Log.i("Location",location.toString());
        TextView Lattitude = findViewById(R.id.lattitude);
        TextView Longitude = findViewById(R.id.longitude);
        TextView Accuracy = findViewById(R.id.accuracy);
        TextView Altitude = findViewById(R.id.altitude);
        Lattitude.setText("Lattitude :"+location.getLatitude());
        Longitude.setText("Longitude :"+location.getLongitude());
        Accuracy.setText("Accuracy :"+location.getAccuracy());
        Altitude.setText("Altitude :"+location.getTime());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address =" Address could not find";
            List<Address> list = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(list != null && list.size()>0){
                address="";
                Log.i("PlaceInfo",list.get(0).toString());
                if(list.get(0).getSubThoroughfare()!= null)
                {
                    address+=list.get(0).getSubThoroughfare().toString()+"\n";
                }
                if(list.get(0).getThoroughfare()!= null)
                {
                    address+=list.get(0).getThoroughfare().toString()+"\n";
                }
                if(list.get(0).getLocality()!= null)
                {
                    address+=list.get(0).getLocality().toString()+"\n";
                }
                if(list.get(0).getCountryName()!= null)
                {
                    address+=list.get(0).getCountryName().toString()+"\n";
                }
                if(list.get(0).getPostalCode()!= null)
                {
                    address+=list.get(0).getPostalCode().toString()+" ";
                }
                TextView Address = findViewById(R.id.address);
                Address.setText("Address  : "+address);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void StartListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationlistener);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
        };

        if(Build.VERSION.SDK_INT<23) {
            StartListening();
        }else{
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        updateLocationInfo(location);

                    }
                }
            }
        }
}