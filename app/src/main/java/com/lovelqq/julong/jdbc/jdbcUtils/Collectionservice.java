package com.lovelqq.julong.jdbc.jdbcUtils;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Collectionservice extends Service {
	private final static String TGA="CollentionServer";
	@Override
	public void onCreate() {
		Log.e(TGA, "onCreate");
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("Hpmepage","onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.e(TGA, "onDestroy");
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
