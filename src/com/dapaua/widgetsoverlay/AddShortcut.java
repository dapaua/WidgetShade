package com.dapaua.widgetsoverlay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class AddShortcut extends Activity{
	
		Activity mActivity = null;
		String msctext = null;
		boolean use_custom_icon=false;
		Bitmap custom_icon=null;
	  @Override
      protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  mActivity=this;
		  SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
          String num_of_pages = sp.getString("pref_number_of_pages", "1");
          Integer inum_pages=(Integer.parseInt(num_of_pages))-1;
          //I have the number of pages. Now fill a layout.
          this.setContentView(R.layout.add_shortcut); 
		  Button b1 =(Button)this.findViewById(R.id.button1);
		  Button b2 =(Button)this.findViewById(R.id.button2);
		  Button b3 =(Button)this.findViewById(R.id.button3);
		  Button b4 =(Button)this.findViewById(R.id.button4);
		  Button b5 =(Button)this.findViewById(R.id.button5);
		  Button pickimage=(Button)this.findViewById(R.id.buttonimage);
		 
		  if (inum_pages>=4) {b5.setVisibility(View.VISIBLE);} else {b5.setVisibility(View.GONE);}
		  if (inum_pages>=3) {b4.setVisibility(View.VISIBLE);} else {b4.setVisibility(View.GONE);}
		  if (inum_pages>=2) {b3.setVisibility(View.VISIBLE);} else {b3.setVisibility(View.GONE);}
		  if (inum_pages>=1) {b2.setVisibility(View.VISIBLE);} else {b2.setVisibility(View.GONE);}
		  if (inum_pages>=0) {b1.setVisibility(View.VISIBLE);} else {b1.setVisibility(View.GONE);}
		  
		  pickimage.setOnClickListener(new OnClickListener()
		  {
			  @Override
			  public void onClick(View v) 
			  {
				  Intent intent = new Intent();
			        intent.setType("image/*");
			        intent.setAction(Intent.ACTION_GET_CONTENT);
			        intent.addCategory(Intent.CATEGORY_OPENABLE);
			        startActivityForResult(intent, 777777);
			  }
			  
		  }
				  
				  
				  );
		  b5.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) 
			  {
			     shortcut_to(4);
			     mActivity.finish();
			     
			  }  
			  
		  }
          );
		  b4.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) 
			  {
			     shortcut_to(3);
			     mActivity.finish();
			     
			  }  
			  
		  }
          );
		  b3.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) 
			  {
			     shortcut_to(2);
			     mActivity.finish();
			     
			  }  
			  
		  }
          );
		  b2.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) 
			  {
			     shortcut_to(1);
			     mActivity.finish();
			     
			  }  
			  
		  }
          );
		  b1.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) 
			  {
			     shortcut_to(0);
			     mActivity.finish();
			     
			  }  
			  
		  }
          );
          
		  
	  }
	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == 777777 && resultCode == Activity.RESULT_OK)
	            try {
	                // We need to recyle unused bitmaps
	                if (custom_icon != null) {
	                    custom_icon.recycle();
	                }
	                InputStream stream = getContentResolver().openInputStream(
	                        data.getData());
	                custom_icon = BitmapFactory.decodeStream(stream);
	                custom_icon = Bitmap.createScaledBitmap(custom_icon, 128, 128, true);
	                stream.close();  
	                use_custom_icon=true;
	                ImageView imageView = (ImageView) findViewById(R.id.result);
	                imageView.setImageBitmap(custom_icon);
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	  
	  
	  void shortcut_to(int screen)
	  	{
		  Intent shortcutIntent = new Intent();
		  String shortcutname="WS Screen "+(screen+1);
		 
		  EditText sc = (EditText)this.findViewById(R.id.titleSC);
		  msctext=sc.getText().toString();
		  if (msctext !=null)
		  {	if (!msctext.equals(""))	{shortcutname=msctext; }  }
		  System.out.println("WO:msctext:"+msctext);
		  //shortcutIntent.setClassName("com.dapaua.widgetsoverlayfree", "com.dapaua.widgetsoverlayfree.Launcher");
		  
		  //ComponentName cname = new ComponentName(this.getPackageName(), ".Launcher");
		  //shortcutIntent.setComponent(cname);
		  shortcutIntent.setAction("com.dapaua.SHOW_WIDGET_SHADE");
		  shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		  shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  shortcutIntent.putExtra("screen", Integer.toString(screen));
		  
		  
		  
		  Intent addIntent = new Intent();
		  addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		  addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutname);
		  
		  //use_custom_icon=false;
		  if (use_custom_icon & (custom_icon!=null))
		  {
			  /*int w =200, h =200;

			  Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
			  Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
			  Canvas canvas = new Canvas(bmp);
			  canvas.drawARGB(255, 200, 100, 000);
			  */
			  
			  
			  addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, custom_icon);  
		  }
		  else
		  {
		  addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getBaseContext(), R.drawable.ic_iconpro));
		  } 
		  addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		  //getBaseContext().sendBroadcast(addIntent);
		  setResult(Activity.RESULT_OK,addIntent);
	  	}
}
