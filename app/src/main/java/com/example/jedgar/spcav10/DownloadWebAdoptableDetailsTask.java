package com.example.jedgar.spcav10;


import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by pascal on 02/04/15.
 */
public class DownloadWebAdoptableDetailsTask implements Runnable {

    public SPCAWebAPI web;
    public int range_from = -1;
    public int range_to = -1;
    String localStorage;
    SQLiteDatabase db;
    DBHelper dbh;

    public DownloadWebAdoptableDetailsTask(SPCAWebAPI web_inst,
                                           String localStoragePath,
                                           DBHelper dbh_param,
                                           SQLiteDatabase db_param,
                                           int from,
                                           int to)
    {
        web = web_inst;
        range_from = from;
        range_to = to;
        localStorage = localStoragePath;
        db = db_param;
        dbh = dbh_param;
        localStorage = localStoragePath;
    }

    @Override
    public void run() {

        Log.d("DownloadWebAnimalTask:", "Processing list from " + range_from + " to " + range_to);
        ContentValues cv = new ContentValues();
        for (int i = range_from; i < range_to; i++) {

            Log.d("DownloadWebAnimalTask:", "Processing animal:" + web.animals.get(i).id);
            Animal a;
            try {
                a = web.callAdoptableDetails(i);
            } catch (Exception e) {
                Log.d("DownloadWebAnimalTask:", "callAdoptableDetails");
                Log.d("Reason        :", e.getMessage());
                return;
            }

            dbh.addAnimal(db, a);
        }
        Log.d("DownloadWebAnimalTask", "Done with range from " + range_from + " to " + range_to);
    }
}
