/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.taucoin.android.wallet.util;

import android.widget.Chronometer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.taucoin.foundation.util.StringUtil;

/**
 * Description: Date tools
 * Author:wuQy
 * Date: 2017/10/15
 */
public class DateUtil {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String pattern1 = "mm:ss";
    public static final String pattern2 = "MM-dd";
    public static final String pattern3 = "yyyy-MM";
    public static final String pattern4 = "yyyy-MM-dd";
    public static final String pattern5 = "yyyy-MM-dd HH:mm";
    public static final String pattern6 = "yyyy-MM-dd HH:mm:ss";
    public static final String pattern7 = "yyyy-MM-dd\'T\'HH:mm:ss";
    public static final String pattern8 = "yyyy-MM-dd\'T\'HH:mm:ss.SS SZ";
    public static final String pattern9 = "yyyy-MM-dd HH:mm:ss.SSS";

    @SuppressWarnings("CanBeFinal")
    private static SimpleDateFormat format;

    static {
        if (format == null) {
            synchronized (DateUtil.class) {
                if (format == null) {
                    format = new SimpleDateFormat();
                }
            }
        }
    }

    public static String format(String time, String parsePattern, String pattern) {
        try {
            format.applyPattern(parsePattern);
            Date parse = format.parse(time);
            format.applyPattern(pattern);
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(long time, String pattern) {
        format.applyPattern(pattern);
        return format.format(new Date(time));
    }

    public static String getCurrentTime() {
        Date date = new Date();
        Long time = date.getTime();
        time = time / 1000;
        return String.valueOf(time);
    }

    public static String formatTime(String time, String pattern) {
        try {
            long timeSeconds = Long.valueOf(time);
            return formatTime(timeSeconds, pattern);
        }catch (Exception ignore){}
        return time;
    }

    public static String formatTime(long timeSeconds, String pattern) {
        timeSeconds = timeSeconds * 1000;
        Date date = new Date(timeSeconds);
        TimeZone timeZone = TimeZone.getDefault();
        format.setTimeZone(timeZone);

        format.applyPattern(pattern);
        return format.format(date);
    }

    public static String formatBestTime(long timeSeconds) {
        long minutes = timeSeconds / (1000 * 60);
        long seconds = (timeSeconds - minutes*(1000 * 60))/1000;
        String diffTime = minutes < 10 ? "0" + minutes+":" : minutes+":";
        diffTime = seconds < 10 ? diffTime +"0" + seconds : diffTime + seconds ;

        long millisecond = (int) (timeSeconds%1000/10);
        String count = millisecond > 9 ? "." + millisecond : ".0" + millisecond;
        diffTime += count;
        return diffTime;
    }

    /**
     *
     * @param cmt  Chronometer
     * @return hour+min+send
     */
    public  static int getChronometerSeconds(Chronometer cmt) {
        int total = 0;
        String string = cmt.getText().toString();
        if(string.length()==7){

            String[] split = string.split(":");
            String string2 = split[0];
            int hour = Integer.parseInt(string2);
            int Hours =hour*3600;
            String string3 = split[1];
            int min = Integer.parseInt(string3);
            int minCount =min*60;
            int  SS =Integer.parseInt(split[2]);
            total = Hours+minCount+SS;
        } else if(string.length()==5){

            String[] split = string.split(":");
            String string3 = split[0];
            int min = Integer.parseInt(string3);
            int minCount =min*60;
            int  SS =Integer.parseInt(split[1]);
            total =minCount+SS;
        }
        return total;


    }

    @SuppressWarnings("SameParameterValue")
    private static long getLong(String time, String pattern) {
        try {
            format.applyPattern(pattern);
            return format.parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean compare(String formerTime, String latterTime, String pattern) {
       if(StringUtil.isNotEmpty(formerTime) && StringUtil.isNotEmpty(latterTime)){
           return getLong(formerTime, pattern) > getLong(latterTime, pattern);
       }
       return false;
    }
}