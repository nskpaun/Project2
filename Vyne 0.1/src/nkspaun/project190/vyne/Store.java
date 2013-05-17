package nkspaun.project190.vyne;

import android.content.Context;
import android.widget.ImageView;

public class Store extends ImageView{
	public String name;
	public String imageurl;
	public String address;
	public String phone;
	public int identifier;
	
	public Store(Context c,String n, String i, String a, String p, int id){
		super(c);
		name = n;
		imageurl = i;
		address = a;
		phone = p;
		identifier = id;
	}
	

}
