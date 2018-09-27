package com.example.okcomputers.mybusapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by OK Computers on 9/27/2018.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double Latitude;
    double longitude;

    private static final long MIN_DISTANCE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BTW_UPDATES = 1000 *60*1;

    protected LocationManager locationManager;

    public GPSTracker(Context context)
    {
        this.mContext = context;
        getLocation();
    }
    public Location getLocation()
    {
        try {
            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled)
            {

            }
            else
            {
                this.canGetLocation =true;
                if(isNetworkEnabled)
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BTW_UPDATES,MIN_DISTANCE_FOR_UPDATES,this);
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location !=null)
                        {
                          Latitude = location.getLatitude();
                          longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled)
                {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BTW_UPDATES, MIN_DISTANCE_FOR_UPDATES, this);


                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                        if (location != null) {
                            Latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS()
    {
        if (locationManager != null)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    public double getLatitude()
    {
        if (location != null)
        {
            Latitude = location.getLatitude();
        }
        return Latitude;
    } public double getLongitude()
    {
        if (location != null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation()
    {
        return  this.canGetLocation;
    }

    public void showSettingsAlert()
    {
        final AlertDialog.Builder alertDialog =new AlertDialog.Builder(mContext);

        //setting dialog title
        alertDialog.setTitle("GPS SETTINGS");
        alertDialog.setMessage("GPS is not enabled, do you want to go to Settings Menu");

        //Setting dialog on positive click listener
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent =new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
    //setting dialog on cancel or no option
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
//showing alert dialog
        alertDialog.show();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
