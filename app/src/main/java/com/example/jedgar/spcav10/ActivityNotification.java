package com.example.jedgar.spcav10;

import android.app.Activity;
import android.app.NotificationManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityNotification extends Activity {

    DBHelper dbh;
    //SQLiteDatabase db;
    Cursor c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browse_row);


        SearchCriteria sc = new SearchCriteria(BrowsePageFragment.db);
        String sql = new String(sc.getCommandForNotifs());
        dbh.setCursorForSelect(BrowsePageFragment.db, sql, DBHelper.CURSOR_NAME_NEW_ANIMALS);
        c = dbh.getCursorForSelect(DBHelper.CURSOR_NAME_NEW_ANIMALS);

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


