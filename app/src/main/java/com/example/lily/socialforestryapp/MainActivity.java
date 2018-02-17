package com.example.lily.socialforestryapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 2;

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
        private String lastPhotoPath;

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

            Button galleryButton = (Button) rootView.findViewById(R.id.buttonGallery);

            if(helper.getSectionNumber() != 1) {
                galleryButton.setVisibility(View.GONE);
            } else {
                galleryButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        // Show only images, no videos or anything else
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        // Always show the chooser (if there are multiple options available)
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                });
            }

            Button button = (Button) rootView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switch(helper.getSectionNumber()) {
                        case 0: ExecuteShoppingButtonFunction(); break;
                        case 1: ExecuteCameraButtonFunction(); break;
                        case 2: ExecuteLoginButtonFunction(); break;
                    }
                }
            });
        }

        void ExecuteShoppingButtonFunction() {
            Toast.makeText(getContext(), "Shopping is my life", Toast.LENGTH_SHORT).show();
        }

        void ExecuteCameraButtonFunction() {
            Toast.makeText(getContext(), "Smile for the camera", Toast.LENGTH_SHORT).show();
            dispatchTakePictureIntent();
        }

        void ExecuteLoginButtonFunction() {
            Toast.makeText(getContext(), "Welcome to my world", Toast.LENGTH_SHORT).show();
        }

        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "com.example.lily.socialforestryapp.fileprovider",
                                photoFile);
                        lastPhotoPath = photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }

            }
        }

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "diseased_plant_" + timeStamp;
            File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
            return image;
        }

        @Override
         public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                galleryAddPic(lastPhotoPath);
                Intent uploadImageIntent = new Intent(getActivity(), UploadPlantImageActivity.class);
                uploadImageIntent.putExtra("path", lastPhotoPath);
                startActivity(uploadImageIntent);
            }

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri uri = data.getData();
                String realPath = getFilePathFromUri(uri);
                Intent uploadImageIntent = new Intent(getActivity(), UploadPlantImageActivity.class);

                uploadImageIntent.putExtra("path", realPath);
                startActivity(uploadImageIntent);
            }
        }

        private String getFilePathFromUri(Uri uri) {
            if (Build.VERSION.SDK_INT < 11) {
                return RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity(), uri);
            }
            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19) {
                return RealPathUtil.getRealPathFromURI_API11to18(getActivity(), uri);
            }
            // SDK > 19 (Android 4.4)
            else {
                return RealPathUtil.getRealPathFromURI_API19(getActivity(), uri);
            }
        }

        private void galleryAddPic(String path) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(path);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
        }

    }

    public static class FragmentHelper {

        private int _sectionNumber;

        public FragmentHelper(int sectionNumber) {
            _sectionNumber = sectionNumber;
        }

        public int getSectionNumber() {
            return _sectionNumber;
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
