package com.example.jedgar.spcav10;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
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


    public DetailsPageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.details_page_fragment, container, false);


        mAdapter = new PagerAdapter(getActivity().getFragmentManager());
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        ((CirclePageIndicator) mIndicator).setSnap(true);


        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        Toast.makeText(DetailsPageFragment.this.getActivity(),
                                "Changed to page " + position,
                                Toast.LENGTH_SHORT).show();
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
       // Intent intent = new Intent(getActivity(), AnimalImageViewer.class);
       // startActivity(intent);

      //  mAnimalImageViewerFragment = (AnimalImageViewer)
       //         getFragmentManager().findFragmentById(R.id.animal_image_viewer);

    }
}








