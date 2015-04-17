package com.example.jedgar.spcav10;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by pascal on 15/04/15.
 */
public class DownloadAdoptableSearch extends AsyncTask<Void, Integer, Void> {

    static final int downloadRangeSizePerThread = 1;
    SPCAWebAPI web;
    ProgressBar progressBar;
    TextView progressText;
    DBHelper dbh;
    Button searchButton;
    boolean refresh;
    Cursor animalList = null;

    public DownloadAdoptableSearch(ProgressBar pProgressBar,
                                   TextView pProgressText,
                                   Button psearchButton,
                                   Context activity){
        dbh = DBHelper.getInstance(activity);
        progressBar = pProgressBar;
        progressText = pProgressText;
        searchButton = psearchButton;
        refresh = false;
        animalList = null;
    }

    public DownloadAdoptableSearch(ProgressBar pProgressBar,
                                   TextView pProgressText,
                                   Button psearchButton,
                                   Context activity,
                                   Cursor panimalList){
        dbh = DBHelper.getInstance(activity);
        progressBar = pProgressBar;
        progressText = pProgressText;
        searchButton = psearchButton;
        refresh = true;
        animalList = panimalList;
    }
    @Override
    protected Void doInBackground(Void... params) {

        publishProgress(0);

        web = new SPCAWebAPI();

        try {
            web.callAdoptableSearch();
        } catch (Exception e) {
            Log.d("doInBackground:", "call AdoptableSearch.");
            Log.d("Reason        :", e.getMessage());
            return null;
        }
        publishProgress(new Integer[]{1, web.animals.size() + 5});

        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(4,
                        4,
                        90,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );

        publishProgress(5);
        Log.d("DownloadWebTask:", "Processing list of " + web.animals.size() + " elements.");
        int size = web.animals.size();
        long threadCount = 0;
        if (refresh == false) {

            for (int i = 0; i < size; i += downloadRangeSizePerThread) {
                executor.execute(new DownloadWebAdoptableDetailsTask(
                        web,
                        dbh,
                        dbh.getWritableDatabase(),
                        i,
                        Math.min(i + downloadRangeSizePerThread, size)));
                threadCount++;
            }
        } else {
            String where = "";
            Log.d("DownloadAdoptableSearch", "animalList item count is " + animalList.getCount());
            animalList.moveToFirst();
            int animals = animalList.getCount();
            int i = 0;
            int aidx = 0;
            while (i < size && !animalList.isAfterLast()) {
                String aID = animalList.getString(DBHelper.C_ANIMAL_ID);
                Animal animal = web.animals.get(i);
                if (aID.equals(animal.id)) {
                    animalList.moveToNext();
                    i++;
                } else {
                    int db_id =  Integer.parseInt(aID);
                    int web_id = Integer.parseInt(animal.id);
                    if (db_id < web_id) {
                        where += " a.id = '" + db_id + "'";
                        animalList.moveToNext();
                    } else {
                        executor.execute(new DownloadWebAdoptableDetailsTask(
                                web,
                                dbh,
                                dbh.getWritableDatabase(),
                                i,
                                Math.min(i + 1, size)));
                        threadCount++;
                        i++;
                    }
                }
            }
            while(animalList.isAfterLast() == false) {
                String aID = animalList.getString(DBHelper.C_ANIMAL_ID);
                where += " a.id = '" + aID + "'";
                animalList.moveToNext();
            }
            while(i < size) {
                executor.execute(new DownloadWebAdoptableDetailsTask(
                        web,
                        dbh,
                        dbh.getWritableDatabase(),
                        i,
                        Math.min(i + 1, size)));
                threadCount++;
                i++;
            }

            executor.execute(new Job(where));
            threadCount++;
        }

        long completed = 0;
        for(;completed < threadCount;) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e("Thread", e.getMessage());
            }
            completed = executor.getCompletedTaskCount();
            final int finalCompleted = (int)completed;
            publishProgress(finalCompleted + 5);
            Log.d("DownloadWebTask:", executor.getCompletedTaskCount() + " completed out of " + threadCount + " scheduled...");
        }

        executor.shutdown();

        return null;
    }

    @Override
    protected void onProgressUpdate (Integer... values) {
        Integer progress = values[0];
        switch (progress) {
            case 0:
                searchButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                progressText.setVisibility(View.VISIBLE);
                progressBar.incrementProgressBy(1);
                break;
            case 1:
                progressBar.incrementProgressBy(5);
                progressBar.setMax(values[1] + 6);
                break;
            case 2:
                progressBar.incrementProgressBy(1);
                break;
            default:
                progressBar.setProgress(6 + progress);
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        // btn.setEnabled(false);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void v) {
        progressBar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
    }

    public class Job implements Runnable {

        String whereClause;
        Job(String where) {
            whereClause = where;
        }
        @Override
        public void run() {
            dbh.deleteAnimalList(dbh.getWritableDatabase(), whereClause);
        }
    }
}

