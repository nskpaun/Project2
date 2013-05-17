package nkspaun.project190.vyne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import android.os.Bundle;
import android.app.Activity;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class BrowseActivity extends Activity {
	private int width;
	private int height;
	public double phoneLat;
	public double phoneLong;
	public String merchantid;
	private RelativeLayout feedLayout;
	private EditText searchText;
	public DataBaseHelper myDbHelper;
	public ImageView searchbar;
	private ImageButton searchbut;
	private RelativeLayout mainlayout;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay(); 
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated  
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        
        feedLayout = (RelativeLayout) findViewById(R.id.feedView);
        searchText = (EditText) findViewById(R.id.editText1);
        searchbar = (ImageView) findViewById(R.id.searchbar);
        searchbut = (ImageButton) findViewById(R.id.compass);
        
        mainlayout = (RelativeLayout) findViewById(R.id.mainLayout);
        
        mainlayout.removeView(searchText);
        mainlayout.removeView(searchbar);
        
        phoneLat = this.getIntent().getExtras().getDouble("geoLat");
        phoneLong = this.getIntent().getExtras().getDouble("geoLng");
        merchantid = this.getIntent().getExtras().getString("myStore");
        String imageurl = this.getIntent().getExtras().getString("myImage");
        String address = this.getIntent().getExtras().getString("myAddress");
        String mercname = this.getIntent().getExtras().getString("myName");
        
        ImageView lv = new ImageView(this);
        
        new DownloadImageTask(lv).execute(imageurl);
        
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/4, height/4);
		params.topMargin = height/10;
		params.leftMargin = 30;
		
		mainlayout.addView(lv,params);
		
		TextView namet = new TextView(this);
		namet.setBackgroundColor(Color.TRANSPARENT);
		namet.setText(mercname);
		namet.setTextSize(14);		
		namet.setTextColor(Color.WHITE);
		namet.setGravity(Gravity.CENTER);
		
		params = new RelativeLayout.LayoutParams(width/2, height/10);
		params.topMargin = height/10;
		params.leftMargin = width/2;
		
		mainlayout.addView(namet,params);
		
		TextView addt = new TextView(this);
		addt.setBackgroundColor(Color.TRANSPARENT);
		addt.setText(address);
		addt.setTextSize(12);
		addt.setTextColor(Color.WHITE);
		addt.setGravity(Gravity.CENTER);
		
		params = new RelativeLayout.LayoutParams(width/2, height/5);
		params.topMargin = 9*height/80;
		params.leftMargin = width/2;
		
		mainlayout.addView(addt,params);
        
        searchText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					mainlayout.removeView(searchText);
			        mainlayout.removeView(searchbar);
					loadWithQuery(v.getText().toString());
                }
				return false;
			}
        });

        
        loadWithQuery("");
        
        myDbHelper = new DataBaseHelper(this);
        
        try {
 
        	myDbHelper.createDataBase();
 
	   		} catch (IOException ioe) {
	   
	   			throw new Error("Unable to create database");
	   
	   		}
	   
	   	try {
	   
	   		myDbHelper.openDataBase();
	   		myDbHelper.myDataBase.close();
	   
	   	}catch(SQLException sqle){
	   
	   		throw sqle;
	   
	   	}

        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    public void loadWithQuery(String query) {
    	String searchterms = query.replace(" ", "+");
    	searchterms = searchterms.replace("'", "%27");
    	searchterms = searchterms.replace("&", "and");
    	
    	
		Milo m = new Milo(this);
		
		
		Log.d("In Store Query", searchterms);
		m.queryMiloProductBrowse(searchterms,phoneLat,phoneLong,merchantid,this);
    	
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_browse, menu);
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
    
    public void endQuery(ArrayList<Product> milop) {
    	feedLayout.removeAllViews();
		if(milop!=null){
				for(int i = 0; i < milop.size(); i = i+1){	//ADDING TEXTVIEWS
					Product iv = milop.get(i);
					
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
		        
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/6, height/6);
					RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(width/2, height/16);
					RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams((int)(width*.375), height/32);

					RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(width/2, height/32);
					
					if( i % 3 == 0 ){
						params.leftMargin = 10;
						params2.leftMargin = 0;
						params.topMargin = height/12*i;
						params2.topMargin = height/6*i+height/4-height/16;

					} else if (i % 3 == 1) {
						params.leftMargin = width/3;
						params2.leftMargin = width/3;
						params.topMargin = height/12*(i-1);
						params2.topMargin = height/6*(i-1)+height/4-height/16;
						
					} else {
						params.leftMargin = 2*width/3;
						params2.leftMargin = 2*width/3;
						params.topMargin = height/12*(i-2);
						params2.topMargin = height/6*(i-1)+height/4-height/16;

					}
					
					params3.leftMargin = params2.leftMargin;
					params3.topMargin = params2.topMargin + height/32;
					params4.leftMargin = params2.leftMargin;
					params4.topMargin = params2.topMargin;
		        
					feedLayout.addView(iv,params);
					//feedLayout.addView(p,params2);
					//feedLayout.addView(namet, params4);
					//feedLayout.addView(distancet, params3);
				}
		}
    	
    }
    
    public void onHouseClick(View view){
    	this.finish();
    }
    public void onCompassClick(View view){
    	searchbut.setImageResource(R.drawable.search);
    	mainlayout.addView(searchbar);
    	mainlayout.addView(searchText);
    }

}
