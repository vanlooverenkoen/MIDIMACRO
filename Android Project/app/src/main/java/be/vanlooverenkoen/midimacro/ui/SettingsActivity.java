package be.vanlooverenkoen.midimacro.ui;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.sharedprefs.SharedPrefsMidiMode;
import be.vanlooverenkoen.midimacro.sharedprefs.SharedPrefsMixer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingsActivity extends MidiMacroActivity {
    SharedPrefsMixer sharedPrefsMixer;
    SharedPrefsMidiMode sharedPrefsMidiMode;

    @BindView(R.id.macro_layout_tv)
    TextView macroLayoutTv;
    @BindView(R.id.mixer_layout_tv)
    TextView mixerLayoutTv;
    @BindView(R.id.configure_midi_bridge)
    LinearLayout configureTouchOSCBridge;
    @BindView(R.id.midi_touchosc_bridge_ip_tv)
    TextView touchOSCBridgeIpTv;
    @BindView(R.id.configure_mqtt)
    LinearLayout configureMqtt;
    @BindView(R.id.mqtt_ip_tv)
    TextView mqttIpTv;
    @BindView(R.id.mqtt_led)
    RelativeLayout mqttLed;
    @BindView(R.id.mqtt_led_switch)
    SwitchCompat mqttLedSwitch;
    @BindView(R.id.midi_mode_tv)
    TextView midiModeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = new Bundle();
        savedInstanceState.putBoolean("back_btn", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        sharedPrefsMixer = new SharedPrefsMixer(getApplicationContext());
        sharedPrefsMidiMode = new SharedPrefsMidiMode();
        mixerLayoutTv.setText(sharedPrefsMixer.getMixerLayout());
        String ip = sharedPrefsMidiMode.getTouchOSCBridgeIp();
        if (ip == null || ip.isEmpty())
            touchOSCBridgeIpTv.setText(R.string.ip_not_configured);
        else
            touchOSCBridgeIpTv.setText(ip);

        //MQTT
        String mqttIp = sharedPrefsMidiMode.getMQTTIp();
        if (mqttIp == null || mqttIp.isEmpty())
            mqttIpTv.setText(R.string.ip_not_configured);
        else
            mqttIpTv.setText(mqttIp);
        midiModeTv.setText(sharedPrefsMidiMode.getMidiMode());
        mqttLedSwitch.setChecked(sharedPrefsMidiMode.isLed());
        validateTouchOSCBridge();
    }

    @OnClick(R.id.midi_service_setting)
    void onClickMidiService() {
        final List<String> midiModes;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                && getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            midiModes = Arrays.asList(getResources().getStringArray(R.array.midi_modes));
        } else {
            midiModes = Arrays.asList(getResources().getStringArray(R.array.midi_modes_no_over_usb));
        }
        String midiMode = sharedPrefsMidiMode.getMidiMode();
        int index = -1;
        if (midiModes.contains(midiMode)) {
            index = midiModes.indexOf(midiMode);
        }
        new MaterialDialog.Builder(this)
                .title("Midi Mode")
                .items(midiModes)
                .backgroundColorRes(R.color.windowBgDark)
                .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        return false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String text = midiModes.get(dialog.getSelectedIndex());
                        sharedPrefsMidiMode.saveMidiMode(text);
                        midiModeTv.setText(text);
                        validateTouchOSCBridge();
                    }
                })
                .positiveText("Ok")
                .show();
    }

    private void validateTouchOSCBridge() {
        if (sharedPrefsMidiMode.getMidiMode().equals("Midi over TouchOSC Bridge")) {
            configureTouchOSCBridge.setVisibility(View.VISIBLE);
        } else {
            configureTouchOSCBridge.setVisibility(View.GONE);
        }

        if (sharedPrefsMidiMode.getMidiMode().equals("MQTT")) {
            configureMqtt.setVisibility(View.VISIBLE);
            mqttLed.setVisibility(View.VISIBLE);
        } else {
            configureMqtt.setVisibility(View.GONE);
            mqttLed.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.configure_midi_bridge)
    void onClickConfigureMidiMode() {
        new MaterialDialog.Builder(this)
                .title("TouchOSC Bridge IP")
                .content("Type the ip of the computer with TouchOSC Bridge running")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .backgroundColorRes(R.color.windowBgDark)
                .alwaysCallInputCallback()
                .input("TouchOSC Bridge Ip", sharedPrefsMidiMode.getTouchOSCBridgeIp(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Pattern PATTERN = Pattern.compile(
                                "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
                        if (!PATTERN.matcher(input.toString()).matches()) {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        } else {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialog.getInputEditText() != null) {
                            String input = dialog.getInputEditText().getText().toString();
                            sharedPrefsMidiMode.saveTouchOSCBridgeIp(input);
                            touchOSCBridgeIpTv.setText(input);
                        }
                    }
                }).show();
    }

    @OnClick(R.id.configure_mqtt)
    void onClickMQTTIp() {
        new MaterialDialog.Builder(this)
                .title("MQTT IP and Port")
                .content("Type the ip of the MQTT server with port")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .backgroundColorRes(R.color.windowBgDark)
                .alwaysCallInputCallback()
                .input("tcp://ip:port", sharedPrefsMidiMode.getMQTTIp(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialog.getInputEditText() != null) {
                            String input = dialog.getInputEditText().getText().toString();
                            sharedPrefsMidiMode.saveMQTTIp(input);
                            mqttIpTv.setText(input);
                        }
                    }
                }).show();
    }

    @OnClick(R.id.macro_layout_setting)
    void onClickMacroLayout() {

    }

    @OnClick(R.id.mqtt_led)
    void onClickLed() {
        mqttLedSwitch.setChecked(!mqttLedSwitch.isChecked());
    }

    @OnCheckedChanged(R.id.mqtt_led_switch)
    void onCheckChanged(boolean checked) {
        sharedPrefsMidiMode.saveLed(checked);
    }


    @OnClick(R.id.mixer_layout_setting)
    void onClickMixerLayout() {
        final List<String> mixerLayouts = Arrays.asList(getResources().getStringArray(R.array.mixer_layouts));
        String layout = sharedPrefsMixer.getMixerLayout();
        int index;
        if (mixerLayouts.contains(layout))
            index = mixerLayouts.indexOf(layout);
        else
            index = -1;
        new MaterialDialog.Builder(this)
                .title("Mixer layout")
                .items(R.array.mixer_layouts)
                .backgroundColorRes(R.color.windowBgDark)
                .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        return false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialog.getSelectedIndex() != -1) {
                            String layout = mixerLayouts.get(dialog.getSelectedIndex());
                            sharedPrefsMixer.saveMixerLayout(layout);
                            mixerLayoutTv.setText(layout);
                        }
                    }
                })
                .positiveText("Ok")
                .show();
    }
}
