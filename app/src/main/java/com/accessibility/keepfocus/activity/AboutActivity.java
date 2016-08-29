package com.accessibility.keepfocus.activity;

import com.accessibility.keepfocus.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

    private TextView mTextVersionApp;
    private int versionCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity_layout);
        
        mTextVersionApp = (TextView) findViewById(R.id.txt_version);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTextVersionApp.setText("Version: " + String.valueOf(versionCode));
    }
}
