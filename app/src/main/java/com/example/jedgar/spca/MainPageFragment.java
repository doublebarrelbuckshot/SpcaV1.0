package com.example.jedgar.spca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class MainPageFragment extends Fragment implements View.OnClickListener, GetActionBarTitle, DownloadAdoptableSearch.AsyncTaskDelegate {

    ProgressBar progressBar;
    TextView progressText;
    DBHelper dbh = null;
    SQLiteDatabase db;
    static SearchCriteria sc;

    ImageButton catButton;
    ImageButton dogButton;
    ImageButton rabbitButton;
    ImageButton otherButton;
    Button searchButton;
    TextView ageText;
    CheckBox femaleCheckBox;
    CheckBox maleCheckBox;
    RangeSeekBar<Integer> seekBar;
    FragmentManager fragmentManager;
    boolean loaded = false;
    private boolean firstTime = true;
    MenuItem refreshMI = null;
    ProgressBar refreshProgressBar;

    String newsImage;
    String newsText;
    String newsHeadline;


    int downloadMode;
    static final int MODE_DEFAULT = 0;
    static final int MODE_ACTION_BAR = 1;


    private ImageView important_message;


    @Override
    public int getActionBarTitleId() {
        return R.string.mainPageTitle;
    }

    public interface OnSearchListener {
        public void doSearch(SearchCriteria sc);
    }


    //implemented onClick on each button when added to the inflater/// this one keep it empty else
    //you cant have multiple buttons selected at the same time
    @Override
    public void onClick(View v) {
        sc.saveSearchCriteria(db);
        ((MainActivity) MainPageFragment.this.getActivity()).doSearch(sc);
    }

    public static MainPageFragment newInstance() {

        MainPageFragment fragment = new MainPageFragment();
        return fragment;
    }


    public MainPageFragment() {
        downloadMode = MODE_DEFAULT;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_page_fragment, container, false);

       /*
       Load news image and information
        */
        important_message = (ImageView) rootView.findViewById(R.id.important_message);
        newsText = getResources().getString(R.string.hardcodedNews);
        newsHeadline =getResources().getString(R.string.hardcodeNewsHeadline);
        new DownloadNews().execute();

        //Log.d("MainPageFrag", "In onCreateView");
        if (!loaded) {
            dbh = DBHelper.getInstance(getActivity());
            db = dbh.getWritableDatabase();
            sc = new SearchCriteria(db);
            loaded = true;
        }

        setHasOptionsMenu(true);





        important_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MainPageFragment.this.getActivity()).doNews(newsHeadline, newsText);
            }
        });


        ageText = (TextView) rootView.findViewById(R.id.ageTV);

        catButton = (ImageButton) rootView.findViewById(R.id.cat_button);
        if ((sc.species & SearchCriteria.SPECIES_CAT) == SearchCriteria.SPECIES_CAT)
            catButton.setSelected(true);
        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchCats();
                if (!v.findViewById(R.id.cat_button).isSelected()) {
                    v.findViewById(R.id.cat_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "cat selected!", Toast.LENGTH_SHORT).show();
                } else {
                    v.findViewById(R.id.cat_button).setSelected(false);
                }
            }
        });

        dogButton = (ImageButton) rootView.findViewById(R.id.dog_button);
        if ((sc.species & SearchCriteria.SPECIES_DOG) == SearchCriteria.SPECIES_DOG)
            dogButton.setSelected(true);
        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchDogs();
                if (!v.findViewById(R.id.dog_button).isSelected()) {
                    v.findViewById(R.id.dog_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "dog selected!", Toast.LENGTH_SHORT).show();
                } else {
                    v.findViewById(R.id.dog_button).setSelected(false);
                }

            }
        });

        rabbitButton = (ImageButton) rootView.findViewById(R.id.rabbit_button);
        if ((sc.species & SearchCriteria.SPECIES_RABBIT) == SearchCriteria.SPECIES_RABBIT)
            rabbitButton.setSelected(true);
        rabbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchRabbits();
                if (!v.findViewById(R.id.rabbit_button).isSelected()) {
                    v.findViewById(R.id.rabbit_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "rabbit selected!", Toast.LENGTH_SHORT).show();
                } else {
                    v.findViewById(R.id.rabbit_button).setSelected(false);
                }
            }
        });

        otherButton = (ImageButton) rootView.findViewById(R.id.other_button);
        if ((sc.species & SearchCriteria.SPECIES_SMALLFURRY) == SearchCriteria.SPECIES_SMALLFURRY)
            otherButton.setSelected(true);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchSmallFurry();
                if (!v.findViewById(R.id.other_button).isSelected()) {
                    v.findViewById(R.id.other_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "others selected!", Toast.LENGTH_SHORT).show();
                } else {
                    v.findViewById(R.id.other_button).setSelected(false);
                }
            }
        });

        searchButton = (Button) rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

        // create RangeSeekBar as Integer range between 20 and 75
        int max = 16;
        seekBar = new RangeSeekBar<Integer>(0, max, MainPageFragment.this.getActivity());
        if (sc.ageMax == 0)
            seekBar.setSelectedMaxValue(max);
        else
            seekBar.setSelectedMaxValue(sc.ageMax/12);
        seekBar.setSelectedMinValue(sc.ageMin/12);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                //Log.i("rangeSeek", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                sc.setAgeMin(minValue*12);
                if (maxValue == seekBar.getAbsoluteMaxValue()) {
                    ageText.setText(getResources().getString(R.string.age_text_view) +
                            getResources().getString(R.string.rsbAgeFrom) + minValue +
                            getResources().getString(R.string.rsbIllimited));
                    sc.setAgeMax(0);
                } else {
                    ageText.setText(getResources().getString(R.string.age_text_view) +
                            getResources().getString(R.string.rsbAgeFrom) + minValue +
                            getResources().getString(R.string.rsbAgeTo) + maxValue +
                            getResources().getString(R.string.rsbAgeYears));
                    sc.setAgeMax(maxValue*12);
                }
            }
        });
