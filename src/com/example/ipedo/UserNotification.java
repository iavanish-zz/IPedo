package com.example.ipedo;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class UserNotification extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_notification);
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

		Intent intent = new Intent(this, Pedometer.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0, intent, 0);
		
		Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		mBuilder.setContentTitle("IPedo")
		.setContentText("Start walking dude!")
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentIntent(contentIntent)
		.setSound(sound)
		.setAutoCancel(true);

		mNotificationManager.notify(0, mBuilder.build());

		finish();
	}
}
