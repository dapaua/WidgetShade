package com.dapaua.widgetsoverlay;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

//Service to create a handle to open contacts sidebar
public class SidebarService extends Service {

	private ImageView barhandle;
	private WindowManager myWindowManager;
	public static int last_swipe = 0;
	public static final int NO_SWIPE = 0;
	public static final int HORIZONTAL_SWIPE = 1;
	public static final int VERTICAL_SWIPE = 2;
	public static boolean longpressed = false;
	public static boolean tapped=false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void start_settings()
		{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ComponentName cn = new ComponentName(
				getBaseContext(), SettingsActivity.class);
		intent.setComponent(cn);
		startActivity(intent);
		
		}
	private void sidebar_activated() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext()
						.getApplicationContext());
		boolean vibrate_on_sidebar = sp.getBoolean("pref_vibrate_on_sidebar",
				true);
		if (vibrate_on_sidebar) {
			barhandle
					.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		}
		;
		boolean use_root = sp.getBoolean("pref_use_root", false);
		if (!use_root) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ComponentName cn = new ComponentName(getBaseContext(),
					Launcher.class);
			intent.setComponent(cn);

			startActivity(intent);
		} else {
			try {
				// Root
				Process process;
				String command = "am start -a com.dapaua.SHOW_WIDGET_SHADE";
				System.out.println("WO:command:" + command);
				process = Runtime.getRuntime().exec(
						new String[] { "su", "-c", command });
			} catch (Exception e) {
			}
		}
		return;
	}

	private OnTouchListener handleListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			int action = event.getAction();
			int action_response1 = MotionEvent.ACTION_DOWN;
			int action_response2 = MotionEvent.ACTION_HOVER_EXIT;

			if (action == action_response1) {
				sidebar_activated();
			}

			return true;
		}

	};

	private void doSidebarAction(int action)
		{
		
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
			boolean vibrate_on_sidebar = sp.getBoolean(
					"pref_vibrate_on_sidebar", true);
			if (vibrate_on_sidebar) {
				barhandle.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
			}
			
		switch (action)
			{
		case 0:
			//Do nothing
			break;
		case 1:
			//Trigger widgetshade
			sidebar_activated();
			break;
		case 2:
			//Triger settings
			start_settings();
			break;
		case 3:
			//move trigger
			move_trigger();
			break;
		
		
			}
		
		
		}
	public void move_trigger() {
		if (barhandle == null) {
			return;
		}
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		boolean vibrate_on_sidebar = sp.getBoolean("pref_vibrate_on_sidebar",
				true);
		if (vibrate_on_sidebar) {
			barhandle
					.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		}
		;

		String St_pos = sp.getString("pref_show_trigger_position", "0");
		int t_pos = Integer.parseInt(St_pos);
		int new_pos = -1;
		if (t_pos == 0) {
			new_pos = 2;
		}
		if (t_pos == 2) {
			new_pos = 0;
		}
		if (t_pos == 3) {
			new_pos = 5;
		}
		if (t_pos == 5) {
			new_pos = 3;
		}

		if (new_pos != -1) {
			SharedPreferences.Editor prefEditor = sp.edit();
			prefEditor.putString("pref_show_trigger_position",
					Integer.toString(new_pos));
			prefEditor.commit();

		}
		hide_trigger();
		show_trigger();

	}

	public void hide_trigger() {
		if (myWindowManager != null) {
			if (barhandle != null) {
				myWindowManager.removeView(barhandle);
				barhandle = null;
				myWindowManager = null;
			}
		}

	}

	public void show_trigger() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		boolean use_sidebar = sp.getBoolean("pref_show_trigger", false);
		boolean visibletrigger = !sp.getBoolean("pref_transparent_trigger",
				false);

		String s_width = sp.getString("pref_show_trigger_width", "2");
		String s_height = sp.getString("pref_show_trigger_height", "1");
		int t_width = Integer.parseInt(s_width);
		int t_height = Integer.parseInt(s_height);
		String St_pos = sp.getString("pref_show_trigger_position", "0");
		int t_pos = Integer.parseInt(St_pos);

		// visibletrigger=true;
		barhandle = new ImageView(this);

		barhandle.setImageResource(R.drawable.ic_icon1);

		if (t_pos <= 2) {
			barhandle.setImageResource(R.drawable.ic_trigger_right);
		} else {
			barhandle.setImageResource(R.drawable.ic_trigger_left);
		}
		
		//Old method
		/*
		if (!visibletrigger) {
			barhandle.setAlpha(20);
		} else {
			barhandle.setAlpha(150);
		}
		*/
		//New transparency method
		int sTriggerTransparency=100-sp.getInt("pref_trigger_numerical_transparency", 0);
		//int iTriggerTransparency=Integer.parseInt(sTriggerTransparency);
		int myalpha= (int)((float)sTriggerTransparency * 2.5f);
		barhandle.setAlpha(myalpha);
		
		// NEW METHOD
		final GestureDetector gdt = new GestureDetector(new GestureListener());
		//boolean respond_to_taps = sp.getBoolean("pref_respond_to_taps", false);
		// respond_to_taps=true;
		if (true) {
			barhandle.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(final View view, final MotionEvent event) {
					if (longpressed) {

						longpressed = false;
						// Start settings activity.
						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(getBaseContext()
										.getApplicationContext());
						int action = Integer.parseInt(sp.getString("pref_action_longpress", "2"));
						doSidebarAction(action);

					}
					if (tapped) {

						tapped = false;
						// Start settings activity.
						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(getBaseContext()
										.getApplicationContext());
						int action = Integer.parseInt(sp.getString("pref_action_tap", "0"));
						doSidebarAction(action);

					}
					
					boolean swipe = gdt.onTouchEvent(event);
					// System.out.println("WO:GDT:"+swipe);
					// System.out.println("WO:here");
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext()
									.getApplicationContext());

					if (swipe) {
						if (SidebarService.last_swipe == SidebarService.HORIZONTAL_SWIPE) {
							int action = Integer.parseInt(sp.getString("pref_action_horizontal", "1"));
							doSidebarAction(action);
						}
						if (SidebarService.last_swipe == SidebarService.VERTICAL_SWIPE) {
							int action = Integer.parseInt(sp.getString("pref_action_vertical", "3"));
							doSidebarAction(action);
						}

					}

					return true;
				}

			});
		}
		// END NEW METHOD

		WindowManager.LayoutParams lParams;
		float divisorw = 2; // was 20
		float divisorh = 2;
		switch (t_width) {
		case 0:
			divisorw = 10;
			break;
		case 1:
			divisorw = 5;
			break;
		case 2:
			divisorw = 2;
			break;
		case 3:
			divisorw = 1;
			break;
		}

		switch (t_height) {
		case 0:
			divisorh = 1.5f;
			break;
		case 1:
			divisorh = 0.8f;
			break;
		case 2:
			divisorh = 0.2f;
			break;
		case 3:
			divisorh = 0.1f;
			break;

		}

		// debug.
		// barhandle.setAlpha(255);

		int width = (int) (barhandle.getDrawable().getIntrinsicWidth() / divisorw);
		int height = (int) (barhandle.getDrawable().getIntrinsicHeight() / divisorh);
		lParams = new WindowManager.LayoutParams(width, height,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		lParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;

		switch (t_pos) {
		case 0:
			lParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			break;
		case 1:
			lParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
			break;
		case 2:
			lParams.gravity = Gravity.RIGHT | Gravity.TOP;
			break;
		case 3:
			lParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
			break;
		case 4:
			lParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
			break;
		case 5:
			lParams.gravity = Gravity.LEFT | Gravity.TOP;
			break;

		}
		int tr_numerical_offset= sp.getInt("pref_trigger_numerical_offset",0);
		//String S_offset = sp.getString("pref_trigger_offset", "0");
		//int dev = Integer.parseInt(S_offset);
		int dev=tr_numerical_offset;
		// Deviation from top/bottom. 1=100% ; 0.5f : middle of the screen.
		lParams.verticalMargin = (float) dev / 100f;
		barhandle.setScaleType(ImageView.ScaleType.FIT_XY);
		myWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		myWindowManager.addView(barhandle, lParams);

	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		// System.out.println("WO:sidebarHandle started!");
		if (myWindowManager != null) {
			if (barhandle != null) {
				// The handle is there. Just return.
				return START_STICKY;
			}
		}
		show_trigger();

		// System.out.println("WO:sidebarHandle view added!");
		return START_STICKY;

	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onDestroy() {

		hide_trigger();
		super.onDestroy();

	}

	private static final int SWIPE_MIN_DISTANCE = 50;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				System.out.println("WO:RTL");
				SidebarService.last_swipe = SidebarService.HORIZONTAL_SWIPE;
				return true; // Right to left
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				System.out.println("WO:LTR");
				SidebarService.last_swipe = SidebarService.HORIZONTAL_SWIPE;
				return true; // Left to right
			}

			if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				SidebarService.last_swipe = SidebarService.VERTICAL_SWIPE;
				return true; // Bottom to top
			} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				SidebarService.last_swipe = SidebarService.VERTICAL_SWIPE;
				return true; // Top to bottom
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			System.out.println("WO:Long press on trigger");
			longpressed = true;

		}
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			System.out.println("WO:tap on trigger");
			tapped = true;
			return true;
		}
		
	}
}
