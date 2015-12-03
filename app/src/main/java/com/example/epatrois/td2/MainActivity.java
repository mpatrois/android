package com.example.epatrois.td2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.widget.AdapterView.*;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase mydatabase;
    EditText nomVille;
    Spinner spinnerBram;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nomVille = ((EditText) (findViewById(R.id.editText)));

        spinnerBram = (Spinner) findViewById(R.id.spinnerBram);

        SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);
        String defaultValue = "";
        String ville = sharedPref.getString("SavedVille", defaultValue);
        nomVille.setText(ville);

        mydatabase = openOrCreateDatabase("Villes", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS City(name VARCHAR);");
    }


    public void addElementToDatabase(String value) {
        Cursor resultSet = mydatabase.rawQuery("Select * from City where name like '"+value+"'", null);
        if(resultSet.getCount()==0)
        mydatabase.execSQL("INSERT INTO City (name) VALUES ('" + value + "');");
    }

    public void setSpinnerValues() {
        Cursor resultSet = mydatabase.rawQuery("Select * from City", null);

        resultSet.moveToFirst();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item);
        spinnerBram.setAdapter(arrayAdapter);

        while (resultSet.moveToNext()) {
            arrayAdapter.add(resultSet.getString(0));
        }

        spinnerBram.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                nomVille.setText(spinnerBram.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void loadActivity(View v) {


        addElementToDatabase(nomVille.getText().toString());

        Intent intent = new Intent(this, VilleInfo.class);

        intent.putExtra("nomVille", nomVille.getText().toString());

        startActivity(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("SavedVille", nomVille.getText().toString());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinnerValues();


    }

    public GPS getGPS() {
        GPS gps=new GPS(this);
        return gps;
    }

    public void putCurrentCity(View v){
        nomVille.setText(getGPS().getCity());
    }
}






//        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
//
//
//        LocationListener locationListener = new LocationListener() {
//
//            @Override
//            public void onLocationChanged(Location location) {
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };

//        if (this.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                && this.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//        {

//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//
//            String locationProvider = LocationManager.NETWORK_PROVIDER;
//            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
//
//            Log.i("location",lastKnownLocation.getLatitude()+" "+lastKnownLocation.getLongitude());
//
//            Geocoder gcd = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = null;
//            try {
//                addresses = gcd.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
//                if (addresses.size() > 0)
//                    Log.i("location name",addresses.get(0).getLocality());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//        }