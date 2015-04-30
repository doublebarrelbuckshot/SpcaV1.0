package com.example.jedgar.spca;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.view.View;


public class AlarmReceiver extends BroadcastReceiver {
    boolean finishedDownload;
    boolean downloadErrors;
    DBHelper dbh;
    SQLiteDatabase db;
    Cursor c;
    // l'id unique correspondant à notre notification
    public static final int ID_NOTIFICATION = 1983;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (context == null) {
            Log.d("AlarmReceiver", "context == null");
            return;
        }

        dbh = DBHelper.getInstance(context);
        db = dbh.getWritableDatabase();

        /*
        String deleteWhereClause = DBHelper.T_ANIMAL_ID + " = 27648585 OR " + DBHelper.T_ANIMAL_ID + " = 19340025";
        db.delete(DBHelper.TABLE_ANIMAL, deleteWhereClause, null);
        Log.d("SQL", "DELETE FROM " + DBHelper.TABLE_ANIMAL + " WHERE " + deleteWhereClause + ";");
        db.delete(DBHelper.TABLE_NEW_ANIMALS, deleteWhereClause, null);
        Log.d("SQL", "DELETE FROM " + DBHelper.TABLE_NEW_ANIMALS + " WHERE " + deleteWhereClause + ";");

        Cursor animalList = dbh.getAnimalListOrdered(db, dbh.T_ANIMAL_ID + " asc ");

        finishedDownload = false;
        downloadErrors = false;

        // Log.d("avant2", "aloooo");

        //if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
        //if (("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()))){
        // Set the alarm here.

        try {
            Log.d("DownloadAdoptableSearch", "About to call trigger AdopdatableSearch in update mode.");
            new DownloadAdoptableSearch(context, animalList, new AlarmDownloadMSGDelegate(this)).execute();
        } catch (Exception e) {
            Log.d("DownloadAdoptableSearch", "Failure" + e.getMessage());
            return;
        }

        Log.d("Notifs", "7");

        while (finishedDownload == false) {
            Log.d("Notifs", "8");
            try {
                Thread.sleep((long) 3000);
            } catch (InterruptedException e) {
                Log.d("ACTIVIYYNOTI", "Sleep Failure: " + e.getMessage());
                return;
            }
        }

        Log.d("Notifs", "9");

        if (downloadErrors) {  // do not proceed with notif.
            Log.d("Notifs", "Download error.");
            return;
        }
        Log.d("Notifs", "A");
*/
        try {
            Log.d("Notifs", "Checking new.");
            SearchCriteria sc = new SearchCriteria(db);
            String sql = new String(sc.getCommandForNotifs());

           //KEEP THESE 2 LINES
            Log.d("Notifs", sql);

            dbh.setCursorForSelect(db, sql, DBHelper.CURSOR_NAME_NEW_ANIMALS);
            c = dbh.getCursorForSelect(DBHelper.CURSOR_NAME_NEW_ANIMALS);


            //TEST WITH c = FAVORITES TEST USING FAVORITES LIST
           // dbh.setCursorForFavoriteList(db);
            //c = dbh.getCursorForFavoriteList();
            //END TEST
            c.moveToFirst();
            if (c.isAfterLast() != true) {

                Log.d("Notifs", "Found new.");

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
                // Log.d("pending intent", "PENDING INTENT!!!!!!!");

                //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ActivityNotification.class), 0);

                //On définit le titre de la notification
                String titreNotification = context.getResources().getString(R.string.spcaMontreal);
                //On définit le texte qui caractérise la notification
                String texteNotification = context.getResources().getString(R.string.notificationText); //
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

                // update time stamp of last call datetime.
                dbh.setNotificationLastCall(db);
            }
            else {
                Log.d("ACTIVIYYNOTI", "NO ANIMALS, notification cancelled");
            }

        } catch (Exception e) {
            Log.d("Notif", e.getMessage());
        }
        //



    }

    public class AlarmDownloadMSGDelegate implements DownloadAdoptableSearch.AsyncTaskDelegate {


        AlarmReceiver alarmReceiver;

        public AlarmDownloadMSGDelegate(AlarmReceiver ar) {
            alarmReceiver = ar;
        }

        public void asyncTaskFinished(DownloadAdoptableSearch.DownloadAdoptableSearchResponse response) {

            Log.d("AlarmReceiver", "In asyncTaskFinished");
            alarmReceiver.finishedDownload = true;
            alarmReceiver.downloadErrors = false;

            Log.d("AlarmReceiver", "AScode:" + response.adoptableSearchErrorCode + " ADerrors:" + response.adoptableDetailsErrors + " postJobError:" + response.postJobError);
            if (response.adoptableSearchErrorCode != 0) {
                alarmReceiver.downloadErrors = true;
                return;
            }

            if (response.adoptableDetailsErrors > 0) {
                alarmReceiver.downloadErrors = true;
                return;
            }
        /*
        if (response.postJobError) { post Job is to flag favorites and delete animals not adoptable anymore.
            return;
        }*/
        }

        public void asyncTaskProgressUpdate(Integer... values) {
            Log.d("AlarmReceiver", "In asyncTaskProgressUpdate");
        }
    }
}
