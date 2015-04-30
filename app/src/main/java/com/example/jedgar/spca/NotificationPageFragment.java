package com.example.jedgar.spca;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;


public class NotificationPageFragment extends Fragment implements  View.OnClickListener, GetActionBarTitle {
    static final int ALARM_ID = 1234567;
    static Alarm alarm;
    View rootView;
    CheckBox ck_alarm;
    long interval;
    int val ;
    DBHelper dbh;
    SQLiteDatabase db;
    Cursor c;
    //long interval=MainActivity.interv;

    RadioButton ck_heure, ck_sixheures, ck_douzeheures, ck_jours, ck_semaines;
    RadioGroup radioInterval;
    AlarmManager alarmManager;
    PendingIntent pendingintent;


    public static NotificationPageFragment newInstance(){

        NotificationPageFragment fragment = new NotificationPageFragment();
        return fragment;
    }

    public NotificationPageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dbh = DBHelper.getInstance(getActivity());
        db = dbh.getWritableDatabase();



       /* SearchCriteria sc = new SearchCriteria(db);
        String sql = new String(sc.getCommandForNotifs());*/


        rootView = inflater.inflate(R.layout.notification_page_fragment, container, false);

        radioInterval=(RadioGroup)rootView.findViewById(R.id.radioInterval);
        radioInterval.setOnClickListener(NotificationPageFragment.this);

        ck_alarm = (CheckBox)rootView.findViewById(R.id.activer);
        ck_alarm.setOnClickListener(NotificationPageFragment.this);

        ck_heure = (RadioButton)rootView.findViewById(R.id.heure);
        ck_heure.setOnClickListener(NotificationPageFragment.this);


        ck_sixheures = (RadioButton)rootView.findViewById(R.id.sixheures);
        ck_sixheures.setOnClickListener(NotificationPageFragment.this);

        ck_douzeheures = (RadioButton)rootView.findViewById(R.id.douzeheures);
        ck_douzeheures.setOnClickListener(NotificationPageFragment.this);

        ck_jours = (RadioButton)rootView.findViewById(R.id.jours);
        ck_jours.setOnClickListener(NotificationPageFragment.this);

        ck_semaines = (RadioButton)rootView.findViewById(R.id.semaines);
        ck_semaines.setOnClickListener(NotificationPageFragment.this);


        c=dbh.getPreferences(db);
        if (c.moveToFirst()) {
            val=c.getInt(2);
        }
        if (val==60){
            interval=0;
            val=0;
        }else{
            interval=val*1000;
        }
        etatAvant();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(5);
    }


    /*
   * Job de planification du reveil
   */
    public void planifierAlarm() {
        val= (int) (interval/1000);
        ComponentName  component = new ComponentName(this.getActivity(), com.example.jedgar.spca.AlarmReceiver.class);
         int status = this.getActivity().getPackageManager().getComponentEnabledSetting(component);
            if(status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                Log.d("avantplanificationnnnn", "enableddd");
            }else if(status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
                this.getActivity().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED , PackageManager.DONT_KILL_APP);
                Log.d("avantplanificationnnnn", "enableddd");
        }
        Intent intent = new Intent(this.getActivity(), com.example.jedgar.spca.AlarmReceiver.class);
        pendingintent = PendingIntent.getBroadcast(this.getActivity(), ALARM_ID, intent, 0);
        alarmManager = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingintent);
        Calendar reveil  = Calendar.getInstance();
        reveil.setTimeInMillis(System.currentTimeMillis());
        reveil.add(Calendar.SECOND, val);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reveil.getTimeInMillis(), interval, pendingintent);
    }

    public void cancel(){
        alarmManager = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingintent);
        ComponentName component = new ComponentName(getActivity(), com.example.jedgar.spca.AlarmReceiver.class);
        int status = this.getActivity().getPackageManager().getComponentEnabledSetting(component);

        if(status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            Log.d("avantcancelll", "disabledddd");
        }else if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED){
            this.getActivity().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);
            Log.d("avantcancelll", "disabledddd");
        }
    }

    public void optionActive(boolean etat){
        ck_alarm.setChecked(etat);
        if(interval==((AlarmManager.INTERVAL_HOUR)/60)/2){
            ck_heure.setChecked(etat);
        }else if(interval==6*(AlarmManager.INTERVAL_HOUR)){
            ck_sixheures.setChecked(etat);
        }else if( interval==AlarmManager.INTERVAL_HALF_DAY){
            ck_douzeheures.setChecked(etat);
        }else if(interval==AlarmManager.INTERVAL_DAY){
            ck_jours.setChecked(etat);
        }else if(interval==(7*(AlarmManager.INTERVAL_DAY))){
            ck_semaines.setChecked(etat);
        }
    }

    public void etatAvant(){
        if(interval==0) {
            cancel();
            optionActive(false);
        }else{
            optionActive(true);
            planifierAlarm();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.heure) {
            interval=((AlarmManager.INTERVAL_HOUR)/60)/2;
            //interval=AlarmManager.INTERVAL_HOUR;
            ck_alarm.setChecked(true);
            planifierAlarm();
        }else if (v.getId() == R.id.sixheures) {
            interval=6*(AlarmManager.INTERVAL_HOUR);
            //interval=(60*1000);
            ck_alarm.setChecked(true);
            planifierAlarm();
        }else if (v.getId() == R.id.douzeheures){
            interval=AlarmManager.INTERVAL_HALF_DAY;
            ck_alarm.setChecked(true);
            planifierAlarm();
        }else if (v.getId() == R.id.jours){
            interval=AlarmManager.INTERVAL_DAY;
            ck_alarm.setChecked(true);
            planifierAlarm();
        }else if(v.getId() == R.id.semaines){
            interval=7*(AlarmManager.INTERVAL_DAY);
            ck_alarm.setChecked(true);
            planifierAlarm();
        }else if(v.getId() == R.id.activer) {
            if (ck_alarm.isChecked()==false) {
                cancel();
                radioInterval.clearCheck();
                interval=0;
                val=0;
            }
        }


        if(interval!=0){
          val= (int) (interval/1000);
          dbh.setNotifications(db,"Y",val);
        }else{
          val= 60;
          dbh.setNotifications(db,"Y",val);
        }
    }

    @Override
    public int getActionBarTitleId() {
        return R.string.notificationsTitle;
    }
}







