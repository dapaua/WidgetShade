package com.dapaua.widgetsoverlay;





import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;




public class SettingsActivity extends PreferenceActivity {
	static Context sContext;
	static int stransp_old_value;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        sContext=this.getBaseContext();
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Resources res = getResources();
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		/*
		//Set icon for market.
		IconPreferenceScreen getpro = (IconPreferenceScreen) findPreference("pref_market_pro");
		Drawable icon_pro = res.getDrawable(R.drawable.ic_google_play_icon2);
		getpro.setIcon(icon_pro);
        */
		
		//Set icon for help.
		IconPreferenceScreenHelpDialog gethelp = (IconPreferenceScreenHelpDialog) findPreference("pref_help");
		Drawable icon_help = res.getDrawable(R.drawable.ic_help);
		gethelp.setIcon(icon_help);
        
		
		//Set icon for contact.
		IconPreferenceScreen contactme = (IconPreferenceScreen) findPreference("pref_contact");
		Drawable icon_contact = res.getDrawable(R.drawable.ic_contact);
		contactme.setIcon(icon_contact);

		
        
        //Here we bind the preferences to an action after changes.
        findPreference("pref_show_trigger").setOnPreferenceChangeListener(refreshTriggerA);
        findPreference("pref_show_trigger_position").setOnPreferenceChangeListener(refreshTriggerB);
        //findPreference("pref_trigger_offset").setOnPreferenceChangeListener(refreshTriggerB);
        findPreference("pref_show_trigger_width").setOnPreferenceChangeListener(refreshTriggerB);
       	findPreference("pref_show_trigger_height").setOnPreferenceChangeListener(refreshTriggerB);
        //findPreference("pref_transparent_trigger").setOnPreferenceChangeListener(refreshTriggerB);
        findPreference("pref_number_of_pages").setOnPreferenceChangeListener(screenschanged);
        findPreference("pref_num_cells").setOnPreferenceChangeListener(cellschanged);
       // findPreference("pref_background_transparency").setOnPreferenceChangeListener(bgtranschanged);
        findPreference("pref_transparent_buttons").setOnPreferenceChangeListener(buttonstranschanged);
        findPreference("pref_small_buttons").setOnPreferenceChangeListener(buttonssmallchanged);
        //Add new message to this.
        findPreference("pref_hide_buttons").setOnPreferenceChangeListener(buttonsbarhidden);
        
        findPreference("pref_action_tap").setOnPreferenceChangeListener(actiontap);
        findPreference("pref_action_horizontal").setOnPreferenceChangeListener(actiontap);
        findPreference("pref_action_vertical").setOnPreferenceChangeListener(actiontap);
        findPreference("pref_action_longpress").setOnPreferenceChangeListener(actiontap);
        
        findPreference("pref_swap_back_settings").setOnPreferenceChangeListener(buttonsbarswapped);
        
        
        //Set summaries for actions:
        String[] entries = sContext.getResources().getStringArray(R.array.pref_actions_entries);
		int intvalue0 = Integer.parseInt((String)sp.getString("pref_action_tap", "0"));
		findPreference("pref_action_tap").setSummary(sContext.getResources().getString(R.string.action) + entries[intvalue0]);
		
		int intvalue1 = Integer.parseInt((String)sp.getString("pref_action_horizontal", "1"));
		findPreference("pref_action_horizontal").setSummary(sContext.getResources().getString(R.string.action) + entries[intvalue1]);
		
		int intvalue2 = Integer.parseInt((String)sp.getString("pref_action_vertical", "3"));
		findPreference("pref_action_vertical").setSummary(sContext.getResources().getString(R.string.action) + entries[intvalue2]);
		
		int intvalue3 = Integer.parseInt((String)sp.getString("pref_action_longpress", "2"));
		findPreference("pref_action_longpress").setSummary(sContext.getResources().getString(R.string.action) + entries[intvalue3]);
		
		
		//Transitions
				findPreference("pref_transition").setOnPreferenceChangeListener(transition);
				String[] entries_trans = sContext.getResources().getStringArray(R.array.pref_transition_entries);

				int transitionvalue = Integer.parseInt((String)sp.getString("pref_transition", "0"));
				findPreference("pref_transition").setSummary(sContext.getResources().getString(R.string.pref_transition_summary) + entries_trans[transitionvalue]);
	
        
        //Numerical offset for trigger

        SeekBarPreference trig_off = (SeekBarPreference)findPreference("pref_trigger_numerical_offset");
        trig_off.setMax(49);
        trig_off.setOnPreferenceChangeListener(trig_of_ch);
		trig_off.setSummary(sContext.getResources().getString(R.string.pref_trigger_numerical_offset_summary)+sp.getInt("pref_trigger_numerical_offset", 0));

        
        
        //Numerical transp for trigger
        
