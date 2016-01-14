package com.omkardokur.calendarlogger;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


import java.util.Calendar;

/**
 * Created by omkardokur on 1/6/16.
 */
public class CallsReceiver extends BroadcastReceiver {
    Context mContext;
    String description = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            mContext = context;
            // TELEPHONY MANAGER class object to register one listener
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            //Create Listener
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

            // Register listener for LISTEN_CALL_STATE
            telephonyManager.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }
    }


    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {
            Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                long myCurrentTimeMillis = System.currentTimeMillis();
                Calendar beginTime = Calendar.getInstance();
                ContentValues l_event = new ContentValues();
                beginTime.setTimeInMillis(myCurrentTimeMillis);
                l_event.put("calendar_id", 1);
                l_event.put("title", incomingNumber);
                l_event.put("description", "Incoming Call" + "\n" + "Phone Number:" + incomingNumber);
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
                Uri l_uri = mContext.getContentResolver()
                        .insert(l_eventUri, l_event);
            }
        }
    }
}
