package com.dapaua.widgetsoverlay;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class NotifierService extends AccessibilityService {
//This does nothing, but if enabled avoids the app from being killed by android system.
	@Override
	public void onAccessibilityEvent(AccessibilityEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

}
