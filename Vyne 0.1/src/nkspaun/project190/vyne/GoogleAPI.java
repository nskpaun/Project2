package nkspaun.project190.vyne;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class GoogleAPI {
	private Context context;
	final static String GOOGLE_API_KEY = "AIzaSyCeQZg095BGp-noLSTPegE9uXcNVD67vQY";
	private MainActivity _mymain;
	
	public GoogleAPI(Context c){
		context = c;
	}
	
    public void queryGoogleProduct(String product, MainActivity mymain){
    	_mymain = mymain;
    	
		String myURL = "https://www.googleapis.com/shopping/search/v1/public/products?key=" + GOOGLE_API_KEY + "&country=US&"+"q="+product+"&alt=json";
        try {

            DownloadWebPageTask task = new DownloadWebPageTask();
            task.execute(new String[] { myURL});
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
          String response = "";
          for (String url : urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
              HttpResponse execute = client.execute(httpGet);
              InputStream content = execute.getEntity().getContent();

              BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
              String s = "";
              while ((s = buffer.readLine()) != null) {
                response += s;
              }

            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          return response;
        }

        @Override
        protected void onPostExecute(String result) {
        	Log.d("GOOGLE PRODUCTS RESULT:", result);
        	JSONObject jobj = null;
        	ArrayList<Product> myProducts = new ArrayList<Product>();
			try {
				jobj = new JSONObject(result);
				
				JSONArray jarr = new JSONArray(jobj.get("items").toString());
				
				for(int i = 0; i < jarr.length(); i = i+1){
					JSONObject jobj2 = jarr.getJSONObject(i);
					jobj2 = new JSONObject(jobj2.get("product").toString());
					JSONObject jimage = new JSONObject(jobj2.getJSONArray("images").get(0).toString());
					JSONArray jinv = new JSONArray(jobj2.get("inventories").toString());
					Product p = new Product(context, jobj2.getString("title"), jobj2.getString("description"), jobj2.getString("link"), jimage.getString("link"), 5, 5, QueryType.google);
					double pp = jinv.getJSONObject(0).getInt("price");
					p.price = "$" + pp +"";
					
					p.setOnClickListener(new OnClickListener() {
						
				        public void onClick(View v) {					        						        
				        	Product p = (Product) v;
				        	
				        	Uri uriUrl = Uri.parse(p.identifier);   
				        	
				        	Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
				        	context.startActivity(launchBrowser);  
				        }
				    });
					
					myProducts.add(p);
					
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
        	_mymain.endQuery(myProducts);
        }
      }

}
