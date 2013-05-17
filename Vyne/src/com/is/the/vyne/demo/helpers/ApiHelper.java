package com.is.the.vyne.demo.helpers;

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

import com.is.the.vyne.demo.models.Store;
import com.is.the.vyne.demo.models.VyneItem;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ApiHelper {
	
	AsyncListener listener;
	
	public ApiHelper(AsyncListener l) {
		listener = l;
	}
	
	public void queryStore(String pk){
    	
		String myURL = "http://gentle-beach-5711.herokuapp.com/store/?storeId="+pk;
        
		try {

            DownloadStoreTask task = new DownloadStoreTask();
            task.execute(new String[] { myURL});
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryFeed(){
    	
		String myURL = "http://gentle-beach-5711.herokuapp.com/feed/";
        
		try {

            DownloadFeedTask task = new DownloadFeedTask();
            task.execute(new String[] { myURL});
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private class DownloadFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        	try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
        	
        	JSONObject jobj = null;
        	ArrayList<Store> storeList = new ArrayList<Store>();
			try {
				jobj = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(jobj!=null){
				try {
					JSONArray jarr = new JSONArray(jobj.get("stores").toString());
					
					for(int i = 0; i<jarr.length();i++ ){
						JSONObject j2 = new JSONObject(jarr.get(i).toString());
						
						Store s = new Store();
						
						s.name = j2.getJSONObject("fields").getString("name");
						s.pk = j2.getInt("pk");
						
						storeList.add(s);
						Log.d("SDKJSLKJFLKJSKLDJFLKJSLDK", "whif"+j2.getJSONObject("fields").getString("name"));
						
					}
					listener.updateUI(1, storeList);
				} catch (JSONException e) {				
					e.printStackTrace();
				}
			}
        }
      }
    
    private class DownloadStoreTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        	try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
          String response = "";
          for (String url : urls) {
        	  Log.d("Nate's result: ", url);
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
        	
        	JSONArray jarr = null;
        	ArrayList<VyneItem> itemList = new ArrayList<VyneItem>();
			try {
				jarr = new JSONArray(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(jarr!=null){
				try {
					
					for(int i = 0; i<jarr.length();i++ ){
						JSONObject j2 = new JSONObject(jarr.get(i).toString());
						
						VyneItem item = new VyneItem();
						
						item.name = j2.getJSONObject("fields").getString("name");
						
						itemList.add(item);
						
					}
					listener.updateUI(1, itemList);
				} catch (JSONException e) {				
					e.printStackTrace();
				}
			}
        }
      }

}
