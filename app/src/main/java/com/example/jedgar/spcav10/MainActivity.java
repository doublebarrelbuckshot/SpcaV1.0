package com.example.jedgar.spcav10;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.app.PendingIntent.getActivity;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainPageFragment.OnSearchListener,
        BrowsePageFragment.OnDetailsListener, DetailsPageFragment.OnAdoptListener{

    FragmentManager fragmentManager;
    MenuItem mRemoveAllMI;

    public static boolean firstRun = true;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbh = DBHelper.getInstance(getApplicationContext());
        dbh.setSessionModeOnLine(dbh.getWritableDatabase());

        fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("BACKSTACK", "BACKSTACK ACTIVATED");
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                if (backCount == 0)
                {
                    finish();
                }
                else
                {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                    mTitle = getString(((GetActionBarTitle) fragment).getActionBarTitleId());
                    restoreActionBar();
                }
            }
        });
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        fragmentManager = getSupportFragmentManager();
        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MainPageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            mTitle = getString(R.string.mainPageTitle);

        } else if (position == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, InfoPageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            mTitle = getString(R.string.infoTitle);

        } else if (position == 2) {
            getIntent().putExtra("sender", DBHelper.CURSOR_NAME_FAVORITE_ANIMALS);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, BrowsePageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            mTitle = getString(R.string.favoritesTitle);


        } else if (position == 3) {
            getIntent().putExtra("sender", DBHelper.CURSOR_NAME_NEW_ANIMALS);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, BrowsePageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            mTitle = getString(R.string.newTitle);

        } else if (position == 4) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, NotificationPageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            mTitle = getString(R.string.notificationsTitle);

        } else if (position == 5) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DetailsPageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
       // restoreActionBar();
         /*else if (position == 6) {
            getIntent().putExtra("sender", "Browse");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, BrowsePageFragment.newInstance())
                    .commit();
        }*/
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle =  getString(R.string.mainPageTitle);// getString(R.string.title_section1); //mainpage
                break;
            case 2:
                mTitle = getString(R.string.infoTitle);//getString(R.string.title_section2); //info
                break;
            case 3:
                mTitle = getString(R.string.favoritesTitle);//getString(R.string.title_section3); //favorites
                break;
            case 4:
                mTitle = getString(R.string.searchResultsTitle);//getString(R.string.title_section4); //search
                break;
            case 5:
                mTitle = getString(R.string.notificationsTitle);//getString(R.string.title_section5); //notificationPage
                break;
            case 6:
                mTitle = getString(R.string.newTitle);//getString(R.string.title_section6); //searchPage
                break;
            case 7:
                mTitle = getString(R.string.detailsTitle);//getString(R.string.title_section7);
                break;
        }
        Log.d("SECTION ATTACHED", "" + number);
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


//    public void browse(View view){
//        Intent intent = new Intent(this, Browsing.class);
//        startActivity(intent);
//
//
//    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            mRemoveAllMI = menu.findItem(R.id.removeAllMI);
            mRemoveAllMI.setVisible(false);

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void doDetails(String cursorName, int pos) {
        Log.d("main", "SHOWING DETAILS FRAGMENT");
        Fragment frag = DetailsPageFragment.newInstance(pos);

        Bundle args = new Bundle();
        args.putString("cursorName", cursorName);
        frag.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack(null)
                .commit();
        mTitle = getString(R.string.detailsTitle);//getString(R.string.title_section5); //notificationPage
        restoreActionBar();
    }



    @Override
    public void doSearch(SearchCriteria sc) {
        Log.d("main", "je cherche");
        Fragment frag = BrowsePageFragment.newInstance();
        getIntent().putExtra("SearchCriteria", sc);
        getIntent().putExtra("sender", DBHelper.CURSOR_NAME_SEARCH_ANIMALS);
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack(null)
                .commit();
        mTitle = getString(R.string.searchResultsTitle);//getString(R.string.title_section5); //notificationPage
        restoreActionBar();

    }

    @Override
    public void doAdopt(String animalID) {
        Fragment frag = InfoPageFragment.newInstance(animalID);
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack(null)
                .commit();
        mTitle = getString(R.string.infoTitle);//getString(R.string.title_section5); //notificationPage
        restoreActionBar();
    }




//    @Override
//    public void goDetails() {
//        Log.d("Main", "Take me to the Details nigga");
//        Fragment frag2 = DetailsPageFragment.newInstance();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, frag2)
//                .addToBackStack(null)
//                .commit();
//    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

