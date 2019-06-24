package com.practice.findmap.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.practice.findmap.data.AddNewFindDB;
import com.practice.findmap.data.DBHelper;
import com.practice.findmap.domain.model.FindData;
import com.practice.findmap.domain.model.MarkerCoordinates;
import com.practice.findmap.R;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        AddDialog.AddDialogListener, CategoryDialog.CategoryDialogListener, SearchView.OnQueryTextListener {

    private GoogleMap mMap;
    private SearchView searchView;
    private DBHelper dbHelper;
    private AddNewFindDB addNewFindDB;
    private final LatLng saintPetersburg = new LatLng(59.9386, 30.3141);;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private MenuItem item_add;
    private MenuItem item_search;
    private MenuItem item_add_market;
    private MenuItem item_delete;
    private MenuItem item_update;
    private MenuItem item_delete_update;
    private Marker addMarker;
    private Marker clickMarker;
    private FindData findClick;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.title_map);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        requestPermission();
        dbHelper = new DBHelper(this);
        addNewFindDB = new AddNewFindDB(dbHelper);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setCompassEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(saintPetersburg));


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag (Marker marker) {
            }

            @Override
            public void onMarkerDragEnd (Marker marker) {
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng positionlatLng = marker.getPosition();
                MarkerCoordinates coordinates = new MarkerCoordinates(positionlatLng.latitude,
                        positionlatLng.longitude);

                findClick = addNewFindDB.findByCoordinates(coordinates);
                marker.setTitle(findClick.getComment());
                marker.showInfoWindow();
                clickMarker = marker;
                updateTopMenu();

                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng arg0){
               mainTopMenu();
            }
        });

        findByTextInDB("");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_map_menu, menu);
        item_add = menu.findItem(R.id.add_btn);
        item_search = menu.findItem(R.id.search_btn);
        item_add_market = menu.findItem(R.id.add_market_btn);
        item_delete = menu.findItem(R.id.delete_btn);
        item_update = menu.findItem(R.id.update_btn);
        item_delete_update = menu.findItem(R.id.delete_btn_update);

        MenuItem searchItem = menu.findItem(R.id.search_btn);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        showMarkers();

        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_btn:
                addMarker();
                return true;
            case R.id.delete_btn:
                deleteMarker(addMarker);
                return true;
            case R.id.add_market_btn:
                openDialog();
                return true;
            case R.id.update_btn:
                openEdit(findClick);
                return true;
            case R.id.delete_btn_update:
                openDelete(findClick, clickMarker);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void openDialog() {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "add dialog");
    }

    public void openDelete(FindData findClick, Marker marker) {
        deleteMarker(marker);
        addNewFindDB.deleteById(findClick.getId());
        mainTopMenu();
    }

    public void openEdit (FindData findClick) {
        Intent editActivityIntent = new Intent(MapsActivity.this, EditActivity.class);
        editActivityIntent.putExtra(EditActivity.LAT, Double.toString(findClick.getCoordinates().getLatitude()));
        editActivityIntent.putExtra(EditActivity.LNG, Double.toString(findClick.getCoordinates().getLongitude()));
        startActivity(editActivityIntent);
    }

    public void addMarker() {
        Location myLocation = mMap.getMyLocation();
        LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        addMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true).title("Находка"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        addMarkerTopMenu();

    }

    public void deleteMarker(Marker marker) {
        marker.remove();
        mainTopMenu();
    }

    public void makeMarkerAdded(Marker addMarker, String category) {
        mainTopMenu();
        setIconMarker(category, addMarker);

        LatLng addlatLng = addMarker.getPosition();
        MarkerCoordinates coordinates = new MarkerCoordinates(addlatLng.latitude, addlatLng.longitude);
        FindData find = new FindData (coordinates, comment, category);
        addNewFindDB.saveFind(find);

    }

    public void showMarkers() {
        mMap.clear();
        findByTextInDB(searchView.getQuery().toString());

    }

    public void findByTextInDB(String text) {
        List<? extends FindData> finds = addNewFindDB.findByComment(text);
        for (FindData find : finds) {
            MarkerCoordinates coordinates = find.getCoordinates();
            String find_cat = find.getCategory();

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinates.getLatitude(), coordinates.getLongitude())));
            setIconMarker(find_cat, marker);
        }

    }

    @Override
    public void applyComment(String find_comment) {
        comment = find_comment;
    }

    @Override
    public void applyCategory(String category) {
        if ((!comment.equals("")) && !category.equals(""))
        {
            makeMarkerAdded(addMarker, category);
        } else deleteMarker(addMarker);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showMarkers();
        return false;
    }


    public void mainTopMenu() {
        item_add.setVisible(true);
        item_search.setVisible(true);
        item_add_market.setVisible(false);
        item_delete.setVisible(false);
        item_delete_update.setVisible(false);
        item_update.setVisible(false);

    }

    public void updateTopMenu() {
        item_add.setVisible(false);
        item_search.setVisible(false);
        item_add_market.setVisible(false);
        item_delete.setVisible(false);
        item_delete_update.setVisible(true);
        item_update.setVisible(true);
    }

    public void addMarkerTopMenu() {
        item_add.setVisible(false);
        item_search.setVisible(false);
        item_add_market.setVisible(true);
        item_delete.setVisible(true);
        item_delete_update.setVisible(false);
        item_update.setVisible(false);
    }


    public void setIconMarker(String category, Marker marker) {
        switch (category) {
            case "Еда и напитки":
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food));
                break;
            case "Магазины":
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.supermarket));
                break;
            case "Достопримечательности":
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sight));
                break;
            case "Природа":
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.nature));
                break;
            case "Развлечения":
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.entertainment));
                break;
            default:
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.other));
                break;
        }
    }

}
