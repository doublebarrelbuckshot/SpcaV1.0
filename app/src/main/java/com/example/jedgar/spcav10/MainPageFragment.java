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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainPageFragment extends Fragment implements View.OnClickListener {

    ProgressBar progressBar;
    TextView progressText;
    DBHelper dbh;
    SQLiteDatabase db;
    SPCAWebAPI web;
    static final int downloadRangeSizePerThread = 1;
    ImageView img;
    static private boolean toggle = false;
    int action = 0;

    // Indexes des champs dans les cursors
    // si c'est pas les bons indexes, corriger stp.
    public static final int C_ANIMAL_ID = 0;
    public static final int C_ANIMAL_SPECIES = 1;
    public static final int C_ANIMAL_NAME = 2;
    public static final int C_ANIMAL_AGE = 3;
    public static final int C_ANIMAL_PRIMARY_BREED = 4;
    public static final int C_ANIMAL_SECONDARY_BREED = 5;
    public static final int C_ANIMAL_SEX = 6;
    public static final int C_ANIMAL_SIZE = 7;
    public static final int C_ANIMAL_STERILE = 8;
    public static final int C_ANIMAL_INTAKE_DATE = 9;
    public static final int C_ANIMAL_PRIMARY_COLOR = 10;
    public static final int C_ANIMAL_SECONDARY_COLOR = 11;
    public static final int C_ANIMAL_DECLAWED = 12;
    public static final int C_ANIMAL_DESCRIPTION = 13;


    public interface OnSearchListener {
        public void doSearch();
    }


    //implemented onClick on each button when added to the inflater/// this one keep it empty else
    //you cant have multiple buttons selected at the same time
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.search_button){

            ((MainActivity)MainPageFragment.this.getActivity()).doSearch();

        }



    }

    ImageButton catButton;
    ImageButton dogButton;
    ImageButton rabbitButton;
    ImageButton otherButton;
    Button searchButton;
    TextView ageText;


    public static MainPageFragment newInstance(){

        MainPageFragment fragment = new MainPageFragment();
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
                if(!v.findViewById(R.id.cat_button).isSelected()){
                    v.findViewById(R.id.cat_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "cat selected!", Toast.LENGTH_SHORT).show();}
                else{
                    v.findViewById(R.id.cat_button).setSelected(false);
                }
            }
        });

        dogButton = (ImageButton)rootView.findViewById(R.id.dog_button);
        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.findViewById(R.id.dog_button).isSelected()){
                    v.findViewById(R.id.dog_button).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "dog selected!", Toast.LENGTH_SHORT).show();}
                else{
                    v.findViewById(R.id.dog_button).setSelected(false);
                }

            }
        });

        rabbitButton = (ImageButton)rootView.findViewById(R.id.rabbit_button);
        rabbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 20, MainPageFragment.this.getActivity());
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                Log.i("rangeSeek", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                ageText.setText(getResources().getString(R.string.age_text_view) +
                        getResources().getString(R.string.rsbAgeFrom) + minValue +
                                getResources().getString(R.string.rsbAgeTo)+ maxValue +
                                        getResources().getString(R.string.rsbAgeYears));
            }
        });
//https://github.com/yahoo/android-range-seek-bar
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.seekbar_placeholder);
        layout.addView(seekBar);


        ageText.setText(getResources().getString(R.string.age_text_view) +
                getResources().getString(R.string.rsbAgeFrom) + seekBar.getSelectedMinValue() +
                getResources().getString(R.string.rsbAgeTo)+ seekBar.getSelectedMaxValue() +
                getResources().getString(R.string.rsbAgeYears));


        progressBar = (ProgressBar)rootView.findViewById(R.id.webProgressBar);
        progressBar.setVisibility(View.GONE);
        progressText = (TextView)rootView.findViewById(R.id.progressText);
        progressText.setVisibility(View.GONE);


        if(MainActivity.firstRun){
            getData();
            MainActivity.firstRun = false;
        }

        dbh = new DBHelper(getActivity());
        db = dbh.getWritableDatabase();
        Cursor c = dbh.getAnimalList(db);


        return rootView;
    }

    public void getData(){//View v) {

        /* this was all for testing */
        if (toggle == false) {
            Toast.makeText(getActivity(), "Chargement des donnees du Web", Toast.LENGTH_SHORT).show();
            new DownloadWebTask().execute();// REMOVE COMMENT FOR PROPER FUNCTIONING
            // currentAct.loadMainActivity(); //SET COMMENT FOR PROPER FUNCTIONING

            toggle = true;
        }
        else {
            // btn.setEnabled(false);
            toggle = false;
            if (action == 0) {
                Log.d("onClick:", "action == " + action);
                dbh.addToFavoriteList(db, "25289349");
                dbh.addToFavoriteList(db, "24296807");
                dbh.addToFavoriteList(db, "25020484");
            }
            else if (action == 1) {
                Log.d("onClick:", "action == " + action);
                dbh.removeFromFavoriteList(db, "24296807");
            }
            else if (action == 2) {
                Log.d("onClick:", "action == " + action);
                dbh.removeAllFromFavoriteList(db);
                action = 0;
            }
            action++;
            Toast.makeText(getActivity(), "Chargement des favoris", Toast.LENGTH_SHORT).show();
            Cursor c = dbh.getFavoriteList(db);
            //adapter.changeCursor(c);
            //btn.setEnabled(true);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(1);
    }

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
            /* for testing
            if (action == 0) {
                dbh.deleteAll(db);
            }*/
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void v) {
            // btn.setEnabled(true);
            Cursor c = dbh.getAnimalList(db);

            //  currentAct.loadMainActivity();
        }
    }
}
