/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dapaua.widgetsoverlay;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class Hotseat extends FrameLayout {
    private static final String TAG = "Hotseat";
    private static final int sAllAppsButtonRank = 2; // In the middle of the dock
    private static final int sBackButtonRank = 0; // In the beginning of the dock
    private static final int sSettingsButtonRank = 4; // In the end of the dock
    
    private Launcher mLauncher;
    private CellLayout mContent;

    private int mCellCountX;
    private int mCellCountY;
    private boolean mIsLandscape;

    public Hotseat(Context context) {
        this(context, null);
    }

    public Hotseat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Hotseat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Hotseat, defStyle, 0);
        mCellCountX = a.getInt(R.styleable.Hotseat_cellCountX, -1);
        mCellCountY = a.getInt(R.styleable.Hotseat_cellCountY, -1);
        mIsLandscape = context.getResources().getConfiguration().orientation ==
            Configuration.ORIENTATION_LANDSCAPE;
    }

    public void setup(Launcher launcher) {
        mLauncher = launcher;
        setOnKeyListener(new HotseatIconKeyEventListener());
    }

    CellLayout getLayout() {
        return mContent;
    }

    /* Get the orientation invariant order of the item in the hotseat for persistence. */
    int getOrderInHotseat(int x, int y) {
        return mIsLandscape ? (mContent.getCountY() - y - 1) : x;
    }
    /* Get the orientation specific coordinates given an invariant order in the hotseat. */
    int getCellXFromOrder(int rank) {
        return mIsLandscape ? 0 : rank;
    }
    int getCellYFromOrder(int rank) {
        return mIsLandscape ? (mContent.getCountY() - (rank + 1)) : 0;
    }
    public static boolean isAllAppsButtonRank(int rank) {
        return rank == sAllAppsButtonRank;
    }
    static public Drawable scaleDrawable(Drawable drawable, float scalewidth, float 
    		scaleheight) 
    		{ 
    		      int wi = drawable.getIntrinsicWidth(); 
    		      int hi = drawable.getIntrinsicHeight(); 
    		      //int dimDiff = Math.abs(wi - width) - Math.abs(hi - height); 
    		      //float scale = (dimDiff > 0) ? width/(float)wi : height/ (float)hi; 
    		      Rect bounds = new Rect(0, 0, (int)(scalewidth*wi), (int)(scaleheight*hi)); 
    		      drawable.setBounds(bounds); 
    		      return drawable; 
    		} 

    @Override
    protected void onFinishInflate() {
    	
        super.onFinishInflate();
        if (mCellCountX < 0) mCellCountX = LauncherModel.getCellCountX();
        if (mCellCountY < 0) mCellCountY = LauncherModel.getCellCountY();
        mContent = (CellLayout) findViewById(R.id.layout);
        mContent.setGridSize(mCellCountX, mCellCountY);

        resetLayout();
    }

    void resetLayout() {
        mContent.removeAllViewsInLayout();

        Context context = getContext();
        SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(context);
        float scaleButtons=1f;
        boolean small_buttons = sp.getBoolean("pref_small_buttons",false);
        if (small_buttons) {scaleButtons=0.5f;}
        
        /*String scalestring=sp.getString("pref_Scale", "100");
        int scaleint=Integer.parseInt(scalestring);
        scaleButtons=(float)scaleint/100f;
        */
        boolean invisible_buttons = sp.getBoolean("pref_transparent_buttons",false);
        // Add the Apps button (add widget)
        
        boolean swap = sp.getBoolean("pref_swap_back_settings",false);
        
        int drawablebutton1 = R.drawable.all_apps_button_icon;
        if (invisible_buttons) {drawablebutton1 = R.drawable.invisible_button;}
        LayoutInflater inflater = LayoutInflater.from(context);
        BubbleTextView allAppsButton = (BubbleTextView)
                inflater.inflate(R.layout.application, mContent, false);
        
        //allAppsButton.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(drawablebutton1), null, null);
        allAppsButton.setCompoundDrawables(null,null,null,scaleDrawable(context.getResources().getDrawable(drawablebutton1),scaleButtons,scaleButtons));

        
        										//If invisible, load invisible_button
        
        // allAppsButton.setText(context.getString(R.string.all_apps_button_label));
        allAppsButton.setContentDescription(context.getString(R.string.all_apps_button_label));
        allAppsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mLauncher != null &&
                    (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    mLauncher.onTouchDownAllAppsButton(v);
                }
                return false;
            }
        });

        allAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (mLauncher != null) {
                    mLauncher.onClickAllAppsButton(v);
                }
            }
        });

        // Note: We do this to ensure that the hotseat is always laid out in the orientation of
        // the hotseat in order regardless of which orientation they were added
        int x = getCellXFromOrder(sAllAppsButtonRank);
        int y = getCellYFromOrder(sAllAppsButtonRank);
        mContent.addViewToCellLayout(allAppsButton, -1, 0, new CellLayout.LayoutParams(x,y,1,1),
                true);
        
        
        
        //Repeat for back button.
        int drawablebutton2 = R.drawable.back_button_icon;
        if (invisible_buttons) {drawablebutton2 = R.drawable.invisible_button;}
        
        LayoutInflater inflater2 = LayoutInflater.from(context);
        BubbleTextView backButton = (BubbleTextView)
                inflater2.inflate(R.layout.backbutton, mContent, false);
       
        backButton.setCompoundDrawables(null,null,null,scaleDrawable(context.getResources().getDrawable(drawablebutton2),scaleButtons,scaleButtons));

        //backButton.setCompoundDrawablesWithIntrinsicBounds(null,                context.getResources().getDrawable(drawablebutton2), null, null);
        
        backButton.setContentDescription(context.getString(R.string.back_button_label));
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mLauncher != null &&
                    (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    //TODO
                	mLauncher.onTouchDownBackButton(v);
                }
                return false;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (mLauncher != null) {
                	//TODO
                    mLauncher.onClickBackButton(v);
                }
            }
        });

        // Note: We do this to ensure that the hotseat is always laid out in the orientation of
        // the hotseat in order regardless of which orientation they were added
        int xb = getCellXFromOrder(sBackButtonRank);
        int yb = getCellYFromOrder(sBackButtonRank);
        if (swap)
        	{
        	xb = getCellXFromOrder(sSettingsButtonRank);
        	yb = getCellYFromOrder(sSettingsButtonRank);
        	}
        
        mContent.addViewToCellLayout(backButton, -1, 0, new CellLayout.LayoutParams(xb,yb,1,1),
                true);
        
        
 //Repeat for settings button.
        int drawablebutton3 = R.drawable.settings_button_icon;
        if (invisible_buttons) {drawablebutton3 = R.drawable.invisible_button;}
        LayoutInflater inflater3 = LayoutInflater.from(context);
        BubbleTextView settingsButton = (BubbleTextView)
                inflater2.inflate(R.layout.settingsbutton, mContent, false);
        settingsButton.setCompoundDrawables(null,null,null,scaleDrawable(context.getResources().getDrawable(drawablebutton3),scaleButtons,scaleButtons));

        
        //settingsButton.setCompoundDrawablesWithIntrinsicBounds(null,
        //        context.getResources().getDrawable(drawablebutton3), null, null);
        settingsButton.setContentDescription(context.getString(R.string.settings_button_label));
        settingsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mLauncher != null &&
                    (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    //TODO
                	mLauncher.onTouchDownSettingsButton(v);
                }
                return false;
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (mLauncher != null) {
                	//TODO
                    mLauncher.onClickSettingsButton(v);
                }
            }
        });

        // Note: We do this to ensure that the hotseat is always laid out in the orientation of
        // the hotseat in order regardless of which orientation they were added
        int xs = getCellXFromOrder(sSettingsButtonRank);
        int ys = getCellYFromOrder(sSettingsButtonRank);
        
        if (swap)
    	{
    	xs = getCellXFromOrder(sBackButtonRank);
    	ys = getCellYFromOrder(sBackButtonRank);
    	}
        mContent.addViewToCellLayout(settingsButton, -1, 0, new CellLayout.LayoutParams(xs,ys,1,1),
                true);
        
        
    }
}
