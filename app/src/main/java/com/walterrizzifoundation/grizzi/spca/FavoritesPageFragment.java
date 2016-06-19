package com.walterrizzifoundation.grizzi.spca;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.walterrizzifoundation.grizzi.spca.R;

public class FavoritesPageFragment extends Fragment {

    public static FavoritesPageFragment newInstance(){

        FavoritesPageFragment fragment = new FavoritesPageFragment();
        return fragment;
    }

    public FavoritesPageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.favorites_page_fragment, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(3);
    }



}

