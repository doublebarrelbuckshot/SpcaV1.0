package com.example.jedgar.spcav10;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.io.FileOutputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by a on 3/31/2015.
 */
public class DetailsPageFragment extends Fragment{//} implements View.OnClickListener {
    DBHelper dbh;
    SQLiteDatabase db;
    public static final int C_ANIMAL_ID = 0;
    public static final int C_ANIMAL_SPECIES = 1;
    public static final int C_ANIMAL_NAME = 2;
    public static final int C_ANIMAL_AGE = 3;
    public static final int C_ANIMAL_PRIMARY_BREED = 4;
    public static final int C_ANIMAL_SECONDARY_BREED = 5;
    public static final int C_ANIMAL_SEX = 6;
    public static final int C_ANIMAL_SIZE = 7;
    public static final int C_ANIMAL_STERILE = 8;
    public static final int C_ANIMAL_INTAKE_DATE = 9;
    public static final int C_ANIMAL_PRIMARY_COLOR = 10;
    public static final int C_ANIMAL_SECONDARY_COLOR = 11;
    public static final int C_ANIMAL_DESCRIPTION = 12;
    public static final int C_ANIMAL_DECLAWED = 13;
    public static final int C_ANIMAL_PHOTO1 = 14;
    public static final int C_ANIMAL_PHOTO2 = 15;
    public static final int C_ANIMAL_PHOTO3 = 16;



    private int goToPosition;

    LayoutInflater inflater;
    int positionx;


    Cursor c;
    boolean parSections;

    private ViewPager detailsPager;
    private DetailsPagerAdapter detailsAdapter;


    public DetailsPageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View rootView = inflater.inflate(R.layout.details_page_fragment, container, false);
        View rootView = inflater.inflate(R.layout.details, container, false);

        dbh = new DBHelper(getActivity());
        db = dbh.getWritableDatabase();

        c = dbh.getAnimalList(db);
        c.moveToPosition(1);
//        String t = Integer.toString(c.getInt(C_ANIMAL_ID));
//        String d = c.getString(C_ANIMAL_NAME);

//
//        String p1 = c.getString(C_ANIMAL_PHOTO1);
//        String p2 = c.getString(C_ANIMAL_PHOTO2);
//        String p3 = c.getString(C_ANIMAL_PHOTO3);

        // TextView detail = (TextView)rootView.findViewById(R.id.textView2);
        //  detail.setText(t + d + " P1: " + p1 + " P2: " + p2 + " P3: " + p3);

        this.inflater = inflater;// =(LayoutInflater) getActivity().getSystemService(getActivity().getBaseContext().LAYOUT_INFLATER_SERVICE);//.getLayoutInflater();

        detailsAdapter = new DetailsPagerAdapter();
        detailsPager = (ViewPager) rootView.findViewById(R.id.detailsPager);
        detailsPager.setAdapter(detailsAdapter);
        detailsPager.setCurrentItem(goToPosition);
        return rootView;
    }

    public static DetailsPageFragment newInstance() {
        DetailsPageFragment fragment = new DetailsPageFragment();
        fragment.goToPosition = 0;
        return fragment;
    }

    //come from browse list
    public static DetailsPageFragment newInstance(int pos) {
        DetailsPageFragment fragment = new DetailsPageFragment();
        fragment.goToPosition = pos;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(6);
    }
