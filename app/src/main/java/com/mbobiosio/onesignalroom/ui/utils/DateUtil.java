package com.mbobiosio.onesignalroom.ui.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mbuodile Obiosio on Jul 25,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class DateUtil {

    public static String notificationTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    public static String covertTimeToText(String dataDate) {

        String convTime = null;
        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second+" seconds "+suffix;
            } else if (minute < 60) {
                convTime = minute+" minutes "+suffix;
            } else if (hour < 24) {
                if (minute > 60) {
                    convTime = hour+" hours "+suffix;
                } else {
                    convTime = hour + " hour " + suffix;
                }
            } else if (day >= 7) {
                if (day > 30) {
                    convTime = (day / 30)+" months "+suffix;
                } else if (day > 360) {
                    convTime = (day / 360)+" years "+suffix;
                } else {
                    convTime = (day / 7) + " week "+suffix;
                }
            } else if (day < 7) {
                convTime = day+" days "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;

    }
}
