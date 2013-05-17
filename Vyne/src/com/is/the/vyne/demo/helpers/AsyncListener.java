package com.is.the.vyne.demo.helpers;

import android.os.Bundle;

public interface AsyncListener {
	
	public Bundle extra = new Bundle();
	
	public static final int RESULT_SUCCESS=0;
	public static final int RESULT_FAILURE=-1;
	
	public void updateUI(int status, Object obj);
	
	
}
