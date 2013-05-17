package com.is.the.vyne.demo;

import java.util.ArrayList;

import com.is.the.vyne.demo.adapters.FollowingAdapter;
import com.is.the.vyne.demo.adapters.StoreItemAdapter;
import com.is.the.vyne.demo.helpers.ApiHelper;
import com.is.the.vyne.demo.helpers.AsyncListener;
import com.is.the.vyne.demo.models.Store;
import com.is.the.vyne.demo.models.VyneItem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

public class StoreActivity extends Activity {
	public static final String STORE_PK = "STORE_PK";
	GridView storeGrid;
	String storePK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_store);
		
		storePK = "";
		storePK = (String) getIntent().getExtras().get(STORE_PK);
		
		ApiHelper helper = new ApiHelper(getRecallListener());
        helper.queryStore(storePK);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_store, menu);
		return true;
	}
	
	private AsyncListener getRecallListener(){
		return new AsyncListener(){
			
			@Override
			public void updateUI(int status, Object obj) {
				storeGrid = (GridView) findViewById(R.id.store_activity_grid_view);
				ArrayList<VyneItem> itemList = (ArrayList<VyneItem>) obj;
				storeGrid.setAdapter(new StoreItemAdapter(getApplicationContext(),itemList));
			}
			
		};
	}
}
