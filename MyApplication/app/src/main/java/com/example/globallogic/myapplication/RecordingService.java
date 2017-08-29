package com.example.globallogic.myapplication;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

/**
 * Created by globallogic on 23/8/17.
 */

public class RecordingService extends IntentService {

    private static String mFileName = null;

    private MediaRecorder mRecorder;

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;

    public RecordingService(){
        super("Recording Service");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        System.out.println("===============IN SERVICE===================");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(mCallReceiver, filter);
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mCallReceiver);
        stopRecording();
    }

    BroadcastReceiver mCallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.w("intent " , intent.getAction().toString());

            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

            }
            else{
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = 0;
                if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    state = TelephonyManager.CALL_STATE_IDLE;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                    state = TelephonyManager.CALL_STATE_RINGING;
                }

                onCallStateChanged(context, state, number);
            }
        }


        public void onCallStateChanged(Context context, int state, String number) {
            if(lastState == state){
                //No change, debounce extras
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;
                    callStartTime = new Date();
                    savedNumber = number;

                    Toast.makeText(context, "Incoming Call Ringing" , Toast.LENGTH_SHORT).show();
                    System.out.println("Incoming Call Ringing");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                    if(lastState != TelephonyManager.CALL_STATE_RINGING){
                        isIncoming = false;
                        callStartTime = new Date();
                        Toast.makeText(context, "Outgoing Call Started" , Toast.LENGTH_SHORT).show();
                        System.out.println("Outgoing Call Started");
                        startRecording();
                    }else{
                        //Its an incoming call
                        startRecording();
                    }

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                    if(lastState == TelephonyManager.CALL_STATE_RINGING){
                        //Ring but no pickup-  a miss
                        Toast.makeText(context, "Ringing but no pickup" + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();
                        System.out.println("Ringing but no pickup" + savedNumber + " Call time " + callStartTime +" Date " + new Date() );
                    }
                    else if(isIncoming){

                        Toast.makeText(context, "Incoming " + savedNumber + " Call time " + callStartTime  , Toast.LENGTH_SHORT).show();
                        System.out.println("Incoming " + savedNumber + " Call time " + callStartTime );
                        stopRecording();
                    }
                    else{

                        Toast.makeText(context, "outgoing " + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();
                        System.out.println("outgoing " + savedNumber + " Call time " + callStartTime +" Date " + new Date() );
                        stopRecording();
                    }

                    break;
            }
            lastState = state;
        }
    };


    private void startRecording() {
        try {

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Record to the external cache directory for visibility
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            String audioId = "RecordedAudio";


            mFileName += "/" + audioId + ".mp3";
            mRecorder.setOutputFile(mFileName);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "prepare() failed");
            }

            mRecorder.start();

            Log.e("TAG", "Recording:Calling ");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void stopRecording() {
        try {
            if(mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
            }
            mRecorder = null;

            Log.e("TAG", "stopRecording:Calling ");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
