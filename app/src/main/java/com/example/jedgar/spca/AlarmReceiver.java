//package com.example.jedgar.spca;
//
//
//import android.app.AlertDialog;
//import android.app.IntentService;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//
//
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.support.v4.content.WakefulBroadcastReceiver;
//import android.util.Log;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.view.View;
//
//
//public class AlarmReceiver extends WakefulBroadcastReceiver {
//    boolean finishedDownload;
//    boolean downloadErrors;
//    DBHelper dbh;
//    SQLiteDatabase db;
//    Cursor c;
//    // l'id unique correspondant à notre notification
//    public static final int ID_NOTIFICATION = 1983;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        Log.d("AlarmReceiver", "onReceive");
//
//        if (context == null) {
//            Log.d("AlarmReceiver", "context == null");
//            return;
//        }
//
//        Intent service = new Intent(context, IntentServiceSPAC.class);
//        //Intent intent = new Intent(this.getActivity(), com.example.jedgar.spca.IntentServiceSPAC.class);
//        Log.d("IntentServiceSPAC", "Starting service @ " + SystemClock.elapsedRealtime());
//        context.startService(service);
//
//    }
//}
