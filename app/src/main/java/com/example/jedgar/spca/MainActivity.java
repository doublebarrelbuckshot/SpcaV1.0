package com.example.jedgar.spca;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends ActionBarActivity
        implements  NavigationDrawerFragment.NavigationDrawerCallbacks,
                    MainPageFragment.OnSearchListener,
                    BrowsePageFragment.OnDetailsListener,
                    DetailsPageFragment.OnAdoptListener,
                    MainPageFragment.OnNewsListener,
                    SystemStatus {

    FragmentManager fragmentManager;
    MenuItem mRemoveAllMI;
    MenuItem mSystemStatus;
    MenuItem mRefresh = null;
    //static long interval;
    int systemStatusCode = 0;
    int sectionAttached = 0;
    static Activity mainActivity = null;

    static final int SECTION_ID_MAIN          = 1;
    static final int SECTION_ID_INFO          = 2;
    static final int SECTION_ID_FAVORITES     = 3;
    static final int SECTION_ID_SEARCH        = 4;
    static final int SECTION_ID_NOTIFICATIONS = 5;
    static final int SECTION_ID_CONTACT       = 6;
    static final int SECTION_ID_NEWS          = 7;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onNewIntent(Intent intent){ ///Traite le cas ou l'application tourne deja, et on est venu de la notification
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle b = getIntent().getExtras();
        if(b != null){
            if(b.getString("key") != null)
            {//mettre du code ICI

              //  Log.d("FROM NOTIFI xxx", b.getString("key") + "intent already exists");
                /*getIntent().putExtra("sender", DBHelper.CURSOR_NAME_FAVORITE_ANIMALS);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BrowsePageFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                mTitle = getString(R.string.favoritesTitle);*/


                getIntent().putExtra("sender", DBHelper.CURSOR_NAME_NEW_ANIMALS);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BrowsePageFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                mTitle = getString(R.string.newTitle);
            }}
        else{
           // Log.d("NOT FROM NOTIF", "NOT FROM NOTIFICAITON");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                fragmentManager.findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        Bundle b = getIntent().getExtras();
        if(b != null){
        if(b.getString("key") != null)
        { //on est venu du notification, mettre du code ici
          //  Log.d("FROM NOTIFI", b.getString("key"));

           /* getIntent().putExtra("sender", DBHelper.CURSOR_NAME_FAVORITE_ANIMALS);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, BrowsePageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            mTitle = getString(R.string.favoritesTitle);*/

            getIntent().putExtra("sender", DBHelper.CURSOR_NAME_NEW_ANIMALS);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, BrowsePageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            mTitle = getString(R.string.newTitle);


        }}
        else{
          //  Log.d("NOT FROM NOTIF", "NOT FROM NOTIFICAITON");
        }


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
              //  Log.d("BACKSTACK", "BACKSTACK ACTIVATED");
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
                    .replace(R.id.container, ContactPageFragment.newInstance())
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
        sectionAttached = number;
        switch (number) {
            case SECTION_ID_MAIN:
                mTitle =  getString(R.string.mainPageTitle);
                Log.d("MAIN", "onSectionAttached mainPage");
                break;
            case SECTION_ID_INFO:
                mTitle = getString(R.string.infoTitle);
                Log.d("MAIN", "onSectionAttached infoPage");
                break;
            case SECTION_ID_FAVORITES:
                mTitle = getString(R.string.favoritesTitle);
                Log.d("MAIN", "onSectionAttached favoritesPage");
                break;
            case SECTION_ID_SEARCH:
                mTitle = getString(R.string.searchResultsTitle);
                Log.d("MAIN", "onSectionAttached searchResultsPage");
                break;
            case SECTION_ID_NOTIFICATIONS:
                Log.d("MAIN", "onSectionAttached notificationsPage");
                mTitle = getString(R.string.notificationsTitle);
                break;
            case SECTION_ID_CONTACT:
                Log.d("MAIN", "onSectionAttached ???Page");
                mTitle = getString(R.string.contactTitle);
                break;
            case SECTION_ID_NEWS:
                mTitle = getString(R.string.newsTitle);
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
        Log.d("MAIN", "in onCreateOptionsMenu");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            mRemoveAllMI = menu.findItem(R.id.removeAllMI);
            mRemoveAllMI.setVisible(false);

            mSystemStatus = menu.findItem(R.id.statusSyncError);
            mRefresh = menu.findItem(R.id.refresh);
            switch (sectionAttached) {
                case SECTION_ID_MAIN:
                    mRefresh.setVisible(true);
                    break;
                case SECTION_ID_SEARCH:
                case SECTION_ID_INFO:
                case SECTION_ID_FAVORITES:
                case SECTION_ID_NOTIFICATIONS:
                case SECTION_ID_CONTACT:
                default:
                    mRefresh.setVisible(false);
                    break;
            }

            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Log.d("MAIN", "In onPrepareOptionsMenu");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            setSystemStateVisibility(menu);
            mRefresh = menu.findItem(R.id.refresh);
            Log.d("MAIN", "mRefresh.setVisible(false);");

            //mRefresh.setVisible(true);
        }

        return true;
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
        Fragment frag = ContactPageFragment.newInstance(animalID);
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack(null)
                .commit();
        mTitle = getString(R.string.contactTitle);//getString(R.string.title_section5); //notificationPage
        restoreActionBar();
    }

    @Override
    public void doNews(String newsHeadline, String news) {
        Fragment frag = NewsPageFragment.newInstance(newsHeadline, news);
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack(null)
                .commit();
        mTitle = getString(R.string.newsTitle);//getString(R.string.title_section5); //notificationPage
        restoreActionBar();
    }

    @Override
    public void setSystemStatus(int statusCodeParam) {
        Log.d("MAIN", "In setSystemStatus");
        systemStatusCode = statusCodeParam;
        invalidateOptionsMenu();
    }

    private void setSystemStateVisibility(Menu menu) {
        if (mSystemStatus == null) {
            mSystemStatus = menu.findItem(R.id.statusSyncError);
            if (mSystemStatus == null)
                Log.e("MAIN", "mSystemStatus still null");
        }
        if (systemStatusCode == 1) {
            mSystemStatus.setVisible(true);
        } else {
            mSystemStatus.setVisible(false);
        }
    }

    @Override
    public void displaySystemStatus(Fragment fragment, Menu menu) {
        Log.d("MAIN", "In displaySystemStatus.  systemStatusCode = " + systemStatusCode);
        setSystemStateVisibility(menu);
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
            Log.d("MAIN", "In OnAttach.");
            mainActivity = activity;
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}

