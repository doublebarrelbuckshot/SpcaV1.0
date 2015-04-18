package com.example.jedgar.spcav10;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;


import android.app.Notification;
import android.app.NotificationManager;



public class AlarmReceiver extends BroadcastReceiver{

    // l'id unique correspondant à notre notification
    public static final int ID_NOTIFICATION = 1983;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

                //On crée un "gestionnaire de notification"
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                //On crée la notification Avec son icône et son texte défilant (optionnel si l'on ne veut pas de texte défilant on met cet argument à null)
                Notification notification = new Notification(R.drawable.notification, "SPCA  : Message important !", System.currentTimeMillis());

                //Le PendingIntent c'est ce qui va nous permettre d'atteindre notre deuxième Activity
                //ActivityNotification sera donc le nom de notre seconde Activity
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, com.example.jedgar.spcav10.ActivityNotification.class), 0);
                //On définit le titre de la notification
                String titreNotification = "SPCA  : Message important !";
                //On définit le texte qui caractérise la notification
                String texteNotification = "YEEH !! On a trouvé un animal qui correspond à ta dernière recherche...";
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
        } catch (Exception e) {
            Log.d("Notif", e.getMessage());
        }
    }
}
