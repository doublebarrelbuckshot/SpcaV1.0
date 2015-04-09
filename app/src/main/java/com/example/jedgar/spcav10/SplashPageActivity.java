package com.example.jedgar.spcav10;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import static java.lang.Thread.sleep;


public class SplashPageActivity extends ActionBarActivity  {//implementsView.OnClickListener {
    static SplashPageActivity currentAct;
    // Button btn;
    ListView listv;
    TextView titre;
    ProgressBar progressBar;
    DBHelper dbh;
    SQLiteDatabase db;
    MyAdapter adapter;
    SPCAWebAPI web;
    static final int downloadRangeSizePerThread = 1;
    ImageView img;
    boolean toggle = false;
    int action = 0;

    // Indexes des champs dans les cursors
    // si c'est pas les bons indexes, corriger stp.
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
    public static final int C_ANIMAL_DECLAWED = 12;
    public static final int C_ANIMAL_DESCRIPTION = 13;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        currentAct = this;

        progressBar = (ProgressBar)findViewById(R.id.webProgressBar);
        progressBar.setVisibility(View.GONE);

        //titre.setText("Main Activity!");

        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();
        Cursor c = dbh.getAnimalList(db);

        adapter = new MyAdapter(this, c);

        //btn.setOnClickListener(this);
        // btn.performClick();
        getData();
        //  listv.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // @Override
    public void getData(){//View v) {

        /* this was all for testing */
        if (toggle == false) {
            Toast.makeText(this, "Chargement des donnees du Web", Toast.LENGTH_SHORT).show();
           new DownloadWebTask().execute();// REMOVE COMMENT FOR PROPER FUNCTIONING
            //currentAct.loadMainActivity(); //SET COMMENT FOR PROPER FUNCTIONING

            toggle = true;
        }
        else {
            // btn.setEnabled(false);
            toggle = false;
            if (action == 0) {
                Log.d("onClick:", "action == " + action);
                dbh.addToFavoriteList(db, "25289349");
                dbh.addToFavoriteList(db, "24296807");
                dbh.addToFavoriteList(db, "25020484");
            }
            else if (action == 1) {
                Log.d("onClick:", "action == " + action);
                dbh.removeFromFavoriteList(db, "24296807");
            }
            else if (action == 2) {
                Log.d("onClick:", "action == " + action);
                dbh.removeAllFromFavoriteList(db);
                action = 0;
            }
            action++;
            Toast.makeText(this, "Chargement des favoris", Toast.LENGTH_SHORT).show();
            Cursor c = dbh.getFavoriteList(db);
            adapter.changeCursor(c);
            //btn.setEnabled(true);
        }
    }

    public void loadMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public class DownloadWebTask extends AsyncTask<Void, Void, Void>{

        public DownloadWebTask(){

        }
        @Override
        protected Void doInBackground(Void... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.incrementProgressBy(1);
                }
            });

            web = new SPCAWebAPI();

            try {
                web.callAdoptableSearch();
            } catch (Exception e) {
                Log.d("doInBackground:", "call AdoptableSearch.");
                Log.d("Reason        :", e.getMessage());
                return null;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.incrementProgressBy(5);
                    progressBar.setMax(web.animals.size() + 6);
                }
            });


            ThreadPoolExecutor executor =
                    new ThreadPoolExecutor(4,
                            4,
                            90,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>()
                    );

            Log.d("DownloadWebTask:", "Processing list of " + web.animals.size() + " elements.");
            String localStoragePath = getFilesDir().getAbsolutePath();
            int size = web.animals.size();
            long threadCount = 0;
            for(int i = 0; i < size; i+=downloadRangeSizePerThread) {
                executor.execute(new DownloadWebAdoptableDetailsTask(
                        web,
                        localStoragePath,
                        dbh,
                        dbh.getWritableDatabase(),
                        i,
                        Math.min(i + downloadRangeSizePerThread, size)));
                threadCount++;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.incrementProgressBy(1);
                }
            });

            long completed = 0;
            for(;completed < threadCount;) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("Thread", e.getMessage());
                }
                completed = executor.getCompletedTaskCount();
                final long finalCompleted = completed;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(3 + (int) finalCompleted);
                    }
                });
                Log.d("DownloadWebTask:", executor.getCompletedTaskCount() + " completed out of " + threadCount + " scheduled...");
            }

            executor.shutdown();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            // btn.setEnabled(false);
            /* for testing
            if (action == 0) {
                dbh.deleteAll(db);
            }*/
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void v) {
            // btn.setEnabled(true);
            Cursor c = dbh.getAnimalList(db);
            adapter.changeCursor(c);
            currentAct.loadMainActivity();
        }
    }

    public class MyAdapter extends CursorAdapter {
        LayoutInflater inflater;

        public MyAdapter(Context context, Cursor c) {
            super(context, c, true);
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(v==null){
                v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            Cursor c = getCursor();
            c.moveToPosition(position);
/*
            public static final int C_ANIMAL_ID = 0;
            public static final int C_ANIMAL_SPECIES = 1;
            public static final int C_ANIMAL_NAME = 2;
            public static final int C_ANIMAL_AGE = 3;

             ***** for complete list check at the top of the class definition
*/
            String t = Integer.toString(c.getInt(C_ANIMAL_ID));
            String d = c.getString(C_ANIMAL_NAME);

            TextView titre = (TextView)v.findViewById(android.R.id.text1);
            TextView description = (TextView)v.findViewById(android.R.id.text2);
            titre.setText(t);
            description.setText(d);

            return v;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }
}
