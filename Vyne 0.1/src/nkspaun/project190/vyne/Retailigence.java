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
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class Retailigence {
	final static String RETAILIGENCE_API_KEY = "wP413XHcg2QdpcLuASYSXXcj5TSnh2iJ";
	private MainActivity _mymain;
	Context context;
	//private ProductActivity _myproduct;
	//private int pid;
	
	public Retailigence(Context c) {
	
		context = c;
		
	}
	
    public void queryRProduct(String product,double phoneLat,double phoneLong, MainActivity mymain){
    	_mymain = mymain;
    	
		String myURL = "http://apitest.retailigence.com/v1.2/products?apikey=" + RETAILIGENCE_API_KEY + "&" + "latitude="+phoneLat+"&longitude="+phoneLong+"&radius=2&units=M&keywords="+product+"&format=JSON";
		Log.d("MY RESULTS: ", myURL);
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
        	Log.d("MY RESULTS: ", result);
        	
        	JSONObject jobj = null;
			try {
				jobj = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ArrayList<Product> rProducts = new ArrayList<Product>(); 
	    	JSONArray jarr;
			if(jobj!=null){
				try {
					jobj = new JSONObject(jobj.get("RetailigenceSearchResult").toString());
					jarr = new JSONArray(jobj.get("results").toString());

					
					for(int i = 0; i<jarr.length();i++ ){
						JSONObject j2 = new JSONObject(jarr.get(i).toString());
						j2 = new JSONObject(j2.get("SearchResult").toString());
						j2 = new JSONObject(j2.get("product").toString());
						JSONArray jiarray = new JSONArray(j2.get("images").toString());
						JSONObject jiobj = new JSONObject(jiarray.get(0).toString());
						jiobj = new JSONObject(jiobj.get("ImageInfo").toString());
						
						
						
						Product iv = new Product(context,j2.get("name").toString(),j2.get("descriptionShort").toString(),j2.get("id").toString(), jiobj.get("link").toString(),5,5,QueryType.retailigence);
						iv.price = "" + 55 + "";
						
						iv.setOnClickListener(new OnClickListener() {
							
					        public void onClick(View v) {					        						        
					        	
					        	Intent myIntent = new Intent(context, ProductActivity.class);
					        	Product p = (Product) v;
					        	
					        	p.onClick(_mymain.myDbHelper);
					    	   					        	
					        	myIntent.putExtra("myProduct", p.identifier);
					        	myIntent.putExtra("pname", p.name);
					        	myIntent.putExtra("description", p.description);
					        	myIntent.putExtra("url", p.image_url);
					        	myIntent.putExtra("geoLat", _mymain.phoneLat); // latitude
					        	myIntent.putExtra("geoLng", _mymain.phoneLong); // longitude
					        	context.startActivity(myIntent);  
					        }
					    });
						rProducts.add(iv);
						
						Log.d("MY RESULTS: ", rProducts.get(0).description);
					}
				} catch (JSONException e) {				
					e.printStackTrace();
				}
					
				
			}
        	_mymain.endQueryRetailigence(rProducts);
        }
      }

}
