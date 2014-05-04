package com.dapaua.widgetsoverlay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient; 
import android.widget.ImageView;

public class IconPreferenceScreenHelpDialog extends Preference {

    private Drawable mIcon;
    private static Context mcontext;

    public IconPreferenceScreenHelpDialog(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconPreferenceScreenHelpDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mcontext=context;
        setLayoutResource(R.layout.preference_icon);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.IconPreferenceScreen, defStyle, 0);
        mIcon = a.getDrawable(R.styleable.IconPreferenceScreen_ip_icon);  
    }

    @Override
    public void onClick()
    {
    	System.out.println("WO: Help Clicked!!");
    	//Show a dialog with the help and licenses.
    	AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);

        alert.setTitle("Help");
        WebView wv = new WebView(mcontext);

        wv.loadUrl("file:///android_asset/help.html");

        wv.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        alert.show();
    	
    	
    	/*
    	final Dialog dialog = new Dialog(mcontext);
		dialog.setContentView(R.layout.tabbed_dialog);
		dialog.setTitle("Help");
		WebView wb = (WebView)dialog.findViewById(R.id.webView1);
		wb.loadUrl("file:///android_asset/help.html");
		dialog.show();
		*/
    	super.onClick();
    	
    }
    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.ip_icon);
        if (imageView != null && mIcon != null) {
            imageView.setImageDrawable(mIcon);
        }
    }

    public void setIcon(Drawable icon) {
        if ((icon == null && mIcon != null) || (icon != null && !icon.equals(mIcon))) {
            mIcon = icon;
            notifyChanged();
        }
    }

    public Drawable getIcon() {
        return mIcon;
    }
}