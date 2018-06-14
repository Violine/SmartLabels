package alexander.korovin.com.smartlabels;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.MenuInflater;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListViewAdapter listViewAdapter;
    ListView listView;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initListView();
        initFloatingActionButton();
        //initDrawerLayout(toolbar);
        //initNavigationView();
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
                mode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
                return true;
            }
            case R.id.action_edit_label: {
                Label oldLabel = LabelList.getLabelList().get(position);
                Intent intent = new Intent(MainActivity.this, AddLabelActivity.class);
                intent.putExtra("LABEL_HEADER", oldLabel.getLabelHeader());
                intent.putExtra("LABEL_DESCRIPTION", oldLabel.getLabelDescription());
                intent.putExtra("POSITION", position);
                intent.putExtra("EDIT","EDIT");
                startActivity(intent);
                return true;

            }
            case R.id.action_remove_label: {
                listViewAdapter.removeLabel(position);
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
