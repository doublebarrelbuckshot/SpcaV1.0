package com.example.jedgar.spcav10;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

/**
 * Created by a on 3/31/2015.
 */
public class DetailsPageFragment extends Fragment implements View.OnClickListener {

    PagerAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

    int positionx;

    LargeAnimalImageFragment mLargeAnimalImageFragment;

    public DetailsPageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.details_page_fragment, container, false);
       // animalImage = (ViewPager) rootView.findViewById(R.id.pager);
        //animalImage.setOnClickListener(this);




        mAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOnClickListener(this);

        mPager.setOnTouchListener(
                new View.OnTouchListener(){
                    private boolean moved;


                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            moved = false;
                        }
                        if(event.getAction() == MotionEvent.ACTION_MOVE){
                            moved = true;
                        }
                        if(event.getAction() == MotionEvent.ACTION_UP){
                            if(!moved){
                                v.performClick();
                            }
                        }
                        return false;
                    }
                }
        );

        mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        ((CirclePageIndicator) mIndicator).setSnap(true);


        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                       // Toast.makeText(DetailsPageFragment.this.getActivity(),
                       //         "Changed to page " + position,
                       //         Toast.LENGTH_SHORT).show();
                            positionx = position;
                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

        return rootView;
    }

    public static DetailsPageFragment newInstance() {
        DetailsPageFragment fragment = new DetailsPageFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(6);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(DetailsPageFragment.this.getActivity(), "clickedImage", Toast.LENGTH_SHORT).show();
        Toast.makeText(DetailsPageFragment.this.getActivity(), "Count is: " + positionx, Toast.LENGTH_SHORT).show();


        /////    mLargeAnimalImageFragment = (LargeAnimalImageFragment)
        //////        getFragmentManager().findFragmentById(R.id.large_animal_image);

       FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, LargeAnimalImageFragment.newInstance(mAdapter, positionx));
        transaction.addToBackStack(null);
        transaction.commit();



//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//        transaction.replace(R.id.container,  mAdapter.getItem(positionx));
//        transaction.addToBackStack(null);
//        transaction.commit();

        // Fragment fg = LargeAnimalImageFragment.newInstance();
        // adding fragment to relative layout by using layout id
      //  getFragmentManager().beginTransaction().add(R.id.details_fragment, fg).commit();
    }
}








