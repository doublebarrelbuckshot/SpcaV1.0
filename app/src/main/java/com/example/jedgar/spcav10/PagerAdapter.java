package com.example.jedgar.spcav10;


//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.support.v13.app.FragmentPagerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.IconPagerAdapter;


/**
 * Created by a on 3/31/2015.
 */
public class PagerAdapter  extends FragmentPagerAdapter implements IconPagerAdapter {

    DBHelper dbh;
    SQLiteDatabase db;
    public static final int C_ANIMAL_PHOTO1 = 14;
    public static final int C_ANIMAL_PHOTO2 = 15;
    public static final int C_ANIMAL_PHOTO3 = 16;
    //MainActivity ma;
    //GOOD
    private int[] Images = new int[] { R.drawable.testcat, R.drawable.testcat2,
            R.drawable.testcat3, R.drawable.testcat4

    };

    //NEW
    //private String[] Images = new String[3];

    protected static final int[] ICONS = new int[] { R.drawable.dog_logo,
            R.drawable.dog_logo_selected, R.drawable.dog_pressed, R.drawable.dog_logo_selected };

    private int mCount = Images.length;
    public PagerAdapter(FragmentManager fm){
        super(fm);
    }
    public PagerAdapter(FragmentManager fm, Context ctx){  //remove MainActivity ma
        super(fm);
       // this.ma = ma;
       // dbh = new DBHelper(ctx);
       // db = dbh.getWritableDatabase();

      //  Cursor c = dbh.getAnimalList(db);
      //  c.moveToPosition(0);
       // Images[0] = c.getString(C_ANIMAL_PHOTO1);
       // Images[1] = c.getString(C_ANIMAL_PHOTO2);
       // Images[2] = c.getString(C_ANIMAL_PHOTO3);
        Log.d("pageAdapter", "Image0: " + Images[0]);
    }

    @Override
   // GOOD
    public Fragment getItem(int position) {
        return new ViewPagerImagesFragment(Images[position]);
    }

    //new
//    public Fragment getItem(int position) {
//        //GOOD
//       return new ViewPagerImagesFragment(Images[position]);
//
//        //return new ViewPagerImagesFragment(Images[0]);
//    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

    @Override
    public void destroyItem(View c, int p, Object v) {
        ((ViewPager)c).removeView((View)v);
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}