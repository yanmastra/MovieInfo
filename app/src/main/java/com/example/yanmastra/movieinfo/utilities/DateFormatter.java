package com.example.yanmastra.movieinfo.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yan Mastra on 8/19/2017.
 */

public class DateFormatter {
    public static String getReadableDate(String date){
        String result = "";
        DateFormat old = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date oldDate = old.parse(date);
            DateFormat newFormat = new SimpleDateFormat("MM dd, yyyy");
            result = newFormat.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
