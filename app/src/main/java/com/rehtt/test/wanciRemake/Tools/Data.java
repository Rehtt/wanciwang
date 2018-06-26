package com.rehtt.test.wanciRemake.Tools;

public class Data {
    public static String user;

    /**
     * PvP模式：0
     * 人机模式：1~3
     */
    public static int PvEGrade;

    public static boolean LoadDone;

    public static boolean isLoadDone() {
        return LoadDone;
    }

    public static void setLoadDone(boolean loadDone) {
        LoadDone = loadDone;
    }

    public static int getPvEGrade() {
        return PvEGrade;
    }

    public static void setPvEGrade(int pvEGrade) {
        PvEGrade = pvEGrade;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        Data.user = user;
    }
}
