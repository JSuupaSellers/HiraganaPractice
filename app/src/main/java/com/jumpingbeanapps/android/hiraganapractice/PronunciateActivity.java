package com.jumpingbeanapps.android.hiraganapractice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class PronunciateActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;
    private SoundSetup mSoundSetup;

    private ImageButton recordButton;
    private ImageButton playButton;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private Sound currentSound;
    private RecyclerView mLetters;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        //mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pronunciation);
        final boolean mStartRecording = true;

        playButton = (ImageButton)findViewById(R.id.play_recording);
        recordButton = (ImageButton)findViewById(R.id.record);

        // Record to the external cache directory for visibility
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        mSoundSetup = new SoundSetup(this);
        mLetters = (RecyclerView)findViewById(R.id.hiragana_letters_recycler_view);
        mLetters.setLayoutManager(layoutManager);
        mLetters.setAdapter(new PronunciateActivity.SoundAdapter(mSoundSetup.getSounds()));

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecorder == null){
                    startRecording();
                    recordButton.setImageResource(R.drawable.ic_action_name);
                }else{
                    stopRecording();
                    recordButton.setImageResource(R.drawable.not_recording);
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer == null){
                    startPlaying();
                }else{
                    stopPlaying();
                    startPlaying();
                }

            }
        });

//        LinearLayout ll = new LinearLayout(this);
//        mRecordButton = new RecordButton(this);
//        ll.addView(mRecordButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        mPlayButton = new PlayButton(this);
//        ll.addView(mPlayButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        setContentView(ll);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private class SoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mLetterName;
        private ImageView mLetterImage;

        private Sound mSound;

        public SoundHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.list_item_sound,container,false));

            mLetterName = (TextView)itemView.findViewById(R.id.letter);
            mLetterImage = (ImageView)itemView.findViewById(R.id.letter_image);
            mLetterImage.setOnClickListener(this);
        }

        public void bindSound(Sound sound){
            mSound = sound;
            mLetterName.setText(mSound.getName());
            Resources res = getResources();
            int id = res.getIdentifier(mSound.getName(), "drawable", getPackageName());
            mLetterImage.setImageResource(id);

        }

        @Override
        public void onClick(View v){
            currentSound = mSound;
            Resources res = getResources();
            if(currentSound != null) {
                mSoundSetup.play(currentSound);
            }else{
                return;
            }
        }



    }
    private class SoundAdapter extends RecyclerView.Adapter<PronunciateActivity.SoundHolder>{
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds){
            mSounds = sounds;
        }
        @Override
        public PronunciateActivity.SoundHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = getLayoutInflater();
            return new PronunciateActivity.SoundHolder(inflater,parent);
        }
        @Override
        public void onBindViewHolder(PronunciateActivity.SoundHolder soundHolder, int position){

            Sound sound = mSounds.get(position);
            soundHolder.bindSound(sound);

        }
        @Override
        public int getItemCount(){
            return mSounds.size();
        }
    }
}