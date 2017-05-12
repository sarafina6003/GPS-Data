package com.serafynurh.gpsdata;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvLongitude, tvLatitude, tvAltitude, tvSpeed, tvStatus;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvAltitude = (TextView) findViewById(R.id.tvAltitude);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        //LOCATION_SERVICE alternatives...
        //GPS==Accurate ...but sucks battery power
        //WIFI==Low Battery, 100m
        //NETWORK==Low Battery, 200m
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Intent settings =new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settings);
        }

        PackageManager pm =this.getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS))
        {
            tvStatus.setText("Phone has GPS");
        }else {
            tvStatus.setText("Phone has no GPS");
        }

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
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3500, 2, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tvAltitude.setText("Altitude is "+location.getAltitude()+" meters");
                tvLatitude.setText("Latitude is "+location.getLatitude());
                tvLongitude.setText("Longitude is "+location.getLongitude());
                double kph = location.getSpeed() *3600/1000.0;
                tvSpeed.setText("Speed is "+kph+" Kph");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                tvStatus.setText("GPS Status Changed");
            }

            @Override
            public void onProviderEnabled(String provider) {
                tvStatus.setText("GPS Status Changed");
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });





    }
}
