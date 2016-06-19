package com.walterrizzifoundation.grizzi.spca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.walterrizzifoundation.grizzi.spca.R;

/**
 * Created by JEdgar on 4/21/2015.
 */
public class DogInfoFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dog_info_page_fragment, null);
        return rootView;
    }

}
