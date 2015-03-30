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

public class SearchPageFragment extends Fragment implements View.OnClickListener {

    ImageButton catButton;
    ImageButton dogButton;
    ImageButton rabbitButton;
    ImageButton otherButton;

    public static SearchPageFragment newInstance(){

        SearchPageFragment fragment = new SearchPageFragment();
        return fragment;
    }

    public SearchPageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_page_fragment, container, false);
        catButton = (ImageButton)rootView.findViewById(R.id.cat_button);
        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.findViewById(R.id.cat_button).isSelected()){
                    v.findViewById(R.id.cat_button).setSelected(true);
                    Toast.makeText(SearchPageFragment.this.getActivity(), "cat selected!", Toast.LENGTH_SHORT).show();}
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
                    Toast.makeText(SearchPageFragment.this.getActivity(), "dog selected!", Toast.LENGTH_SHORT).show();}
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
                    Toast.makeText(SearchPageFragment.this.getActivity(), "rabbit selected!", Toast.LENGTH_SHORT).show();}
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
                    Toast.makeText(SearchPageFragment.this.getActivity(), "others selected!", Toast.LENGTH_SHORT).show();}
                else{
                    v.findViewById(R.id.other_button).setSelected(false);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(4);
    }

    @Override
    public void onClick(View v) {

    }
}