//https://github.com/yahoo/android-range-seek-bar
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.seekbar_placeholder);
        layout.addView(seekBar);

        maleCheckBox = (CheckBox) rootView.findViewById(R.id.male_checkBox);
        if ((sc.sex & SearchCriteria.SEX_MALE) == SearchCriteria.SEX_MALE)
            maleCheckBox.setChecked(true);
        else
            maleCheckBox.setChecked(false);
        maleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchMales();
            }
        });

        femaleCheckBox = (CheckBox) rootView.findViewById(R.id.female_checkBox);
        if ((sc.sex & SearchCriteria.SEX_FEMALE) == SearchCriteria.SEX_FEMALE) {
            femaleCheckBox.setChecked(true);
        } else {
            femaleCheckBox.setChecked(false);
        }
        femaleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchFemales();
            }
        });

        ageText.setText(getResources().getString(R.string.age_text_view) +
                getResources().getString(R.string.rsbAgeFrom) + seekBar.getSelectedMinValue() +
                getResources().getString(R.string.rsbAgeTo) + seekBar.getSelectedMaxValue() +
                getResources().getString(R.string.rsbAgeYears));

        progressBar = (ProgressBar) rootView.findViewById(R.id.webProgressBar);
        progressBar.setVisibility(View.GONE);
        progressText = (TextView) rootView.findViewById(R.id.progressText);
        progressText.setVisibility(View.GONE);

        getData(false);

        return rootView;
    }

    public void asyncTaskFinished(DownloadAdoptableSearch.DownloadAdoptableSearchResponse response) {

        //Log.d("MAIN", "In asyncTaskFinished");

        /*
        if (downloadMode == MODE_ACTION_BAR) {
            refreshProgressBar.setVisibility(View.INVISIBLE);
            refreshMI.setVisible(true);
        }
        else {*/
        progressBar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        //}

        FragmentActivity activity = getActivity();
        if (activity != null) {

            Log.d("asyncTaskFinished", "AScode:" + response.adoptableSearchErrorCode + " ADerrors:" + response.adoptableDetailsErrors + " postJobError:" + response.postJobError);
            if (response.adoptableSearchErrorCode != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.adoptableSearchError)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getData(true);
                            }
                        })
                        .setNegativeButton(R.string.offline, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbh.setSessionModeOffLine(db);
                            }
                        });
                builder.create();
                builder.show();
                ((MainActivity) getActivity()).setSystemStatus(1);
                return;
            }

            if (response.adoptableDetailsErrors > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.adoptableDetailsErrors)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getData(true);
                            }
                        })
                        .setNegativeButton(R.string.offline, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbh.setSessionModeOffLine(db);
                            }
                        });
                builder.create();
                builder.show();
                ((MainActivity) getActivity()).setSystemStatus(1);
                return;
            }

            if (response.postJobError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.adoptableSearchPostJobError)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getData(true);
                            }
                        })
                        .setNegativeButton(R.string.offline, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbh.setSessionModeOffLine(db);
                            }
                        });
                builder.create();
                builder.show();
                ((MainActivity) getActivity()).setSystemStatus(1);
                return;
            }

            downloadMode = MODE_DEFAULT;
            // unset out of sync state.
            ((MainActivity) getActivity()).setSystemStatus(0);

            dbh.setSessionModeOffLine(db);
        }
    }

    public void asyncTaskProgressUpdate(Integer... values) {
        //Log.d("MAIN", "In asyncTaskProgressUpdate");
        Integer progress = values[0];
        /*
        if (downloadMode == MODE_ACTION_BAR) {
            switch (progress) {
                case 0:
                    refreshMI.setVisible(false);
                    refreshProgressBar.setVisibility(View.VISIBLE);
                    //invalidateOptionsMenu();
                    //refreshProgressBar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
        else {*/
        switch (progress) {
            case 0:
                searchButton.setVisibility(View.GONE);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.incrementProgressBy(1);
                progressText.setVisibility(View.VISIBLE);
                break;
            case 1:
                progressBar.incrementProgressBy(5);
                progressBar.setMax(values[1] + 6);
                break;
            case 2:
                progressBar.incrementProgressBy(1);
                break;
            default:
                progressBar.setProgress(6 + progress);
                break;
        }
        //}
    }

    public void getData(boolean refresh) {

        /*
        if (dbh == null) {
            dbh = DBHelper.getInstance(getActivity());
            db = dbh.getWritableDatabase();
        }
        */

        if (dbh.appFirstTime(db)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.InitialDownloadMsg), Toast.LENGTH_SHORT).show();
            try {
                new DownloadAdoptableSearch(getActivity(), this, false).execute();
            } catch (Exception e) {
                Log.d("DownloadAdoptableSearch", "Failure" + e.getMessage());
                return;
            }
            dbh.appFirstTimeFalse(db);
        } else {
            dbh.setSessionModeOnLineIfStart(db);
            Cursor prefs = dbh.getPreferences(db);
            prefs.moveToFirst();
            if (refresh || prefs.getString(DBHelper.C_PREFERENCES_SESSION_MODE).equals(DBHelper.T_PREFERENCES_SESSION_MODE_ONLINE)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.SynchMsg), Toast.LENGTH_SHORT).show();
                Cursor animalList = dbh.getAnimalListOrdered(db, dbh.T_ANIMAL_ID + " asc ");
                try {
                    Log.d("DownloadAdoptableSearch", "About to call trigger AdopdatableSearch in update mode.");
                    new DownloadAdoptableSearch(getActivity(), animalList, this).execute();
                } catch (Exception e) {
                    Log.d("DownloadAdoptableSearch", "Failure" + e.getMessage());
                    return;
                }
            }
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        //Log.d("MainPageFrag", "In onPrepareOptionsMenu");

        refreshMI = menu.findItem(R.id.refresh);
        if (refreshMI != null) {
            refreshMI.setVisible(true);
            refreshMI.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    refreshMI.setVisible(false);
                    downloadMode = MODE_ACTION_BAR;
                    getData(true);
                    return false;
                }
            });

            //refreshProgressBar = (ProgressBar)menu.findItem(R.id.menuProgressBar);
        }


        //menu.findItem(R.id.refresh).setVisible(false);

        //((MainActivity)getActivity()).displaySystemStatus(this, menu);

        return;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Log.d("MainPageFrag", "In onAttach()");

        ((MainActivity) activity).onSectionAttached(1);
    }


