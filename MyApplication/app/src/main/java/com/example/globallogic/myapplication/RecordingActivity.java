package com.example.globallogic.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by globallogic on 23/8/17.
 */

public class RecordingActivity extends AppCompatActivity{

    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    ImageView imgRecording;
    boolean isRecording = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        startService(new Intent(this, RecordingService.class));
        imgRecording = (ImageView) findViewById(R.id.imgRecording);

        imgRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndRecord();
            }
        });

        requestForPermissionsAndAllow();
    }

    public void requestForPermissionsAndAllow(){
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS}, 100);
        }
    }

    public void checkPermissionAndRecord(){
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  ) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            validateRecording();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
//            validateRecording();
        }
    }

    public void validateRecording(){
        if(!isRecording){
            startRecording();
            isRecording = true;
            Toast.makeText(RecordingActivity.this, "Recording Started", Toast.LENGTH_SHORT).show();
            imgRecording.setImageResource(android.R.drawable.ic_media_pause);
        }else{
            stopRecording();
            isRecording = false;
            Toast.makeText(RecordingActivity.this, "Recording Stopped", Toast.LENGTH_SHORT).show();
            imgRecording.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void startRecording() {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        // Record to the external cache directory for visibility
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        String audioId= "RecordedAudio";


        mFileName += "/"+audioId+".mp3";
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "prepare() failed");
        }

        mRecorder.start();

        Log.e("TAG", "Recording:Calling " );

    }


    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        Log.e("TAG", "stopRecording:Calling " );

    }
}
