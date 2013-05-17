package nkspaun.project190.vyne;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;

import android.util.Log;
import android.view.Display;
import android.view.Menu;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductActivity extends Activity {
	final static String GOOGLE_API_KEY = "AIzaSyCeQZg095BGp-noLSTPegE9uXcNVD67vQY";
	private double phoneLat;
	private double phoneLong;
	private String product_id;
	private TextView productname;
	private TextView description;
	private TextView storename;
	private TextView address;
	private TextView price;
	@SuppressWarnings("unused")
	private TextView distance;
	private ImageView product;
	private RelativeLayout productlayout;
	private String phoneNumber;
	private DataBaseHelper myDbHelper;
	private Product p;
	int width;
	int height;
	private ImageView iv;
	double plong;
	double plat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_product);
        
        productname = (TextView) findViewById(R.id.productname);
        description = (TextView) findViewById(R.id.description);
        storename = (TextView) findViewById(R.id.storename);
        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        distance = (TextView) findViewById(R.id.distance);
        productlayout = (RelativeLayout) findViewById(R.id.product_layout);
        product = new ImageView(this);
        
        String htmlDesc = this.getIntent().getExtras().getString("description");
        
        product_id = this.getIntent().getExtras().getString("myProduct");
        String productn = this.getIntent().getExtras().getString("pname");
        productname.setText(productn.substring(0, (int)Math.min(24, productn.length()))+"...");
        description.setText(Jsoup.parse(htmlDesc).text());
        phoneLat = this.getIntent().getExtras().getDouble("geoLat");
        phoneLong = this.getIntent().getExtras().getDouble("geoLng");
        
        Display display = getWindowManager().getDefaultDisplay(); 
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated

        p = new Product(this, productname.getText().toString(), null, null, null,5,5,QueryType.milo);
        
        if(product_id!=null){
        	
            Milo m = new Milo(this);
            m.queryMiloStore(product_id, phoneLat, phoneLong,this);
  
        }
        
		iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.loader);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		
		MyAnimationRoutine mar = new MyAnimationRoutine(iv);
		MyAnimationRoutine2 mar2 = new MyAnimationRoutine2(iv);

		Timer t = new Timer(false);
		t.schedule(mar, 100);
		Timer t2 = new Timer(false);
		t2.schedule(mar2, 15000);
		
		productlayout.addView(iv,params);
        
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

	   	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_product, menu);
        return true;
    }
    
    public void onCallClick(View view){
    	
    	p.onReserve(myDbHelper);
    	
    	String url = "tel:"+phoneNumber;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        startActivity(intent);
    	
    }
    
    public void endQuery(JSONObject jobj){
    	productlayout.removeView(iv);
        try {
			address.setText(jobj.get("street").toString()+"\n"+jobj.get("city")+", " + jobj.get("region") + " " + jobj.get("zip_code"));
			distance.setText("< "+manhattanDist(jobj.getDouble("latitude"),jobj.getDouble("longitude"))+" miles away");
			plat = jobj.getDouble("latitude");
			plong = jobj.getDouble("longitude");
			storename.setText(jobj.get("name").toString());
			price.setText("$" + jobj.get("price").toString());
			phoneNumber = jobj.get("phone").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        new DownloadImageTask(product).execute(this.getIntent().getExtras().getString("url"));
        
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height/4, height/4);
		
		params.leftMargin = width/2-(height/8);			
		params.topMargin = height/10;        
		productlayout.addView(product,params);
		
		ImageView iv = new ImageView(this); 
	   	String markers = "&markers=color:green%7Clabel:"+1+"%7C"+address.getText().toString().replace(" ", "+").replace("\n", "+");
	   	String myURL = "http://maps.googleapis.com/maps/api/staticmap?size="+((int)(3*width/4))+"x"+(int)(height/6)+"&key="+GOOGLE_API_KEY + "&sensor=true&center="+phoneLat+","+phoneLong+markers;
		new DownloadImageTask(iv).execute(myURL);
		params = new RelativeLayout.LayoutParams(3*width/4, height/6);
		
		params.leftMargin = width/8;			
		params.topMargin = 13*height/20;   
		productlayout.addView(iv,params);
    	
    }
    
    public double rad(double x) {return x*Math.PI/180;}

    public String manhattanDist(double lat, double lon) {
      double R = 3959; // earth's mean radius in km
      double circ = 2*Math.PI*R;
      double dLat  = Math.abs(lat - phoneLat)/360;
      double dLon = rad(Math.abs(lon - phoneLong));
      
      double LongDist = Math.cos(rad(lat)) * Math.cos(rad(phoneLat)) * Math.sin(dLon/2) * Math.sin(dLon/2);
      double c = 2 * Math.atan2(Math.sqrt(LongDist), Math.sqrt(1-LongDist));
      LongDist = R * c;
      
      
      
      
      Log.d("HERE IS YOUR STUFF FOO","lat:"+lat+" long:"+phoneLat+"");
      double e = ((lat-phoneLat)/360)*24875.13;
      double answer = (e+LongDist);
      DecimalFormat df = new DecimalFormat("#.#");
      return df.format(answer);
    }



}


