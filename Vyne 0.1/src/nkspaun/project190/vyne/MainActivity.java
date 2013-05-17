package nkspaun.project190.vyne;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {
	final static String MILO_API_KEY = "573ebb267db6e1eae39f4ae324558f18";
	final static String GOOGLE_API_KEY = "AIzaSyCeQZg095BGp-noLSTPegE9uXcNVD67vQY";
	public double phoneLat;
	public double phoneLong;
	private EditText searchText;
	private RelativeLayout feedLayout;
	public DataBaseHelper myDbHelper;
	int width;
	int height;
	private ArrayList<Product> mapList;
	private ImageButton mapbut;
	int cachecount;
	boolean mapavailable;
	private ViewAnimator viewAnimator;
	private ScrollView scroll;
	boolean feedAvailable;
	private RelativeLayout mapLayout;
	private ArrayList<AsyncTask<String, Void, String>> taskstocancel;
	boolean googleworks;
	public String lastQuery;
	private ImageButton profileim;
	private ImageButton compassim;
	private ImageView searchbar;
	private RelativeLayout mainlayout;
	
	/*@Override
	public void onResume(){
        overridePendingTransition( R.drawable.push_right_out, R.drawable.push_right_in);
	}*/
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	mapList = new ArrayList<Product>();

    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	viewAnimator = new ViewAnimator(this);
        
        super.onCreate(savedInstanceState);
        mapavailable = false;
        feedAvailable = false;
        googleworks = true;
        cachecount = 0;
        taskstocancel = new ArrayList<AsyncTask<String,Void,String>>();
        
        setContentView(R.layout.activity_main);
        feedLayout = (RelativeLayout) findViewById(R.id.feedView);
        searchText = (EditText) findViewById(R.id.editText1);
        mapbut = (ImageButton) findViewById(R.id.mapicon);
        scroll = (ScrollView) findViewById(R.id.scrollView1);
        searchbar = (ImageView) findViewById(R.id.mysearchbar);
        mainlayout = (RelativeLayout) findViewById(R.id.mainLayout);
        profileim = (ImageButton) findViewById(R.id.profile);
        compassim = (ImageButton) findViewById(R.id.compass);
        
        mapLayout = new RelativeLayout(this);  	
        
        /*scroll.removeAllViews();
        viewAnimator.addView(feedLayout);
        scroll.addView(viewAnimator);*/
        
        Display display = getWindowManager().getDefaultDisplay(); 
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated   
        
        searchText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					mapavailable = false;
					cachecount=0;
					mapbut.setImageResource(R.drawable.mapbuttongrey);
			    	for(int i = 0; i < taskstocancel.size(); i = i+1){
			    		if(!taskstocancel.get(i).isCancelled()){
			    			taskstocancel.get(i).cancel(false);
			    		}
			    		Log.d("CANCELING MAP TASK: ", ""+i+"");
			    		googleworks = true;
			    	}
			    	loadWithQuery(searchText.getText().toString());
                }
				return false;
			}
        });
        
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
              //text.setText(location.toString());
            	phoneLat = location.getLatitude();
            	phoneLong = location.getLongitude();              
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        
        phoneLat = 37.8;
        phoneLong = -122.4;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        String locationProvider = LocationManager.GPS_PROVIDER;

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        try {
        	phoneLat = lastKnownLocation.getLatitude();
        	phoneLong = lastKnownLocation.getLongitude();
        } catch(Exception e){

        }
        
        myDbHelper = new DataBaseHelper(this);

        try {
        	myDbHelper.createDataBase();
        } catch (IOException ioe) {
        	throw new Error("Unable to create database");
        }
	   
	   	try {
	   		myDbHelper.openDataBase();
	   	} catch(SQLException sqle) {
	   		throw sqle;
	   	}
		mapavailable = false;
		cachecount = 0;
		mapbut.setImageResource(R.drawable.mapbuttongrey);
	   	onFourClick(null);
        
    }
    
    public void onCompassClick(View view)  
    {
    	mainlayout.addView(searchbar);
    	mainlayout.addView(searchText);
    	compassim.setImageResource(R.drawable.search);
    	profileim.setImageResource(R.drawable.profileimage);
    	//loadWithQuery(searchText.getText().toString());
    }
    
    public void onMapClick(View view) {	
    	if(mapavailable){
    		mapLayout.removeAllViews();
    		RelativeLayout.LayoutParams paramsL = new RelativeLayout.LayoutParams(width, 10*height/6);
    		mapLayout.setBackgroundColor(Color.WHITE);
    		viewAnimator.removeAllViews();
        	viewAnimator.addView(mapLayout,paramsL);
        	feedLayout.addView(viewAnimator);  
    		
	    	ImageView iv = new ImageView(this); 
	    	
	    	String markers = "";
	    	for(int i = 0; i < 6; i = i+1){
	    		Product p = mapList.get(i);
	    		markers = markers + "&markers=color:green%7Clabel:"+(i+1)+"%7C"+p.address.replace(" ", "+").replace("\n", "+");
	    		//Log.d("THIS IS THE GOOGLE QUERY", p.address.replace(" ", "+").replace("\n", "+"));
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/6, height/6);
				
				params.leftMargin = 10;
				params.topMargin = height/15*(i*3)+9*height/20;
					
				feedLayout.removeView(p);
				mapLayout.addView(p,params);

				TextView mark = new TextView(this);
				SpannableString spanString = new SpannableString(""+(i+1)+"");
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
				mark.setText(spanString);
				mark.setBackgroundColor(Color.TRANSPARENT);
				mark.setGravity(Gravity.LEFT);
				mark.setTextSize(18);
				mark.setTextColor(Color.RED);
				
				
				params = new RelativeLayout.LayoutParams(height/6, height/6);
				params.topMargin = height/15*(i*3)+9*height/20+params.topMargin + 10;
				params.leftMargin = 10 + height/6;
				
				mapLayout.addView(mark,params);
				
				TextView name = new TextView(this);
				spanString = new SpannableString(p.name);
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
				name.setText(spanString);
				name.setBackgroundColor(Color.TRANSPARENT);
				name.setGravity(Gravity.LEFT);
				name.setTextSize(16);
				
				params = new RelativeLayout.LayoutParams(width/2, height/6);
				params.leftMargin = height/6+50;
				params.topMargin = height/15*(i*3)+9*height/20+params.topMargin + 20;
				
				mapLayout.addView(name,params);
				
				TextView pricet = new TextView(this);
				spanString = new SpannableString(p.price);
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
				pricet.setText(spanString);
				pricet.setBackgroundColor(Color.TRANSPARENT);
				pricet.setGravity(Gravity.RIGHT);
				pricet.setTextSize(16);
				
				params = new RelativeLayout.LayoutParams(2*width/3, height/6);
				params.leftMargin = height/6+50;
				params.topMargin = height/15*(i*3)+9*height/20+params.topMargin + 20;
				
				mapLayout.addView(pricet,params);
				
				TextView white = new TextView(this);
				white.setText(p.address);
				white.setBackgroundColor(Color.WHITE);
				white.setGravity(Gravity.CENTER);
				white.setTextSize(12);
				
				params = new RelativeLayout.LayoutParams(width/2, height/12);
				params.leftMargin = height/6+50;
				params.topMargin = height/15*(i*3)+9*height/20+height/12;
				
				mapLayout.addView(white,params);
				
				TextView addr = new TextView(this);
				addr.setText(p.address);
				addr.setBackgroundColor(Color.WHITE);
				addr.setGravity(Gravity.CENTER);
				addr.setTextSize(12);
				
				params = new RelativeLayout.LayoutParams(width/2, height/12);
				params.leftMargin = height/6+50;
				params.topMargin = height/15*(i*3)+9*height/20+height/10;
				
				mapLayout.addView(addr,params);
				
				TextView dist = new TextView(this);
				DecimalFormat df = new DecimalFormat("#.#");
				dist.setText(""+df.format(p.latitude)+" mi away");
				dist.setBackgroundColor(Color.TRANSPARENT);
				dist.setGravity(Gravity.RIGHT);
				dist.setTextSize(10);
				
				params = new RelativeLayout.LayoutParams(2*width/3, height/6);
				params.leftMargin = height/6+50;
				params.topMargin = height/15*(i*3)+9*height/20+height/8;
				
				mapLayout.addView(dist,params);
				
				TextView merc = new TextView(this);
				spanString = new SpannableString(p.merchant);
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
				merc.setText(spanString);
				merc.setBackgroundColor(Color.TRANSPARENT);
				merc.setGravity(Gravity.CENTER);
				merc.setTextSize(12);
				
				params = new RelativeLayout.LayoutParams(width/2, height/12);
				params.leftMargin = height/6+50;
				params.topMargin = height/15*(i*3)+9*height/20+height/14;
				
				mapLayout.addView(merc,params);
				
				
	    		
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
			feedAvailable = true;
    	} else if(feedAvailable){
    		AnimationFactory.flipTransition(viewAnimator, FlipDirection.RIGHT_LEFT);
    		for(int i = 0; i < 6; i = i+1){
    			Product pr = mapList.get(i);
    			
				TextView p = new TextView(this);
				SpannableString spanString = new SpannableString("$"+pr.price+"   ");
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
				p.setText(spanString);
				p.setBackgroundColor(Color.GRAY);
				p.getBackground().setAlpha(120);
				p.setGravity(Gravity.RIGHT);
				p.setTextSize(18);
				
				TextView namet = new TextView(this);
				namet.setBackgroundColor(Color.TRANSPARENT);
				spanString = new SpannableString(pr.name);
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
				namet.setText(spanString);
				namet.setTextSize(14);
				
				TextView distancet = new TextView(this);
				distancet.setBackgroundColor(Color.TRANSPARENT);
				distancet.setText("< 0.5 mi away");
				distancet.setTextSize(12);					
	        
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/4, height/4);
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(width/2, height/16);
				RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams((int)(width*.375), height/32);

				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(width/2, height/32);
				
				if( i % 2 == 0 ){
					params.leftMargin = 10;
					params2.leftMargin = 0;
					params.topMargin = height/6*i;
					params2.topMargin = height/6*i+height/4-height/16;

				} else {
					params.leftMargin = width/2;
					params2.leftMargin = width/2;
					params.topMargin = height/6*(i-1);
					params2.topMargin = height/6*(i-1)+height/4-height/16;

				}
				
				params3.leftMargin = params2.leftMargin;
				params3.topMargin = params2.topMargin + height/32;
				params4.leftMargin = params2.leftMargin;
				params4.topMargin = params2.topMargin;
				mapLayout.removeView(pr);
				feedLayout.removeView(pr);
				feedLayout.addView(pr,params);
				feedLayout.addView(p,params2);
				feedLayout.addView(namet, params4);
				feedLayout.addView(distancet, params3);
    		}
			mapavailable = true;
			mapbut.setImageResource(R.drawable.mapbutton);
			feedAvailable = false;
			feedLayout.removeView(viewAnimator);
    		
    	}
    	
    }
    
    public void onFourClick(View view) {
    	mainlayout.removeView(searchText);
    	mainlayout.removeView(searchbar);
    	compassim.setImageResource(R.drawable.searchgrey);
    	profileim.setImageResource(R.drawable.profileimagewhite);
		String query = "";
		
	   	Cursor cursor = myDbHelper.myDataBase.rawQuery("select name, clicks from item order by 2 desc limit 4;", new String[] { });
	   	if(cursor.getCount()>0){
    	   	cursor.moveToFirst();
    	   	for(int i = 0; i<cursor.getCount(); i = i+1){
    	   		if(i>0){
    	   			query = query + "%20OR%20 "+cursor.getString(0);
    	   		} else {
    	   			query = cursor.getString(0);
    	   		}
    	   		cursor.moveToNext();
    	   	}
    	   	loadWithQuery(query); 	
	   	}
	   	cursor.close();
    }
    
    public void loadWithQuery(String query) {
    	String searchterms = query.replace(" ", "+");
    	searchterms = searchterms.replace("'", "%27");
    	searchterms = searchterms.replace("&", "and");
    	
    	lastQuery = searchterms.replace("%20OR%20", "%7C");
		Milo m = new Milo(this);
		
		
		Log.d("KDKLJSLKSSKLJS", searchterms);
		m.queryMiloProduct(searchterms,phoneLat,phoneLong,this);
    	
    	/*Retailigence r = new Retailigence(this);
    	r.queryRProduct("searchterms", phoneLat, phoneLong, this);*/
    	
    	
		
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
    
    public void endMapQuery(JSONObject jobj,int pid){
    	//TODO kill these when hitting something else
    	cachecount = cachecount+1;
    	String address;
    	String storename;
    	String price;
    	String phoneNumber;
    	Product p = mapList.get(pid);
    	
    	//productlayout.removeView(iv);
        try {
			address = jobj.get("street").toString()+"\n"+jobj.get("city")+", " + jobj.get("region") + " " + jobj.get("zip_code");
			storename = jobj.get("name").toString();
			price = "$" + jobj.get("price").toString();
			phoneNumber = jobj.get("phone").toString();
			p.setMapInfo(storename, address, phoneNumber, price);
			p.latitude = manhattanDist(jobj.getDouble("latitude"),jobj.getDouble("longitude"));
			p.merchant = jobj.get("name").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(cachecount==6){
        	mapbut.setImageResource(R.drawable.mapbutton);
        	mapavailable = true;
        }
    	
    }
    
    public double rad(double x) {return x*Math.PI/180;}

    public double manhattanDist(double lat, double lon) {
      double R = 3959; // earth's mean radius in km
      double dLon = rad(Math.abs(lon - phoneLong));
      
      double LongDist = Math.cos(rad(lat)) * Math.cos(rad(phoneLat)) * Math.sin(dLon/2) * Math.sin(dLon/2);
      double c = 2 * Math.atan2(Math.sqrt(LongDist), Math.sqrt(1-LongDist));
      LongDist = R * c;
      
      
      
      
      Log.d("HERE IS YOUR STUFF FOO","lat:"+lat+" long:"+phoneLat+"");
      double e = ((lat-phoneLat)/360)*24875.13;
      double answer = (e+LongDist);
      DecimalFormat df = new DecimalFormat("#.#");
      return answer;
    }
    
    public void killTasks(){
    	
    	for(int i = 0; i < taskstocancel.size(); i = i+1){
    		if(!taskstocancel.get(i).isCancelled()){
    			taskstocancel.get(i).cancel(true);
    		}
    		Log.d("CANCELING MAP TASK: ", ""+i+"");
    	}
    	
    }
    
    public void endQuery(ArrayList<Product> milop) {
    	feedLayout.removeAllViews();
		if(milop.size()>0){
				for(int i = 0; i < milop.size(); i = i+1){	//ADDING TEXTVIEWS
					Product iv = milop.get(i);
					
					new DownloadImageTask(iv).execute(iv.image_url);
					
					TextView p = new TextView(this);
					p.setText("\n$"+iv.price+"   ");
					p.setBackgroundColor(Color.GRAY);
					p.getBackground().setAlpha(120);
					p.setGravity(Gravity.RIGHT);
					p.setTextSize(16);
					
					TextView namet = new TextView(this);
					SpannableString spanString = new SpannableString(iv.name);
					spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
					namet.setBackgroundColor(Color.TRANSPARENT);
					spanString = new SpannableString(iv.name);
					spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
					namet.setText(spanString);
					namet.setTextSize(14);				
		        
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/4, height/4);
					RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(width/2-width/5, height/16);
					RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams((int)(width*.375), height/32);
					
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
					
					params4.leftMargin = params2.leftMargin;
					params4.topMargin = params2.topMargin;
		        
					feedLayout.addView(iv,params);
					feedLayout.addView(p,params2);
					feedLayout.addView(namet, params4);
				}
			if (milop.size()>6){
				taskstocancel = new ArrayList<AsyncTask<String,Void,String>>();
	    		for(int i = 0; i < 6; i = i+1){
	    			Product p = (Product) feedLayout.getChildAt(i*3);
	    			if(p.type==QueryType.milo){
	    				mapList.add(i, p);
	    				Milo m = new Milo(this);
	    				taskstocancel.add(m.queryMiloStoreMap(p.identifier, phoneLat, phoneLong,this,i));
	    				Log.d("TESTING GETTING THE VIEWS", p.name);
	    			}
	    		}
			}
			googleworks = true;
		} else if (googleworks) {
			GoogleAPI g = new GoogleAPI(this);
			Toast.makeText(this, "No results found searching for online products", Toast.LENGTH_LONG).show();
			g.queryGoogleProduct(lastQuery, this);
			googleworks = false;
		}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void endQueryRetailigence(ArrayList<Product> rProducts){
    	
    	feedLayout.removeAllViews();
				for(int i = 0; i<rProducts.size();i++ ){
			
					
					//ADDING TEXTVIEWS
					Product iv = rProducts.get(i);
					Log.d("MY RESULTS: ", iv.description);
					new DownloadImageTask(iv).execute(iv.image_url);
					
					TextView p = new TextView(this);
					SpannableString spanString = new SpannableString("$"+iv.price+"   ");
					spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
					p.setText(spanString);
					p.setBackgroundColor(Color.GRAY);
					p.getBackground().setAlpha(120);
					p.setGravity(Gravity.RIGHT);
					p.setTextSize(18);
					
					TextView namet = new TextView(this);
					namet.setBackgroundColor(Color.TRANSPARENT);
					spanString = new SpannableString(iv.name);
					spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
					namet.setText(spanString);
					namet.setTextSize(14);
					
					TextView distancet = new TextView(this);
					distancet.setBackgroundColor(Color.TRANSPARENT);
					distancet.setText("< 0.5 mi away");
					distancet.setTextSize(12);					
		        
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/4, height/4);
					RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(width/2, height/16);
					RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams((int)(width*.375), height/32);

					RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(width/2, height/32);
					
					if( i % 2 == 0 ){
						params.leftMargin = 10;
						params2.leftMargin = 0;
						params.topMargin = height/6*i;
						params2.topMargin = height/6*i+height/4-height/16;

					} else {
						params.leftMargin = width/2;
						params2.leftMargin = width/2;
						params.topMargin = height/6*(i-1);
						params2.topMargin = height/6*(i-1)+height/4-height/16;

					}
					
					params3.leftMargin = params2.leftMargin;
					params3.topMargin = params2.topMargin + height/32;
					params4.leftMargin = params2.leftMargin;
					params4.topMargin = params2.topMargin;
		        
					feedLayout.addView(iv,params);
					feedLayout.addView(p,params2);
					feedLayout.addView(namet, params4);
					feedLayout.addView(distancet, params3);
				}
		
    	
    }
    
    public void onHouseClick(View view){
    	
    	for(int i = 0; i < taskstocancel.size(); i = i+1){
    		if(!taskstocancel.get(i).isCancelled()){
    			taskstocancel.get(i).cancel(true);
    		}
    		Log.d("CANCELING MAP TASK: ", ""+i+"");
    	}
    	
    	Intent myIntent = new Intent(this, StoreActivity.class);
    	myIntent.putExtra("geoLat", phoneLat); // latitude
    	myIntent.putExtra("geoLng", phoneLong);
    	this.startActivity(myIntent);
    	
    }
    
    @Override
	public void onResume(){
        
        super.onResume();
        overridePendingTransition( R.drawable.push_right_out, R.drawable.push_right_in);
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



