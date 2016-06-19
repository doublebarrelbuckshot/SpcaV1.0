package com.walterrizzifoundation.grizzi.spca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by a on 12/30/2015.
 */
    public class UpdateAdoptableDetails extends AsyncTask<Void, Integer, Void> {

    SPCAWebAPI web;
    DBHelper dbh;
    SQLiteDatabase db;
    String animalID = "";



    public UpdateAdoptableDetails(Context activity, String animalID)
    {
        dbh = DBHelper.getInstance(activity);
        db = dbh.getWritableDatabase();
        web = new SPCAWebAPI();
        this.animalID = animalID;

    }

    @Override
    protected Void doInBackground(Void... params) {

        boolean updatingPictures = true;
        boolean result  = false;
        Animal a = null;

        try {
            a = web.callAdoptableDetails(0,updatingPictures, animalID);
            if (a.photo1 == null)
                a.photo1 = "";
            if (a.photo2 == null)
                a.photo2= "";
            if (a.photo3 == null)
                a.photo3 = "";
            dbh.updateAnimalPictures(db, animalID, a.photo1, a.photo2, a.photo3);
            Log.d("UpdateAdoptableDetails", "NEW PHOTOS FOR: " + animalID + "\tPhoto1: " + a.photo1);//, "\tPhoto2: " + a.photo2 + "\tPhoto3: " + a.photo3);
        }
            catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
