package be.vanlooverenkoen.midi.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import be.vanlooverenkoen.midi.R;

public class SettingsActivity extends AppCompatActivity {

    private EditText serverEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        final SharedPreferences sharedPreferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        Button saveBtn = (Button) findViewById(R.id.save_btn);
        serverEt = (EditText) findViewById(R.id.server_et);
        serverEt.setText(sharedPreferences.getString("server", ""));
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("server", serverEt.getText().toString()).apply();
                onBackPressed();
            }
        });
    }

    //region Override Methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            new MaterialDialog.Builder(this)
                    .title("Don't save")
                    .content("Do you want to leave without saving your settings?")
                    .widgetColorRes(R.color.colorPrimary)
                    .backgroundColorRes(R.color.windowBgDark)
                    .positiveText("Yes")
                    .negativeText("No")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            onBackPressed();
                        }
                    })
                    .cancelable(false)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
}
