package distributed.androidclientforpointsofinterest;

import android.content.Intent;
import android.graphics.Color;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        final LatLng centerPin = new LatLng(MainActivity.locationPoi.getLatitude(), MainActivity.locationPoi.getLongtitude());
        mMap.addMarker(new MarkerOptions()
                .position(centerPin)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).setTag(-1);
        mMap.addCircle(new CircleOptions()
                .center(centerPin)
                .radius(MainActivity.kilometers * 1000)
                .strokeWidth(10)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPin, 12.0f));

        for(int i=0; i< MainActivity.poisInfo.length; i++)
        {
            LatLng pin = new LatLng(MainActivity.poisInfo[i].getLatitude(), MainActivity.poisInfo[i].getLongtitude());
            marker = mMap.addMarker(new MarkerOptions().position(pin));
            marker.setTag(i);

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                int index = (int)(marker.getTag());
                if(index != -1){
                    Intent i = new Intent(MapsActivity.this, PinInfo.class);
                    i.putExtra("pinIndex", index);
                    startActivity(i);
                }
                return false;
            }
        });
    }

}
