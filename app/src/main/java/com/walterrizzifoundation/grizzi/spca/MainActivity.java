package com.walterrizzifoundation.grizzi.spca;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.walterrizzifoundation.grizzi.spca.R;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


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
    public static boolean firstLoad = true;

    static final int SECTION_ID_MAIN          = 1;
    static final int SECTION_ID_INFO          = 2;
    static final int SECTION_ID_FAVORITES     = 3;
    static final int SECTION_ID_SEARCH        = 4;
    static final int SECTION_ID_NOTIFICATIONS = 5;
    static final int SECTION_ID_CONTACT       = 6;
    static final int SECTION_ID_NEWS          = 7;
    static final int SECTION_ID_FAQ           = 8;
    static final int SECTION_ID_DONATE        = 9;


    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;//ENVIRONMENT_SANDBOX;//PayPalConfiguration.ENVIRONMENT_NO_NETWORK; PayPalConfiguration.

    // note that these credentials will differ between live & sandbox environments.

    static final String sandboxID = "AbfSckYTLi5pS-KXLvRbejFbUHO0oL4EIZ4_wyF8gDwBmDw3wiMTdIoAi15rDPqzV-zvL_ojAMx8yCDs";
    static final String liveID = "AYoNGOVMZGUeur10Oj_4S5eLHq8R24lqvdbicZj6B60lwQhvCZB5pUSeQFzIidXZLN_gUnbVIcQq_G_p";
    private static final String CONFIG_CLIENT_ID = liveID;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);/*
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));*/
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final String TAG = "paymentExample";

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
        Log.d("MAIN", " in onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle b = getIntent().getExtras();

        /*
         *
         * ONLY FOR NOTIFICATIONS
         *
         */
//        if(b != null){
//            if(b.getString("command") != null) {//mettre du code ICI
//                Log.d("FROM NOTIFI xxx", b.getString("command"));
//
//                /*
//                *  ne pas mettre des extra ici.
//                //getIntent().putExtra("sender", "notifs");
//                //getIntent().putExtra("command", b.getString("command"));
//                 */
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, BrowsePageFragment.newInstance(), DBHelper.CURSOR_NAME_NOTIFICATIONS)
//                        .addToBackStack(null)
//                        .commit();
//                mTitle = getString(R.string.newTitle);
//            }
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("MAIN", " in onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                fragmentManager.findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        Bundle b = getIntent().getExtras();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                //  Log.d("BACKSTACK", "BACKSTACK ACTIVATED");
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                if (backCount == 0) {
                    finish();
                } else {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

                    mTitle = getString(((GetActionBarTitle) fragment).getActionBarTitleId());
                    restoreActionBar();

                    //This will update the navigation drawer so that the proper list item is selected when we press back.
                    NavigationDrawerFragment nav = (NavigationDrawerFragment)getSupportFragmentManager()
                            .findFragmentById(R.id.navigation_drawer);

                    if (fragment instanceof GetActionBarTitle) {
                        for (int i = 0; i<nav.navItems.length; i++)
                        {
                            if (nav.navItems[i].equals(mTitle))
                                nav.updateSelected(i);
                        }
                    }
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
                    .replace(R.id.container, BrowsePageFragment.newInstance(), DBHelper.CURSOR_NAME_FAVORITE_ANIMALS)
                    .addToBackStack(null)
                    .commit();
            mTitle = getString(R.string.favoritesTitle);


        } else if (position == 3/*5*/) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ContactPageFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
        else if(position == 4){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FAQFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
        else if(position == 5){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DonateFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }

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
//            case SECTION_ID_NOTIFICATIONS:
//                Log.d("MAIN", "onSectionAttached notificationsPage");
//                mTitle = getString(R.string.notificationsTitle);
//                break;
            case SECTION_ID_CONTACT:
                Log.d("MAIN", "onSectionAttached ???Page");
                mTitle = getString(R.string.contactTitle);
                break;
            case SECTION_ID_NEWS:
                mTitle = getString(R.string.newsTitle);

            case SECTION_ID_FAQ:
                mTitle = getString(R.string.faqTitle);
            case SECTION_ID_DONATE:
                mTitle = getString(R.string.donateTitle);
        }
        Log.d("SECTION ATTACHED", "" + number);
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

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
                //case SECTION_ID_NOTIFICATIONS:
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
        }

        return true;
    }

    @Override
    public void doDetails(String cursorName, int pos) {
        Log.d("main", "SHOWING DETAILS FRAGMENT");
        Fragment frag = DetailsPageFragment.newInstance(pos);

        Log.d("MAIN", "In doDetails with cursorName:" + cursorName);
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
                .replace(R.id.container, frag, DBHelper.CURSOR_NAME_SEARCH_ANIMALS)
                .addToBackStack(null)
                .commit();
        mTitle = getString(R.string.searchResultsTitle);//getString(R.string.title_section5); //notificationPage
        restoreActionBar();
    }

    @Override
    public void doAdopt(String animalID, String animalImage) {
        Fragment frag = ContactPageFragment.newInstance(animalID, animalImage);
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

    public void doDonation(PayPalPayment ppp) {
            /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = ppp;

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        Log.d("paymentid", confirm.getProofOfPayment().getPaymentId());
                        String payID = confirm.getProofOfPayment().getPaymentId();

                        //String paypalurl = "http://www.spca-staging.walterrizzifoundation.com/pdf/payment/" + payID;
                        String paypalurl = "http://www.spca-live.walterrizzifoundation.com/pdf/payment/" + payID;

                        new HttpAsyncTask().execute(paypalurl);

                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(
                                getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }

        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Receipt Sent!", Toast.LENGTH_LONG).show();
            //Log.d("POSTEX", result);
        }
    }
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
           // Log.d("GETTT", url);

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            //Log.d(TAG, "After httpResponse");
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            //Log.d(TAG, "after statusCode, code= " +statusCode);
            // receive response as inputStream

            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
               // Log.d("RESULT", result);
            }
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}


