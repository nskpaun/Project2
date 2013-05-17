package nkspaun.project190.vyne;


import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class Product extends ImageView{

	public String name;
	public String description;
	public String identifier;
	public String image_url;
	public double latitude;
	public double longitude;
	public String address;
	public String storename;
	public String price;
	public String phone;
	public String merchant;
	public QueryType type;
	
	
	
	public Product(Context context, String nm, String desc, String id, String url, double lat, double lon,QueryType t){
		super(context);
		name = nm;
		description = desc;
		identifier = id;
		image_url = url;
		latitude = lat;
		longitude = lon;
		type = t;
		
		//What
		storename = "";
		address = "";
		price = "";
		phone = "";
	}
	
	public Product(Context context){
		super(context);
	}
	
	public Product(Context context, AttributeSet as){
		super(context,as); 
	}
	
	public void onClick(DataBaseHelper myDbHelper){
    	int clicks;
    	int reservations;
    	SQLiteDatabase db = myDbHelper.getWritableDatabase();
        Cursor cursor = db.query("item", new String[] { "name",
                "clicks", "reservations" }, "name = ?",
                new String[] { this.name }, null, null, null, null);
	   	//Cursor cursor = myDbHelper.myDataBase.rawQuery("select name, clicks, reservations from item where name = '"+this.name+"';", new String[] { });
	   	if(cursor.getCount()>0){
    	   	cursor.moveToFirst();
    	   	clicks = cursor.getInt(1);
    	   	clicks = clicks + 1;
    	   	reservations = cursor.getInt(2);
    	   	Log.d("Clicks", ""+clicks+" YOLO!");
    	   	
	   	} else {
	   		clicks = 1;
	   		reservations = 0;
	   	}
	   	
	   	cursor.close();
	   	
	   	Calendar c = Calendar.getInstance();
    	SQLiteStatement stmt2 = db.compileStatement("insert or replace into item values(?,?,?,?);");
    	
    	stmt2.bindString(1, this.name);
    	stmt2.bindDouble(2, clicks);
    	stmt2.bindDouble(3, reservations);
    	stmt2.bindDouble(4, c.getTimeInMillis());

    	
    	stmt2.execute();
    	db.close();
	   	//myDbHelper.myDataBase.execSQL("insert or replace into item values('" +this.name+"',"+clicks+ ","+reservations+","+c.getTimeInMillis()+");");
	}
	
	public void setMapInfo(String store, String add, String p, String pr){
		storename = store;
		address = add;
		price = pr;
		phone = p;
	}
	
	public void onReserve(DataBaseHelper myDbHelper){
    	int clicks;
    	int reservations;
    	SQLiteDatabase db = myDbHelper.getWritableDatabase();
        Cursor cursor = db.query("item", new String[] { "name",
                "clicks", "reservations" }, "name = ?",
                new String[] { this.name }, null, null, null, null);
	   	//Cursor cursor = myDbHelper.myDataBase.rawQuery("select name, clicks, reservations from item where name = '"+this.name+"';", new String[] { });
	   	if(cursor.getCount()>0){
    	   	cursor.moveToFirst();
    	   	clicks = cursor.getInt(1);
    	   	
    	   	reservations = cursor.getInt(2)+1;
    	   	Log.d("Reserves ", ""+reservations+" YOLO!");
    	   	
	   	} else {
	   		clicks = 1;
	   		reservations = 1;
	   	}
	   	
	   	cursor.close();
	   	
	   	Calendar c = Calendar.getInstance();
	   	
    	SQLiteStatement stmt2 = db.compileStatement("insert or replace into item values(?,?,?,?);");
    	
    	stmt2.bindString(1, this.name);
    	stmt2.bindDouble(2, clicks);
    	stmt2.bindDouble(3, reservations);
    	stmt2.bindDouble(4, c.getTimeInMillis());

    	
    	stmt2.execute();
    	db.close();
	   	
	   	//myDbHelper.myDataBase.execSQL("insert or replace into item values('" +this.name+"',"+clicks+ ","+reservations+","+c.getTimeInMillis()+");");
	}

}
