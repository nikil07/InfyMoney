package com.androidworks.nikhil.infymoney.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.androidworks.nikhil.infymoney.model.SMS;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nikhil on 23-Nov-16.
 */
public class Utils {

    public static String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void getMessages(Activity activity) {

        int count = 0;
        DataStore.deleteAllMessages();

        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date"};
            Cursor cur = activity.getContentResolver().query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                String strAddress = cur.getString(index_Address);

                do {
                    strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);

                    if (strAddress.contains("IMONEY")) {
//                        if (count == 0) {
//                            if (strbody.contains("topped"))
//                                DataStore.storeBalance(Utils.getBalance(strbody, true));
//                            else
//                                DataStore.storeBalance(Utils.getBalance(strbody, false));
//                            //count++ ,  had initially put this, don't klnow for what, removing..
//                        }
                        if (!strbody.contains("topped") && !strbody.contains("insufficient")) {
                            SMS sms = new SMS();
                            sms.setAddress(strAddress);
                            sms.setLongDate(longDate);
                            sms.setMessageBody(strbody);
                            DataStore.storeMessages(sms);
                            smsBuilder.append("[ ");
                            smsBuilder.append(strAddress + ", ");
                            smsBuilder.append(intPerson + ", ");
                            smsBuilder.append(strbody + ", ");
                            smsBuilder.append(longDate + ", ");
                            smsBuilder.append(" ]\n\n");
                            count++;
                        }
                    }
                } while (cur.moveToNext() && count < 5);

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    public static void updateBalance(Activity activity) {
        final String SMS_URI_INBOX = "content://sms/inbox";
        int count = 0;
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date"};
            Cursor cur = activity.getContentResolver().query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()) {
                while (cur.moveToNext() && count < 1) {
                    int index_Address = cur.getColumnIndex("address");
                    int index_Body = cur.getColumnIndex("body");
                    String strAddress = cur.getString(index_Address);
                    String strbody = cur.getString(index_Body);
                    if (strAddress.contains("IMONEY")) {
                        if (strbody.contains("topped"))
                            DataStore.storeBalance(Utils.getBalance(strbody, true));
                        else
                            DataStore.storeBalance(Utils.getBalance(strbody, false));
                        count++;
                    }

                }
            }
            cur.close();
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    public static String getBalance(String messageBody, boolean isFromRecharge) {

        if (isFromRecharge) {
            Log.d("nikhil", "message body " + messageBody);
            String start = "\\bINR\\b";
            String end = "\\bas\\b";
            int startIndex = 0;
            int endIndex = 0;

            Pattern patternStart = Pattern.compile(start, Pattern.CASE_INSENSITIVE);
            Matcher matcherStart = patternStart.matcher(messageBody);
            while (matcherStart.find()) {
                startIndex = matcherStart.start();
            }

            Pattern patternEnd = Pattern.compile(end, Pattern.CASE_INSENSITIVE);
            Matcher matcherEnd = patternEnd.matcher(messageBody);
            while (matcherEnd.find()) {
                endIndex = matcherEnd.end();
            }

            System.out.println("Start Index:" + startIndex);
            System.out.println("End Index:" + endIndex);

            String subStr = messageBody.substring(startIndex, endIndex);

            Log.d("nikhil", subStr);
            String[] money;
            money = subStr.split("\\s+");

            Log.d("nikhil", money[1]);
            return money[1];
        } else {
            Log.d("nikhil", "message body " + messageBody);
            String start = "\\bAvailable\\b";
            String end = "\\bDate\\b";
            int startIndex = 0;
            int endIndex = 0;

            Pattern patternStart = Pattern.compile(start, Pattern.CASE_INSENSITIVE);
            Matcher matcherStart = patternStart.matcher(messageBody);
            while (matcherStart.find()) {
                startIndex = matcherStart.start();
            }

            Pattern patternEnd = Pattern.compile(end, Pattern.CASE_INSENSITIVE);
            Matcher matcherEnd = patternEnd.matcher(messageBody);
            while (matcherEnd.find()) {
                endIndex = matcherEnd.end();
            }

            System.out.println("Start Index:" + startIndex);
            System.out.println("End Index:" + endIndex);

            String subStr = messageBody.substring(startIndex, endIndex);

            String[] money;
            money = subStr.split("\\s+");

            Log.d("nikhil", money[3]);
            return money[3];
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}