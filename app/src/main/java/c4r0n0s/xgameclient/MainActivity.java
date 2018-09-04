package c4r0n0s.xgameclient;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import c4r0n0s.xgameclient.entities.AccountEntity;
import c4r0n0s.xgameclient.fragments.AccountFragment;
import c4r0n0s.xgameclient.fragments.StatusFragment;
import c4r0n0s.xgameclient.fragments.WebViewFragment;
import c4r0n0s.xgameclient.services.AccountManagerService;
import c4r0n0s.xgameclient.utils.DetectConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static String androidId;
    private IntentFilter mIntentFilter = new IntentFilter();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", "-------------------onReceive-------------------");
            String measurement = intent.getStringExtra("measurement");
            Log.d("MainActivity", "measurement - 2 : " + measurement);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        this.checkInternetConnection();
        androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccountManagerService.loadAccount();
        final Timer refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.hasChild(MainActivity.getAndroidId())) {
                            refreshTimer.cancel();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Failed to read value.");
                        refreshTimer.cancel();
                    }
                });

                AccountEntity accountSettings = AccountManagerService.getAccountSettings();
                if (Objects.nonNull(accountSettings)) {
                    loadDefaultPageFragment();
                    refreshTimer.cancel();
                }
            }
        }, 0, 100);

        mIntentFilter.addAction("c4r0n0s.MY_ACTION");
        registerReceiver(mReceiver, mIntentFilter);

        // TODO make send request btn
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;
        Class fragmentClass = WebViewFragment.class;;

        if (id == R.id.nav_browser) {
            fragmentClass = WebViewFragment.class;
        } else if (id == R.id.nav_status) {
            fragmentClass = StatusFragment.class;
        } else if (id == R.id.nav_tasks) {
        } else if (id == R.id.nav_account) {
            fragmentClass = AccountFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void checkInternetConnection() {
        if (!DetectConnection.isInternetConnectionAvailable(this)) {
            String text = "No Internet connection, Please fix it";
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    private void loadDefaultPageFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContainer, WebViewFragment.newInstance())
                .commit();
    }

    public static String getAndroidId() {
        return androidId;
    }
}
