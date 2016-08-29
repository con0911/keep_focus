package com.accessibility.keepfocus.database;

import java.util.ArrayList;

public class KeepFocusItem {
    private int keepFocusId;
    private String dayFocus;
    private String nameFocus;
    private boolean isLaunch;
    private boolean isNotifi;
    private boolean isActive;
    private ArrayList<TimeItem> listTimeFocus;
    private ArrayList<AppItem> listAppFocus;

    public KeepFocusItem() {
        this.keepFocusId = -1;
        this.dayFocus = "";
        this.nameFocus = "Unknow";
        this.isLaunch = true;
        this.isNotifi = true;
        this.isActive = true;
        this.listAppFocus = new ArrayList<AppItem>();
        this.listTimeFocus = new ArrayList<TimeItem>();
    }

    public int getKeepFocusId() {
        return keepFocusId;
    }

    public void setKeepFocusId(int keepFocusId) {
        this.keepFocusId = keepFocusId;
    }

    public String getDayFocus() {
        return dayFocus;
    }

    public void setDayFocus(String dayFocus) {
        this.dayFocus = dayFocus;
    }

    public String getNameFocus() {
        return nameFocus;
    }

    public void setNameFocus(String nameFocus) {
        this.nameFocus = nameFocus;
    }

    public boolean isLaunch() {
        return isLaunch;
    }

    public void setLaunch(boolean isLaunch) {
        this.isLaunch = isLaunch;
    }

    public boolean isNotifi() {
        return isNotifi;
    }

    public void setNotifi(boolean isNotifi) {
        this.isNotifi = isNotifi;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ArrayList<TimeItem> getListTimeFocus() {
        return listTimeFocus;
    }

    public void setListTimeFocus(ArrayList<TimeItem> listTimeFocus) {
        this.listTimeFocus = listTimeFocus;
    }

    public ArrayList<AppItem> getListAppFocus() {
        return listAppFocus;
    }

    public void setListAppFocus(ArrayList<AppItem> listAppFocus) {
        this.listAppFocus = listAppFocus;
    }

    /*
     * Input a day , example : Mon (day = 2), Tue(day = 3) , Sun (day = 1) In
     * order to String value dayFocus , this method will return true or false if
     * input day is in dayFocus or not.
     */
    public boolean checkDayFocus(int day) {
        if (dayFocus == null) {
            return false; // return false if don't have any day is focused
        }
        // Need more code continue ...
        return false;
    }

}
