package Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.namghjk.dailyselfie.MainActivity;
import com.namghjk.dailyselfie.R;

public class ReminderBoardCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent  resultIntent = new Intent(context,MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyAppSelfy")
                .setSmallIcon(R.drawable.ic_camera)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie right now")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(114, builder.build());
    }
}
