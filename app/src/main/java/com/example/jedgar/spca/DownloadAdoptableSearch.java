package com.example.jedgar.spca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by pascal on 15/04/15.
 */
public class DownloadAdoptableSearch extends AsyncTask<Void, Integer, Void> {

    static final int downloadRangeSizePerThread = 1;
    SPCAWebAPI web;
    DBHelper dbh;
    SQLiteDatabase db;
    boolean refresh;
    Cursor animalList = null;
    int errorCode = 0;
    int errorMsg = 0;
    int adoptableDetailsErrors = 0;
    boolean postJobError = false;

    ArrayList<Jobable> jobList;
    AsyncTaskDelegate delegate;

    public class DownloadAdoptableSearchResponse {
        public int adoptableSearchErrorCode = 0;
        public int adoptableDetailsErrors = 0;
        public boolean postJobError = false;

        DownloadAdoptableSearchResponse(int ascode, int aderrors, boolean postjoberror) {
            adoptableSearchErrorCode = ascode;
            adoptableDetailsErrors = aderrors;
            postJobError = postjoberror;
        }
    }
    public interface AsyncTaskDelegate {
        public void asyncTaskFinished(DownloadAdoptableSearchResponse response);
        public void asyncTaskProgressUpdate(Integer... values);
    }

    public interface Jobable {
        boolean terminatedWithError();
        public void setError(String msg);
        public String getErrorMsg();
    }

    public class DownloadAdoptableSearchException extends Exception {

    }
    public class DownloadAdoptableDetailsException extends Exception {

    }


    private void initCommons() {
        animalList = null;
        errorCode = 0;
        errorMsg = 0;
        adoptableDetailsErrors = 0;
        boolean postJobError = false;
        jobList = new ArrayList<Jobable>();
        refresh = false;
    }

    public DownloadAdoptableSearch(Context activity,
                                   Cursor panimalList,
                                   AsyncTaskDelegate caller) {
        initCommons();
        dbh = DBHelper.getInstance(activity);
        db = dbh.getWritableDatabase();
        delegate = caller;
        refresh = true;
        animalList = panimalList;
    }

    public DownloadAdoptableSearch(Context activity, AsyncTaskDelegate caller, boolean refreshReq){
        Log.d("callAdoptableSearch", "Constructor");
        initCommons();
        dbh = DBHelper.getInstance(activity);
        db = dbh.getWritableDatabase();
        delegate = caller;
        refresh = refreshReq;
        animalList = null;
    }


