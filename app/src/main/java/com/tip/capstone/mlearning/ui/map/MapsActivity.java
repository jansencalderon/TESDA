package com.tip.capstone.mlearning.ui.map;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.DialogSchoolBinding;
import com.tip.capstone.mlearning.model.School;
import com.tip.capstone.mlearning.utils.BitmapUtils;

import java.util.List;

import io.realm.Realm;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Realm realm;
    private LatLngBounds bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        realm = Realm.getDefaultInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        List<School> schools = realm.where(School.class).findAll();
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        MarkerOptions markerOptions = new MarkerOptions();
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_view, null);
        if (!schools.isEmpty()) {
            for (School school : schools) {
                markerOptions.position(new LatLng(school.getLatitude(), school.getLongitude()));
                markerOptions.title(school.getName());
                markerOptions.snippet(school.getId() + "");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.createDrawableFromView(this, marker)));
                mMap.addMarker(markerOptions);
                builder.include(markerOptions.getPosition());

            }
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                mMap.moveCamera(cu);
            }
        });

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        DialogSchoolBinding schoolBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_school,
                null,
                false);
        schoolBinding.setItem(realm.where(School.class).equalTo("id", Integer.parseInt(marker.getSnippet())).findFirst());
        Dialog dialog = new Dialog(this);
        dialog.setContentView(schoolBinding.getRoot());
        dialog.show();
        return true;
    }
   /* if (!marker.getSnippet().equals("Me")) {

        final Clinic clinic = realm.where(Clinic.class).equalTo("clinicId", Integer.parseInt(marker.getSnippet())).findFirst();
        DialogMapBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_map,
                null,
                false);
        final Dialog dialog = new Dialog(MapActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.setClinic(clinic);

        switch (clinic.getClinicImage()) {
            case "ophthal.jpg":
                Glide.with(this).load(R.drawable.opht).into(dialogBinding.imageView);
                break;
            case "dental.jpg":
                Glide.with(this).load(R.drawable.dent).into(dialogBinding.imageView);
                break;
            case "derma.jpg":
                Glide.with(this).load(R.drawable.derm).into(dialogBinding.imageView);
                break;
        }

        dialogBinding.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ClinicActivity.class);
                intent.putExtra("id", clinic.getClinicId());
                startActivity(intent);
            }
        });
        dialog.show();

        return true;
    } else {


        return false;
    }*/
}
