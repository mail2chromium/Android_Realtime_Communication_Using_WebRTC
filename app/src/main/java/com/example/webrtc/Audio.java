package com.example.webrtc;

import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

public class Audio {

    private static final String TAG = "Audio";

    public static final int SAMPLE_RATE = 16000;

    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private int minBufferSizeInBytes;
    private byte[] byteData;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void inititalize(){

        minBufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        Log.e(TAG, "MIN BUFFER SIZE: " + minBufferSizeInBytes);
//        minBufferSizeInShort = minBufferSizeInBytes / 2;

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSizeInBytes);

        audioTrack = new AudioTrack(new AudioAttributes.Builder()
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
                , new AudioFormat.Builder()
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setSampleRate(SAMPLE_RATE)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .build()
                , minBufferSizeInBytes, AudioTrack.MODE_STREAM, audioRecord.getAudioSessionId());

        byteData = new byte[minBufferSizeInBytes];
    }

    public void startRecording(){
        audioRecord.startRecording();
    }

    public byte[] getRecordedData(){
        audioRecord.read(byteData,0, minBufferSizeInBytes);
        return byteData;
    }

    public void startPlaying(){
        audioTrack.play();
    }

    public void playBytes(byte[] byteData){
        audioTrack.write(byteData,0,byteData.length);
    }
}

