package alexander.korovin.com.smartlabels.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alexander.korovin.com.smartlabels.Utils.App;
import alexander.korovin.com.smartlabels.Utils.LabelsBDHelper;
import alexander.korovin.com.smartlabels.Utils.ListViewAdapter;
import alexander.korovin.com.smartlabels.Models.Label;
import alexander.korovin.com.smartlabels.Models.LabelList;
import alexander.korovin.com.smartlabels.Models.Weather;
import alexander.korovin.com.smartlabels.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListViewAdapter listViewAdapter;
    private ListView listView;
    private Menu menu;
    private TextView currentLocationTextView;
    private TextView currentWeatherTextView;
    private LocationManager locationManager;
    private Location currentLocation;
    private MyLocationListener locationListener = new MyLocationListener(this);
    private String currentLocality = "";
    private SQLiteDatabase database;
    private TextView listIsEmpty;

    private static final long MIN_DISTANCE_DELTA = 10; // 10 meters
    private static final long MIN_TIME_DELTA = 15000; // 1 min
    private static final String APP_ID_KEY = "480977819e7a40036456f129028da3f7";
    private static final String UNITS = "metric";
    private static final String LABEL_POSITION = "LABEL_POSITION";
    private static final String LABELS_FILE_NAME = "labels_base";

    @Override
    protected void onPause() {
        super.onPause();
       // saveLabelsToFIle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        currentLocationTextView = findViewById(R.id.currentLocationTextView);
        currentWeatherTextView = findViewById(R.id.currentWeatherTextView);
        setSupportActionBar(toolbar);

        initDB();
        //readLabelsFromFIle();
        initListView();
        initFloatingActionButton();
        startLocation();
        getWeather(currentLocality);

        //initDrawerLayout(toolbar);
        //initNavigationView();
    }

    private void readLabelsFromFIle() {
        String pathToBase = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + LABELS_FILE_NAME;
        // labels = SaveReadToFileUtils.readFromFile(pathToBase);
        if (listViewAdapter != null) {
            listViewAdapter.notifyDataSetChanged();
        }
    }

    private void saveLabelsToFIle() {
        String pathToBase = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + LABELS_FILE_NAME;
        // SaveReadToFileUtils.saveToFile(pathToBase);
    }

    private void getWeather(String currentLocality) {
        App.getApi().getData(currentLocality, APP_ID_KEY, UNITS).enqueue(new Callback<Weather>() {

            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Weather data = response.body();
                if (response.isSuccessful()) {
                    currentWeatherTextView.setText(String.format("%s%s", getString(R.string.weather_in_your_city), data.getTempWithDegree()));
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDB() {
        database = new LabelsBDHelper(getApplicationContext()).getWritableDatabase();
    }

    private void startLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d("LOCATION", "PROVIDER_NOT_ACTIVE");
        } else {
            if (checkPermission()) {
                if (isNetworkEnabled) {
                    Log.d("Location", "Coarse Enabled");
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_DELTA, MIN_DISTANCE_DELTA, locationListener);
                        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (currentLocation != null) {
                            currentLocationTextView.setText(getApplicationState(currentLocation));
                        }
                    }
                }
                if (isGPSEnabled) {
                    Log.d("Location", "GPS Enabled");
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_DELTA, MIN_DISTANCE_DELTA, locationListener);
                        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (currentLocation != null) {
                            currentLocationTextView.setText(getApplicationState(currentLocation));
                        }
                    }
                }
            }
        }
    }

    private String getApplicationState(Location location) {
        final Geocoder geocoder = new Geocoder(this);

        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        if (addressList.isEmpty()) return "LOCATION NOT FOUND";
        Address currentAddress = addressList.get(0);

        StringBuilder builder = new StringBuilder();
        final String separator = ", ";
        currentLocality = currentAddress.getLocality();
        builder.append(currentAddress.getCountryName()).append(separator)
                .append(currentLocality);
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
        listView = findViewById(R.id.smartlabels_list_view);
        listIsEmpty = findViewById(R.id.empty);
        listView.setEmptyView(listIsEmpty);
        listViewAdapter = new ListViewAdapter(getApplicationContext(), database);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setAdapter(listViewAdapter);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    listViewAdapter.getItem(position).setChecked(true);
                } else {
                    listViewAdapter.getItem(position).setChecked(false);
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
               // labels = LabelList.getLabelList();
//                for (int i = 0; i < labels.size(); i++) {
//                    labels.get(i).setChecked(false);
//                }
                listViewAdapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startLabelInfoActivity(position);
                //popupMenuInit(view, position);
            }
        });
    }

    private void startLabelInfoActivity(int position) {
        Intent intent = new Intent(this, LabelInfoActivity.class);
        intent.putExtra(LABEL_POSITION, position);
        startActivity(intent);
    }

    private void popupMenuInit(View view, final int position) {
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

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //readLabelsFromFIle();
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation != null) {
                currentLocationTextView.setText(getApplicationState(currentLocation));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean persmissionsGranted = false;
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    persmissionsGranted = true;
                } else {
                    persmissionsGranted = false;
                }
            } else {
                persmissionsGranted = false;
            }
        }
        if (persmissionsGranted) {
            recreate();
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

class MyLocationListener implements LocationListener {
    Context context;

    MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Toast.makeText(context, "КООРДИНАТЫ ОБНОВЛЕНЫ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "НЕ УДАЛОСЬ ОПРЕДЕЛИТЬ КООРДИНАТЫ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}