//
//    @Override
//    public void onClick(View v) {
// //       Toast.makeText(DetailsPageFragment.this.getActivity(), "clickedImage", Toast.LENGTH_SHORT).show();
////        Toast.makeText(DetailsPageFragment.this.getActivity(), "Count is: " + positionx, Toast.LENGTH_SHORT).show();
////
////
////        /////    mLargeAnimalImageFragment = (LargeAnimalImageFragment)
////        //////        getFragmentManager().findFragmentById(R.id.large_animal_image);
////
////        FragmentTransaction transaction = getFragmentManager().beginTransaction();
////
////// Replace whatever is in the fragment_container view with this fragment,
////// and add the transaction to the back stack so the user can navigate back
////        transaction.replace(R.id.container, LargeAnimalImageFragment.newInstance(mAdapter, positionx));
////        transaction.addToBackStack(null);
////        transaction.commit();
////
//
//
////        FragmentTransaction transaction = getFragmentManager().beginTransaction();
////
////        transaction.replace(R.id.container,  mAdapter.getItem(positionx));
////        transaction.addToBackStack(null);
////        transaction.commit();
//
//        // Fragment fg = LargeAnimalImageFragment.newInstance();
//        // adding fragment to relative layout by using layout id
//        //  getFragmentManager().beginTransaction().add(R.id.details_fragment, fg).commit();
//    }

    private class DetailsPagerAdapter extends android.support.v4.view.PagerAdapter {

        String blah;
        Boolean isFav;
        // private DetailsPagerAdapter(){ }
        @Override
        public Object instantiateItem(View collection, int position) {
            LinearLayout detail = (LinearLayout) inflater.inflate(
                    R.layout.details_page_fragment /* date_page2 */, null);
            Log.d("pagerAdapter", "after linearlayout inflate");

            c = dbh.getAnimalList(db);

            c.moveToPosition(position);
            String animalID = Integer.toString(c.getInt(C_ANIMAL_ID));
            String animalName = c.getString(C_ANIMAL_NAME);
            String animalSpecies = c.getString(C_ANIMAL_SPECIES);
            String animalAge = c.getString(C_ANIMAL_AGE);
            String animalSex = c.getString(C_ANIMAL_SEX);
            String animalPrimaryBreed = c.getString(C_ANIMAL_PRIMARY_BREED);
            String animalIntake = c.getString(C_ANIMAL_INTAKE_DATE);
            String animalSterilized = c.getString(C_ANIMAL_STERILE);
            String animalDeclawed = c.getString(C_ANIMAL_DECLAWED);
            String animalDescription = c.getString(C_ANIMAL_DESCRIPTION);

            for(int i = 0; i<17; i++){
                Log.d(i + "  !!!!:", c.getString(i));
            }


            TextView animalIDTV = (TextView)detail.findViewById(R.id.animalIDTV);
            TextView animalNameTV = (TextView)detail.findViewById(R.id.animalNameTV);
            TextView animalAgeTV = (TextView)detail.findViewById(R.id.animalAgeTV);
            TextView animalSexTV = (TextView)detail.findViewById(R.id.animalSexTV);
            TextView animalPrimaryBreedTV = (TextView)detail.findViewById(R.id.animalPrimaryBreedTV);
            TextView animalIntakeTV = (TextView)detail.findViewById(R.id.animalIntakeTV);
            TextView animalSterilizedTV = (TextView)detail.findViewById(R.id.animalSterilizedTV);
            TextView animalDeclawedTV = (TextView)detail.findViewById(R.id.animalDeclawedTV);




            animalIDTV.setText("ID: " + animalID);
            animalNameTV.setText(animalName);
            animalAgeTV.setText(animalAge);
            animalPrimaryBreedTV.setText(animalPrimaryBreed);


            /*
            SET UP FOR INTAKE DATE
             */
            boolean dateConversionSuccess = true;
            Date intakeDate = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                intakeDate = simpleDateFormat.parse(animalIntake);
            } catch (ParseException ex)
            {
                Log.d("PARSEERROR", ex.getMessage());
                dateConversionSuccess = false;
                animalIntakeTV.setVisibility(View.GONE);
            }

            String formattedIntakeDate = "";
            if(dateConversionSuccess){
                formattedIntakeDate = new SimpleDateFormat("dd/MM/yyyy").format(intakeDate);
                animalIntakeTV.setText("Intake: " + formattedIntakeDate);
            }

            /*
//            SETUP FOR DESCRIPTION
//             */

            TextView detailsText = (TextView)detail.findViewById(R.id.detailsText);
            detailsText.setText(Html.fromHtml(animalDescription));
           // String htmlText = "<html><body><big><strong>" + animalName + "\t" + animalID + "</strong></big> </body> </html>";

           // ;




            //  if(animalSpecies.equalsIgnoreCase("Cat")) {
//                if(animalDeclawed.equalsIgnoreCase("Y") || animalDeclawed.equalsIgnoreCase("Yes") ||
//                        animalDeclawed.equalsIgnoreCase("Both") || animalDeclawed.equalsIgnoreCase("Front")
//                        || animalDeclawed.equalsIgnoreCase("Back")) {
//
//                    animalDeclawedTV.setText(getResources().getText(R.string.declawed));
//                }
//                else if(animalDeclawed.equalsIgnoreCase("N") || animalDeclawed.equalsIgnoreCase("No")) {
//
//                    animalDeclawedTV.setText(getResources().getText(R.string.not_declawed));
//                }
//                else {
//                    animalDeclawedTV.setVisibility(View.GONE);
//
//                }
//                Log.d("DECLAWED", animalDeclawed);
          //  }

//             /*
//            SETUP FOR DECLAWED
//             */
            if(animalSpecies.equalsIgnoreCase("Cat")) {
                if(animalDeclawed.equalsIgnoreCase("Y") || animalDeclawed.equalsIgnoreCase("Yes") ||
                        animalDeclawed.equalsIgnoreCase("Both") || animalDeclawed.equalsIgnoreCase("Front")
                        || animalDeclawed.equalsIgnoreCase("Back")) {

                    animalDeclawedTV.setText(getResources().getText(R.string.declawed));
                }
                else if(animalDeclawed.equalsIgnoreCase("N") || animalDeclawed.equalsIgnoreCase("No")) {

                    animalDeclawedTV.setText(getResources().getText(R.string.not_declawed));
                }
                else {
                    animalDeclawedTV.setVisibility(View.GONE);

                }
                Log.d("DECLAWED", animalDeclawed);
            }
            /*
            SETUP FOR STERILIZED
             */
            if(animalSterilized.equalsIgnoreCase("Y")) {

                animalSterilizedTV.setText(getResources().getText(R.string.sterilized));
            }
            else if(animalSterilized.equalsIgnoreCase("N")) {

                animalSterilizedTV.setText(getResources().getText(R.string.not_sterilized));
            }
            else {
                animalSterilizedTV.setVisibility(View.GONE);
            }

 /*
            SETUP FOR SEX
             */

            if(animalSex.equalsIgnoreCase("M") || animalSex.equalsIgnoreCase("MALE") ) {
                // Log.d("SEX", "SEX MALE");
                animalSexTV.setText(getResources().getText(R.string.male));
            }
            else if(animalSex.equalsIgnoreCase("F")|| animalSex.equalsIgnoreCase("FEMALE")) {
                //Log.d("SEX", "SEX FEMALE");
                animalSexTV.setText(getResources().getText(R.string.female));
            }
            else {
                // Log.d("SEX", "SEX NOT M OR F !!" + animalSex + "!!");
                animalSexTV.setVisibility(View.GONE);
            }



            // TextView animalBlankTV = (TextView)detail.findViewById(R.id.animalBlankTV);
            // animalBlankTV.setText(animalIntake + "blank");
            String p1 = c.getString(C_ANIMAL_PHOTO1);
            String p2 = c.getString(C_ANIMAL_PHOTO2);
            String p3 = c.getString(C_ANIMAL_PHOTO3);

            ArrayList<String> imagesUrl = new ArrayList<String>();
            if(!p1.equals("")) {
                imagesUrl.add(p1);
            }
            if(!p2.equals("")){
                imagesUrl.add(p2);
            }
            if(!p3.equals("")){
                imagesUrl.add(p3);
            }



            //Log.d("IMAGE PAGER", "" + imageCounterImages.size());

            ImagePagerAdapter imageAdapter = new ImagePagerAdapter(imagesUrl, detail);

            // ImagePagerAdapter imageAdapter = new ImagePagerAdapter(imagesUrl, imageCounterImages, detail);
            ViewPager imageViewPager = (ViewPager) detail.findViewById(R.id.imageViewPager);
            imageViewPager.setAdapter(imageAdapter);
            imageViewPager.setCurrentItem(0);


            final ImageButton btnShare = (ImageButton)detail.findViewById(R.id.btnShare);
            btnShare.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    try{
                        // Construct a ShareIntent
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        // type of file to share
                        shareIntent.setType("text/html*");

                    /* Parameter
                    ImageAge:animal ID.
                    ImageID:animal ID.
                    Current_imageURl :URL of the image on the currentView.
                    Image description:if you have futher information.Put it here
                    Remove this <a>http://www.w3schools.com</a>":It's  just an example of link.

                     */
                        String html = "<!DOCTYPE html>" +
                                "<html>" +
                                "<body>" +
                                "<p>ImageAge: </p>" + "<p>ImageID: </p>" +

                                "<p>Image description:</p>" +
                                "<p>To see the Animal,follow the link below</p>" +
                                "<a>http://www.w3schools.com</a>" +

                                "</body></html>";

                        // Message subject
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Animal sharing");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(html));

                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);



                        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_with)));
                    }catch (Exception e){
                        // Log any error messages to LogCat using Log.e()
                        e.printStackTrace();
                    }
                }


            });

            final ImageButton btnFav = (ImageButton)detail.findViewById(R.id.btnFav);
            Log.d("ISFAV", ""+dbh.isFavorite(db, animalID));
            if(dbh.isFavorite(db,animalID )){
                isFav = true;
                btnFav.setImageResource(R.drawable.ic_favorite_black_36dp);
            }
            else{
                btnFav.setImageResource(R.drawable.ic_favorite_outline_black_36dp);
                isFav = false;
            }
            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String anID = Integer.toString(c.getInt(C_ANIMAL_ID));

                    if(!isFav) {
                        Log.d("FAV", "88888888888 FAV ACTIVATED");
                        Toast.makeText(getActivity(), "FAVORITE ACTIVATED", Toast.LENGTH_SHORT).show();
                        btnFav.setImageResource(R.drawable.ic_favorite_black_36dp);
                        dbh.addToFavoriteList(db, anID);
                        isFav = true;
                    }
                    else{
                        Log.d("FAV", "88888888888 FAV DEACTIVATED");
                        Toast.makeText(getActivity(), "FAVORITE DEACTIVATED", Toast.LENGTH_SHORT).show();
                        btnFav.setImageResource(R.drawable.ic_favorite_outline_black_36dp);
                        isFav = false;
                        dbh.removeFromFavoriteList(db, anID);

                    }
                }
            });
            CirclePageIndicator titleIndicator = (CirclePageIndicator)detail.findViewById(R.id.pageIndicator);
            titleIndicator.setViewPager(imageViewPager);

            ((ViewPager) collection).addView(detail, 0);

            return detail;
        }
        @Override
        public int getCount() {
            return c.getCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == ((LinearLayout) o);
        }

        public void destroyItem(View collection, int position, Object view) {
            Log.d("detail", "remove view @ " + position);
            ((ViewPager) collection).removeView((LinearLayout) view);
        }
    }


    private class ImagePagerAdapter extends android.support.v4.view.PagerAdapter {

        ArrayList<String> imagesUrl;
        // ArrayList<ImageView>imageCounterImages;
        LinearLayout detailsPageFrag;
        boolean clicked = false;
        TextView detailsText;
        LinearLayout detailsLayout;
        LinearLayout detailsScrollLayout;
        private ImagePagerAdapter(ArrayList<String> imagesUrl, LinearLayout detailsPageFrag ){

            //  private ImagePagerAdapter(ArrayList<String> imagesUrl, ArrayList<ImageView> imageCounterImages, LinearLayout detailsPageFrag ){
            this.imagesUrl = imagesUrl;
            //this.imageCounterImages = imageCounterImages;
            this.detailsPageFrag = detailsPageFrag;
            detailsText = (TextView) detailsPageFrag.findViewById(R.id.detailsText);
            //detailsScrollLayout = (LinearLayout)detailsPageFrag.findViewById(R.id.detailsScrollLayout);
            detailsLayout = (LinearLayout) detailsPageFrag.findViewById(R.id.detailsLayout);



        }
        @Override
        public Object instantiateItem(View collection, int position) {



            LinearLayout detail = (LinearLayout) inflater.inflate(R.layout.images /* date_page2 */, null);

            detail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(!clicked) {
                        //Log.d("BLAH", "clicklistener99999999999999999999");
                        detailsLayout.setVisibility(View.GONE);
                        clicked = true;
                    }
                    else{
                        detailsLayout.setVisibility(View.VISIBLE);
                        clicked = false;
                    }
                    // Toast.makeText(getActivity(), "clickedImage", Toast.LENGTH_SHORT).show();

                }
            });
            // Log.d("pagerAdapter", "after linearlayout inflate");
            // Button button = (Button)detail.findViewById(R.id.button);
            //button.setText(blah + "29038572364578236945234");


//            c = dbh.getAnimalList(db);
//            c.moveToPosition(position);
//            String t = Integer.toString(c.getInt(C_ANIMAL_ID));
//            String d = c.getString(C_ANIMAL_NAME);
//            String p1 = c.getString(C_ANIMAL_PHOTO1);
//            String p2 = c.getString(C_ANIMAL_PHOTO2);
//            String p3 = c.getString(C_ANIMAL_PHOTO3);

            ImageView detailsText = (ImageView)detail.findViewById(R.id.animalImage);
            Picasso.with(getActivity().getApplicationContext()).load(imagesUrl.get(position)).into(detailsText);
            //  for (int i =0; i<imageCounterImages.size(); i++){
            //      imageCounterImages.get(i).setImageResource(android.R.drawable.radiobutton_off_background);
            //  }
            //  imageCounterImages.get(position).setImageResource(android.R.drawable.radiobutton_on_background);

            ((ViewPager) collection).addView(detail, 0);

            return detail;
        }
        @Override
        public int getCount() {
            return imagesUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == ((LinearLayout) o);
        }

        public void destroyItem(View collection, int position, Object view) {
            Log.d("detail", "remove view @ " + position);
            ((ViewPager) collection).removeView((LinearLayout) view);
        }
    }
}








