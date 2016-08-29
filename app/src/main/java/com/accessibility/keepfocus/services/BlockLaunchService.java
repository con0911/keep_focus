package com.accessibility.keepfocus.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import com.accessibility.keepfocus.activity.BlockLauncher;
import com.accessibility.keepfocus.database.KeepFocusDatabaseHelper;
import com.accessibility.keepfocus.utils.Utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class BlockLaunchService extends Service {
    private Context mContext;
    private static final String TAG = "BlockLaunchService";
    private Timer mainTimer;
    private String oldPackageApp;
    private String currentPackageApp;
    private boolean isVersionLagerLL;
    private KeepFocusDatabaseHelper dbHelper;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.d(TAG, "onCreate");
        dbHelper = new KeepFocusDatabaseHelper(mContext);
        // Check version Android
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            isVersionLagerLL = true;
        } else {
            isVersionLagerLL = false;
        }
        oldPackageApp = " ";
        currentPackageApp = " ";
        // Make Timer for Test
        mainTimer = new Timer();
        mainTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (isVersionLagerLL) {
                    getCurrentPackageNewVersion();
                } else {
                    getCurrentPackageOldVersion();
                }
                // Log.e(TAG, "Find out " + currentPackageApp);
                if (isHaveToCheck()) {
                    oldPackageApp = currentPackageApp;
                    Log.e(TAG, "Check " + currentPackageApp);
                    Calendar c = Calendar.getInstance();
                    Date rightNow = c.getTime();
                    if (dbHelper.isAppOrNotifiBlock(currentPackageApp,
                            Utils.DAY_OF_WEEK[rightNow.getDay()],
                            rightNow.getHours(), rightNow.getMinutes(),
                            Utils.LAUNCHER_APP_BLOCK)) {
                        // Need more code to block app
                        Utils.namePackageBlock = currentPackageApp;
                        oldPackageApp = Utils.PACKET_APP;
                        currentPackageApp = Utils.PACKET_APP;
                        Log.e(TAG, "Block app " + currentPackageApp);
                        if (BlockLauncher.isPause) {
                            BlockLauncher.finishBlock();
                        }
                        Intent i = new Intent();
                        i.setClass(mContext, BlockLauncher.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                } else {
                    // Log.e(TAG, "Don't check duplicate");
                }

            }
        }, 0, Utils.timeSleep);
    }

    private boolean isHaveToCheck() {
        if (!oldPackageApp.equals(currentPackageApp)
                && !currentPackageApp.equals(Utils.PACKET_APP)) {
            return true;
        } else {
            return false;
        }
    }

    private void getCurrentPackageOldVersion() {
        ActivityManager mActivityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager
                .getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        currentPackageApp = componentName.getPackageName();
    }

    @SuppressLint("NewApi")
    private void getCurrentPackageNewVersion() {
        UsageStatsManager usm = (UsageStatsManager) this
                .getSystemService("usagestats");
        long time = System.currentTimeMillis();
        List<UsageStats> appList = usm.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                currentPackageApp = mySortedMap.get(mySortedMap.lastKey())
                        .getPackageName();
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),
                this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
