package com.accessibility.keepfocus.receiver;

import com.accessibility.keepfocus.database.KeepFocusDatabaseHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UninstallReceiver extends BroadcastReceiver {

	private final String PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	private final String TAG = "UninstallReceiver";
	private KeepFocusDatabaseHelper mKeepFocusDatabaseHelper;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if(action.equals(PACKAGE_REMOVED)){
			mKeepFocusDatabaseHelper = new KeepFocusDatabaseHelper(context);
			String packageName = intent.getData().toString();
			if(packageName != null){
					Log.d(TAG, "The package : " + packageName + " removed");
					mKeepFocusDatabaseHelper.deleteAppAndNotiByUninstall(packageName);
			}
			
		}
		
	}

}
