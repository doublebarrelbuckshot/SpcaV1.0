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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;


public class ViewPagerImagesFragment extends Fragment {
    int imageResourceId;
   String url;
    ImageView downloadedImg = null;
    public ViewPagerImagesFragment(){}

    public ViewPagerImagesFragment(int i) {
        imageResourceId = i;
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

    public int getImageResourceId(){
        return this.imageResourceId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
        //good
        image.setImageResource(imageResourceId);

        //test
        //image.setImageDrawable(new BitmapDrawable( bm));

       // new DownloadImageTask().execute(url);

        LinearLayout layout = new LinearLayout(getActivity());
       // layout.setLayoutParams(new LayoutParams());

        //layout.setGravity(Gravity.CENTER);
//while(downloadedImg == null);
     //   layout.addView(downloadedImg);
        layout.addView(image);

        return layout;
    }


    static class FlushedInputStream extends FilterInputStream
    {
        public FlushedInputStream( InputStream inputStream )
        {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException
        {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n)
            {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L)
                {
                    int byte1 = read();
                    if (byte1 < 0)
                    {
                        break; // we reached EOF
                    }
                    else
                    {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    private class DownloadImageTask extends AsyncTask<String,Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            Log.d("DownloadImageTask", "&&&&&&&&&&&&&&&&&&&&&&");
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            String urldisplay = (String) params[0];
            Log.d("DownloadImageTask", "******************" + params[0]);
            Bitmap mIcon11 = null;
            Bitmap image = null;
            InputStream in = null;
            try
            {
                in = new java.net.URL(url).openStream();
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                image = BitmapFactory.decodeStream(new FlushedInputStream(in),null,opts);
                in.close();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }



            //this.bm = mIcon11;
            Log.d("DownloadImageTask", "!!!!!!!!!!!!" + (String) params[0]);
            downloadedImg.setImageBitmap(image);
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
//            Log.i("Async-Example", "onPostExecute Called");
//            downloadedImg.setImageBitmap(result);


        }
    }

}
