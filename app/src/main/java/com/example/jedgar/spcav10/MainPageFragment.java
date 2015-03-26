package com.example.jedgar.spcav10;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainPageFragment extends Fragment implements View.OnClickListener {

    ImageButton catButton;

    public static MainPageFragment newInstance(){

        MainPageFragment fragment = new MainPageFragment();
        return fragment;
    }

    public MainPageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_page_fragment, container, false);
        catButton = (ImageButton)rootView.findViewById(R.id.cat_button);
        catButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(1);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(MainPageFragment.this.getActivity(), "Successfully Sent!", Toast.LENGTH_LONG).show();

            if(!v.findViewById(R.id.cat_button).isSelected())
                v.findViewById(R.id.cat_button).setSelected(true);
            else{
                v.findViewById(R.id.cat_button).setSelected(false);
            }
    }
}
