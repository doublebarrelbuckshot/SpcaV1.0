package com.example.jedgar.spcav10;


import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by pascal on 02/04/15.
 */
public class DownloadWebAdoptableDetailsTask implements Runnable, DownloadAdoptableSearch.Jobable {

    public SPCAWebAPI web;
    public int range_from = -1;
    public int range_to = -1;
    SQLiteDatabase db;
    DBHelper dbh;
    long status;
    String errorMsg;

    public DownloadWebAdoptableDetailsTask(SPCAWebAPI web_inst,
                                           DBHelper dbh_param,
                                           SQLiteDatabase db_param,
                                           int from,
                                           int to)
    {
        web = web_inst;
        range_from = from;
        range_to = to;
        db = db_param;
        dbh = dbh_param;
        status = 0;
        errorMsg = null;
    }

    public void setError(String msg) {
        status = 1;
        errorMsg = msg;
    }
    public boolean terminatedWithError() {
        return (status != 0);
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void run() {

        //Log.d("DownloadWebAnimalTask:", "Processing list from " + range_from + " to " + range_to);
        for (int i = range_from; i < range_to; i++) {
            Animal a;
            try {
                a = web.callAdoptableDetails(i);
            }
            catch (IOException e) {
                Log.e("callAdoptableDetails", e.getMessage());
                status = 1;
                return;
            }
            catch (SAXException e) {
                Log.e("callAdoptableDetails", e.getMessage());
                status = 1;
                return;
            }
            catch (ParserConfigurationException e) {
                Log.e("callAdoptableDetails", e.getMessage());
                status = 1;
                return;
            }
            catch (Exception e) {
                Log.e("callAdoptableDetails", e.getMessage());
                status = 1;
                return;
            }

            try {
                dbh.addAnimal(db, a);
            }
            catch (SQLException e) {
                status = 1;
                return;
            }
        }
        //Log.d("DownloadWebAnimalTask", "Done with range from " + range_from + " to " + range_to);
    }
}
