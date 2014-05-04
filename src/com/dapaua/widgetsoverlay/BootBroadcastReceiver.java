package com.dapaua.widgetsoverlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
		System.out.println("WO: Boot signal received.");
		
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(context);
        boolean pref_start_on_boot = sp.getBoolean("pref_start_on_boot", true);
        if (pref_start_on_boot)
        {
		Launcher.startSidebarService(context);
        Launcher.updateNotification(context);
        }
		
    }
	
	
	    
	   
	    
	    
	    	

}
