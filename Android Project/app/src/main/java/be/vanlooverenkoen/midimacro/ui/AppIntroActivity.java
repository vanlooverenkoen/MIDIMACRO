package be.vanlooverenkoen.midimacro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.sharedprefs.SharedPrefsGeneral;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import butterknife.OnTouch;

public class AppIntroActivity extends AppCompatActivity {

    private static final int AMOUNT_OF_FRAGMENTS = 4;
    private List<View> indicators;
    private int page;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.intro_btn_next)
    ImageButton nextBtn;
    @BindView(R.id.intro_btn_previous)
    ImageButton previousBtn;
    @BindView(R.id.intro_btn_finish)
    Button finishBtn;
    @BindView(R.id.intro_btn_skip)
    Button skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        ButterKnife.bind(this);
        indicators = new ArrayList<>();
        indicators.add(findViewById(R.id.intro_indicator_0));
        indicators.add(findViewById(R.id.intro_indicator_1));
        indicators.add(findViewById(R.id.intro_indicator_2));
        indicators.add(findViewById(R.id.intro_indicator_3));
        finishBtn = (Button) findViewById(R.id.intro_btn_finish);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    @OnTouch(R.id.viewpager)
    boolean onTouch() {
        return true;
    }

    @OnPageChange(R.id.viewpager)
    void onPageChange(int position) {
        updateIndicators(position);
        nextBtn.setVisibility(position == AMOUNT_OF_FRAGMENTS - 1 ? View.GONE : View.VISIBLE);
        previousBtn.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        finishBtn.setVisibility(position == AMOUNT_OF_FRAGMENTS - 1 ? View.VISIBLE : View.GONE);
        skipBtn.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.intro_btn_finish)
    void onClickFinishBtn() {
        close();
    }

    @OnClick(R.id.intro_btn_skip)
    void onClickSkip() {
        close();
    }

    private void close() {
        SharedPrefsGeneral sharedPrefsGeneral = new SharedPrefsGeneral();
        if (sharedPrefsGeneral.getFirstRun()) {
            sharedPrefsGeneral.saveFirstRun(false);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @OnClick(R.id.intro_btn_next)
    void onClickNext() {
        page++;
        viewPager.setCurrentItem(page);
    }

    @OnClick(R.id.intro_btn_previous)
    void onClickPrevious() {
        page--;
        viewPager.setCurrentItem(page);
    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.size(); i++) {
            if (i == position)
                indicators.get(i).setBackgroundResource(R.drawable.indicator_selected);
            else
                indicators.get(i).setBackgroundResource(R.drawable.indicator_unselected);
        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WelcomeFragment();
                case 1:
                    return new WindowsFragment();
                case 2:
                    return new TouchOSCBridge();
                case 3:
                    return new FinishedFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return AMOUNT_OF_FRAGMENTS;
        }

        /**
         * This method returns the title of the tab according to the position.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Welcome";
                case 1:
                    return "Windows App";
                case 2:
                    return "ToucOSC Bridge";
                case 3:
                    return "Finished";
            }
            return null;
        }
    }

    public static class WelcomeFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.app_intro_welcome_fragment, container, false);
            if (view != null) {

            }
            return view;
        }
    }

    public static class WindowsFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.app_intro_windows_fragment, container, false);
            if (view != null) {

            }
            return view;
        }
    }

    public static class TouchOSCBridge extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.app_intro_touchoscbridge_fragment, container, false);
            if (view != null) {

            }
            return view;
        }
    }

    public static class FinishedFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.app_intro_finished_fragment, container, false);
            if (view != null) {

            }
            return view;
        }
    }
}
