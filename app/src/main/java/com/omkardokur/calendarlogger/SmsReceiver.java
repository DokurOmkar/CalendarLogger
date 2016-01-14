package com.omkardokur.calendarlogger;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

/**
 * Created by omkardokur on 1/5/16.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        Bundle bundle = intent.getExtras();
        String senderNumber = null;
        String message = null;
        long time = 0;
        Calendar beginTime = Calendar.getInstance();
        ContentValues l_event = new ContentValues();
        try {

            if (bundle != null) {

                Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    senderNumber = smsMessage.getOriginatingAddress();
                    time = smsMessage.getTimestampMillis();
                    message = smsMessage.getDisplayMessageBody();
                   // Toast.makeText(context, "senderNum: "+ senderNumber + ", message: " + message, Toast.LENGTH_SHORT).show();
                }
            }
            beginTime.setTimeInMillis(time);
            l_event.put("calendar_id", 1);
            l_event.put("title", senderNumber);
            l_event.put("description", message);
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

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
