package nkspaun.project190.vyne;

import java.util.ArrayList;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.support.v4.app.NavUtils;

public class StoreActivity extends Activity {
	final static String GOOGLE_API_KEY = "AIzaSyCeQZg095BGp-noLSTPegE9uXcNVD67vQY";
	public double phoneLat;
	public double phoneLong;
	private RelativeLayout feedLayout;
	private int height;
	private int width;
	private ArrayList<Store> maplist;
	private int cachecount;
	private ImageButton mapbut;
	private ViewAnimator viewAnimator;
	private RelativeLayout mapLayout;
	boolean mapavailable;
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay(); 
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated  
        cachecount = 0;
        mapavailable = false;
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        overridePendingTransition( R.drawable.inrighttransition, R.drawable.outlefttransition);
        setContentView(R.layout.activity_store);
        
        feedLayout = (RelativeLayout) findViewById(R.id.feedView);
        mapbut = (ImageButton) findViewById(R.id.mapicon);
        
        viewAnimator = new ViewAnimator(this);
        mapLayout = new RelativeLayout(this);
        
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        phoneLat = this.getIntent().getExtras().getDouble("geoLat");
        phoneLong = this.getIntent().getExtras().getDouble("geoLng");
        
        
        Milo m = new Milo(this);
        m.queryNearbyStores(phoneLat, phoneLong, this);
        
		ImageView iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.loader);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		
		MyAnimationRoutine mar = new MyAnimationRoutine(iv);
		MyAnimationRoutine2 mar2 = new MyAnimationRoutine2(iv);

		Timer t = new Timer(false);
		t.schedule(mar, 100);
		Timer t2 = new Timer(false);
		t2.schedule(mar2, 15000);
		
		feedLayout.addView(iv,params);
		
        gestureDetector = new GestureDetector(new MyGestureDetector(this));
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_store, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
    
    class MyGestureDetector extends SimpleOnGestureListener {
    	Activity act;
    	public MyGestureDetector(Activity a)
    	{
    		super();
    		act = a;
    	}
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > 200)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                   Log.d("FLING EVENT!","");// Toast.makeText(SelectFilterActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    act.finish();//Toast.makeText(SelectFilterActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                    Log.d("FLING EVENT!","");
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

    }
    
    public void onFourClick(View view) {

    	this.finish();
    }
    
    public void endQuery(ArrayList<Store> mystores){
    	feedLayout.removeAllViews();
		for(int i = 0; i<mystores.size();i++ ){
	
			
			//ADDING TEXTVIEWS
			Store iv = mystores.get(i);
			Log.d("MY RESULTS: ", iv.name);
			new DownloadImageTask(iv).execute(iv.imageurl);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/4, height/4);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(width/2-width/5, height/16);

			if( i % 2 == 0 ){
				params.leftMargin = 10;
				params2.leftMargin = width/5;
				params.topMargin = height/6*i;
				params2.topMargin = height/6*i+height/4-height/16;

			} else {
				params.leftMargin = width/2;
				params2.leftMargin = width/2+width/5;
				params.topMargin = height/6*(i-1);
				params2.topMargin = height/6*(i-1)+height/4-height/16;

			}
			
			feedLayout.addView(iv,params);
			
			TextView p = new TextView(this);
			SpannableString spanString = new SpannableString(iv.name);
			spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
			p.setText(spanString);
			p.setBackgroundColor(Color.GRAY);
			p.getBackground().setAlpha(120);
			p.setGravity(Gravity.RIGHT);
			p.setTextSize(16);
			
			feedLayout.addView(p,params2);
			
		}
		
		if (mystores.size()>6){
			maplist = new ArrayList<Store>();
    		for(int i = 0; i < 6; i = i+1){
    			Store s = (Store) feedLayout.getChildAt(2*i);
    			
    			
    			
    			maplist.add(s);
    			Log.d("TESTING GETTING THE VIEWS", s.name);
    		}
    		mapavailable = true;
		}
	}
    
    public void onMapClick(View view){
    	if(mapavailable){
		mapLayout.removeAllViews();
		RelativeLayout.LayoutParams paramsL = new RelativeLayout.LayoutParams(width, 5*height/6);
		mapLayout.setBackgroundColor(Color.WHITE);
		viewAnimator.removeAllViews();
    	viewAnimator.addView(mapLayout,paramsL);
    	feedLayout.addView(viewAnimator);  
		
    	ImageView iv = new ImageView(this); 
    	
    	String markers = "";
    	for(int i = 0; i < maplist.size(); i = i+1){
    		Store p = maplist.get(i);
    		markers = markers + "&markers=color:green%7Clabel:"+(i+1)+"%7C"+p.address.replace(" ", "+").replace("\n", "+");
    		//Log.d("THIS IS THE GOOGLE QUERY", p.address.replace(" ", "+").replace("\n", "+"));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/6, height/6);
			
			if( i % 3 == 0 ){
				params.leftMargin = 10;
				params.topMargin = height/15*(i)+9*height/20;

			} else if( i % 3 == 1) {
				params.leftMargin = width/3;
				params.topMargin = height/15*(i-1)+9*height/20;

			} else {
				params.leftMargin = 2 * width/3;
				params.topMargin = height/15*(i-2)+9*height/20;
			}
			TextView mark = new TextView(this);
			SpannableString spanString = new SpannableString(""+(i+1)+"");
			spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
			mark.setText(spanString);
			mark.setBackgroundColor(Color.TRANSPARENT);
			mark.setGravity(Gravity.LEFT);
			mark.setTextSize(18);
			mark.setTextColor(Color.RED);
			
			feedLayout.removeView(p);
			mapLayout.addView(p,params);
			
			params.topMargin = params.topMargin + 20;
			params.leftMargin = params.leftMargin + 20;
			mapLayout.addView(mark,params);
			
    		
    	}
    	
    	
		String myURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=&size="+width+"x"+(int)(height/2.3)+"&key="+GOOGLE_API_KEY + "&sensor=true&center="+phoneLat+","+phoneLong+markers;
		Log.d("THIS IS THE GOOGLE QUERY", myURL);
		new DownloadImageTask(iv).execute(myURL);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, (int)(height/2.3));
		params.topMargin = 0;
		mapLayout.addView(iv,params);
		AnimationFactory.flipTransition(viewAnimator, FlipDirection.LEFT_RIGHT);
		
		mapavailable = false;
		mapbut.setImageResource(R.drawable.fourobjectsred);
		//feedAvailable = true;
    	} else {
    		mapLayout.removeAllViews();
    		feedLayout.removeAllViews();
    		viewAnimator.removeAllViews();
    		mapbut.setImageResource(R.drawable.mapbutton);
            Milo m = new Milo(this);
            m.queryNearbyStores(phoneLat, phoneLong, this);
            
    		ImageView iv = new ImageView(this);
    		iv.setBackgroundResource(R.drawable.loader);
    		
    		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
    		
    		MyAnimationRoutine mar = new MyAnimationRoutine(iv);
    		MyAnimationRoutine2 mar2 = new MyAnimationRoutine2(iv);

    		Timer t = new Timer(false);
    		t.schedule(mar, 100);
    		Timer t2 = new Timer(false);
    		t2.schedule(mar2, 15000);
    		
    		feedLayout.addView(iv,params);
    		
    	}
    }

}
