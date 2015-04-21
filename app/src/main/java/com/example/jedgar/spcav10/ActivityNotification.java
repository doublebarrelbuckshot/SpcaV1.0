package com.example.jedgar.spcav10;

import android.app.Activity;
import android.app.NotificationManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.widget.TextView;


public class ActivityNotification extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*DBHelper dbh;
        SQLiteDatabase db;

         Cursor c;*/

        //On crée un TextView en Java
       /* TextView txt=new TextView(this);
        txt.setText("Voici à l'Activity qui apparait lorsque l'on clique sur la notification !");

        //On ajoute notre TextView à la vue
        setContentView(txt);
*/
        /*SearchCriteria sc = new SearchCriteria(BrowsePageFragment.db);
        String sql = new String(sc.getCommandForNotifs());
        dbh.setCursorForSelect(db, sql, DBHelper.CURSOR_NAME_NEW_ANIMALS);
        c = dbh.getCursorForSelect(DBHelper.CURSOR_NAME_NEW_ANIMALS);*/
        BrowsePageFragment.newFrame();

        //On supprime la notification de la liste de notification comme dans la méthode cancelNotify de l'Activity principale
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(com.example.jedgar.spcav10.AlarmReceiver.ID_NOTIFICATION);
    }



}