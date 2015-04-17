package com.example.jedgar.spcav10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
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


public class MainPageFragment extends Fragment implements View.OnClickListener {

    ProgressBar progressBar;
    TextView progressText;
    DBHelper dbh;
    SQLiteDatabase db;
    SPCAWebAPI web;
    ImageView img;
    static private boolean toggle = false;
    int action = 0;
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

    public interface OnSearchListener {
        public void doSearch(SearchCriteria sc);
    }


    //implemented onClick on each button when added to the inflater/// this one keep it empty else
    //you cant have multiple buttons selected at the same time
    @Override
    public void onClick(View v) {
        ((MainActivity)MainPageFragment.this.getActivity()).doSearch(sc);
    }

    public static MainPageFragment newInstance(){

        MainPageFragment fragment = new MainPageFragment();
        sc = new SearchCriteria();
        return fragment;
    }

    public MainPageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_page_fragment, container, false);

        ageText = (TextView) rootView.findViewById(R.id.ageTV);

        catButton = (ImageButton)rootView.findViewById(R.id.cat_button);
        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchCats();
                if(!v.findViewById(R.id.cat_button).isSelected()){
                    v.findViewById(R.id.cat_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "cat selected!", Toast.LENGTH_SHORT).show();
                }
                else{
                    v.findViewById(R.id.cat_button).setSelected(false);
                }
            }
        });

        dogButton = (ImageButton)rootView.findViewById(R.id.dog_button);
        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchDogs();
                if(!v.findViewById(R.id.dog_button).isSelected()){
                    v.findViewById(R.id.dog_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "dog selected!", Toast.LENGTH_SHORT).show();
                }
                else{
                    v.findViewById(R.id.dog_button).setSelected(false);
                }

            }
        });

        rabbitButton = (ImageButton)rootView.findViewById(R.id.rabbit_button);
        rabbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchRabbits();
                if(!v.findViewById(R.id.rabbit_button).isSelected()){
                    v.findViewById(R.id.rabbit_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "rabbit selected!", Toast.LENGTH_SHORT).show();}
                else{
                    v.findViewById(R.id.rabbit_button).setSelected(false);
                }
            }
        });

        otherButton = (ImageButton)rootView.findViewById(R.id.other_button);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchSmallFurry();
                if(!v.findViewById(R.id.other_button).isSelected()){
                    v.findViewById(R.id.other_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "others selected!", Toast.LENGTH_SHORT).show();}
                else{
                    v.findViewById(R.id.other_button).setSelected(false);
                }
            }
        });

        searchButton = (Button)rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

        // create RangeSeekBar as Integer range between 20 and 75
        seekBar = new RangeSeekBar<Integer>(0, 20, MainPageFragment.this.getActivity());
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                Log.i("rangeSeek", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                ageText.setText(getResources().getString(R.string.age_text_view) +
                        getResources().getString(R.string.rsbAgeFrom) + minValue +
                                getResources().getString(R.string.rsbAgeTo)+ maxValue +
                                        getResources().getString(R.string.rsbAgeYears));
                sc.setAgeMin(minValue);
                sc.setAgeMax(maxValue);
            }
        });
