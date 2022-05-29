package com.protect.safetylife.recorder;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.protect.safetylife.controller.dashboard.DashboardActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Recorder {

    public static final String LOG_TAG = "nimic";
    public static MediaRecorder mRecorder;
    public  static String mFilename ="";
    public static MediaRecorder mediaRecorder;


    public Recorder()
    {
        mFilename= Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilename+="/recorder_audio.3gp";
    }
    public static void startRecording() {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFilename);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e){
            Log.e(LOG_TAG, "prepare()failed");
        }
        mRecorder.start();
    }
    public static void startRecording2() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(DashboardActivity.pathRecord);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            Log.e("nimin", "startRecording: ", e);
        }
    }


    public static void stopRecording()
    {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;
    }

}