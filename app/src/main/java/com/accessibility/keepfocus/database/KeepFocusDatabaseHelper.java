package com.accessibility.keepfocus.database;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.accessibility.keepfocus.utils.Utils;

public class KeepFocusDatabaseHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "datadb";
    // Database Table
    private static final String TABLE_KEEPFOCUS = "tblKeepFocus";
    private static final String TABLE_APPITEM = "tblAppItem";
    private static final String TABLE_TIMEITEM = "tblTimeItem";
    private static final String TABLE_KEEPFOCUS_APPITEM = "tblKeepfocusApp";
    // table to save Notification history
    private static final String TABLE_NOTIFICATION_HISTORY = "tblNotificationHistory";
    private SQLiteDatabase dbMain;

    public KeepFocusDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tblKeepFocus
        String CREATE_TABLE_KEEPFOCUS = "CREATE TABLE " + TABLE_KEEPFOCUS + "("
                + "keep_focus_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " day_focus text not null," + " name_focus text not null,"
                + " is_launch integer,"
                + " is_notifi integer," + " is_active integer" + ")";
        db.execSQL(CREATE_TABLE_KEEPFOCUS);
        // tblAppItem
        String CREATE_TABLE_APPITEM = "CREATE TABLE " + TABLE_APPITEM + "("
                + "app_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name_package text not null," + " name_app text not null"
                + ")";
        db.execSQL(CREATE_TABLE_APPITEM);
        // tblTimeItem
        String CREATE_TABLE_TIMEITEM = "CREATE TABLE " + TABLE_TIMEITEM + "("
                + "time_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " keep_focus_id integer not null," + " hour_begin integer,"
                + " minus_begin integer," + " hour_end integer,"
                + " minus_end integer" + ")";
        db.execSQL(CREATE_TABLE_TIMEITEM);
        // tblKeepfocusApp
        String CREATE_TABLE_KEEPFOCUS_APPITEM = "CREATE TABLE "
                + TABLE_KEEPFOCUS_APPITEM + "("
                + "id_keep_app INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " keep_focus_id integer not null,"
                + " app_id integer not null" + ")";
        db.execSQL(CREATE_TABLE_KEEPFOCUS_APPITEM);
        // tblNotificaionHistory
        String CREATE_TABLE_NOTIFICAION_HISTORY = "CREATE TABLE "
                + TABLE_NOTIFICATION_HISTORY + "("
                + "id_noti_history INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_app INTEGER NOT NULL, " + "noti_title text, "
                + "noti_sumary text, " + "packageName text not null, " + "noti_date INTEGER" + ")";
        db.execSQL(CREATE_TABLE_NOTIFICAION_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEEPFOCUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMEITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEEPFOCUS_APPITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_HISTORY);
        // Create tables again
        onCreate(db);
    }

    public ArrayList<KeepFocusItem> getAllKeepFocusFromDb() {
        ArrayList<KeepFocusItem> listKeepFocus = new ArrayList<KeepFocusItem>();
        // Select All Query
        String selectQuery = "SELECT * FROM tblKeepFocus";
        dbMain = this.getWritableDatabase();
        Cursor cursor = dbMain.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // get data by cursor
                int keep_focus_id = Integer.parseInt(cursor.getString(0));
                String day_focus = cursor.getString(1);
                String name_focus = cursor.getString(2);
                int is_launch = cursor.getInt(3);
                int is_notifi = cursor.getInt(4);
                int is_active = cursor.getInt(5);
                // make keep focus item
                KeepFocusItem focusItem = new KeepFocusItem();
                focusItem.setKeepFocusId(keep_focus_id);
                focusItem.setDayFocus(day_focus);
                focusItem.setNameFocus(name_focus);
                if (is_launch == 0) {
                    focusItem.setLaunch(false);
                } else {
                    focusItem.setLaunch(true);
                }
                if (is_notifi == 0) {
                    focusItem.setNotifi(false);
                } else {
                    focusItem.setNotifi(true);
                }
                if (is_active == 0) {
                    focusItem.setActive(false);
                } else {
                    focusItem.setActive(true);
                }
                // Get TimeItem for focusItem
                focusItem.setListTimeFocus(getListTimeById(focusItem
                        .getKeepFocusId()));
                // Get AppItem for focusItem
                focusItem.setListAppFocus(getListAppById(focusItem
                        .getKeepFocusId()));
                listKeepFocus.add(focusItem);
            } while (cursor.moveToNext());
        }
        dbMain.close();
        return listKeepFocus;
    }

    public ArrayList<TimeItem> getListTimeById(int idFocus) {
        ArrayList<TimeItem> listTime = new ArrayList<TimeItem>();
        if (dbMain == null) {
            dbMain = this.getWritableDatabase();
        }
        String selectQuery = "SELECT * FROM tblTimeItem WHERE keep_focus_id = "
                + idFocus;
        Cursor cursor = dbMain.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int time_id = Integer.parseInt(cursor.getString(0));
                int hour_begin = cursor.getInt(2);
                int minus_begin = cursor.getInt(3);
                int hour_end = cursor.getInt(4);
                int minus_end = cursor.getInt(5);
                //
                TimeItem timeItem = new TimeItem();
                timeItem.setTimeId(time_id);
                timeItem.setKeepFocusId(idFocus);
                timeItem.setHourBegin(hour_begin);
                timeItem.setMinusBegin(minus_begin);
                timeItem.setHourEnd(hour_end);
                timeItem.setMinusEnd(minus_end);
                listTime.add(timeItem);
            } while (cursor.moveToNext());
        }
        return listTime;
    }

    public ArrayList<AppItem> getListAppById(int idFocus) {
        ArrayList<AppItem> listApp = new ArrayList<AppItem>();
        if (dbMain == null) {
            dbMain = this.getWritableDatabase();
        }
        String selectQuery = "SELECT * FROM tblKeepfocusApp WHERE keep_focus_id = "
                + idFocus;
        Cursor cursor = dbMain.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int app_id = cursor.getInt(2);
                AppItem item = getAppItemById(app_id);
                listApp.add(item);
            } while (cursor.moveToNext());
        }
        return listApp;
    }

    public AppItem getAppItemById(int id) {
        AppItem appItem = new AppItem();
        if (dbMain == null) {
            dbMain = this.getWritableDatabase();
        }
        String selectQuery = "SELECT * FROM tblAppItem WHERE app_id = " + id;
        Cursor cursor = dbMain.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            String name_package = cursor.getString(1);
            String name_app = cursor.getString(2);
            appItem.setAppId(id);
            appItem.setNamePackage(name_package);
            appItem.setNameApp(name_app);
            return appItem;
        }
        return null;
    }

    public int addNewFocusItem(KeepFocusItem keepFocus) {
        dbMain = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("day_focus", keepFocus.getDayFocus());
        values.put("name_focus", keepFocus.getNameFocus());
        if (keepFocus.isLaunch()) {
            values.put("is_launch", 1);
        } else {
            values.put("is_launch", 0);
        }
        if (keepFocus.isNotifi()) {
            values.put("is_notifi", 1);
        } else {
            values.put("is_notifi", 0);
        }
        if (keepFocus.isActive()) {
            values.put("is_active", 1);
        } else {
            values.put("is_active", 0);
        }
        int keep_focus_id = (int) dbMain.insert("tblKeepFocus", null, values);
        keepFocus.setKeepFocusId(keep_focus_id);
        dbMain.close();
        return keep_focus_id;
    }

    public int addTimeItemToDb(TimeItem timeItem, int keep_focus_id) {
        dbMain = this.getWritableDatabase();
        timeItem.setKeepFocusId(keep_focus_id);
        ContentValues values2 = new ContentValues();
        values2.put("keep_focus_id", timeItem.getKeepFocusId());
        values2.put("hour_begin", timeItem.getHourBegin());
        values2.put("minus_begin", timeItem.getMinusBegin());
        values2.put("hour_end", timeItem.getHourEnd());
        values2.put("minus_end", timeItem.getMinusEnd());
        int time_id = (int) dbMain.insert("tblTimeItem", null, values2);
        timeItem.setTimeId(time_id);
        return time_id;
    }

    public int addAppItemToDb(AppItem appItem, int keep_focus_id) {
        int app_id = -1;
        dbMain = this.getWritableDatabase();
        String selectQuery = "SELECT tblAppItem.app_id FROM tblAppItem WHERE name_package = '"
                + appItem.getNamePackage() + "'";
        Cursor cursor = dbMain.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            app_id = cursor.getInt(0);
        }
        if (app_id == -1) {
            ContentValues values3 = new ContentValues();
            values3.put("name_package", appItem.getNamePackage());
            values3.put("name_app", appItem.getNameApp());
            app_id = (int) dbMain.insert("tblAppItem", null, values3);
            appItem.setAppId(app_id);
        }
        // InsertTableTemp
        ContentValues values3 = new ContentValues();
        values3.put("keep_focus_id", keep_focus_id);
        values3.put("app_id", app_id);
        dbMain.insert("tblKeepfocusApp", null, values3);
        return app_id;
    }

    public void deleteFocusItemById(int keep_focus_id) {
        dbMain = this.getWritableDatabase();
        // Delete from tblKeepFocus
        String deleteKeepFocus = "DELETE FROM tblKeepFocus WHERE keep_focus_id = "
                + keep_focus_id;
        dbMain.execSQL(deleteKeepFocus);
        // Delete from tblTimeItem
        String deleteTimeItem = "DELETE FROM tblTimeItem WHERE keep_focus_id = "
                + keep_focus_id;
        dbMain.execSQL(deleteTimeItem);
        // Delete from tblKeepfocusApp
        String deleteKeepFocusApp = "DELETE FROM tblKeepfocusApp WHERE keep_focus_id = "
                + keep_focus_id;
        dbMain.execSQL(deleteKeepFocusApp);
        dbMain.close();
    }

    public void deleteTimeItemById(int time_id) {
        dbMain = this.getWritableDatabase();
        // Delete from tblTimeItem
        String deleteTimeItem = "DELETE FROM tblTimeItem WHERE time_id = "
                + time_id;
        dbMain.execSQL(deleteTimeItem);
        dbMain.close();
    }

    public void deleteAppFromKeepFocus(int app_id, int keep_focus_id) {
        dbMain = this.getWritableDatabase();
        // Delete from tblKeepfocusApp
        String deleteKeepFocusApp = "DELETE FROM tblKeepfocusApp WHERE app_id = "
                + app_id + " and keep_focus_id = " + keep_focus_id;
        dbMain.execSQL(deleteKeepFocusApp);
        dbMain.close();
    }

    public void deleteAppAndNotiByUninstall(String name_package) {
        int app_id = -1;
        dbMain = this.getWritableDatabase();
        // String selectQuery =
        // "SELECT tblAppItem.app_id FROM tblAppItem WHERE name_package = '"
        // + name_package + "'";
        // Cursor cursor = dbMain.rawQuery(selectQuery, null);
        // if (cursor.moveToFirst()) {
        // app_id = cursor.getInt(0);
        // }
        app_id = getAppItemIdByPackageName(name_package);
        if (app_id == -1) {
            return;// Return if don't find app in Db
        }
        // Delete from tblAppItem
        String deletetblAppItem = "DELETE FROM tblAppItem WHERE app_id = "
                + app_id;
        dbMain.execSQL(deletetblAppItem);
        // Delete from tblKeepfocusApp
        String deleteKeepFocusApp = "DELETE FROM tblKeepfocusApp WHERE app_id = "
                + app_id;
        dbMain.execSQL(deleteKeepFocusApp);
        // Delete form tblNotificationHistoryItem
        String query = "DELETE FROM " + TABLE_NOTIFICATION_HISTORY
                + " WHERE id_app = " + app_id;
        dbMain.execSQL(query);
        dbMain.close();
        //

    }

    public void updateFocusItem(KeepFocusItem keepFocusItem) {
        int keep_focus_id = keepFocusItem.getKeepFocusId();
        String day_focus = "'" + keepFocusItem.getDayFocus() + "'";
        String name_focus = "'" + keepFocusItem.getNameFocus() + "'";
        int is_launch = keepFocusItem.isLaunch() ? 1 : 0;
        int is_notifi = keepFocusItem.isNotifi() ? 1 : 0;
        int is_active = keepFocusItem.isActive() ? 1 : 0;
        //
        dbMain = this.getWritableDatabase();
        String update = "update tblKeepFocus set day_focus = " + day_focus
                + ", name_focus = " + name_focus + ", is_launch = " + is_launch + ", is_notifi = "
                + is_notifi + ", is_active = " + is_active
                + " where keep_focus_id = " + keep_focus_id;
        dbMain.execSQL(update);
        dbMain.close();
    }

    public void updateTimeItem(TimeItem timeItem) {
        int time_id = timeItem.getTimeId();
        int hour_begin = timeItem.getHourBegin();
        int minus_begin = timeItem.getMinusBegin();
        int hour_end = timeItem.getHourEnd();
        int minus_end = timeItem.getMinusEnd();
        //
        dbMain = this.getWritableDatabase();
        String update = "update tblTimeItem set hour_begin = " + hour_begin
                + ", minus_begin = " + minus_begin + ", hour_end = " + hour_end
                + ", minus_end = " + minus_end + " where time_id = " + time_id;
        dbMain.execSQL(update);
        dbMain.close();
    }

    /*
     * Method check Notifications is block input: String package name, system
     * time of moment notifications fire output: Boolean value true if block
     * else false
     */
    public boolean isAppOrNotifiBlock(String packageName, String day, int hour,
            int min, int flagBlock) {
        dbMain = this.getWritableDatabase();
        int appId = getAppItemIdByPackageName(packageName);
        if (appId == -1) {
            return false;
        }
        ArrayList<KeepFocusItem> list_KeepFocusItem = getListFocusItemByAppItemId(appId);
        ArrayList<TimeItem> list_TimeItem;
        for (KeepFocusItem a_KeepFocusItem : list_KeepFocusItem) {
            if (a_KeepFocusItem.isActive()) {
                if (flagBlock == Utils.NOTIFICATION_BLOCK) {
                    if (a_KeepFocusItem.isNotifi()) {
                        if (!a_KeepFocusItem.getDayFocus().contains(day)) {
                            continue;
                        }
                        list_TimeItem = a_KeepFocusItem.getListTimeFocus();
                        if (list_TimeItem.size() == 0) {
                            return true;
                        }
                        for (TimeItem a_TimeItem : list_TimeItem) {
                            if (a_TimeItem.checkInTime(hour, min))
                                return true;
                        }
                    }
                } else if (flagBlock == Utils.LAUNCHER_APP_BLOCK) {
                    if (a_KeepFocusItem.isLaunch()) {
                        if (!a_KeepFocusItem.getDayFocus().contains(day)) {
                            continue;
                        }
                        list_TimeItem = a_KeepFocusItem.getListTimeFocus();
                        if (list_TimeItem.size() == 0) {
                            return true;
                        }
                        for (TimeItem a_TimeItem : list_TimeItem) {
                            if (a_TimeItem.checkInTime(hour, min)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /*
     * Method get AppItem Id which maybe block input : String[] packageName
     * output :AppItem id if exist else -1
     */
    public int getAppItemIdByPackageName(String packageName) {
        int appId = -1;
        String query = "SELECT ai.app_id FROM tblAppItem AS ai WHERE ai.name_package = '"
                + packageName + "'"; 
        Cursor cursor = dbMain.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            appId = cursor.getInt(0);
        }
        return appId;
    }

    /*
     * Method query KeepFocus item via AppItem id input : int AppItem id output
     * : value of KeepFocus item if exist else -1
     */
    private ArrayList<KeepFocusItem> getListFocusItemByAppItemId(int appId) {
        ArrayList<KeepFocusItem> keepFocusItemList = new ArrayList<KeepFocusItem>();
        String query = "SELECT kf.* FROM tblKeepFocus AS kf JOIN tblKeepfocusApp AS kfa ON kf.keep_focus_id = kfa.keep_focus_id WHERE kfa.app_id ="
                + appId;
        Cursor cursor = dbMain.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int keepFocusId = cursor.getInt(0);
                String dayFocus = cursor.getString(1);
                String nameFocus = cursor.getString(2);
                boolean isLaunch = cursor.getInt(3) == 1 ? true : false;
                boolean isNotifi = cursor.getInt(4) == 1 ? true : false;
                boolean isActive = cursor.getInt(5) == 1 ? true : false;
                ArrayList<TimeItem> listTimeFocus = getListTimeById(keepFocusId);
                KeepFocusItem a_KeepFocusItem = new KeepFocusItem();
                a_KeepFocusItem.setKeepFocusId(keepFocusId);
                a_KeepFocusItem.setDayFocus(dayFocus);
                a_KeepFocusItem.setNameFocus(nameFocus);
                a_KeepFocusItem.setLaunch(isLaunch);
                a_KeepFocusItem.setNotifi(isNotifi);
                a_KeepFocusItem.setActive(isActive);
                a_KeepFocusItem.setListTimeFocus(listTimeFocus);
                keepFocusItemList.add(a_KeepFocusItem);
            } while (cursor.moveToNext());
        }
        return keepFocusItemList;
    }

    // =========================================================Need to save
    // Notification history for user see back when out of time
    // block=======================
    public void addNotificationHistoryItemtoDb(NotificationItemMissHistory notiHistory) {
        dbMain = this.getWritableDatabase();
        String packageName = notiHistory.getPakageName();
        int app_id = getAppItemIdByPackageName(packageName);
        String title = notiHistory.getmNotiTitle();
        String text = notiHistory.getmNotiSumary();
        int date = notiHistory.getmNotiDate();
        ContentValues contentValue = new ContentValues();
        contentValue.put("id_app", app_id);
        contentValue.put("noti_title", title);
        contentValue.put("noti_sumary", text);
        contentValue.put("packageName", packageName);
        contentValue.put("noti_date", date);
        dbMain.insert(TABLE_NOTIFICATION_HISTORY, null, contentValue);
        dbMain.close();
    }

    public void deleteNotificationHistoryItemById(int aNotiHistoryItemId) {
        dbMain = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NOTIFICATION_HISTORY
                + " WHERE id_noti_history = " + aNotiHistoryItemId;
        dbMain.execSQL(query);
        dbMain.close();
    }

    // public void deleteNotificationHistoryItemByUninstallApp (String
    // packageName){
    // int app_id = getAppItemIdByPackageName(packageName);
    // if(app_id == -1){
    // return;
    // }
    // dbMain = this.getWritableDatabase();
    // String query = "DELETE FROM " + TABLE_NOTIFICATION_HISTORY +
    // " WHERE id_app = " + app_id;
    // dbMain.execSQL(query);
    // dbMain.close();
    // }
    
    public void deleteAllNotifications(ArrayList<NotificationItemMissHistory> clearNotifications){
    	for(NotificationItemMissHistory aNoti : clearNotifications){
    		deleteNotificationHistoryItemById(aNoti.getmNotiItem_id());
    	}
    }

    public ArrayList<NotificationItemMissHistory> getListNotificaionHistoryItem() {
        ArrayList<NotificationItemMissHistory> list_NotificationHistory = new ArrayList<NotificationItemMissHistory>();
        String query = "SELECT * FROM tblNotificationHistory";
        dbMain = this.getWritableDatabase();
        Cursor cursor = dbMain.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int noti_id = cursor.getInt(0);
                int app_id = cursor.getInt(1);
                String noti_Title = cursor.getString(2);
                String noti_Surmary = cursor.getString(3);
                String packageName = cursor.getString(4);
                int noti_Date = cursor.getInt(5);
                NotificationItemMissHistory aNotiHistoryItem = new NotificationItemMissHistory();
                aNotiHistoryItem.setmNotiItem_id(noti_id);
                aNotiHistoryItem.setmApp_id(app_id);
                aNotiHistoryItem.setmNotiTitle(noti_Title);
                aNotiHistoryItem.setmNotiSumary(noti_Surmary);
                aNotiHistoryItem.setPakageName(packageName);
                aNotiHistoryItem.setmNotiDate(noti_Date);
                list_NotificationHistory.add(aNotiHistoryItem);
            } while (cursor.moveToNext());
        }
        dbMain.close();
        return list_NotificationHistory;
    }

    // =========================================================End of part save
    // Notification
    // history============================================================
}
