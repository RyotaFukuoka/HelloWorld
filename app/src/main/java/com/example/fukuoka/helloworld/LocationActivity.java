package com.example.fukuoka.helloworld;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity implements LocationListener {

    private LocationManager locationManager;
    private TextView textView;
    private String text = "start\n";
    private Button buttonStart, buttonStop;

    private static final int MinTime = 1000;
    private static final float MinDistance = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        textView = (TextView) findViewById(R.id.a1);
        text += "onCreate()\n";
        textView.setText(text);

        // GPS測位開始
        buttonStart = (Button)findViewById(R.id.button1);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGPS();
            }
        });

        // GPS測位終了
        buttonStop = (Button)findViewById(R.id.button2);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGPS();
            }
        });
    }

    protected void startGPS() {
        text += "onResume()\n";
        textView.setText(text);

        Log.d("LocationActivity", "gpsEnabled");
        final boolean gpsEnabled
                = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // GPSを設定するように促す
            enableLocationSettings();
        }

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.requestLocationUpdates");
            // バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                // minTime = 1000msec, minDistance = 50m
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)!=
                                PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MinTime, MinDistance,this);
            } catch (Exception e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(this,
                        "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT);
                toast.show();

                //MainActivityに戻す
                finish();
            }
        } else {
            text += "locationManager=null\n";
            textView.setText(text);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.removeUpdates");
            // update を止める
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(this);
        } else {
            text += "onPause()\n";
            textView.setText(text);
        }

        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        text += "----------\n";
        text += "Latitude=" + String.valueOf(location.getLatitude()) + "\n";
        text += "Longitude=" + String.valueOf(location.getLongitude()) + "\n";
        text += "Accuracy=" + String.valueOf(location.getAccuracy()) + "\n";
        text += "Altitude=" + String.valueOf(location.getAltitude()) + "\n";
        text += "Time=" + String.valueOf(location.getTime()) + "\n";
        text += "Speed=" + String.valueOf(location.getSpeed()) + "\n";
        text += "Bearing=" + String.valueOf(location.getBearing()) + "\n";
        text += "----------\n";

        textView.setText(text);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                text += "LocationProvider.AVAILABLE\n";
                textView.setText(text);

                break;
            case LocationProvider.OUT_OF_SERVICE:
                text += "LocationProvider.OUT_OF_SERVICE\n";
                textView.setText(text);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                text += "LocationProvider.TEMPORARILY_UNAVAILABLE\n";
                textView.setText(text);
                break;
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    private void stopGPS(){
        if (locationManager != null) {
            Log.d("LocationActivity", "onStop()");
            // update を止める
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        } else {
            text += "onStop()\n";
            textView.setText(text);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        stopGPS();
    }
}
