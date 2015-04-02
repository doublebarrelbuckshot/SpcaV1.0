package com.example.jedgar.spcav10;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LargeAnimalImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LargeAnimalImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LargeAnimalImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PagerAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    int position;

    private OnFragmentInteractionListener mListener;


    public static LargeAnimalImageFragment newInstance(){

        LargeAnimalImageFragment fragment = new LargeAnimalImageFragment();
        return fragment;
    }

    public static LargeAnimalImageFragment newInstance(PagerAdapter pA, int position){

        LargeAnimalImageFragment fragment = new LargeAnimalImageFragment(pA, position);
        return fragment;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LargeAnimalImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LargeAnimalImageFragment newInstance(String param1, String param2) {
        LargeAnimalImageFragment fragment = new LargeAnimalImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
public LargeAnimalImageFragment(PagerAdapter pAdapter, int position){
    mAdapter = pAdapter;
    this.position = position;

}

    public LargeAnimalImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_large_animal_image, container, false);



        mPager = (ViewPager) rootView.findViewById(R.id.pagerLarge);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(position);

        mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicatorLarge);
        mIndicator.setViewPager(mPager);
        ((CirclePageIndicator) mIndicator).setSnap(false);


        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // Toast.makeText(DetailsPageFragment.this.getActivity(),
                        //         "Changed to page " + position,
                        //         Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
        mIndicator.setCurrentItem(position);
       // mIndicator.setCurrentItem(position);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