        SeekBarPreference trig_tr = (SeekBarPreference)findPreference("pref_trigger_numerical_transparency");
        trig_tr.setMax(100);
        trig_tr.setOnPreferenceChangeListener(trig_tr_ch);
		trig_tr.setSummary(sContext.getResources().getString(R.string.pref_trigger_numerical_transparency_summary)+sp.getInt("pref_trigger_numerical_transparency", 0));

        //Numerical transparence for background
		 SeekBarPreference bg_tr = (SeekBarPreference)findPreference("pref_bg_numerical_transparency");
	        bg_tr.setMax(100);
	        bg_tr.setOnPreferenceChangeListener(bg_tr_ch);
			bg_tr.setSummary(sContext.getResources().getString(R.string.pref_bg_numerical_transparency_summary)+sp.getInt("pref_bg_numerical_transparency", 50));

		stransp_old_value=sp.getInt("pref_bg_numerical_transparency", 50);
		
    }
	
	private static Preference.OnPreferenceChangeListener actiontap = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			
			String[] entries = sContext.getResources().getStringArray(R.array.pref_actions_entries);
			int intvalue = Integer.parseInt((String)value);
			preference.setSummary(sContext.getResources().getString(R.string.action) + entries[intvalue]);
			return true;
		}
	
	};
	private static Preference.OnPreferenceChangeListener transition = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			
				
			String[] entries_trans = sContext.getResources().getStringArray(R.array.pref_transition_entries);

			int transitionvalue = Integer.parseInt((String)value);
			preference.setSummary(sContext.getResources().getString(R.string.pref_transition_summary) + entries_trans[transitionvalue]);

			
			return true;
		}
	
	};
	
	private static Preference.OnPreferenceChangeListener bg_tr_ch = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			preference.setSummary(sContext.getResources().getString(R.string.pref_bg_numerical_transparency_summary)+value.toString());
			//System.out.println("WO:cells"+value);
			int svalue= (Integer)value;
			SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
	      int oldvalue = stransp_old_value;
	      System.out.println("WO:oldvalue, value:"+oldvalue+","+value);
	      //If no change, do not refresh.
	      if (svalue==oldvalue) {return true;}
	      stransp_old_value=svalue;
	      Intent intent = new Intent(sContext,Launcher.class);
	      
	      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
	      Toast.makeText(sContext, sContext.getString(R.string.refresh_bgtrans_changed), Toast.LENGTH_LONG).show();

	      
	      sContext.startActivity(intent);
			
			
			
			return true;
			}
		};
		//trigger offset changed
		private static Preference.OnPreferenceChangeListener trig_of_ch = new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object value) {
				preference.setSummary(sContext.getResources().getString(R.string.pref_trigger_numerical_offset_summary)+value.toString());
				Intent startBarhandleIntent= new Intent(sContext,SidebarService.class);
		    	 sContext.stopService(startBarhandleIntent);	
		    	 sContext.startService(startBarhandleIntent);
				
				return true;
				}
			};
		
	private static Preference.OnPreferenceChangeListener trig_tr_ch = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			preference.setSummary(sContext.getResources().getString(R.string.pref_trigger_numerical_transparency_summary)+value.toString());
			Intent startBarhandleIntent= new Intent(sContext,SidebarService.class);
	    	 sContext.stopService(startBarhandleIntent);	
	    	 sContext.startService(startBarhandleIntent);
			
			return true;
			}
		};
	
	//If number of screens changed, refresh
	private static Preference.OnPreferenceChangeListener screenschanged = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
		
			String svalue= (String)value;
			SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
	        String oldvalue = sp.getString("pref_number_of_pages", "1");
	        
	        //If no change, do not refresh.
	        if (svalue.equals(oldvalue)) {return true;}
	        
	        Intent intent = new Intent(sContext,Launcher.class);
	        
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
	        Toast.makeText(sContext, sContext.getString(R.string.refresh_screens_changed), Toast.LENGTH_LONG).show();

	        
	        sContext.startActivity(intent);
			
			return true;
		}
		
};	
//If number of cells changed, refresh
private static Preference.OnPreferenceChangeListener cellschanged = new Preference.OnPreferenceChangeListener() {
	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
	
		System.out.println("WO:cells"+value);
		String svalue= (String)value;
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
        String oldvalue = sp.getString("pref_num_cells", "5");
        
        //If no change, do not refresh.
        if (svalue.equals(oldvalue)) {return true;}
        
        Intent intent = new Intent(sContext,Launcher.class);
        
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
        Toast.makeText(sContext, sContext.getString(R.string.refresh_cells_changed), Toast.LENGTH_LONG).show();

        
        sContext.startActivity(intent);
		
		return true;
	}
	
};	
//If bgtransparency changed, refresh
private static Preference.OnPreferenceChangeListener bgtranschanged = new Preference.OnPreferenceChangeListener() {
	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
	
		//System.out.println("WO:cells"+value);
		String svalue= (String)value;
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
      String oldvalue = sp.getString("pref_background_transparency", "50");
      
      //If no change, do not refresh.
      if (svalue.equals(oldvalue)) {return true;}
      
      Intent intent = new Intent(sContext,Launcher.class);
      
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
      Toast.makeText(sContext, sContext.getString(R.string.refresh_bgtrans_changed), Toast.LENGTH_LONG).show();

      
      sContext.startActivity(intent);
		
		return true;
	}
	
};	

