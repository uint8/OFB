package com.agible.ofb.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by seth on 6/19/15.
 */
public class Utilities {
Context context;
    public Utilities(){

    }
    public Utilities(Context context){
        this.context = context;
    }

    public long getBirthDate(String birthdate){
        Calendar calendar = Calendar.getInstance();
        int[] da = parseDate(birthdate);
        calendar.set(da[2], da[1], da[0]);
        calendar.setTimeZone(TimeZone.getDefault());

        return calendar.getTime().getTime();

    }

    public String getStringFromView(EditText view){
        if(context == null)
            return null;

        String s = view.getText().toString();
        view.setHintTextColor(Color.RED);
        if(s.length() < 1){
            Toast.makeText(context, "Please enter the " + view.getHint().toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return s;
    }
    public static String GetStringFromView(EditText view){
        if(view == null)
            return null;

        String s = view.getText().toString();
        view.setHintTextColor(Color.RED);
        if(s.length() < 1){
            Toast.makeText(view.getContext(), "Please enter the " + view.getHint().toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return s;
    }
    public boolean checkObjects(Object... objects){
        for(Object obj : objects){
            if(obj == null)
                return false;
        }
        return true;
    }

    public int[] parseDate(String date){
        int[] d = new int[3];
        String[] split;
        if(date.contains("/"))
            split = date.split("/");
        else if ( date.contains(":"))
            split = date.split(":");
        else if( date.contains("-"))
            split = date.split("-");
        else if(date.contains("_"))
            split = date.split("_");
        else if(date.contains(" "))
            split = date.split("\\s");
        else
            return d;
        int day = 1;
        int mon = 1;
        int yea = 1;

        if(split[0].length() > 2)
            mon = getMonth(split[0]);
        else
            mon = Integer.valueOf(split[0]);

        day = Integer.valueOf(split[1]);
        yea = Integer.valueOf(split[2]);

        if(yea < 100 && yea > 20)
            yea += 1900;
        if(yea < 20)
            yea += 2000;

        d[0] = day;
        d[1] = mon;
        d[2] = yea;

        return d;
    }
    public int getMonth(String month){
        month = month.toUpperCase();
        System.out.println(month);

        if(month.contains("JAN"))
            return 0;
        if(month.contains("FEB"))
            return 1;
        if(month.contains("MAR"))
            return 2;
        if(month.contains("APR"))
            return 3;
        if(month.contains("MAY"))
            return 4;
        if(month.contains("JUN"))
            return 5;
        if(month.contains("JUL"))
            return 6;
        if(month.contains("AUG"))
            return 7;
        if(month.contains("SEP"))
            return 8;
        if(month.contains("OCT"))
            return 9;
        if(month.contains("NOV"))
            return 10;
        if(month.contains("DEC"))
            return 11;
        return 0;
    }

}
