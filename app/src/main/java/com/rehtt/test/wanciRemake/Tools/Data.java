package com.rehtt.test.wanciRemake.Tools;

public class Data {
    private static String user;

    /**
     * PvP模式：0
     * 人机模式：1~3
     */
    private static int PvEGrade;


    private static boolean LoadDone;

    //头像
    private static String Pic;

    public static String getPic() {
        return Pic;
    }

    public static void setPic(String pic) {
        Pic = pic;
    }

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
