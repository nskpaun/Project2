package nkspaun.vine;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import org.json.JSONArray;

public class MainActivity extends Activity {
	final static String MILO_API_KEY = "573ebb267db6e1eae39f4ae324558f18";


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);

    	//Remove notification bar
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final ImageView locator = new ImageView(this);
        locator.setImageResource(R.drawable.locator);
        
       
        
        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        
        
        final TextView text = (TextView) findViewById(R.id.message);
        
     // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
              //text.setText(location.toString());
            	mainLayout.removeView(locator);
            	 Log.i("LOCATION LAST KNOWN", "FOR THE LOVE OF MARY");
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(20, 20);
                params.topMargin = (int) ( 600 - ((location.getLatitude()-37.881899)*7000)) ;
                params.leftMargin =(int) ( 90 + ((location.getLongitude()+122.295734)*5540)) ;
                mainLayout.addView(locator, params);
              
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        
        String locationProvider = LocationManager.GPS_PROVIDER;
     // Or use LocationManager.GPS_PROVIDER

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        
        //Log.i("LOCATION LAST KNOWN", lastKnownLocation.toString());
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(20, 20);
        
        Log.i("------Latitude", ""+lastKnownLocation.getLatitude()+"");
        Log.i("-------Latitude", ""+lastKnownLocation.getLongitude()+"");
        
        params.topMargin = (int) ( 600 - ((lastKnownLocation.getLatitude()-37.881899)*7000)) ;
        params.leftMargin =(int) ( 90 + ((lastKnownLocation.getLongitude()+122.295734)*5540)) ;
        mainLayout.addView(locator, params);
        
		String myURL = "https://api.x.com/milo/v3/products?key=" + MILO_API_KEY + "&" + "latitude=37.8&longitude=-122.4&radius=.5&q=tooth+paste";
        try {
            URL google = new URL(myURL);
            URLConnection yc = google.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc
                    .getInputStream()));
            String inputLine;
            String finalLine = "";
            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                finalLine = finalLine+inputLine;
            }
            JSONObject jobj = new JSONObject(finalLine);
            JSONArray jarr = new JSONArray(jobj.get("products").toString());
            JSONObject j2 = new JSONObject(jarr.get(1).toString());
            Toast.makeText(getApplicationContext(), j2.get("name").toString(), Toast.LENGTH_LONG).show();

            //System.out.println( j2.get("name").toString() );
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        //text.setText("We're running");
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