/*
    public class ActionDownloadDelegate implements DownloadAdoptableSearch.AsyncTaskDelegate {

        public ActionDownloadDelegate() {

        }
        @Override
        public void asyncTaskFinished(DownloadAdoptableSearch.DownloadAdoptableSearchResponse response) {
            //mProgressBar.setVisibility(View.INVISIBLE);
            Log.d("MAIN", "In asyncTaskFinished");
            refreshMI.setVisible(true);
            refreshProgressBar.setVisibility(View.GONE);
            //invalidateOptionsMenu();
        }

        @Override
        public void asyncTaskProgressUpdate(Integer... values) {
            Log.d("MAIN", "In asyncTaskProgressUpdate");
            Integer progress = values[0];
            switch (progress) {
                case 0:
                    refreshMI.setVisible(false);
                    refreshProgressBar.setVisibility(View.VISIBLE);
                    //invalidateOptionsMenu();
                    //refreshProgressBar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    }*/





    public class DownloadNews extends AsyncTask<Void, Void, NewsWebAPI> {
        @Override
        protected NewsWebAPI doInBackground(Void... params) {

            Context activity = getActivity();
            if (activity == null)
                return null;

            NewsWebAPI nwapi = new NewsWebAPI(activity.getApplicationContext());
            return nwapi;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(NewsWebAPI nwapi) {
            Context activity = getActivity();
            if (nwapi == null || activity == null)
                return;

            if (nwapi.errorCode == 0) {
                newsText = nwapi.newsText;
                newsHeadline = nwapi.newsHeadline;
                Picasso.with(getActivity().getApplicationContext()).load(nwapi.newsImage).into(important_message);
            }
            else{
                Picasso.with(getActivity().getApplicationContext()).load(R.drawable.makefurhistory).into(important_message);
            }
        }
    }

    public interface OnNewsListener {
        public void doNews(String newsHeadline, String news);
    }
}
