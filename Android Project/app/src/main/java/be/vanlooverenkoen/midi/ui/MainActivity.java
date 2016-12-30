package be.vanlooverenkoen.midi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import be.vanlooverenkoen.midi.R;
import be.vanlooverenkoen.midi.model.MidiChannel;
import be.vanlooverenkoen.midi.model.MidiMessage;
import be.vanlooverenkoen.midi.model.MidiValue;
import be.vanlooverenkoen.midi.service.MQTTListener;
import be.vanlooverenkoen.midi.service.Midi;
import be.vanlooverenkoen.midi.adapter.midi.MidiOverMQTT;
import be.vanlooverenkoen.midi.adapter.mqtt.Mosquitto;
import be.vanlooverenkoen.midi.model.MidiControl;
import be.vanlooverenkoen.midi.service.sharedprefs.SettingsSharedPreferencesEditor;
import be.vanlooverenkoen.midi.ui.customview.MixerSliderVertical;

public class MainActivity extends AppCompatActivity implements MQTTListener {
    /*
    private MidiManager mMidiManager;
    private MidiInputPort inputPort;
    private static final int exclusive = 0;
    */

    private List<Button> buttons;
    private List<MixerSliderVertical> sliders;
    private List<View> audio1Level;
    private List<View> audio2Level;
    private LinearLayout failedLayout;
    private RelativeLayout buttonLayout;
    private LinearLayout sliderLayout;
    private Button checkSettingsBtn;
    private Midi midi;

