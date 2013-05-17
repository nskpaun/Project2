package com.is.the.vyne.demo;

import java.util.ArrayList;

import com.is.the.vyne.demo.adapters.FollowingAdapter;
import com.is.the.vyne.demo.helpers.ApiHelper;
import com.is.the.vyne.demo.helpers.AsyncListener;
import com.is.the.vyne.demo.models.Store;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FeedActivity extends Activity {
	RelativeLayout currentLayout;
	EditText search;
	RelativeLayout mainLayout;
	GridView followingGrid;
	FollowingAdapter followingAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_feed);
		
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayoutFeed);
		search = (EditText) mainLayout.findViewById(R.id.editText1);
		
		
		search.setVisibility(View.GONE);
		

	    MyPagerAdapter adapter = new MyPagerAdapter();
	    ViewPager myPager = (ViewPager) findViewById(R.id.pager);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(1);
	}
	
	private class MyPagerAdapter extends PagerAdapter {
        public int getCount() {
            return 4;
        }
        public Object instantiateItem(View collection, int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = 0;
            switch (position) {
            case 0:
                resId = R.layout.nearby;
                break;
            case 1:
                resId = R.layout.following;
                ApiHelper helper = new ApiHelper(getFollowingListener());
                helper.queryFeed();
                break;
            case 2:
                resId = R.layout.activity_feed;
                break;
            case 3:
                resId = R.layout.feed;
                break;
            }
            View view = inflater.inflate(resId, null);
            ((ViewPager) collection).addView(view, 0);
            return view;
        }
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
        }
        @Override
        public Parcelable saveState() {
            return null;
        }
}
	private AsyncListener getFollowingListener(){
		return new AsyncListener(){
			
			@Override
			public void updateUI(int status, Object obj) {
				followingGrid = (GridView) findViewById(R.id.following_grid_view);
				ArrayList<Store> storeList = (ArrayList<Store>) obj;
				followingAdapter = new FollowingAdapter(getApplicationContext(),storeList);
				followingGrid.setAdapter(followingAdapter);
				followingGrid.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						String pk = ""+followingAdapter.storeList.get(pos).pk;
						Intent myIntent = new Intent(FeedActivity.this, StoreActivity.class);
						myIntent.putExtra(StoreActivity.STORE_PK, pk);
						FeedActivity.this.startActivity(myIntent);
						
					}
					
				});
			}
			
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_feed, menu);
		return true;
	}

}
