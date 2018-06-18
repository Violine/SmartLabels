package alexander.korovin.com.smartlabels;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListViewAdapter listViewAdapter;
    private ListView listView;
    private Menu menu;
    private LocationManager locationManager;

    private static long MIN_DISTANCE_DELTA = 10; // 10 meters
    private static long MIN_TIME_DELTA = 15000; // 1 min

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initListView();
        initFloatingActionButton();
        startLocation();
        //initDrawerLayout(toolbar);
        //initNavigationView();
    }

    private void startLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location currentLocation;

        TextView currentLocationTextView = findViewById(R.id.currentLocationTextView);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        double latitude;
        double longitude;

        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d("LOCATION", "PROVIDER_NOT_ACTIVE");
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("LOCATION", "NOT_PERMISSION_FOR_LOCATION");
                return;
            }
            if (isNetworkEnabled) {
                Log.d("Location", "Coarse Enabled");
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_DELTA,
                            MIN_DISTANCE_DELTA, (LocationListener) this);
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (currentLocation != null) {
//                        latitude = currentLocation.getLatitude();
//                        longitude = currentLocation.getLongitude();
                        currentLocationTextView.setText(getApplicationState(currentLocation));
                    }
                }
            }
            if (isGPSEnabled) {
                Log.d("Location", "GPS Enabled");
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_DELTA,
                            MIN_DISTANCE_DELTA, (LocationListener) this);
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (currentLocation != null) {
//                        latitude = currentLocation.getLatitude();
//                        longitude = currentLocation.getLongitude();
                        currentLocationTextView.setText(getApplicationState(currentLocation));
                    }
                }
            }
        }
    }

    private String getApplicationState(Location location) {
        final Geocoder geocoder = new Geocoder(this);

        List<Address> list;
        try {
            list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        if (list.isEmpty()) return "LOCATION NOT FOUND";
        Address currentAddress = list.get(0);
        final int index = currentAddress.getMaxAddressLineIndex();
        String postal = " ";

        if (index >= 0) {
            postal = currentAddress.getAddressLine(index);
        }

        StringBuilder builder = new StringBuilder();
        final String separator = ", ";
        builder.append(postal).append(separator)
                .append(currentAddress.getCountryName()).append(separator)
                .append(currentAddress.getAdminArea()).append(separator)
                .append(currentAddress.getThoroughfare()).append(separator)
                .append(currentAddress.getSubThoroughfare());

        return builder.toString();
    }

    private void initNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initDrawerLayout(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddLabelActivity();
            }
        });
    }

    private void startAddLabelActivity() {
        Intent intent = new Intent(MainActivity.this, AddLabelActivity.class);
        startActivity(intent);
    }

    private void initListView() {
        listViewAdapter = new ListViewAdapter(this, LabelList.getLabelList());
        listView = findViewById(R.id.smartlabels_list_view);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setAdapter(listViewAdapter);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    View view = listView.getChildAt(position);
                    view.setBackgroundColor(getResources().getColor(R.color.checkedElementListView));
                } else {
                    View view = listView.getChildAt(position);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                listViewAdapter.notifyDataSetChanged();
                Log.d("LOG_TAG", "position = " + position + ", checked = "
                        + checked);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.main, menu);
                menu.findItem(R.id.action_add_label).setVisible(false);
                menu.findItem(R.id.action_edit_label).setVisible(false);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_remove_label:
                        deleteSelectedItems();
                        listViewAdapter.notifyDataSetChanged();
                        for (int i = 0; i < listView.getCount(); i++)
                            listView.setItemChecked(i, false);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }


            private void deleteSelectedItems() {
                SparseBooleanArray checkedElement = listView.getCheckedItemPositions();
                ArrayList<Integer> idForRemove = new ArrayList<>();
                for (int i = 0; i < checkedElement.size(); i++) {
                    int position = checkedElement.keyAt(i);
                    if (checkedElement.get(position)) {
                        idForRemove.add(LabelList.getLabelList().get(position).getLabelId());
                    }
                }
                for (int i = 0; i < idForRemove.size(); i++) {
                    LabelList.removeLabelToPosition(idForRemove.get(i));
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                menu = popupMenu.getMenu();
                getMenuInflater().inflate(R.menu.main, menu);
                menu.findItem(R.id.action_add_label).setVisible(false);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return handleMenuItemClick(item.getItemId(), position);
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_edit_label).setVisible(false);
        menu.findItem(R.id.action_remove_label).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        handleMenuItemClick(item.getItemId(), 0);
        return true;
    }

    private boolean handleMenuItemClick(int id, int position) {
        switch (id) {
            case R.id.action_add_label: {
                startAddLabelActivity();
                listViewAdapter.notifyDataSetChanged();
                return true;
            }
            case R.id.action_edit_label: {
                Label oldLabel = LabelList.getLabelList().get(position);
                Intent intent = new Intent(MainActivity.this, AddLabelActivity.class);
                intent.putExtra("LABEL_HEADER", oldLabel.getLabelHeader());
                intent.putExtra("LABEL_DESCRIPTION", oldLabel.getLabelDescription());
                intent.putExtra("ID", oldLabel.getLabelId());
                intent.putExtra("EDIT", "EDIT");
                startActivity(intent);
                listViewAdapter.notifyDataSetChanged();
                return true;
            }
            case R.id.action_remove_label: {
                LabelList.removeLabelToPosition(LabelList.getLabelList().get(position).getLabelId());
                listViewAdapter.notifyDataSetChanged();
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