    private SettingsSharedPreferencesEditor settingsSharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsSharedPreferencesEditor = new SettingsSharedPreferencesEditor(this);
        defineViews();
        showMidiBtn(false);
        setListeners();
        setMidiDevices();
        setServerOptions();
    }

    private void defineViews() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        audio1Level = new ArrayList<>();
        audio2Level = new ArrayList<>();

        checkSettingsBtn = (Button) findViewById(R.id.check_btn);
        buttons.add((Button) findViewById(R.id.midi_btn));
        buttons.add((Button) findViewById(R.id.midi_2_btn));
        buttons.add((Button) findViewById(R.id.midi_3_btn));
        buttons.add((Button) findViewById(R.id.midi_4_btn));
        buttons.add((Button) findViewById(R.id.midi_5_btn));
        buttons.add((Button) findViewById(R.id.midi_6_btn));
        buttons.add((Button) findViewById(R.id.midi_7_btn));
        buttons.add((Button) findViewById(R.id.midi_8_btn));
        sliders.add((MixerSliderVertical) findViewById(R.id.seekBar1));
        sliders.add((MixerSliderVertical) findViewById(R.id.seekBar2));
        failedLayout = (LinearLayout) findViewById(R.id.fail_layout);
        buttonLayout = (RelativeLayout) findViewById(R.id.button_layout);
        sliderLayout = (LinearLayout) findViewById(R.id.slider_layout);
        audio1Level.add(findViewById(R.id.level1_level1));
        audio1Level.add(findViewById(R.id.level1_level2));
        audio1Level.add(findViewById(R.id.level1_level3));
        audio1Level.add(findViewById(R.id.level1_level4));
        audio1Level.add(findViewById(R.id.level1_level5));
        audio1Level.add(findViewById(R.id.level1_level6));
        audio1Level.add(findViewById(R.id.level1_level7));
        audio2Level.add(findViewById(R.id.level2_level1));
        audio2Level.add(findViewById(R.id.level2_level2));
        audio2Level.add(findViewById(R.id.level2_level3));
        audio2Level.add(findViewById(R.id.level2_level4));
        audio2Level.add(findViewById(R.id.level2_level5));
        audio2Level.add(findViewById(R.id.level2_level6));
        audio2Level.add(findViewById(R.id.level2_level7));
    }

    private void setServerOptions() {
        boolean firstRun = settingsSharedPreferencesEditor.isFirstRun();
        if (firstRun) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            settingsSharedPreferencesEditor.saveFirstRun(false);
        } else {
            String server = settingsSharedPreferencesEditor.getServer();
            try {
                String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                Mosquitto mosquitto = new Mosquitto(server, "MIDIMACRO_" + android_id);
                mosquitto.setMqttListenersSoftware(this);
                mosquitto.setMqttListenerHardware(this);
                midi = new MidiOverMQTT(mosquitto);
                mosquitto.connect();
                showMidiBtn(true);
            } catch (Exception e) {
                e.printStackTrace();
                showMidiBtn(false);
            }
        }
    }

    private void setMidiDevices() {
        /*
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            final MidiManager m = (MidiManager) getSystemService(Context.MIDI_SERVICE);
            MidiDeviceInfo[] infos = m.getDevices();
            m.registerDeviceCallback(new MidiManager.DeviceCallback() {
                public void onDeviceAdded(final MidiDeviceInfo info) {
                    System.out.println("Added");
                    m.openDevice(info, new MidiManager.OnDeviceOpenedListener() {
                                @Override
                                public void onDeviceOpened(MidiDevice device) {
                                    if (device == null) {
                                        Log.e("TEST", "could not open device " + info);
                                    } else {
                                        inputPort = device.openInputPort(exclusive);
                                        if (inputPort == null) {
                                            System.out.println("inputport not correctly opened");
                                        } else {
                                            showMidiBtn(true);
                                        }
                                    }
                                }
                            }, new Handler(Looper.getMainLooper())
                    );
                }

                public void onDeviceRemoved(MidiDeviceInfo info) {
                    System.out.println("Removed");
                    inputPort = null;
                    showMidiBtn(false);
                }
            }, new Handler(Looper.getMainLooper()));

            for (final MidiDeviceInfo info : infos) {
                m.openDevice(info, new MidiManager.OnDeviceOpenedListener() {
                            @Override
                            public void onDeviceOpened(MidiDevice device) {
                                if (device == null) {
                                    Log.e("TEST", "could not open device " + info);
                                } else {
                                    inputPort = device.openInputPort(exclusive);
                                    if (inputPort == null) {
                                        System.out.println("inputport not correctly opened");
                                    } else {
                                        showMidiBtn(true);
                                    }
                                }
                            }
                        }, new Handler(Looper.getMainLooper())
                );
            }
        }
        */
    }

    private void setListeners() {
        checkSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        for (final Button button : buttons) {
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int index = buttons.indexOf(button);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        midi.sendChangeControl(MidiChannel.valueOf("MIDI_CHANNEL_1")
                                , MidiControl.valueOf("MIDI_CONTROL_" + index)
                                , MidiValue.MIDI_VALUE_0);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        midi.sendChangeControl(MidiChannel.valueOf("MIDI_CHANNEL_1")
                                , MidiControl.valueOf("MIDI_CONTROL_" + index)
                                , MidiValue.MIDI_VALUE_127);
                        return true;
                    }
                    return false;
                }
            });
        }

        for (final MixerSliderVertical slider : sliders) {
            final int index = sliders.indexOf(slider);
            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    midi.sendChangeControl(MidiChannel.valueOf("MIDI_CHANNEL_2")
                            , MidiControl.valueOf("MIDI_CONTROL_" + index)
                            , MidiValue.valueOf("MIDI_VALUE_" + progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

    }

    public void showMidiBtn(boolean show) {
        if (show) {
            failedLayout.setVisibility(View.GONE);
            sliderLayout.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);
        } else {
            sliderLayout.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
            failedLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        setServerOptions();
        super.onResume();
    }

    @Override
    public void messageReceived(final MidiMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message.getMidiChannel() == MidiChannel.MIDI_CHANNEL_2) {
                    //SLIDERS
                    if (message.getMidiControl() == MidiControl.MIDI_CONTROL_0)
                        sliders.get(0).setProgressWithoutCallback(message.getMidiValue().getValue());
                    else if (message.getMidiControl() == MidiControl.MIDI_CONTROL_1)
                        sliders.get(1).setProgressWithoutCallback(message.getMidiValue().getValue());
                }
                if (message.getMidiChannel() == MidiChannel.MIDI_CHANNEL_3) {
                    if (message.getMidiControl() == MidiControl.MIDI_CONTROL_0) {
                        int index = message.getMidiControl().getValue();
                        if (index < audio1Level.size()) {
                            View view = audio1Level.get(index);
                            setViewBackground(audio1Level, message);
                        }
                    } else if (message.getMidiControl() == MidiControl.MIDI_CONTROL_1) {
                        if (message.getMidiControl() == MidiControl.MIDI_CONTROL_1) {
                            setViewBackground(audio2Level, message);
                        }
                    }
                }
            }
        });
    }

    private void setViewBackground(List<View> audioLevel, MidiMessage message) {
        if (message.getMidiValue().getValue() >= 108)
            audioLevel.get(6).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_red_solid));
        else
            audioLevel.get(6).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_red));
        if (message.getMidiValue().getValue() >= 90)
            audioLevel.get(5).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_orange_solid));
        else
            audioLevel.get(5).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_orange));
        if (message.getMidiValue().getValue() >= 72)
            audioLevel.get(4).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_orange_solid));
        else
            audioLevel.get(4).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_orange));
        if (message.getMidiValue().getValue() >= 54)
            audioLevel.get(3).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_yellow_solid));
        else
            audioLevel.get(3).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_yellow));
        if (message.getMidiValue().getValue() >= 36)
            audioLevel.get(2).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_yellow_solid));
        else
            audioLevel.get(2).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_yellow));
        if (message.getMidiValue().getValue() >= 18)
            audioLevel.get(1).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_yellow_solid));
        else
            audioLevel.get(1).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_yellow));
        if (message.getMidiValue().getValue() > 0)
            audioLevel.get(0).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_yellow_solid));
        else
            audioLevel.get(0).setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rectangle_yellow));

    }
}