//https://github.com/yahoo/android-range-seek-bar
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.seekbar_placeholder);
        layout.addView(seekBar);

        maleCheckBox = (CheckBox)rootView.findViewById(R.id.male_checkBox);
        maleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchMales();
                if(!v.findViewById(R.id.male_checkBox).isSelected()){
                    v.findViewById(R.id.male_checkBox).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "males selected!", Toast.LENGTH_SHORT).show();}
                else{
                    v.findViewById(R.id.male_checkBox).setSelected(false);
                }
            }
        });

        femaleCheckBox = (CheckBox)rootView.findViewById(R.id.female_checkBox);
        femaleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchFemales();
                if(!v.findViewById(R.id.female_checkBox).isSelected()){
                    v.findViewById(R.id.female_checkBox).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "females selected!", Toast.LENGTH_SHORT).show();
                }
                else{
                    v.findViewById(R.id.female_checkBox).setSelected(false);
                }
            }
        });

        ageText.setText(getResources().getString(R.string.age_text_view) +
                getResources().getString(R.string.rsbAgeFrom) + seekBar.getSelectedMinValue() +
                getResources().getString(R.string.rsbAgeTo)+ seekBar.getSelectedMaxValue() +
                getResources().getString(R.string.rsbAgeYears));

        progressBar = (ProgressBar)rootView.findViewById(R.id.webProgressBar);
        progressBar.setVisibility(View.GONE);
        progressText = (TextView)rootView.findViewById(R.id.progressText);
        progressText.setVisibility(View.GONE);

        dbh = new DBHelper(getActivity());
        db = dbh.getWritableDatabase();
        Cursor c = dbh.getAnimalList(db);

        getData();

        return rootView;
    }

    public void getData(){//View v) {
        Log.d("D", "in getData()");
        if (dbh.appFirstTime(db)) {
        //{
            Toast.makeText(getActivity(), "Chargement des donnees du Web", Toast.LENGTH_SHORT).show();
            try {
                new DownloadAdoptableSearch(progressBar, progressText, searchButton, getActivity()).execute();// REMOVE COMMENT FOR PROPER FUNCTIONING
            } catch (Exception e) {
                Log.d("DownloadAdoptableSearch", "Failure" + e.getMessage());
                return;
            }
            dbh.appFirstTimeFalse(db);
        }
        /*else {
            //Toast.makeText(getActivity(), "Chargement des favoris", Toast.LENGTH_SHORT).show();
            Cursor animalList = dbh.getAnimalListOrdered(db, dbh.T_ANIMAL_ID + " asc ");
            Log.d("DownloadAdoptableSearch", "animalList size = " + animalList.getCount());
            try {
                Log.d("DownloadAdoptableSearch", "About to call trigger AdopdatableSearch in update mode.");
                new DownloadAdoptableSearch(progressBar, progressText, searchButton, getActivity(), animalList).execute();// REMOVE COMMENT FOR PROPER FUNCTIONING
            } catch (Exception e) {
                Log.d("DownloadAdoptableSearch", "Failure" + e.getMessage());
                return;
            }

            animalList = dbh.getAnimalList(db);
            //adapter.changeCursor(animalList);
            //btn.setEnabled(true);

        }*/
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(1);
    }

    /*
    public class DownloadWebTask extends AsyncTask<Void, Void, Void> {

        public DownloadWebTask(){

        }
        @Override
        protected Void doInBackground(Void... params) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    searchButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    progressText.setVisibility(View.VISIBLE);
                    progressBar.incrementProgressBy(1);
                }
            });

            web = new SPCAWebAPI();

            try {
                web.callAdoptableSearch();
            } catch (Exception e) {
                Log.d("doInBackground:", "call AdoptableSearch.");
                Log.d("Reason        :", e.getMessage());
                return null;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.incrementProgressBy(5);
                    progressBar.setMax(web.animals.size() + 6);
                }
            });


            ThreadPoolExecutor executor =
                    new ThreadPoolExecutor(4,
                            4,
                            90,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>()
                    );

            Log.d("DownloadWebTask:", "Processing list of " + web.animals.size() + " elements.");
            String localStoragePath = getActivity().getFilesDir().getAbsolutePath();
            int size = web.animals.size();
            long threadCount = 0;
            for(int i = 0; i < size; i+=downloadRangeSizePerThread) {
                executor.execute(new DownloadWebAdoptableDetailsTask(
                        web,
                        localStoragePath,
                        dbh,
                        dbh.getWritableDatabase(),
                        i,
                        Math.min(i + downloadRangeSizePerThread, size)));
                threadCount++;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.incrementProgressBy(1);
                }
            });

            long completed = 0;
            for(;completed < threadCount;) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("Thread", e.getMessage());
                }
                completed = executor.getCompletedTaskCount();
                final long finalCompleted = completed;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(3 + (int) finalCompleted);
                    }
                });
                Log.d("DownloadWebTask:", executor.getCompletedTaskCount() + " completed out of " + threadCount + " scheduled...");
            }

            executor.shutdown();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    progressText.setVisibility(View.GONE);
                    searchButton.setVisibility(View.VISIBLE);
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            // btn.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void v) {
            // btn.setEnabled(true);
            Cursor c = dbh.getAnimalList(db);

            //  currentAct.loadMainActivity();
        }
    }*/
}
