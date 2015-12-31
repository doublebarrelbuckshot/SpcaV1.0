//package com.example.jedgar.spca;
//
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.util.Log;
//
//import org.xml.sax.SAXException;
//
//import java.io.IOException;
//import java.util.logging.Handler;
//import java.util.logging.LogRecord;
//
//import javax.xml.parsers.ParserConfigurationException;
//
///**
// * Created by pascal on 29/04/15.
// */
//public class IntentServiceSPAC extends IntentService {
//
//    boolean downloadErrors;
//    DBHelper dbh;
//    SQLiteDatabase db;
//    SPCAWebAPI web;
//
//    public IntentServiceSPAC() {
//        super("IntentServiceSPAC");
//    }
//
//    private boolean downloadDetails(int i) {
//        Animal a;
//        try {
//            a = web.callAdoptableDetails(i);
//        }
//        catch (IOException e) {
//            Log.e("IntentServiceSPAC", e.getMessage());
//            return false;
//        }
//        catch (SAXException e) {
//            Log.e("IntentServiceSPAC", e.getMessage());
//            return false;
//        }
//        catch (ParserConfigurationException e) {
//            Log.e("IntentServiceSPAC", e.getMessage());
//            return false;
//        }
//        catch (Exception e) {
//            Log.e("IntentServiceSPAC", e.getMessage());
//            return false;
//        }
//
//        try {
//            dbh.addAnimal(db, a);
//        }
//        catch (SQLException e) {
//            Log.e("IntentServiceSPAC", e.getMessage());
//            return false;
//        }
//
//        return true;
//    }
//
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        dbh = DBHelper.getInstance(this);
//        db = dbh.getWritableDatabase();
//
//        downloadErrors = false;
//
//        /* re-inventing the wheel with the follow.
//         * extracted from DownloadAdoptableSearch AsynchTask
//         */
//        web = new SPCAWebAPI();
//        try {
//            web.callAdoptableSearch();
//        } catch (IOException e) {
//            //errorCode = 1;
//            Log.e("IntentServiceSPAC", e.getMessage());
//            downloadErrors = true;
//        } catch (SAXException e) {
//            //errorCode = 1;
//            Log.e("IntentServiceSPAC", e.getMessage());
//            downloadErrors = true;
//        } catch (ParserConfigurationException e) {
//            //errorCode = 1;
//            Log.e("IntentServiceSPAC", e.getMessage());
//            downloadErrors = true;
//        } catch (Exception e) {
//            //errorCode = 1;
//            Log.e("IntentServiceSPAC", e.getMessage());
//            downloadErrors = true;
//        }
//
//        if (downloadErrors) {
//            Log.d("IntentServiceSPAC", "Completed service with error @ " + SystemClock.elapsedRealtime());
//            AlarmReceiver.completeWakefulIntent(intent);
//            return;
//        }
//
//        Log.d("IntentServiceSPAC:", "Processing list of " + web.animals.size() + " elements.");
//        int size = web.animals.size();
//
//        String where = "";
//        String updateWhere = "";
//
//        Cursor animalList = dbh.getAnimalListOrdered(db, dbh.T_ANIMAL_ID + " asc ");
//
//        Log.d("IntentServiceSPAC", "animalList item count is " + animalList.getCount());
//        animalList.moveToFirst();
//            /*
//            if (!animalList.isAfterLast()) {
//                //Log.d("ALIST", animalList.getString(0));
//            }*/
//        int i = 0;
//        int aidx = 0;
//        while (i < size && !animalList.isAfterLast()) {
//            String aID = animalList.getString(DBHelper.C_ANIMAL_ID);
//            Animal animal = web.animals.get(i);
//            if (aID.equals(animal.id)) {
//                animalList.moveToNext();
//                    /*
//                    if (!animalList.isAfterLast())
//                        Log.d("ALIST", animalList.getString(0));
//                        */
//                i++;
//            } else {
//                int db_id =  Integer.parseInt(aID);
//                int web_id = Integer.parseInt(animal.id);
//                if (db_id < web_id) {
//                    if (dbh.isFavorite(db, aID)) {
//                        Cursor fav = dbh.getFavoriteAnimal(db, aID);
//                        fav.moveToFirst();
//                            /*
//                            Log.d("ALIST", animalList.getString(0) + " favorite no longer available.  Your flag:" + fav.getString(DBHelper.C_FAVORITE_ANIMAL_AVAILABLE));
//                            */
//                        if (updateWhere.equals(""))
//                            updateWhere += " " + DBHelper.T_FAVORITE_ANIMALS_ANIMAL_ID + " = '" + db_id + "'";
//                        else
//                            updateWhere += " OR " + DBHelper.T_FAVORITE_ANIMALS_ANIMAL_ID + " = '" + db_id + "'";
//
//                    } else {
//                        //Log.d("ALIST", animalList.getString(0) + " no longer available.");
//                        if (where.equals(""))
//                            where += " " + DBHelper.T_ANIMAL_ID + " = '" + db_id + "'";
//                        else
//                            where += " OR " + DBHelper.T_ANIMAL_ID + " = '" + db_id + "'";
//                    }
//                    animalList.moveToNext();
//                } else {
//                    //Log.d("ALIST", web.animals.get(i).id + " NEW!.");
//                    if (downloadErrors == false)
//                        downloadErrors = downloadDetails(i);
//                    else
//                        downloadDetails(i);
//                    i++;
//                }
//            }
//        }
//        while(animalList.isAfterLast() == false) {
//            String aID = animalList.getString(DBHelper.C_ANIMAL_ID);
//            if (dbh.isFavorite(db, aID)) {
//                if (updateWhere.equals(""))
//                    updateWhere += " " + DBHelper.T_ANIMAL_ID + " = '" + aID + "'";
//                else
//                    updateWhere += " OR " + DBHelper.T_ANIMAL_ID + " = '" + aID + "'";
//            }
//
//            animalList.moveToNext();
//                /*
//                if (!animalList.isAfterLast())
//                    Log.d("ALIST", animalList.getString(0));
//                */
//        }
//        while(i < size) {
//            if (downloadErrors == false)
//                downloadErrors = downloadDetails(i);
//            else
//                downloadDetails(i);
//            i++;
//        }
//
//        try {
//            if (where == null || where.equals("")) {
//                Log.d("IntentServiceSPAC", "No DELETE ");
//            } else {
//                db.delete(DBHelper.TABLE_ANIMAL, where, null);
//                Log.d("IntentServiceSPAC", "DELETE FROM " + DBHelper.TABLE_ANIMAL + " WHERE " + where + ";");
//                db.delete(DBHelper.TABLE_NEW_ANIMALS, where, null);
//                Log.d("IntentServiceSPAC", "DELETE FROM " + DBHelper.TABLE_NEW_ANIMALS + " WHERE " + where + ";");
//            }
//            if (updateWhere == null || updateWhere.equals("")) {
//                Log.d("IntentServiceSPAC", "No UPDATE ");
//            } else {
//                ContentValues args = new ContentValues();
//                args.put(DBHelper.T_FAVORITE_ANIMALS_AVAILABLE, "N");
//                Log.d("IntentServiceSPAC", "UPDATE " + DBHelper.TABLE_FAVORITE_ANIMALS + " SET " + DBHelper.T_FAVORITE_ANIMALS_AVAILABLE + "='N' WHERE " + updateWhere + ";");
//                db.update(DBHelper.TABLE_FAVORITE_ANIMALS, args, updateWhere, null);
//            }
//        } catch (Exception e) {
//            Log.e("IntentServiceSPAC", "DownloadAdoptableSearch.Job failed.  Reason:" + e.getMessage());
//        }
//
//        /*
//        if (downloadErrors) {  // do not proceed with notif.
//            Log.d("SimpleWakefulReceiver", "Completed service with error2 @ " + SystemClock.elapsedRealtime());
//            AlarmReceiver.completeWakefulIntent(intent);
//            return;
//        }*/
//
//        try {
//            Log.d("IntentServiceSPAC", "Checking new.");
//            SearchCriteria sc = new SearchCriteria(db);
//            String sql;
//
//            sql = new String(sc.getCommandForNotifs());
//            Log.d("IntentServiceSPCA", sql);
//            Cursor c = db.rawQuery(sql, null);
//            if (c == null) {
//                Log.e("IntentServiceSPAC", "null cursor for " +  sql);
//                return;
//            }
//
//            c.moveToFirst();
//            if (c.isAfterLast() != true) {
//
//                Log.d("Notifs", "Found new.");
//
//                //On crée un "gestionnaire de notification"
//                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//                //On crée la notification Avec son icône et son texte défilant (optionnel si l'on ne veut pas de texte défilant on met cet argument à null)
//                Notification notification = new Notification(R.drawable.notification, "SPCA", System.currentTimeMillis());
//
//
//                // update time stamp of last call datetime.
//                // un peu cheap à mon goût, mais temps oblige.
//                //String rangeFrom = dbh.getNotificationLastCall(db);
//                //dbh.setNotificationLastCall(db);
//                //String rangeTo = dbh.getNotificationLastCall(db);
//
//                //sql = new String(sc.getCommandForNotifs());
//                //sql = new String(sc.getCommandForNotifs());
//                Log.d("IntentServiceSPCA", sql);
//
//                Intent intent2 = new Intent(this, MainActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                Bundle b = new Bundle();
//                b.putString("sender", DBHelper.CURSOR_NAME_NOTIFICATIONS);
//                b.putString("command", sql);
//                intent2.putExtras(b);
//                intent2.setAction("TESTSTRINGWORKED");
//                //   intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2, 0);
//
//
//                //Le PendingIntent c'est ce qui va nous permettre d'atteindre notre deuxième Activity
//                //ActivityNotification sera donc le nom de notre seconde Activity
//                // Log.d("pending intent", "PENDING INTENT!!!!!!!");
//
//                //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ActivityNotification.class), 0);
//
//                //On définit le titre de la notification
//                String titreNotification = "SPCA";
//                //On définit le texte qui caractérise la notification
//                String texteNotification = "Il y a des nouveaux animaux qui correspond à vos criteres de recherche...";
//                //On configure notre notification avec tous les paramètres que l'on vient de créer
//                notification.setLatestEventInfo(this, titreNotification, texteNotification, pendingIntent);
//                //On regle la notification pour que ca suit le reglage du telephone
//
//                notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
//                notification.ledARGB = Color.BLUE;
//                notification.ledOnMS = 500;
//                notification.ledOffMS = 500;
//                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//
//                //Enfin on ajoute notre notification et son ID à notre gestionnaire de notification
//                notificationManager.notify(AlarmReceiver.ID_NOTIFICATION, notification);
//            }
//            else {
//                Log.d("ACTIVIYYNOTI", "NO ANIMALS, notification cancelled");
//            }
//
//        } catch (Exception e) {
//            Log.d("Notif", e.getMessage());
//        }
//    }
//
//
//    public void asyncTaskFinished(DownloadAdoptableSearch.DownloadAdoptableSearchResponse response) {
//
//        Log.d("AlarmReceiver", "In asyncTaskFinished");
//        downloadErrors = false;
//
//        Log.d("AlarmReceiver", "AScode:" + response.adoptableSearchErrorCode + " ADerrors:" + response.adoptableDetailsErrors + " postJobError:" + response.postJobError);
//        if (response.adoptableSearchErrorCode != 0) {
//            downloadErrors = true;
//            return;
//        }
//
//        if (response.adoptableDetailsErrors > 0) {
//            downloadErrors = true;
//            return;
//        }
//    /*
//    if (response.postJobError) { post Job is to flag favorites and delete animals not adoptable anymore.
//        return;
//    }*/
//    }
//
//    public void asyncTaskProgressUpdate(Integer... values) {
//        Log.d("AlarmReceiver", "In asyncTaskProgressUpdate");
//    }
//
//}
