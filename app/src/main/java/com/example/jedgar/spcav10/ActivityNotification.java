package com.example.jedgar.spcav10;

import android.app.Activity;
import android.app.NotificationManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityNotification extends Activity {

    DBHelper dbh;
    SQLiteDatabase db;
    Cursor c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "yyyyyyyyyyyyyyyyyyyyys" + c.isAfterLast(), Toast.LENGTH_SHORT).show();
        Log.d("ACTIVIYYNOTI", "BEFORE");



        dbh = DBHelper.getInstance(this);
        db = dbh.getWritableDatabase();

        SearchCriteria sc = new SearchCriteria(db);
        String sql = new String(sc.getCommandForNotifs());
       //KEEP THESE 2 LINES
       // dbh.setCursorForSelect(db, sql, DBHelper.CURSOR_NAME_NEW_ANIMALS);
      //  c = dbh.getCursorForSelect(DBHelper.CURSOR_NAME_NEW_ANIMALS);
//

        //TEST WITH c = FAVORITES
        dbh.setCursorForFavoriteList(db);
        c = dbh.getCursorForFavoriteList();
        //END TEST

        c.moveToFirst();
        if (c.isAfterLast() != false) {
            Log.d("ACTIVIYYNOTI", "NO ANIMALS");

            Toast.makeText(this, "No New Animals" + c.isAfterLast(), Toast.LENGTH_SHORT).show();
            //setContentView(R.layout.info_page_fragment);
           // Intent intent = new Intent(this, MainActivity.class);
          //  startActivity(intent);
           // getIntent().putExtra("sender", DBHelper.CURSOR_NAME_NEW_ANIMALS);
            setContentView( R.layout.info_page_fragment);
            //fragmentManager.beginTransaction()
//                    .replace(R.id.container, BrowsePageFragment.newInstance())
//                    .addToBackStack(null)
//                    .commit();
//            mTitle = getString(R.string.newTitle);
//
        }

else {
            TextView txt = new TextView(this);
            Log.d("ACTIVIYYNOTI", "TESTESTESTEST");

            txt.setText("TESTESTEST");
            //On ajoute notre TextView à la vue
            setContentView(txt);
        }
       // c.moveToFirst();
      //  Toast.makeText(this, "c.isAfterLast()" + c.isAfterLast(), Toast.LENGTH_SHORT).show();
       // if (c.isAfterLast() != true)

            //On crée un TextView en Java
           /* TextView txt=new TextView(this);
        txt.setText();
        //On ajoute notre TextView à la vue
        setContentView(txt);*/

        //BrowsePageFragment.newFrame();

        //On supprime la notification de la liste de notification comme dans la méthode cancelNotify de l'Activity principale
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmReceiver.ID_NOTIFICATION);
    }
}


