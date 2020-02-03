package com.pfa.projet;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements LocationListener {
    private static final int PERMS_CALL_ID = 123;
    private LocationManager lm;
    private MapFragment mapFragment;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        FragmentManager fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);

            return;
        }
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }

        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, this);
        }

        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
        loadMap();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALL_ID) {
            checkPermissions();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lm != null) {
            lm.removeUpdates(this);
        }

    }

@SuppressWarnings("MissingPermission")
    private void loadMap(){

       mapFragment.getMapAsync(new OnMapReadyCallback() {
           @Override
           public void onMapReady(GoogleMap googleMap) {
MapActivity.this.googleMap=googleMap;
googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(33.604373, -7.485138)));
googleMap.moveCamera(CameraUpdateFactory.zoomBy(50));
googleMap.setMyLocationEnabled(true);
googleMap.addMarker(new MarkerOptions().position(new LatLng(33.592806, -7.613141)).title("Cih"));
               googleMap.addMarker(new MarkerOptions().position(new LatLng(33.593289, -7.614729)).title("Bmce"));
               googleMap.addMarker(new MarkerOptions().position(new LatLng(33.592726, -7.615566)).title("Bmci"));
               googleMap.addMarker(new MarkerOptions().position(new LatLng(33.593048, -7.614364)).title("cih"));



           }
       });
    }
private BitmapDescriptor BitmapDescriptorFromVector(Context context,int vectorResId){
    Drawable vectorDrawabel= ContextCompat.getDrawable(context,vectorResId);
    vectorDrawabel.setBounds( 0, 0 ,vectorDrawabel.getIntrinsicWidth(),
            vectorDrawabel.getIntrinsicHeight());
    Bitmap bitmap=Bitmap.createBitmap(vectorDrawabel.getIntrinsicWidth(),vectorDrawabel.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
    Canvas canvas=new Canvas(bitmap);
    vectorDrawabel.draw(canvas);
    return BitmapDescriptorFactory.fromBitmap(bitmap);


}
    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        Toast.makeText(this,"Location"+"/"+longitude,Toast.LENGTH_LONG).show();

        if(googleMap != null){
LatLng googleLocation=new LatLng(latitude,longitude);
googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
}
