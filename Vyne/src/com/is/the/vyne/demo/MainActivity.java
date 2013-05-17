package com.is.the.vyne.demo;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {
	int width;
	int height;
	EditText et;
	RelativeLayout mainlayout;
	ImageView polygon;
	HorizontalScrollView scroll;
	RelativeLayout feedlayout;
	ImageView twocol;
	RelativeLayout hlayout;
	ImageView banner;
	ImageView liv;
	ImageView searchbar;
	int x;
	ImageView storepreview;
	ScrollView mainscroll;
	RelativeLayout navbar;

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	x = 0;
        super.onCreate(savedInstanceState);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        Display display = getWindowManager().getDefaultDisplay(); 
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated 
        et = (EditText) findViewById(R.id.editText1);
        mainlayout = (RelativeLayout) findViewById(R.id.mainLayout);
        feedlayout = (RelativeLayout) findViewById(R.id.feedView);
        banner = (ImageView) findViewById(R.id.mybanner);
        searchbar = (ImageView) findViewById(R.id.demonav);
        mainscroll = (ScrollView) findViewById(R.id.mainscroll);
        navbar = (RelativeLayout) findViewById(R.id.nav_bar);
        
        et.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.TYPE_CLASS_TEXT){
					et.setText("Sperry's Top Siders");
				}
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					mainlayout.removeView(polygon);
					mainlayout.removeView(banner);
					mainlayout.removeView(et);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mainscroll.getLayoutParams());
					params.addRule(RelativeLayout.BELOW,R.id.nav_bar);
					mainscroll.setLayoutParams(params);
					Animation a = new TranslateAnimation(
		    		        Animation.ABSOLUTE,-width, Animation.ABSOLUTE,0,
		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,0);
		    		a.setDuration(400);
		    		a.setFillAfter(true); //HERE
		    		
		    		twocol.startAnimation(a);
					hlayout.removeView(twocol);
					twocol.setImageResource(R.drawable.searchpagemapdemo);
					params = new RelativeLayout.LayoutParams(width	, 1396);
					feedlayout.addView(twocol,params);
					
					storepreview = new ImageView(getApplicationContext());
		    		storepreview.setImageResource(R.drawable.storeprev);
					params = new RelativeLayout.LayoutParams(300	, 150);
					params.topMargin = 50;
					params.leftMargin = 170;
					
					feedlayout.addView(storepreview,params);
					
					params = new RelativeLayout.LayoutParams(width	, height);
					
		    		loadingScreen(feedlayout,params);
		    		new LongOperation().execute("");
		    		x = 0;
		    		twocol.setOnTouchListener(new OnTouchListener() {
		        	    @Override
		        	    public boolean onTouch(View v, MotionEvent event) {
		        	    	  if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        	    		  x = x+1;
		        	    		  if(x==2){
		        	    			  Log.d("GIT ER", "DUN");
		        	    			  twocol.setImageResource(R.drawable.searchpagemapdemo2);
		        	    			  storepreview.setImageResource(R.drawable.storeprev2);
		        	    			  TranslateAnimation a = new TranslateAnimation(
		    	    		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,40,
		    	    		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,250);
		    	    		    		a.setDuration(400);
		    	    		    		a.setFillAfter(true);
		    	    		    		storepreview.startAnimation(a);
		        	    		  }
		        	    		  
		        	    		  if(x==3){
		        	    			  ImageView storeprof = new ImageView(getApplicationContext());
		        	    			  storeprof.setBackgroundResource(R.drawable.storefrontdemo);
		        	    			  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width	, height/2-170);
		        	    			  params.leftMargin=0;
		        	    			  params.topMargin=80;
		        	    			  mainlayout.addView(storeprof,params);
		        	    			  x = 0;
		        	    			  
		        	    			  storeprof.setOnTouchListener(new OnTouchListener() {
		        			        	    @Override
		        			        	    public boolean onTouch(View v, MotionEvent event) {
		        			        	    	  if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        			        	    		  
		        			        	    		  ImageView fbut = new ImageView(getApplicationContext());
		        			        	    		  fbut.setImageResource(R.drawable.newfbutton);
		        			        	    		  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(170	, 80);
		        			        	    		  params.topMargin = 368;
		        			        	    		  params.leftMargin = 330;
		        			        	    		  mainlayout.addView(fbut,params);
		        			        	    		  
		        			        	    			  
		        			        	    		  
		        			        	    		  
		        			        	    	  }
		        			        	    	  return true;
		        			        	    }
		        	    			  });
		        	    			  
		        	    			  ImageView storeprods = new ImageView(getApplicationContext());
		        	    			  storeprods.setBackgroundResource(R.drawable.storeproductsdemo);
		        	    			  storeprods.setOnTouchListener(new OnTouchListener() {
		        			        	    @Override
		        			        	    public boolean onTouch(View v, MotionEvent event) {
		        			        	    	  if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        			        	    		  
		        			        	    		  x = x+1;
		        			        	    		  if(x==2){
		        			        	    			  ImageView fbut = new ImageView(getApplicationContext());
			        			        	    		  fbut.setBackgroundResource(R.drawable.productpagedemo);
			        			        	    		  fbut.setOnTouchListener(new OnTouchListener() {
			      		        			        	    @Override
			    		        			        	    public boolean onTouch(View v, MotionEvent event) {
			    		        			        	    	  if (event.getAction() == MotionEvent.ACTION_DOWN) {
			    		        			        	    		  ImageView fbut = new ImageView(getApplicationContext());
						        			        	    		  fbut.setBackgroundResource(R.drawable.confirmationpage);
			    		        			        	    		  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width+100	, height-80);
						        			        	    		  params.topMargin = 80;
						        			        	    		  params.leftMargin = 0;
						        			        	    		  mainlayout.addView(fbut,params);
						        			        	    		  
						        			        	    		  params = new RelativeLayout.LayoutParams(width	, height);
						        		        	    			  
						        		        	    			  loadingScreen(mainlayout,params);
						        		        			    		new LongOperation().execute("");
			    		        			        	    	  }
			    		        			        	    	  return true;
			      		        			        	    }
			        			        	    		  });
			        			        	    		  Log.d("HEY JON THIS IS HARD", "YOU SUCK!");
			        			        	    		  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width	, height-80);
			        			        	    		  params.topMargin = 80;
			        			        	    		  params.leftMargin = 0;
			        			        	    		  mainlayout.addView(fbut,params);
			        			        	    		  
			        			        	    		  params = new RelativeLayout.LayoutParams(width	, height);
			        		        	    			  
			        		        	    			  loadingScreen(mainlayout,params);
			        		        			    		new LongOperation().execute("");
		        			        	    		  }
		        			        	    	  }
		        			        	    	  return true;
		        			        	    }
		        	    			  });
		        	    			  
		        	    			  params = new RelativeLayout.LayoutParams(width	, 1050);
		        	    			  
		        	    			  params.topMargin = height/2-180;
		        	    			  params.leftMargin = 0;
		        	    			  
		        	    			  feedlayout.addView(storeprods,params);
		        	    			  
		        	    			 params = new RelativeLayout.LayoutParams(30	, 30);
		        	    				params.topMargin = 540;
		        	    				params.leftMargin = 120;
		        	    			  
		        	    				mainlayout.addView(polygon,params);
		        	    				
		        	    			  params = new RelativeLayout.LayoutParams(width	, height);
		        	    			  
		        	    			  loadingScreen(mainlayout,params);
		        			    		new LongOperation().execute("");
		        	    			  
		        	    			  
		        	    			  
		        	    		  }
		        	    	  } 
		        	    	  return true;
		        	    	  }
		        	    });
		        	    	  
		    		
					
                }
				return false;
			}
        });
        
        et.addTextChangedListener(new TextWatcher() { 

            public void  afterTextChanged (Editable s){ 
                    Log.d("seachScreen", "afterTextChanged"); 
                    if(et.getText().toString().equals("sp")){
                    	et.setText("Sperry Boat Shoes");
                    }
                    
            } 
            public void  beforeTextChanged  (CharSequence s, int start, int 
count, int after){ 
                    Log.d("seachScreen", "beforeTextChanged"); 
            } 
            public void  onTextChanged  (CharSequence s, int start, int before, 
int count) { 
                    Log.d("seachScreen", "onTextChanged"); 
            }} );
        
        mainlayout.removeView(et);
        
        polygon = new ImageView(this);
        polygon.setImageResource(R.drawable.polygon);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30	, 30);
		params.topMargin = 150;
		params.leftMargin = 60;
		
		mainlayout.addView(polygon,params);
		
		twocol = new ImageView(this);
		twocol.setImageResource(R.drawable.homepagedemo);
		twocol.setOnTouchListener(new OnTouchListener() {
    	    @Override
    	    public boolean onTouch(View v, MotionEvent event) {
    	    	  if (event.getAction() == MotionEvent.ACTION_DOWN) {

    	    		    x = x+1;
	    		    	//Log.d("RUNNING ANIMATION", "RUNNING ANIM");

    	    		    
    	    		    if(x==3){
    	    		    	Log.d("RUNNING ANIMATION", "RUNNING ANIM");
    	    		        AnimationSet animationSet = new AnimationSet(true);
    	    		    	 TranslateAnimation a = new TranslateAnimation(
    	    		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,180,
    	    		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,0);
    	    		    		a.setDuration(400);
    	    		    		a.setFillAfter(true); //HERE
    	    		    		animationSet.addAnimation(a);
    	    		    		
    	    		    		//scroll.scrollBy(width, 0);
    	    		    		
    	    		    	polygon.startAnimation(a);
    	    		    	banner.setBackgroundResource(R.drawable.banneroptions);
    	    		    	
    	    		    	a = new TranslateAnimation(
	    		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,-width,
	    		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,0);
	    		    		a.setDuration(400);
	    		    		a.setFillAfter(true); //HERE
	    		    		animationSet.addAnimation(a);
	    		    		
	    		    		twocol.startAnimation(a);
	    		    		
	    		    		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width	, height);
	    		    		
	    		    		loadingScreen(feedlayout,params);
	    		    		new LongOperation().execute("");
	    		    		
	    		    		
	    		    		
    	    		    	
    	    		    } else if(false){
    	    		    	Log.d("RUNNING ANIMATION", "RUNNING ANIM");
    	    		    	
    	    		        AnimationSet animationSet = new AnimationSet(true);
    	    		    	 TranslateAnimation a = new TranslateAnimation(
    	    		    		        Animation.ABSOLUTE,180, Animation.ABSOLUTE,0,
    	    		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,0);
    	    		    		a.setDuration(400);
    	    		    		a.setFillAfter(true); //HERE
    	    		    		animationSet.addAnimation(a);
    	    		    		
    	    		    	polygon.startAnimation(a);
    	    		    	banner.setBackgroundResource(R.drawable.bannerwhite);
    	    		    	
    	    		    	a = new TranslateAnimation(
	    		    		        Animation.ABSOLUTE,-width, Animation.ABSOLUTE,0,
	    		    		        Animation.ABSOLUTE,0, Animation.ABSOLUTE,0);
	    		    		a.setDuration(400);
	    		    		a.setFillAfter(true); //HERE
	    		    		animationSet.addAnimation(a);
	    		    		
	    		    		twocol.startAnimation(a);
	    		    		
    	    		    }
    	    		    if(x==5){
    	    		    	
    	    		    }

    	    		  } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
    	    			 

    	    		    //create the line from start_x and start_y to the current location
    	    		    //don't forget to invalidate the View otherwise your line won't get redrawn

    	    		  } else if (event.getAction() == MotionEvent.ACTION_UP) {
	    		            
    	    			  //x = event.getX();
    	    			  
    	    		    //might not need anything here

    	    		  }
    	    		  return true;
    	    }
    	});
		params = new RelativeLayout.LayoutParams(2*width, 2134);
		params.topMargin = 0;
		
		hlayout = new RelativeLayout(this);
		scroll = new HorizontalScrollView(this);
		
		scroll.addView(hlayout);
		hlayout.addView(twocol,params);
		
    	scroll.setBackgroundColor(Color.TRANSPARENT);
    	
    	feedlayout.addView(scroll);
    	
    	searchbar.setOnTouchListener(new OnTouchListener() {
    	    @Override
    	    public boolean onTouch(View v, MotionEvent event) {
    	    	  if (event.getAction() == MotionEvent.ACTION_DOWN) {
    	    		  RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(width - 100	, 70);
    	    		  params2.topMargin = 10;
    	    		  
    	    		    mainlayout.addView(et,params2);
    	    		    et.requestFocus();
    	    		    InputMethodManager keyboard = (InputMethodManager)
    	                        getSystemService(Context.INPUT_METHOD_SERVICE);
    	                        keyboard.showSoftInput(et, 0);

    	    		    
    	    		   

    	    		  } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
    	    			 

    	    		    //create the line from start_x and start_y to the current location
    	    		    //don't forget to invalidate the View otherwise your line won't get redrawn

    	    		  } else if (event.getAction() == MotionEvent.ACTION_UP) {
    	    			  //x = event.getX();
    	    			 
    	    		    //might not need anything here

    	    		  }
    	    		  return true;
    	    }
    	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void loadingScreen(RelativeLayout view ,RelativeLayout.LayoutParams params){
    	liv = new ImageView(this);
		liv.setBackgroundResource(R.drawable.loader);
		
		//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		
		MyAnimationRoutine mar = new MyAnimationRoutine(liv);
		MyAnimationRoutine2 mar2 = new MyAnimationRoutine2(liv);

		Timer t = new Timer(false);
		t.schedule(mar, 100);
		Timer t2 = new Timer(false);
		t2.schedule(mar2, 1000);
		view.addView(liv,params);
		//feedLayout.addView(iv,params);
		
		
		
    }
    
    public void removeLoad(){
    	feedlayout.removeView(liv);
    	mainlayout.removeView(liv);
    }
    
    //ASYNC TASK
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Log.d("WTF", "ISGOINGONHERE");
            //removeLoad();
              return null;
        }      

        @Override
        protected void onPostExecute(String result) {   
        	removeLoad();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
  }
}

//ANIMATION STUFF_______________________________________________________________

class MyAnimationRoutine extends TimerTask
{
	private ImageView img;
	MyAnimationRoutine(ImageView i)
	{
		img = i;
	}
	
	public void run()
	{
		// Get the background, which has been compiled to an AnimationDrawable object.
		AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

		// Start the animation (looped playback by default).
		frameAnimation.start();
	}
}

class MyAnimationRoutine2 extends TimerTask
{
	private ImageView img;
	MyAnimationRoutine2(ImageView i)
	{
		img = i;
	}

	public void run()
	{
		// Get the background, which has been compiled to an AnimationDrawable object.
		AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

		// stop the animation (looped playback by default).
		frameAnimation.stop();
	}
}