//If button size changed, restart
private static Preference.OnPreferenceChangeListener buttonssmallchanged = new Preference.OnPreferenceChangeListener() {
	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
	
		//System.out.println("WO:cells"+value);
		Boolean svalue= (Boolean)value;
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
    Boolean oldvalue = sp.getBoolean("pref_small_buttons", false);
    
    //If no change, do not refresh.
    if (svalue == oldvalue) {return true;}
    
    Intent intent = new Intent(sContext,Launcher.class);
    
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
    Toast.makeText(sContext, sContext.getString(R.string.refresh_buttonssmall_changed), Toast.LENGTH_LONG).show();

    
    sContext.startActivity(intent);
		
		return true;
	}
	
};	
//If button bar swapped, restart
private static Preference.OnPreferenceChangeListener buttonsbarswapped = new Preference.OnPreferenceChangeListener() {
	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
	
		//System.out.println("WO:cells"+value);
		Boolean svalue= (Boolean)value;
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
Boolean oldvalue = sp.getBoolean("pref_swap_back_settings", false);

//If no change, do not refresh.
if (svalue == oldvalue) {return true;}

Intent intent = new Intent(sContext,Launcher.class);

intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
Toast.makeText(sContext, sContext.getString(R.string.refresh_buttonssmall_changed), Toast.LENGTH_LONG).show();


sContext.startActivity(intent);
		
		return true;
	}
	
};	

//If button bar hidden, restart
private static Preference.OnPreferenceChangeListener buttonsbarhidden = new Preference.OnPreferenceChangeListener() {
	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
	
		//System.out.println("WO:cells"+value);
		Boolean svalue= (Boolean)value;
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
  Boolean oldvalue = sp.getBoolean("pref_hide_buttons", false);
  
  //If no change, do not refresh.
  if (svalue == oldvalue) {return true;}
  
  Intent intent = new Intent(sContext,Launcher.class);
  
  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
  Toast.makeText(sContext, sContext.getString(R.string.refresh_buttonssmall_changed), Toast.LENGTH_LONG).show();

  
  sContext.startActivity(intent);
		
		return true;
	}
	
};	
//If buttontransparency changed, refresh
private static Preference.OnPreferenceChangeListener buttonstranschanged = new Preference.OnPreferenceChangeListener() {
	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
	
		//System.out.println("WO:cells"+value);
		Boolean svalue= (Boolean)value;
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
    Boolean oldvalue = sp.getBoolean("pref_transparent_buttons", false);
    
    //If no change, do not refresh.
    if (svalue == oldvalue) {return true;}
    
    Intent intent = new Intent(sContext,Launcher.class);
    
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
    Toast.makeText(sContext, sContext.getString(R.string.refresh_buttonstrans_changed), Toast.LENGTH_LONG).show();

    
    sContext.startActivity(intent);
		
		return true;
	}
	
};	
	//This enables or disables the sidebar trigger.
	private static Preference.OnPreferenceChangeListener refreshTriggerA = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
		
			SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
	        boolean pref_show_trigger = sp.getBoolean("pref_show_trigger", true);

	        //This is inverted beacuse it happens before the settings are written to the preferencesmanager.
	        if (!pref_show_trigger)
	        {
	        	//System.out.println("WO:Showing");
	    	Intent startBarhandleIntent= new Intent(sContext,SidebarService.class);
		    sContext.startService(startBarhandleIntent);
	        }
	        else
	        {
	        	//System.out.println("WO:Hiding");
	        	Intent startBarhandleIntent= new Intent(sContext,SidebarService.class);
	    	    sContext.stopService(startBarhandleIntent);	
	        	
	        }
			
			return true;
		}
		
};
//This enables or disables the sidebar trigger.
private static Preference.OnPreferenceChangeListener refreshTriggerB = new Preference.OnPreferenceChangeListener() {
	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
	
		SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(sContext);
        boolean pref_show_trigger = sp.getBoolean("pref_show_trigger", true);

        //This is not inverted.
        if (pref_show_trigger)
        {
        	//System.out.println("WO:Refreshing");
        Intent startBarhandleIntent= new Intent(sContext,SidebarService.class);
    	 sContext.stopService(startBarhandleIntent);	
    	 sContext.startService(startBarhandleIntent);
        }
        else
        {
        	//System.out.println("WO:Hiding");
        	Intent startBarhandleIntent= new Intent(sContext,SidebarService.class);
    	    sContext.stopService(startBarhandleIntent);	
        	
        }
		
		return true;
	}
	
};
	@Override
    public void onPause() {
        super.onPause();
        
        
        //TODO
        //Destroy Launcher activity if number of pages changed.
      //TODO
        //Destroy Launcher activity if number of pages changed.
        // This code here wil re-start the launcher.
       
    }
}
