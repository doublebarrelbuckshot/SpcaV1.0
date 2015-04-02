package com.example.jedgar.spcav10;


//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.IconPagerAdapter;


/**
 * Created by a on 3/31/2015.
 */
public class PagerAdapter  extends FragmentPagerAdapter implements IconPagerAdapter {
//public class PagerAdapter  extends FragmentPagerAdapter implements PagerAdapter {
    private int[] Images = new int[] { R.drawable.testcat, R.drawable.testcat2,
            R.drawable.testcat3, R.drawable.testcat4

    };

    protected static final int[] ICONS = new int[] { R.drawable.dog_logo,
            R.drawable.dog_logo_selected, R.drawable.dog_pressed, R.drawable.dog_logo_selected };

    private int mCount = Images.length;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ViewPagerImagesFragment(Images[position]);
    }

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