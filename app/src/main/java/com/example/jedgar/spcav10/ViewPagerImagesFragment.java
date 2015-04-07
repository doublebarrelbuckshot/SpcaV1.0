package com.example.jedgar.spcav10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;


public class ViewPagerImagesFragment extends Fragment {
    String imageResourceId;
   String url;
    ImageView downloadedImg = null;
    public ViewPagerImagesFragment(){}

    public ViewPagerImagesFragment(String s) {
        imageResourceId = s;
    }

//test
   // public ViewPagerImagesFragment(String s) {
        //imageResourceId = i;
   //   this.url = s;
   //     Log.d("viewpager", "inside viewpager constructor");
   // }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String getImageResourceId(){
        return this.imageResourceId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
        //good
        //image.setImageResource(imageResourceId);

        LinearLayout layout = new LinearLayout(getActivity());
        Picasso.with(getActivity()).load(imageResourceId).into(image);
        Log.d("PICASSO","99999999999999999999999" + imageResourceId);

        layout.setGravity(Gravity.CENTER);

        layout.addView(image);

        return layout;
    }



}
