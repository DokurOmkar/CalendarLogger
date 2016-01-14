package com.omkardokur.calendarlogger;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CallLog;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by omkardokur on 1/4/16.
 */
public class CallsAsyncTask extends AsyncTask<Integer,Void,Void> {
    private ProgressDialog progressDialog;
    private Context context;
    boolean mIn = true;
    boolean mOut = true;
    boolean mMiss = true;
    Cursor managedCursor;

    public CallsAsyncTask(Context context, boolean in, boolean out, boolean miss, Cursor cur) {
        this.context = context;
        this.mIn = in;
        this.mOut = out;
        this.mMiss = miss;
        managedCursor = cur;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Integer... params) {
        Calendar beginTime = Calendar.getInstance();
        ContentValues l_event = new ContentValues();


        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        for (int i = 0; i < params[0]; i++){
            if(managedCursor.moveToNext()) {
                String phNum = managedCursor.getString(number);
                String callTypeCode = managedCursor.getString(type);
                String strcallDate = managedCursor.getString(date);
                beginTime.setTimeInMillis(Long.parseLong(strcallDate));

                String callType = null;
                int callcode = Integer.parseInt(callTypeCode);
                switch (callcode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Outgoing";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Incoming";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Missed";
                        break;
                }
                if ((callcode == CallLog.Calls.OUTGOING_TYPE && mOut) || (callcode == CallLog.Calls.INCOMING_TYPE && mIn) || (callcode == CallLog.Calls.MISSED_TYPE && mMiss)) {
                    l_event.put("calendar_id", 1);
                    l_event.put("title", phNum);
                    l_event.put("description", callType + "\n" + "Phone Number:" + phNum);
                    l_event.put("eventLocation", "Mobile");
                    l_event.put("dtstart", beginTime.getTimeInMillis());
                    l_event.put("dtend", beginTime.getTimeInMillis());
                    l_event.put("allDay", 0);
                    l_event.put("rrule", "FREQ=YEARLY");
                    l_event.put("eventTimezone", "India");
                    Uri l_eventUri;
                    if (Build.VERSION.SDK_INT >= 8) {
                        l_eventUri = Uri.parse("content://com.android.calendar/events");
                    } else {
                        l_eventUri = Uri.parse("content://calendar/events");
                    }
                    Uri l_uri = context.getContentResolver()
                            .insert(l_eventUri, l_event);

                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "Calls saved Successfully", Toast.LENGTH_SHORT).show();
    }
}
