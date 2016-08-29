package com.accessibility.keepfocus.utils;

import com.accessibility.keepfocus.database.KeepFocusItem;

public class Utils {
    public static final int NOTIFICATION_BLOCK = 1;
    public static final int LAUNCHER_APP_BLOCK = 2;
    public static final String[] DAY_OF_WEEK = { "Sun", "Mon", "Tue", "Wed",
            "Thu", "Fri", "Sat" };
    public static final int timeSleep = 500;
    public static final String PACKET_APP = "com.accessibility.sec.keepfocus";
    public static String namePackageBlock;
    public static KeepFocusItem keepTempFocus;
    public static final String EXTRA_MESSAGE = "extra_message"; 
    public static final String EXTRA_PACKAGE = "extra_package";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_NOTI_CONTENT = "android.text";
}
