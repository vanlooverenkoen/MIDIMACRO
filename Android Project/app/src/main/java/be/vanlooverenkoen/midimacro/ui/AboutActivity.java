package be.vanlooverenkoen.midimacro.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import be.vanlooverenkoen.midimacro.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends MidiMacroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = new Bundle();
        savedInstanceState.putBoolean("back_btn", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.getting_started)
    void onClickGettingStarted() {
        Intent intent = new Intent(this, AppIntroActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.send_feedback)
    void onClickSendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"vanlooverenkoen.dev@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Bug/Feature");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    @OnClick(R.id.github)
    void onClickGitHub() {
        int color = ContextCompat.getColor(this, R.color.colorPrimary);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(color);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse("https://github.com/vanlooverenkoen"));
    }


    @OnClick(R.id.licences)
    void onClickLicenses() {
        Intent intent = new Intent(this, LicenceActivity.class);
        startActivity(intent);
    }
}
