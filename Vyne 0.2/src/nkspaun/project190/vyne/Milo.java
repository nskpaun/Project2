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

public class Milo {
	final static String MILO_API_KEY = "573ebb267db6e1eae39f4ae324558f18";
	private MainActivity _mymain;
	private ProductActivity _myproduct;
	private StoreActivity _mystore;
	private BrowseActivity _mybrowse;
	private int pid;
	Context context;
	
	public Milo(Context c){
		context = c;
	}
	
    public void queryMiloProduct(String product,double phoneLat,double phoneLong, MainActivity mymain){
    	_mymain = mymain;
    	
		String myURL = "https://api.x.com/milo/v3/products?key=" + MILO_API_KEY + "&" + "latitude="+phoneLat+"&longitude="+phoneLong+"&radius=2&q="+product+"&show=DescImg100";
        Log.d("MY RESULTS: ", myURL);
		try {

            DownloadWebPageTask task = new DownloadWebPageTask();
            task.execute(new String[] { myURL});
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void queryMiloProductBrowse(String product,double phoneLat,double phoneLong, String mid, BrowseActivity mymain){
    	_mybrowse = mymain;
    	
		String myURL = "https://api.x.com/milo/v3/products?key=" + MILO_API_KEY + "&" + "latitude="+phoneLat+"&longitude="+phoneLong+"&radius=2&show=DescImg100&merchant_ids="+mid+"";
		if(product.length()>0){
			myURL = myURL+"&q="+product+"";
		}
		Log.d("MY RESULTS: ", myURL);
        try {

            DownloadWebPageTask5 task = new DownloadWebPageTask5();
            task.execute(new String[] { myURL});
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void queryMiloStore(String identifier,double phoneLat,double phoneLong, ProductActivity myproduct){
    	
    	_myproduct = myproduct;
		
    	String myURL = "https://api.x.com/milo/v3/availability?key=" + MILO_API_KEY + "&" + "latitude="+phoneLat+"&longitude="+phoneLong+"&radius=2&product_id="+identifier;
    	Log.d("MY RESULTS: ", myURL);
    	try {

        	DownloadWebPageTask2 task = new DownloadWebPageTask2();
            task.execute(new String[] { myURL});
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public AsyncTask<String,Void,String> queryMiloStoreMap(String identifier,double phoneLat,double phoneLong, MainActivity myproduct, int p){
    	pid = p;
    	_mymain = myproduct;
    	DownloadWebPageTask3 task = new DownloadWebPageTask3();
		String myURL = "https://api.x.com/milo/v3/availability?key=" + MILO_API_KEY + "&" + "latitude="+phoneLat+"&longitude="+phoneLong+"&radius=2&product_id="+identifier;
        try {

        	
            task.execute(new String[] { myURL});
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }
    
    public void queryNearbyStores(double phoneLat,double phoneLong, StoreActivity mystore){
    	_mystore = mystore;
    	String myURL = "https://api.x.com/milo/v3/store_addresses?key=" + MILO_API_KEY + "&" + "latitude="+phoneLat+"&longitude="+phoneLong+"&radius=2&show=MlogoMid";
    	Log.d("NEAR STORE URL", myURL);
        try {

        	DownloadWebPageTask4 task = new DownloadWebPageTask4();
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
        	Log.d("SDKJSLKJFLKJSKLDJFLKJSLDK", result);
        	JSONObject jobj = null;
        	ArrayList<Product> myProducts = new ArrayList<Product>();
			try {
				jobj = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(jobj!=null){
				try {
					JSONArray jarr = new JSONArray(jobj.get("products").toString());
					
					for(int i = 0; i<jarr.length();i++ ){
						JSONObject j2 = new JSONObject(jarr.get(i).toString());
						
						Product iv = new Product(context,j2.get("name").toString(),j2.get("description").toString(),j2.get("product_id").toString(), j2.getString("image_100").toString(),5,5,QueryType.milo);
						iv.price = ""+j2.getInt("max_price")/100+"";
						iv.setOnClickListener(new OnClickListener() {
							
					        public void onClick(View v) {					        						        
					        	
					        	_mymain.killTasks();
					        	
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
						myProducts.add(iv);
					}
				} catch (JSONException e) {				
					e.printStackTrace();
				}
			}
			
        	_mymain.endQuery(myProducts);
        }
      }
    
    private class DownloadWebPageTask2 extends AsyncTask<String, Void, String> {
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
        	JSONObject jobj2 = null;
			try {
				
	            JSONObject jobj = new JSONObject(result);	
	            
	            String secondJobj = result.substring(jobj.toString().length()-1);
	            jobj2 = new JSONObject(secondJobj);
	            jobj2 = jobj2.getJSONObject("location");
	            JSONObject jobj3 = new JSONObject(jobj.get("merchant").toString());
	            jobj2.put("image_url", jobj3.get("image_url") );
	            jobj2.put("name", jobj3.get("name"));
	            
	            
	            
	            String thirdJobj = secondJobj.substring(jobj2.toString().length()-1);
	            int myindex = thirdJobj.indexOf("\"price\":");
	            if(myindex>0){
	            	thirdJobj = thirdJobj.substring(thirdJobj.indexOf("\"price\":"));
	            	thirdJobj = thirdJobj.substring(8, thirdJobj.indexOf(","));
		            thirdJobj = thirdJobj.substring(0, thirdJobj.length()-2) + "." + thirdJobj.substring(thirdJobj.length()-2);
		            jobj2.put("price", thirdJobj);
	            } else {
	            	jobj2.put("price", "No Price Data");
	            }
	            

			} catch (JSONException e) {
				e.printStackTrace();
			}
        	_myproduct.endQuery(jobj2);
        }
      }
    
    private class DownloadWebPageTask3 extends AsyncTask<String, Void, String> {
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
        	Log.d("MAP QUERY RESULT: ", "anything?"+result);
        	JSONObject jobj2 = null;
			try {
				
	            JSONObject jobj = new JSONObject(result);	
	            
	            String secondJobj = result.substring(jobj.toString().length()-1);
	            jobj2 = new JSONObject(secondJobj);
	            jobj2 = jobj2.getJSONObject("location");
	            JSONObject jobj3 = new JSONObject(jobj.get("merchant").toString());
	            jobj2.put("image_url", jobj3.get("image_url") );
	            jobj2.put("name", jobj3.get("name"));
	            
	            
	            
	            String thirdJobj = secondJobj.substring(jobj2.toString().length()-1);
	            int myindex = thirdJobj.indexOf("\"price\":");
	            if(myindex>0){
	            	thirdJobj = thirdJobj.substring(thirdJobj.indexOf("\"price\":"));
	            	thirdJobj = thirdJobj.substring(8, thirdJobj.indexOf(","));
		            thirdJobj = thirdJobj.substring(0, thirdJobj.length()-2) + "." + thirdJobj.substring(thirdJobj.length()-2);
		            jobj2.put("price", thirdJobj);
	            } else {
	            	jobj2.put("price", "No Price Data");
	            }
	            

			} catch (JSONException e) {
				e.printStackTrace();
			}
        	_mymain.endMapQuery(jobj2,pid);
        }
      }
    
    private class DownloadWebPageTask4 extends AsyncTask<String, Void, String> {
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
        	Log.d("NEAR STORE Q RESULT", result);
        	JSONObject jobj = null;
        	ArrayList<Store> myStores = new ArrayList<Store>();
			try {
				jobj = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(jobj!=null){
				try {
					JSONArray jarr = new JSONArray(jobj.get("store_addresses").toString());
					
					for(int i = 0; i<jarr.length();i++ ){
						JSONObject j2 = new JSONObject(jarr.get(i).toString());
						
						Store iv = new Store(context, j2.getString("merchant_name"), j2.getString("merchant_logo_url"), j2.getString("street")+"\n"+j2.getString("city")+", "+j2.getString("region") + " " + j2.getString("postal_code"), j2.getString("phone"), j2.getInt("merchant_id"));
						//iv.price = ""+j2.getInt("max_price")/100+"";
						iv.setOnClickListener(new OnClickListener() {
							
					        public void onClick(View v) {
					        	Log.d("YOU CLICKED A STORE!","CONGRATS");
					        	Store s = (Store) v;
					        	Intent myIntent = new Intent(context, BrowseActivity.class);
					        	myIntent.putExtra("geoLat", _mystore.phoneLat); // latitude
					        	myIntent.putExtra("geoLng", _mystore.phoneLong);
					        	myIntent.putExtra("myStore", ""+s.identifier+"");
					        	myIntent.putExtra("myImage", s.imageurl);
					        	myIntent.putExtra("myAddress", s.address);
					        	myIntent.putExtra("myName", s.name);
					        	myIntent.putExtra("myPhone", s.phone);
					        	context.startActivity(myIntent);

					        	/*Product p = (Product) v;
					        	
					        	p.onClick(_mymain.myDbHelper);
					    	   					        	
					        	myIntent.putExtra("myProduct", p.identifier);
					        	myIntent.putExtra("pname", p.name);
					        	myIntent.putExtra("description", p.description);
					        	myIntent.putExtra("url", p.image_url);
					        	myIntent.putExtra("geoLat", _mymain.phoneLat); // latitude
					        	myIntent.putExtra("geoLng", _mymain.phoneLong); // longitude*/
					        }
					    });
						myStores.add(iv);
					}
				} catch (JSONException e) {				
					e.printStackTrace();
				}
			}
			
        	_mystore.endQuery(myStores);
        }
      }
    
    private class DownloadWebPageTask5 extends AsyncTask<String, Void, String> {
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
        	Log.d("Browse Result", result);
        	JSONObject jobj = null;
        	ArrayList<Product> myProducts = new ArrayList<Product>();
			try {
				jobj = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(jobj!=null){
				try {
					JSONArray jarr = new JSONArray(jobj.get("products").toString());
					
					for(int i = 0; i<jarr.length();i++ ){
						JSONObject j2 = new JSONObject(jarr.get(i).toString());
						
						Product iv = new Product(context,j2.get("name").toString(),j2.get("description").toString(),j2.get("product_id").toString(), j2.getString("image_100").toString(),5,5,QueryType.milo);
						iv.price = ""+j2.getInt("max_price")/100+"";
						iv.setOnClickListener(new OnClickListener() {
							
					        public void onClick(View v) {					        						        
					        	
					        	Intent myIntent = new Intent(context, ProductActivity.class);
					        	Product p = (Product) v;
					        	
					        	p.onClick(_mybrowse.myDbHelper);
					        	
					        	//TODO FIGURE OUT DB THING
					    	   					        	
					        	myIntent.putExtra("myProduct", p.identifier);
					        	myIntent.putExtra("pname", p.name);
					        	myIntent.putExtra("description", p.description);
					        	myIntent.putExtra("url", p.image_url);
					        	myIntent.putExtra("geoLat", _mybrowse.phoneLat); // latitude
					        	myIntent.putExtra("geoLng", _mybrowse.phoneLong); // longitude
					        	context.startActivity(myIntent);  
					        }
					    });
						myProducts.add(iv);
					}
				} catch (JSONException e) {				
					e.printStackTrace();
				}
			}
			
        	_mybrowse.endQuery(myProducts);
        }
      }


}
