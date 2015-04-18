package com.example.jedgar.spcav10;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;




public class NotificationPageFragment extends Fragment implements OnTimeSetListener, View.OnClickListener {
    static final int ALARM_ID = 1234567;
    static com.example.jedgar.spcav10.Alarm alarm;
    static  View rootView;
    static CheckBox ck_alarm;
    static AlarmManager am;
    PendingIntent pendingintent;
    public static NotificationPageFragment newInstance(){

        NotificationPageFragment fragment = new NotificationPageFragment();
        return fragment;
    }

    public NotificationPageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.notification_page_fragment, container, false);
        ck_alarm = (CheckBox)rootView.findViewById(R.id.heure);
        ck_alarm.setOnClickListener(NotificationPageFragment.this);

        //Chargement des informations du reveil
        charger();
        //Affichage
        //CheckBox ck_alarm = (CheckBox)rootView.findViewById(R.id.heure);
        affichage();
        //Planification
        planifierAlarm();
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
        affichage();
        planifierAlarm();
    }

    /*
     * Chargement des informations du reveil.
     * Ici pour la sauvegarde on a simplement déserialiser l'objet Alarm.
     */
    public void charger(){
        alarm = null;
        try {
            ObjectInputStream alarmOIS= new ObjectInputStream(this.getActivity().openFileInput("alarm.serial"));
            alarm = (com.example.jedgar.spcav10.Alarm) alarmOIS.readObject();
            alarmOIS.close();

        }
        catch(FileNotFoundException fnfe){
            alarm = new com.example.jedgar.spcav10.Alarm();
            alarm.setActive(true);
            Time t = new Time();
            t.hour = 7;
            t.minute = 30;
            alarm.setHeure(t);

        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }


    public static void affichage() {
        //Ici on a juste voulu créer un affichage de l'heure qui soit au format hh:mm.

        String heureReveil = "";
        heureReveil += alarm.getHeure().hour >10 ? alarm.getHeure().hour : "0" + alarm.getHeure().hour;
        heureReveil +=":";
        heureReveil += alarm.getHeure().minute >10 ? alarm.getHeure().minute : "0" + alarm.getHeure().minute;
        ck_alarm = (CheckBox)rootView.findViewById(R.id.heure);
        ck_alarm.setText(heureReveil);
        ck_alarm.setChecked(alarm.isActive());
    }


    /*
   * Job de planification du reveil
   */
    public void planifierAlarm() {
        //Récupération de l'instance du service AlarmManager.
         am = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
        //On instancie l'Intent qui va être appelé au moment du reveil.
        Intent intent = new Intent(this.getActivity(), com.example.jedgar.spcav10.AlarmReceiver.class);

        //On créer le pending Intent qui identifie l'Intent de reveil avec un ID et un/des flag(s)
         pendingintent = PendingIntent.getBroadcast(this.getActivity(), ALARM_ID, intent, 0);

        //On annule l'alarm pour replanifier si besoin
        am.cancel(pendingintent);

        //La on va déclencher un calcul pour connaitre le temps qui nous sépare du prochain reveil.
        Calendar reveil  = Calendar.getInstance();
        reveil.set(Calendar.HOUR_OF_DAY, alarm.getHeure().hour);
        reveil.set(Calendar.MINUTE, alarm.getHeure().minute);
        if(reveil.compareTo(Calendar.getInstance()) == -1)
            reveil.add(Calendar.DAY_OF_YEAR, 1);

        Calendar cal = Calendar.getInstance();
        reveil.set(Calendar.SECOND, 0);
        cal.set(Calendar.SECOND, 0);
        long diff = reveil.getTimeInMillis() - cal.getTimeInMillis();

        //On ajoute le reveil au service de l'AlarmManager
        am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis() + diff, pendingintent);
        Toast.makeText(this.getActivity(), "Alarme programmée; le " +
                reveil.get(Calendar.DAY_OF_MONTH) + " à " +
                reveil.get(Calendar.HOUR_OF_DAY) + ":" + +
                reveil.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
    }
    /*
    * Sauvegarde des informations du reveil
    */
    public void sauver(){
        try {
            ObjectOutputStream alarmOOS= new ObjectOutputStream(this.getActivity().openFileOutput("alarm.serial",this.getActivity(). MODE_WORLD_WRITEABLE));
            alarmOOS.writeObject(alarm);
            alarmOOS.flush();
            alarmOOS.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.heure) {
            //Si on active l'alarm alors on veut choisir l'heure.
           // if(!(alarm.getHeure().hour==Calendar.HOUR_OF_DAY)) {
                if (ck_alarm.isChecked()) {
                    TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, alarm.getHeure().hour, alarm.getHeure().minute, true);
                    dialog.show();
                    //On replanifie l'alarme.
                    planifierAlarm();
                } else {
                    am.cancel(pendingintent);
                    Toast.makeText(this.getActivity(), "Alarme desactivé!!!!!!!.", Toast.LENGTH_SHORT).show();
                }
            //}
        }
    }
}







