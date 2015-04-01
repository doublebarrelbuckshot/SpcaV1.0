package com.example.jedgar.spcav10;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class ViewPagerImagesFragment extends Fragment {
    int imageResourceId;
    public ViewPagerImagesFragment(){}

    public ViewPagerImagesFragment(int i) {
        imageResourceId = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
        image.setImageResource(imageResourceId);


        LinearLayout layout = new LinearLayout(getActivity());
       // layout.setLayoutParams(new LayoutParams());

        //layout.setGravity(Gravity.CENTER);
        layout.addView(image);

        return layout;
    }
}
