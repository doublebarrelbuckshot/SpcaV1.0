package com.example.jedgar.spcav10;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import android.app.Notification;
import android.app.NotificationManager;



public class AlarmReceiver extends BroadcastReceiver{
    DBHelper dbh;
    SQLiteDatabase db;
    Cursor c;
    // l'id unique correspondant à notre notification
    public static final int ID_NOTIFICATION = 1983;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            dbh = DBHelper.getInstance(context);
            db = dbh.getWritableDatabase();

            SearchCriteria sc = new SearchCriteria(db);
            String sql = new String(sc.getCommandForNotifs());
           //KEEP THESE 2 LINES
            dbh.setCursorForSelect(db, sql, DBHelper.CURSOR_NAME_NEW_ANIMALS);
            c = dbh.getCursorForSelect(DBHelper.CURSOR_NAME_NEW_ANIMALS);


            //TEST WITH c = FAVORITES TEST USING FAVORITES LIST
           // dbh.setCursorForFavoriteList(db);
           // c = dbh.getCursorForFavoriteList();
            //END TEST
            c.moveToFirst();
            if (c.isAfterLast() != true) {



                //On crée un "gestionnaire de notification"
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                //On crée la notification Avec son icône et son texte défilant (optionnel si l'on ne veut pas de texte défilant on met cet argument à null)
                Notification notification = new Notification(R.drawable.notification, "SPCA", System.currentTimeMillis());


                Intent intent2 = new Intent(context, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Bundle b = new Bundle();


                b.putString("key", "TESTSTRINGWORKED");
                intent2.putExtras(b);

                intent2.setAction("TESTSTRINGWORKED");
                //   intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);


                //Le PendingIntent c'est ce qui va nous permettre d'atteindre notre deuxième Activity
                //ActivityNotification sera donc le nom de notre seconde Activity
                Log.d("pending intent", "PENDING INTENT!!!!!!!");

                //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ActivityNotification.class), 0);

                //On définit le titre de la notification
                String titreNotification = "SPCA";
                //On définit le texte qui caractérise la notification
                String texteNotification = "Il y a des nouveaux animaux qui correspond à vos criteres de recherche...";
                //On configure notre notification avec tous les paramètres que l'on vient de créer
                notification.setLatestEventInfo(context, titreNotification, texteNotification, pendingIntent);
                //On regle la notification pour que ca suit le reglage du telephone

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
                notification.ledARGB = Color.BLUE;
                notification.ledOnMS = 500;
                notification.ledOffMS = 500;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;

                //Enfin on ajoute notre notification et son ID à notre gestionnaire de notification
                notificationManager.notify(ID_NOTIFICATION, notification);
            }
            else {
            Log.d("ACTIVIYYNOTI", "NO ANIMALS, notification cancelled");
        }

        } catch (Exception e) {
            Log.d("Notif", e.getMessage());
        }
    }
}
