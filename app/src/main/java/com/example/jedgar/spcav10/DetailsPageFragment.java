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
import android.widget.ScrollView;
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

    private int goToPosition;

    LayoutInflater inflater;
    //int positionx;

    Cursor c;
    //boolean parSections;

    private ViewPager detailsPager;
    private DetailsPagerAdapter detailsAdapter;


    public DetailsPageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // View rootView = inflater.inflate(R.layout.details_page_fragment, container, false);
        View rootView = inflater.inflate(R.layout.details, container, false);

        dbh = DBHelper.getInstance(getActivity());
        db = dbh.getWritableDatabase();

        /*
        Intent intent = ((MainActivity)DetailsPageFragment.this.getActivity()).getIntent();
        Bundle data = intent.getExtras();
        SearchCriteria sc = (SearchCriteria) data.getParcelable("SearchCriteria");
        String sql = sc.getSelectCommand();
        */
        Bundle b = this.getArguments();
        String sql = b.getString("sql");
        c = dbh.getCursorForSelect(db, sql);

/*
        c = dbh.getAnimalList(db);
        c.moveToPosition(1);
*/

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

    private class DetailsPagerAdapter extends android.support.v4.view.PagerAdapter {

        boolean textClicked = false;

        @Override
        public Object instantiateItem(View collection, int position) {
            LinearLayout detail = (LinearLayout) inflater.inflate(
                    R.layout.details_page_fragment /* date_page2 */, null);
            Log.d("pagerAdapter", "after linearlayout inflate");

            LinearLayout detailImages = (LinearLayout) detail.findViewById(R.id.detailsImageLayout);

            //c = dbh.getAnimalList(db);
            if (position == goToPosition) {
                int bookmark = c.getPosition();
                c.moveToPosition(position);
                String aID = c.getString(DBHelper.C_ANIMAL_ID);
                dbh.removeFromNewList(db, aID);
                //c.moveToPosition(bookmark);
            }

            c.moveToPosition(position);
            String animalID = Integer.toString(c.getInt(DBHelper.C_ANIMAL_ID));
            String animalName = c.getString(DBHelper.C_ANIMAL_NAME);
            String animalSpecies = c.getString(DBHelper.C_ANIMAL_SPECIES);
            String animalAge = c.getString(DBHelper.C_ANIMAL_AGE);
            String animalSex = c.getString(DBHelper.C_ANIMAL_SEX);
            String animalPrimaryBreed = c.getString(DBHelper.C_ANIMAL_PRIMARY_BREED);
            String animalIntake = c.getString(DBHelper.C_ANIMAL_INTAKE_DATE);
            String animalSterilized = c.getString(DBHelper.C_ANIMAL_STERILE);
            String animalDeclawed = c.getString(DBHelper.C_ANIMAL_DECLAWED);
            String animalDescription = c.getString(DBHelper.C_ANIMAL_DESCRIPTION);

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

            double rawAge = Double.parseDouble(animalAge);
            int yrs = (int) Math.floor(rawAge / 12);
            Toast.makeText(getActivity(), ""+ animalAge, Toast.LENGTH_LONG).show();

            if(rawAge < 12){
                animalAge =  animalAge + " " + getResources().getText(R.string.months);
            }
            else if((rawAge % 12) == 0){
                animalAge = String.valueOf(Integer.parseInt(animalAge) / 12);
                animalAge += " " + getResources().getText(R.string.years);
            }
            else{
                String animalYrsStr = String.valueOf(yrs);

                String monthStr = String.valueOf( (int) (rawAge - (yrs * 12)));


                animalAge = animalYrsStr + " " + getResources().getText(R.string.years) + " " + monthStr + " " + getResources().getText(R.string.months);


            }



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
                animalIntakeTV.setText(getResources().getText(R.string.abandoned) + ": " + formattedIntakeDate);
            }

            /*
//            SETUP FOR DESCRIPTION
//             */

            TextView detailsText = (TextView)detail.findViewById(R.id.detailsText);
            detailsText.setText(Html.fromHtml(animalDescription));





//             /*
//            SETUP FOR DECLAWED
//             */
            if(animalSpecies.equalsIgnoreCase("Cat")) {
                if(animalDeclawed.equalsIgnoreCase("Y") || animalDeclawed.equalsIgnoreCase("Yes")){
                    animalDeclawedTV.setText(getResources().getText(R.string.declawed) + ": " + getResources().getText(R.string.yes));

                }
                        else if (animalDeclawed.equalsIgnoreCase("Both")){
                    animalDeclawedTV.setText(getResources().getText(R.string.declawed) + ": " + getResources().getText(R.string.both));
                }
                else if (animalDeclawed.equalsIgnoreCase("Front")){
                    animalDeclawedTV.setText(getResources().getText(R.string.declawed) + ": " + getResources().getText(R.string.front));
                }
                else if(animalDeclawed.equalsIgnoreCase("Back")) {
                    animalDeclawedTV.setText(getResources().getText(R.string.declawed) + ": " + getResources().getText(R.string.back));
                }
                else if(animalDeclawed.equalsIgnoreCase("N") || animalDeclawed.equalsIgnoreCase("No")) {
                    animalDeclawedTV.setText(getResources().getText(R.string.declawed) + ": " + getResources().getText(R.string.no));
                }
                else {
                    animalDeclawedTV.setVisibility(View.GONE);
                }
                Log.d("DECLAWED", animalDeclawed);
            }
            else {
                animalDeclawedTV.setVisibility(View.GONE);
            }


            /*
            SETUP FOR STERILIZED
             */
            if(animalSterilized.equalsIgnoreCase("Y")) {

                animalSterilizedTV.setText(getResources().getText(R.string.sterilized) + ": " + getResources().getText(R.string.yes));
            }
            else if(animalSterilized.equalsIgnoreCase("N")) {

                animalSterilizedTV.setText(getResources().getText(R.string.sterilized) + ": " + getResources().getText(R.string.no));
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
            String p1 = c.getString(DBHelper.C_ANIMAL_PHOTO1);
            String p2 = c.getString(DBHelper.C_ANIMAL_PHOTO2);
            String p3 = c.getString(DBHelper.C_ANIMAL_PHOTO3);

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

            ViewPager imageViewPager = (ViewPager) detail.findViewById(R.id.imageViewPager);
            imageViewPager.setAdapter(imageAdapter);
            imageViewPager.setCurrentItem(0);


            /*
            Make text/description bigger if clicked
             */
            RelativeLayout detailRelative = (RelativeLayout) detail.findViewById(R.id.detailRelative);
            detailRelative.setTag(detailImages);
            detailRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout vp = (LinearLayout)v.getTag();
                    TextView descriptionTV = (TextView)v.findViewById(R.id.detailsText);

                    if(!textClicked) {
                        vp.setVisibility(LinearLayout.GONE);
                        descriptionTV.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
                        textClicked = true;
                    }
                    else {
                        textClicked = false;
                        descriptionTV.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Medium);
                        vp.setVisibility(LinearLayout.VISIBLE);

                    }
                }
            });


            ImageButton btnShare = (ImageButton)detail.findViewById(R.id.btnShare);
            btnShare.setTag(R.id.animalIDTag, animalID);
            btnShare.setTag(R.id.animalNameTag, animalID);
            btnShare.setTag(R.id.animalAgeTag, animalAge);
            btnShare.setTag(R.id.animalSexTag, animalSex);

            btnShare.setTag(R.id.animalDescriptionTag, animalDescription);
            btnShare.setTag(R.id.animalURLTag, imagesUrl);


            btnShare.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    String animalIDT = (String)v.getTag(R.id.animalIDTag);
                    String animalNameT = (String)v.getTag(R.id.animalNameTag);
                    String animalAgeT = (String)v.getTag(R.id.animalAgeTag);
                    String animalSexT = (String)v.getTag(R.id.animalSexTag);
                    String animalDescriptionT = (String)v.getTag(R.id.animalDescriptionTag);
                    ArrayList<String> animalURLT = (ArrayList<String>)v.getTag(R.id.animalURLTag);

                    try{
                        // Construct a ShareIntent
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        // type of file to share
                        shareIntent.setType("text/html*");

                        String html = "<!DOCTYPE html>" +
                                "<html>" +
                                "<body>" +
                               // "<p>" +getResources().getText(R.string.lookAtWhatIFound)+ "</p>" +
                                "<p>" +getResources().getText(R.string.name)+ ": " +  animalNameT + "</p>" +
                                "<p>" + getResources().getText(R.string.animalID) +": " + animalIDT + "</p>" +
                                "<p>" + getResources().getText(R.string.age)+ ": " + animalAgeT + "</p>" +
                                "<p>" + getResources().getText(R.string.sex) + ": " + animalSexT + "</p>";

                        if(!animalDescriptionT.equals("")){
                            html += "<p>" +  getResources().getText(R.string.description)+": " + animalDescriptionT + "</p>";
                        }

                              if(animalURLT.size() >0){
                                  //html += "<p>" + getResources().getText(R.string.images)+"</p>";
                                  html +=  getResources().getText(R.string.images)+ ":\n";


                                for(int i=0; i<animalURLT.size(); i++){
                                  html+= "<p>" + animalURLT.get(i) + "</p>";
                                }
                              }
                        html+="<p></p>";
                        html+="<p></p>";
                                html+= "<p>" +"\n\n" + getResources().getText(R.string.spcaMontreal) + "</p>" +
                                       "<p>" + getResources().getText(R.string.spcaAddress) + "</p>";// +

                                        html+="<br>";
                                       html += "<p>" + getResources().getText(R.string.spcaPhone) + "</p>" +
                                        "<p>" +getResources().getText(R.string.spcaEmail) + "</p>";


                                     //   "<a>http://www.w3schools.com</a>" +

                               html += "</body></html>";

                        // Message subject
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.spcaMontreal) + " - " + getResources().getText(R.string.helpMeFindAHome));
                        shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(html));

                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);



                        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_with)));
                    }catch (Exception e){
                        // Log any error messages to LogCat using Log.e()
                        e.printStackTrace();
                    }
                }


            });

            ImageButton btnFav = (ImageButton)detail.findViewById(R.id.btnFav);
            boolean isFav;
            if(dbh.isFavorite(db,animalID )){
                isFav = true;
                btnFav.setImageResource(R.drawable.ic_favorite_black_36dp);
            }
            else{
                btnFav.setImageResource(R.drawable.ic_favorite_outline_black_36dp);
                isFav = false;
            }
            btnFav.setTag(R.id.favTag, Boolean.valueOf(isFav));
            btnFav.setTag(R.id.anIDTag, animalID);
            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton btnFav=(ImageButton)v;
                    boolean isFav=((Boolean)btnFav.getTag(R.id.favTag)).booleanValue();
                    String anID = (String)btnFav.getTag(R.id.anIDTag);

                    if(!isFav) {
                        btnFav.setImageResource(R.drawable.ic_favorite_black_36dp);
                        dbh.addToFavoriteList(db, anID);
                       // Toast.makeText(getActivity(), ""+dbh.isFavorite(db, anID) + "....AnimalID: " + anID, Toast.LENGTH_LONG).show();
                        Log.d("ISFAV", ""+dbh.isFavorite(db, anID) + "....AnimalID: " + anID);
                        isFav = true;
                    }
                    else{
                        btnFav.setImageResource(R.drawable.ic_favorite_outline_black_36dp);
                        isFav = false;
                        dbh.removeFromFavoriteList(db, anID);
                        //Toast.makeText(getActivity(), ""+dbh.isFavorite(db, anID) + "....AnimalID: " + anID, Toast.LENGTH_LONG).show();
                        Log.d("ISFAV", ""+dbh.isFavorite(db, anID) + "....AnimalID: " + anID);
                    }
                    btnFav.setTag(R.id.favTag, Boolean.valueOf(isFav));
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








