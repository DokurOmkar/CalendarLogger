package com.omkardokur.calendarlogger;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by omkardokur on 1/4/16.
 */
public class SmsAsyncTask extends AsyncTask<Integer, Void, Void> {
    private ProgressDialog progressDialog;
    private Context context;
    boolean received = true;
    boolean sent = true;

    public SmsAsyncTask(Context context, boolean rec, boolean se) {
        this.context = context;
        this.received = rec;
        this.sent = se;
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
        Uri inboxURI = Uri.parse("content://sms/inbox");
        Cursor cur =  context.getContentResolver().query(inboxURI, null, null, null, null);
        Uri sentURI = Uri.parse("content://sms/sent");
        Cursor cursorSent = context.getContentResolver().query(sentURI, null, null, null, null);
        for (int i = 0; i < params[0]; i++) {
            if(cur.moveToNext() && received) {
                beginTime.setTimeInMillis(cur.getLong(4));
                l_event.put("calendar_id", 1);
                l_event.put("title", cur.getString(2));
                l_event.put("description", cur.getString(13));
                l_event.put("eventLocation", "Mobile");
                l_event.put("dtstart", beginTime.getTimeInMillis());
                l_event.put("dtend", beginTime.getTimeInMillis());
                l_event.put("allDay", 0);
                l_event.put("rrule", "FREQ=YEARLY");
                l_event.put("eventTimezone", "US");
                Uri l_eventUri;
                if (Build.VERSION.SDK_INT >= 8) {
                    l_eventUri = Uri.parse("content://com.android.calendar/events");
                } else {
                    l_eventUri = Uri.parse("content://calendar/events");
                }
                Uri l_uri = context.getContentResolver()
                        .insert(l_eventUri, l_event);

            }
            if(cursorSent.moveToNext()&& sent){
                beginTime.setTimeInMillis(cursorSent.getLong(4));
                l_event.put("calendar_id", 1);
                l_event.put("title", cursorSent.getString(2));
                l_event.put("description", cursorSent.getString(13));
                l_event.put("eventLocation", "Mobile");
                l_event.put("dtstart", beginTime.getTimeInMillis());
                l_event.put("dtend", beginTime.getTimeInMillis());
                l_event.put("allDay", 0);
                l_event.put("rrule", "FREQ=YEARLY");
                l_event.put("eventTimezone", "US");
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
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "Sms saved Successfully", Toast.LENGTH_SHORT).show();
    }
}
