package com.example.jedgar.spcav10;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;



public class NotificationPageFragment extends Fragment implements OnTimeSetListener, View.OnClickListener, GetActionBarTitle {
    static final int ALARM_ID = 1234567;
    static Alarm alarm;
    View rootView;
    CheckBox ck_alarm;
    //long interval=MainActivity.interv;

    RadioButton ck_heure, ck_sixheures, ck_douzeheures, ck_jours, ck_semaines;

    AlarmManager alarmManager;
    PendingIntent pendingintent;

    public static NotificationPageFragment newInstance(){

        NotificationPageFragment fragment = new NotificationPageFragment();
        return fragment;
    }

    public NotificationPageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.notification_page_fragment, container, false);

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


        etatAvant();


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(5);
    }

    /*
        * Est activé après avoir valider l'heure du reveil.
        * En fait on sauvegarde simplement la nouvelle heure. On l'affiche comme il faut et on replanifie le reveil
        * @see android.app.TimePickerDialog.OnTimeSetListener#onTimeSet(android.widget.TimePicker, int, int)
        */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Time t = new Time();
        t.hour = hourOfDay;
        t.minute = minute;
        alarm.setHeure(t);
    }
    /*
   * Job de planification du reveil
   */
    public void planifierAlarm() {
        int val=(int) (MainActivity.interval/1000) ;

        Intent intent = new Intent(this.getActivity(), AlarmReceiver.class);
        pendingintent = PendingIntent.getBroadcast(this.getActivity(), ALARM_ID, intent, 0);
        //pendingintent = PendingIntent.getService(this.getActivity(), 0, intent, 0);
        alarmManager = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingintent);
        Calendar reveil  = Calendar.getInstance();
        reveil.setTimeInMillis(System.currentTimeMillis());
        reveil.add(Calendar.SECOND, val);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reveil.getTimeInMillis(), MainActivity.interval, pendingintent);

    }
    public void cancel(){
        alarmManager = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingintent);
        Toast.makeText(this.getActivity(), "Notification desactivée!!!!!!!.", Toast.LENGTH_SHORT).show();
    }

    public void message(){
        if(ck_heure.isChecked()){
            Toast.makeText(this.getActivity(),"Notification activée chaque heure!", Toast.LENGTH_LONG).show();
        }else if(ck_sixheures.isChecked()){
            Toast.makeText(this.getActivity(),"Notification activée  chaque six heures!", Toast.LENGTH_LONG).show();
        }else if(ck_douzeheures.isChecked()){
            Toast.makeText(this.getActivity(),"Notification activée chaque douze heures!", Toast.LENGTH_LONG).show();
        }else if((ck_jours.isChecked())){
            Toast.makeText(this.getActivity(),"Notification activée chaque jour!", Toast.LENGTH_LONG).show();
        }else if((ck_semaines.isChecked())){
            Toast.makeText(this.getActivity(),"Notification activée chaque semaine!", Toast.LENGTH_LONG).show();
        }
    }
    public void optionActive(){
        // if(MainActivity.interval==AlarmManager.INTERVAL_HOUR){
        if(MainActivity.interval==((AlarmManager.INTERVAL_HOUR)/60)/2){
            ck_heure.setChecked(true);
        }else if(MainActivity.interval==6*(AlarmManager.INTERVAL_HOUR)){
            ck_sixheures.setChecked(true);
        }else if( MainActivity.interval==AlarmManager.INTERVAL_HALF_DAY){
            ck_douzeheures.setChecked(true);
        }else if(MainActivity.interval==AlarmManager.INTERVAL_DAY){
            ck_jours.setChecked(true);
        }else if(MainActivity.interval==(7*(AlarmManager.INTERVAL_DAY))){
            ck_semaines.setChecked(true);
        }
    }

    public void optionVisible(){
        ck_heure.setVisibility(View.VISIBLE);
        ck_sixheures.setVisibility(View.VISIBLE);
        ck_douzeheures.setVisibility(View.VISIBLE);
        ck_jours.setVisibility(View.VISIBLE);
        ck_semaines.setVisibility(View.VISIBLE);
        ck_alarm.setChecked(true);
    }
    public void optionInvisible(){
        ck_heure.setVisibility(View.INVISIBLE);
        ck_sixheures.setVisibility(View.INVISIBLE);
        ck_douzeheures.setVisibility(View.INVISIBLE);
        ck_jours.setVisibility(View.INVISIBLE);
        ck_semaines.setVisibility(View.INVISIBLE);
        ck_alarm.setChecked(false);
    }
    public void etatAvant(){
        //Toast.makeText(this.getActivity(), "Intervalll="+ MainActivity.interval, Toast.LENGTH_SHORT).show();
        if(MainActivity.interval==0) {
            //MainActivity.interval=0;
            optionInvisible();
            cancel();
        }else{
            optionVisible();
            optionActive();
            planifierAlarm();
            message();

        }

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.heure) {
            //juste pour tester j<ai fais un interval de 30 sec
            MainActivity.interval=((AlarmManager.INTERVAL_HOUR)/60)/2;
            //interval=AlarmManager.INTERVAL_HOUR;
            planifierAlarm();
            message();

        }else if (v.getId() == R.id.sixheures) {
            MainActivity.interval=6*(AlarmManager.INTERVAL_HOUR);
            //interval=(60*1000);
            planifierAlarm();
            message();

        }else if (v.getId() == R.id.douzeheures){
            MainActivity.interval=AlarmManager.INTERVAL_HALF_DAY;
            //interval=AlarmManager.INTERVAL_FIFTEEN_MINUTES;
            planifierAlarm();
            message();

        }else if (v.getId() == R.id.jours){
            MainActivity.interval=AlarmManager.INTERVAL_DAY;
            planifierAlarm();
            message();

        }else if(v.getId() == R.id.semaines){
            MainActivity.interval=7*(AlarmManager.INTERVAL_DAY);
            planifierAlarm();
            message();

        }else if(v.getId() == R.id.activer) {

            if (ck_alarm.isChecked()) {
                //Toast.makeText(this.getActivity(), "ischecked", Toast.LENGTH_SHORT).show();
                ck_heure.setVisibility(View.VISIBLE);
                ck_sixheures.setVisibility(View.VISIBLE);
                ck_douzeheures.setVisibility(View.VISIBLE);
                ck_jours.setVisibility(View.VISIBLE);
                ck_semaines.setVisibility(View.VISIBLE);
                if(MainActivity.interval!=0){
                    planifierAlarm();
                    message();
                }

            } else {
               // Toast.makeText(this.getActivity(), "isnot", Toast.LENGTH_SHORT).show();
                MainActivity.interval=1000;
                planifierAlarm();
                cancel();
                MainActivity.interval=0;
                ck_heure.setVisibility(View.INVISIBLE);
                ck_sixheures.setVisibility(View.INVISIBLE);
                ck_douzeheures.setVisibility(View.INVISIBLE);
                ck_jours.setVisibility(View.INVISIBLE);
                ck_semaines.setVisibility(View.INVISIBLE);
                ck_alarm.setChecked(false);


            }
        }
    }

    @Override
    public int getActionBarTitleId() {
        return R.string.notificationsTitle;
    }
}







