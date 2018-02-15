package com.example.lily.socialforestryapp;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            FragmentHelper helper = new FragmentHelper(getArguments().getInt(ARG_SECTION_NUMBER));
            setFragmentView(rootView, helper);
            return rootView;
        }

        public void setFragmentView(View rootView, final FragmentHelper helper)
        {
            TextView titleView = (TextView) rootView.findViewById(R.id.section_title);
            titleView.setText(helper.getTitle());

            TextView descriptionView = (TextView) rootView.findViewById(R.id.section_description);
            descriptionView.setText(helper.getDescription());

            TextView buttonView = (TextView) rootView.findViewById(R.id.button);
            buttonView.setText(helper.getButtonText());

            Button button = (Button) rootView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switch(helper.getButtonText()) {
                        case "Shopping Cart": ExecuteShoppingButtonFunction2(); break;
                        case "Camera": ExecuteCameraButtonFunction(); break;
                        case "Person Icon": ExecuteLoginButtonFunction(); break;
                    }
                }
            });
        }

        void ExecuteShoppingButtonFunction2() {
            Toast.makeText(getContext(), "Shopping is my life", Toast.LENGTH_SHORT).show();
        }

        void ExecuteCameraButtonFunction() {
            Toast.makeText(getContext(), "Smile for the camera", Toast.LENGTH_SHORT).show();
        }

        void ExecuteLoginButtonFunction() {
            Toast.makeText(getContext(), "Welcome to my world", Toast.LENGTH_SHORT).show();
        }
    }

    public static class FragmentHelper {

        private int _sectionNumber;

        public FragmentHelper(int sectionNumber) {
            _sectionNumber = sectionNumber;
        }

        public String getTitle() {
            switch (_sectionNumber) {
                case 0:
                    return "Buy a Tree";
                case 1:
                    return "Plant Health";
                case 2:
                    return "Join Us";
            }
            return null;
        }

        public String getDescription() {
            switch (_sectionNumber) {
                case 0:
                    return "Search our store to buy seed or young trees";
                case 1:
                    return "Upload photos of sick plants to help us monitor diseases";
                case 2:
                    return "Create an account with us to access more features";
            }
            return null;
        }

        public String getButtonText() {
            switch (_sectionNumber) {
                case 0:
                    return "Shopping Cart";
                case 1:
                    return "Camera";
                case 2:
                    return "Person Icon";
            }
            return null;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Buy a Tree";
                case 1:
                    return "Plant Health";
                case 2:
                    return "Join Us";
            }
            return null;
        }
    }
}
