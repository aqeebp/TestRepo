package com.example.globallogic.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by globallogic on 23/8/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("===============IN RECEIVER===================");
        Intent serviceIntent = new Intent(context, RecordingService.class);
        context.startService(serviceIntent);
    }
}
