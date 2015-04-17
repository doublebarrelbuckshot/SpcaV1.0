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
        return fragment;
    }

    public MainPageFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (!loaded) {
            dbh = new DBHelper(getActivity());
            db = dbh.getWritableDatabase();
            sc = new SearchCriteria(db);
            loaded = true;
        }

        View rootView = inflater.inflate(R.layout.main_page_fragment, container, false);

        ageText = (TextView) rootView.findViewById(R.id.ageTV);

        catButton = (ImageButton)rootView.findViewById(R.id.cat_button);
        if ((sc.species & SearchCriteria.SPECIES_CAT) == SearchCriteria.SPECIES_CAT)
            catButton.setSelected(true);
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
        if ((sc.species & SearchCriteria.SPECIES_DOG) == SearchCriteria.SPECIES_DOG)
            dogButton.setSelected(true);
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
        if ((sc.species & SearchCriteria.SPECIES_RABBIT) == SearchCriteria.SPECIES_RABBIT)
            rabbitButton.setSelected(true);
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
        if ((sc.species & SearchCriteria.SPECIES_SMALLFURRY) == SearchCriteria.SPECIES_SMALLFURRY)
            otherButton.setSelected(true);
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
        int max = 24;
        seekBar = new RangeSeekBar<Integer>(0, max, MainPageFragment.this.getActivity());
        if (sc.ageMax == 0)
            seekBar.setSelectedMaxValue(max);
        else
            seekBar.setSelectedMaxValue(sc.ageMax);
        seekBar.setSelectedMinValue(sc.ageMin);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                //Log.i("rangeSeek", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                sc.setAgeMin(minValue);
                if (maxValue == seekBar.getAbsoluteMaxValue()) {
                    ageText.setText(getResources().getString(R.string.age_text_view) +
                            getResources().getString(R.string.rsbAgeFrom) + minValue +
                            getResources().getString(R.string.rsbIllimited));
                    sc.setAgeMax(0);
                }
                else {
                    ageText.setText(getResources().getString(R.string.age_text_view) +
                            getResources().getString(R.string.rsbAgeFrom) + minValue +
                            getResources().getString(R.string.rsbAgeTo) + maxValue +
                            getResources().getString(R.string.rsbAgeYears));
                    sc.setAgeMax(maxValue);
                }
            }
        });
//https://github.com/yahoo/android-range-seek-bar
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.seekbar_placeholder);
        layout.addView(seekBar);

        maleCheckBox = (CheckBox)rootView.findViewById(R.id.male_checkBox);
        if ((sc.sex & SearchCriteria.SEX_MALE) == SearchCriteria.SEX_MALE)
            maleCheckBox.setChecked(true);
        else
            maleCheckBox.setChecked(false);
        maleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchMales();
                /*
                if(!v.findViewById(R.id.male_checkBox).isSelected()){
                    v.findViewById(R.id.male_checkBox).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "males selected!", Toast.LENGTH_SHORT).show();}
                else{
                    v.findViewById(R.id.male_checkBox).setSelected(false);
                }
                */
            }
        });

        femaleCheckBox = (CheckBox)rootView.findViewById(R.id.female_checkBox);
        if ((sc.sex & SearchCriteria.SEX_FEMALE) == SearchCriteria.SEX_FEMALE) {
            femaleCheckBox.setChecked(true);
        }else {
            femaleCheckBox.setChecked(false);
        }
        femaleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.searchFemales();
                /*
                if(!v.findViewById(R.id.female_checkBox).){
                    v.findViewById(R.id.female_checkBox).setSelected(true);
                    Toast.makeText(MainPageFragment.this.getActivity(), "females selected!", Toast.LENGTH_SHORT).show();
                }
                else{
                    v.findViewById(R.id.female_checkBox).setSelected(false);
                }
                */
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
}
