package be.vanlooverenkoen.midimacro.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.vanlooverenkoen.midimacro.R;

/**
 * Created by Koen on 2/01/2017.
 */

public class KnobMixerFragment {
    private static final int AMOUNT_OF_FRAGMENTS = 3;
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentManager fragmentManager;

    KnobMixerFragment(View view, FragmentManager fragmentManager) {
        this.view = view;
        this.fragmentManager = fragmentManager;
    }

    void init() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setOffscreenPageLimit(AMOUNT_OF_FRAGMENTS);
        viewPager.setAdapter(new MyAdapter(fragmentManager));
        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        //endregion
    }

    //region Fragment Adapter
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
                    return new DeckAFragment();
                case 1:
                    return new MixerFragment();
                case 2:
                    return new DeckBFragment();
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
                    return "DECK A";
                case 1:
                    return "MIXER";
                case 2:
                    return "DECK B";
            }
            return null;
        }
    }
    //endregion

    public static class DeckAFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_mixer_knob_deck_a, container, false);
        }
    }

    public static class MixerFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_mixer_knob_mixer, container, false);
        }
    }

    public static class DeckBFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_mixer_knob_deck_b, container, false);
        }
    }
}