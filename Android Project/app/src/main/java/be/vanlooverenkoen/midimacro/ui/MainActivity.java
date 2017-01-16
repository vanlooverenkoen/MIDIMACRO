package be.vanlooverenkoen.midimacro.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.adapter.MidiOverTouchOSCBridge;
import be.vanlooverenkoen.midimacro.adapter.MidiOverMidiMacro;
import be.vanlooverenkoen.midimacro.adapter.MidiOverUsb;
import be.vanlooverenkoen.midimacro.adapter.mqtt.MQTTService;
import be.vanlooverenkoen.midimacro.adapter.mqtt.MidiOverMQTT;
import be.vanlooverenkoen.midimacro.adapter.mqtt.Mosquitto;
import be.vanlooverenkoen.midimacro.exception.MidiServiceException;
import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.service.MidiService;
import be.vanlooverenkoen.midimacro.service.MidiServiceListener;
import be.vanlooverenkoen.midimacro.sharedprefs.SharedPrefsMidiMode;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , MidiServiceListener
        , MidiMacroFragment.MidiMessageListener {

    private MidiService midiService;

    private MidiMacroFragment currentFragment;
    private SharedPrefsMidiMode midiMacroSharedPrefs;
    private boolean notConnect;

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    TextView midiModeTv;
    TextView midiIPTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        midiModeTv = (TextView) header.findViewById(R.id.midi_mode_tv);
        midiIPTv = (TextView) header.findViewById(R.id.midi_ip_tv);
        midiMacroSharedPrefs = new SharedPrefsMidiMode();
        setFirstFragment();
    }

    private void setupMidiService() {
        String mode = midiMacroSharedPrefs.getMidiMode();
        try {
            if (midiService != null)
                midiService.disconnect();
        } catch (MidiServiceException e) {
            onMidiError(e.getMessage());
        }
        String ip = midiMacroSharedPrefs.getTouchOSCBridgeIp();
        midiModeTv.setText(mode);
        switch (mode) {
            case "Midi over USB":
                midiService = new MidiOverUsb();
                midiIPTv.setVisibility(View.GONE);
                break;
            case "Midi over TouchOSC Bridge":
                midiService = new MidiOverTouchOSCBridge();
                midiIPTv.setText(ip);
                midiIPTv.setVisibility(View.VISIBLE);
                break;
            case "Midi over MIDIMACRO":
                midiService = new MidiOverMidiMacro();
                midiIPTv.setText(ip);
                midiIPTv.setVisibility(View.VISIBLE);
                break;
            case "MQTT":
                String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                ip = midiMacroSharedPrefs.getMQTTIp();
                midiService = new MidiOverMQTT(new Mosquitto(ip, "MIDIMACRO_" + android_id));
                midiIPTv.setText(ip);
                midiIPTv.setVisibility(View.VISIBLE);
                break;
        }
        if (midiService == null)
            return;
        midiService.setMidiServiceListenerList(this);
        try {
            midiService.connect();
        } catch (MidiServiceException e) {
            e.printStackTrace();
        }
        if (midiService instanceof MidiOverMQTT)
            ((MidiOverMQTT) midiService).showLed(midiMacroSharedPrefs.isLed());
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_macro) {
            setFragment(new MacroFragment());
        } else if (id == R.id.nav_mixer) {
            setFragment(new MixerFragment());
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Calls #setFragment(Fragment) with the {@link MixerFragment}
     */
    private void setFirstFragment() {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_mixer).setChecked(true);
        setFragment(new MixerFragment());
    }

    /**
     * Setting Fragments
     *
     * @param fragment is the fragment that will be set to the navigation drawer
     */
    protected void setFragment(MidiMacroFragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        }
        currentFragment = null;
        currentFragment = fragment;
        currentFragment.addOnSendMidiMessageListener(this);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    //region MidiDeviceListeners
    @Override
    public void onMidiConnected() {
        Log.i("MAIN-ACT", "onMidiConnected");
    }

    @Override
    public void onMidiDisconnected() {
        Log.i("MAIN-ACT", "onMidiDisconnected");
    }

    @Override
    public void onMidiError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (error) {
                    case MidiService.NOT_CONNECTED_MIDI_OVERUSB:
                    case MidiService.COULD_NOT_CONNECT_IP:
                    case MidiService.NETWORK_NOT_AVAILABLE:
                    case MidiService.IP_NOT_CONFIGURED:
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        notConnect = true;
                        String prev = midiModeTv.getText().toString();
                        prev = prev.replace(" (Not Connected)", "");
                        midiModeTv.setText(String.format("%s (Not Connected)", prev));
                        midiIPTv.setVisibility(View.GONE);
                        break;
                    default:
                        Log.i("MAIN-ACT", "onMidiError: " + error);
                        break;
                }
            }
        });
    }

    @Override
    public void onMidiError(final String error, Throwable e) {
        Log.i("MAIN-ACT", "onMidiError with Trhowable: " + error);
    }

    @Override
    public void onReceiveMidiMessage(MidiMessage midiMessage) {
        if (currentFragment != null)
            currentFragment.onReceiveMidiMessage(midiMessage);
    }

    @Override
    public void onSendMidiMessage(MidiMessage midiMessage) {
        try {
            if (midiService != null)
                midiService.sendMessage(midiMessage);
        } catch (MidiServiceException e) {
            onMidiError(e.getMessage());
        }
    }
    //endregion

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (midiService != null) {
                midiService.disconnect();
                LocalBroadcastManager
                        .getInstance(this)
                        .unregisterReceiver(broadcastReceiver);
            }
            midiService = null;
        } catch (MidiServiceException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMidiService();
        if (midiService != null)
            LocalBroadcastManager
                    .getInstance(this)
                    .registerReceiver(broadcastReceiver, new IntentFilter("be.vanlooverenkoen.midimacro.send_midi_message"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(intent.getSerializableExtra("midimessage").toString());
            if (midiService != null && midiService instanceof MidiOverMQTT)
                try {
                    midiService.sendMessage((MidiMessage) intent.getSerializableExtra("midimessage"));
                } catch (MidiServiceException e) {
                    e.printStackTrace();
                }
        }
    };
}
