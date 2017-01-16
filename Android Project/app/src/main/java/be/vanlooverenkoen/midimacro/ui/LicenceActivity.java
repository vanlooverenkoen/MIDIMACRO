package be.vanlooverenkoen.midimacro.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.adapter.LicenceAdapter;
import be.vanlooverenkoen.midimacro.model.Licence;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity to show all Licences used in the apps
 *
 * @author Koen Van Looveren
 * @since 1.0
 */
public class LicenceActivity extends MidiMacroActivity {
    //region Layout Variables
    @BindView(R.id.licence_rv)
    RecyclerView licenseRecyclerView;
    //endregion

    //region Variables
    private List<Licence> licences;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = new Bundle();
        savedInstanceState.putBoolean("back_btn", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);
        ButterKnife.bind(this);
        addLicenses();
    }

    //region Methods
    private void addLicenses() {
        licences = new ArrayList<>();
        licences.add(new Licence("Butterknife", "Copyright 2013 Jake Wharton\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.", "https://github.com/JakeWharton/butterknife"));
        licences.add(new Licence("Material Dialogs", "The MIT License (MIT)\n" +
                "\n" +
                "Copyright (c) 2014-2016 Aidan Michael Follestad\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                "of this software and associated documentation files (the \"Software\"), to deal\n" +
                "in the Software without restriction, including without limitation the rights\n" +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                "copies of the Software, and to permit persons to whom the Software is\n" +
                "furnished to do so, subject to the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be included in all\n" +
                "copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                "SOFTWARE.", "https://github.com/afollestad/material-dialogs"));
        //licences.add(new Licence( , , ));
        setAdapter();
    }

    private void setAdapter() {
        licenseRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LicenceAdapter licenceAdapter = new LicenceAdapter(this, licences);
        licenseRecyclerView.setAdapter(licenceAdapter);
    }
    //endregion
}