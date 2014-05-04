package com.dapaua.widgetsoverlay;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class SurvivalService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		//System.out.println("WO:SurvivalService call received");
		postNotification();
		return(START_STICKY);
	}
	
	  @Override
	  public void onDestroy() {
	    clearNotification();
	  }
	
	private void postNotification()
	{
		Notification mynotification = new Notification.Builder(this.getApplicationContext())
		.setContentTitle("Tap to start WidgetShade")
		.setSmallIcon(R.drawable.ic_notif1)
		.getNotification();
        
        Intent myintent = new Intent(this.getApplicationContext(), Launcher.class);
        PendingIntent PI = PendingIntent.getActivity(this.getApplicationContext(), 0, myintent, 0);
        
        mynotification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
	    mynotification.flags |= Notification.FLAG_ONGOING_EVENT;
        
	    mynotification.contentIntent = PI; 
        NotificationManager mNotificationManager =
        	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(42, mynotification);
        
		
	}
	
	private void clearNotification()
	{
		stopForeground(true);
	}
}