    @Override
    protected Void doInBackground(Void... params) {

        publishProgress(0);
        Log.d("DownloadWebTask", "In doInBackground");
        web = new SPCAWebAPI();
        try {
            web.callAdoptableSearch();
        } catch (IOException e) {
            errorCode = 1;
            Log.e("DownloadWebTask", e.getMessage());
            return null;
        } catch (SAXException e) {
            errorCode = 1;
            Log.e("DownloadWebTask", e.getMessage());
            return null;
        } catch (ParserConfigurationException e) {
            errorCode = 1;
            Log.e("DownloadWebTask", e.getMessage());
            return null;
        } catch (Exception e) {
            errorCode = 1;
            Log.e("DownloadWebTask", e.getMessage());
            return null;
        }


        publishProgress(new Integer[]{1, web.animals.size() + 5});

        LinkedBlockingQueue<Runnable> jobQueue = new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(4,
                        4,
                        90,
                        TimeUnit.SECONDS,
                        jobQueue
                );

        publishProgress(5);
        Log.d("DownloadWebTask:", "Processing list of " + web.animals.size() + " elements.");
        int size = web.animals.size();
        long threadCount = 0;
        if (refresh == false) {
            for (int i = 0; i < size; i += downloadRangeSizePerThread) {
                DownloadWebAdoptableDetailsTask dadt =
                        new DownloadWebAdoptableDetailsTask(
                                web,
                                dbh,
                                dbh.getWritableDatabase(),
                                i,
                                Math.min(i + downloadRangeSizePerThread, size));
                jobList.add(dadt);
                executor.execute(dadt);
                threadCount++;
            }
        } else {
            String where = "";
            String updateWhere = "";
            Log.d("DownloadWebTask", "animalList item count is " + animalList.getCount());
            animalList.moveToFirst();
            /*
            if (!animalList.isAfterLast()) {
                //Log.d("ALIST", animalList.getString(0));
            }*/
            int i = 0;
            int aidx = 0;
            while (i < size && !animalList.isAfterLast()) {
                if ((i & 1) == 1)
                    publishProgress(5 + (i >> 1));
                String aID = animalList.getString(DBHelper.C_ANIMAL_ID);
                Animal animal = web.animals.get(i);
                if (aID.equals(animal.id)) {
                    animalList.moveToNext();
                    /*
                    if (!animalList.isAfterLast())
                        Log.d("ALIST", animalList.getString(0));
                        */
                    i++;
                } else {
                    int db_id =  Integer.parseInt(aID);
                    int web_id = Integer.parseInt(animal.id);
                    if (db_id < web_id) {
                        if (dbh.isFavorite(db, aID)) {
                            Cursor fav = dbh.getFavoriteAnimal(db, aID);
                            fav.moveToFirst();
                            /*
                            Log.d("ALIST", animalList.getString(0) + " favorite no longer available.  Your flag:" + fav.getString(DBHelper.C_FAVORITE_ANIMAL_AVAILABLE));
                            */
                            if (updateWhere.equals(""))
                                updateWhere += " " + DBHelper.T_FAVORITE_ANIMALS_ANIMAL_ID + " = '" + db_id + "'";
                            else
                                updateWhere += " OR " + DBHelper.T_FAVORITE_ANIMALS_ANIMAL_ID + " = '" + db_id + "'";

                        } else {
                            //Log.d("ALIST", animalList.getString(0) + " no longer available.");
                            if (where.equals(""))
                                where += " " + DBHelper.T_ANIMAL_ID + " = '" + db_id + "'";
                            else
                                where += " OR " + DBHelper.T_ANIMAL_ID + " = '" + db_id + "'";
                        }
                        animalList.moveToNext();
                    } else {
                        //Log.d("ALIST", web.animals.get(i).id + " NEW!.");
                        DownloadWebAdoptableDetailsTask dadt =
                                new DownloadWebAdoptableDetailsTask(
                                        web,
                                        dbh,
                                        dbh.getWritableDatabase(),
                                        i,
                                        Math.min(i + 1, size));
                        jobList.add(dadt);
                        executor.execute(dadt);
                        threadCount++;
                        i++;
                    }
                }
            }
            while(animalList.isAfterLast() == false) {
                String aID = animalList.getString(DBHelper.C_ANIMAL_ID);
                if (dbh.isFavorite(db, aID)) {
                    if (updateWhere.equals(""))
                        updateWhere += " " + DBHelper.T_ANIMAL_ID + " = '" + aID + "'";
                    else
                        updateWhere += " OR " + DBHelper.T_ANIMAL_ID + " = '" + aID + "'";
                }

                animalList.moveToNext();
                /*
                if (!animalList.isAfterLast())
                    Log.d("ALIST", animalList.getString(0));
                */
            }
            while(i < size) {
                DownloadWebAdoptableDetailsTask dadt =
                        new DownloadWebAdoptableDetailsTask(
                                web,
                                dbh,
                                dbh.getWritableDatabase(),
                                i,
                                Math.min(i + 1, size));
                jobList.add(dadt);
                executor.execute(dadt);
                threadCount++;
                i++;
            }

            Job job = new Job(where, updateWhere);
            jobList.add(job);
            executor.execute(job);
            threadCount++;

            publishProgress(new Integer[]{1, (int)(web.animals.size() + 5 + threadCount)});
        }

        long completed = 0;
        int base = 0;
        if (refresh)
            base = web.animals.size();
        for(;completed < threadCount;) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e("Thread", e.getMessage());
            }
            completed = executor.getCompletedTaskCount();
            final int finalCompleted = (int)completed;
            publishProgress(base + finalCompleted + 5);
            Log.d("DownloadWebTask:", executor.getCompletedTaskCount() + " completed out of " + threadCount + " scheduled...");
        }

        executor.shutdown();

        return null;
    }

    @Override
    protected void onProgressUpdate (Integer... values) {
        delegate.asyncTaskProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        // btn.setEnabled(false);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void v) {
        int end = jobList.size() - 1;
        boolean postJobError = false;
        if (end > -1) {
            for (int i = 0; i < end; i++) {
                if (jobList.get(i).terminatedWithError()) {
                    adoptableDetailsErrors++;
                }
            }
            postJobError = jobList.get(jobList.size() - 1).terminatedWithError();
        }

        delegate.asyncTaskFinished(
                new DownloadAdoptableSearchResponse(
                        errorCode,
                        adoptableDetailsErrors,
                        postJobError
                )
        );
    }

    public class Job implements Runnable, Jobable{

        String deleteWhereClause;
        String updateWhereClause;
        int status;
        String errorMsg;

        Job(String deleteWhere, String updateWhere) {
            status = 0;
            errorMsg = null;
            deleteWhereClause = deleteWhere;
            updateWhereClause = updateWhere;
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
            try {
                if (deleteWhereClause == null || deleteWhereClause.equals("")) {
                    Log.d("SQL", "No DELETE ");
                } else {
                    db.delete(DBHelper.TABLE_ANIMAL, deleteWhereClause, null);
                    Log.d("SQL", "DELETE FROM " + DBHelper.TABLE_ANIMAL + " WHERE " + deleteWhereClause + ";");
                    db.delete(DBHelper.TABLE_NEW_ANIMALS, deleteWhereClause, null);
                    Log.d("SQL", "DELETE FROM " + DBHelper.TABLE_NEW_ANIMALS + " WHERE " + deleteWhereClause + ";");
                }
                if (updateWhereClause == null || updateWhereClause.equals("")) {
                    Log.d("SQL", "No UPDATE ");
                } else {
                    ContentValues args = new ContentValues();
                    args.put(DBHelper.T_FAVORITE_ANIMALS_AVAILABLE, "N");
                    Log.d("SQL", "UPDATE " + DBHelper.TABLE_FAVORITE_ANIMALS + " SET " + DBHelper.T_FAVORITE_ANIMALS_AVAILABLE + "='N' WHERE " + updateWhereClause + ";");
                    db.update(DBHelper.TABLE_FAVORITE_ANIMALS, args, updateWhereClause, null);
                }
            } catch (Exception e) {
                Log.e("JOB", "DownloadAdoptableSearch.Job failed.  Reason:" + e.getMessage());
                status = 1;
            }
        }
    }
}

