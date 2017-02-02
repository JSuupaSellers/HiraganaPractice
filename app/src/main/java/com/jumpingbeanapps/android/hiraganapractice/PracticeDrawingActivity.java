package com.jumpingbeanapps.android.hiraganapractice;

import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

/**
 * Created by Josh on 1/31/2017.
 */

public class PracticeDrawingActivity extends AppCompatActivity {
    private SoundSetup mSoundSetup;
    private Sound currentSound;
    private RecyclerView mLetters;
    private ImageButton mPlay;
    private ImageButton mNew;
    private ImageView traceImage;
    private DrawingView drawingView;
    private int resourceId;
    private AdView mAdView;
    private CheckBox mCheckBox;
    @Override
    protected void onResume(){
        super.onResume();
        mLetters.getAdapter().notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_view_layout);
        currentSound = null;
        mPlay = (ImageButton)findViewById(R.id.play_btn);
        mNew = (ImageButton)findViewById(R.id.clear_btn);
        drawingView = (DrawingView)findViewById(R.id.drawing);
        mCheckBox = (CheckBox)findViewById(R.id.check_blank);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mSoundSetup = new SoundSetup(this);
        mLetters = (RecyclerView)findViewById(R.id.hiragana_letters_recycler_view);
        mLetters.setLayoutManager(layoutManager);
        mLetters.setAdapter(new SoundAdapter(mSoundSetup.getSounds()));
        traceImage = (ImageView)findViewById(R.id.letter_to_draw);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7438194096520605~9660046770");

        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!mCheckBox.isChecked()){
                    traceImage.setImageResource(resourceId);
                }else{
                    traceImage.setImageDrawable(null);
                }
            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentSound != null) {
                    mSoundSetup.play(currentSound);
                }else{
                    return;
                }
            }
        });
        mNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.startNew();
            }
        });
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
            if(!mCheckBox.isChecked()){
                mLetterImage.setImageResource(id);
            }

        }

        @Override
        public void onClick(View v){
            currentSound = mSound;
            Resources res = getResources();
            int id = res.getIdentifier(mSound.getName(),"drawable",getPackageName());
            resourceId = id;
            if(!mCheckBox.isChecked()){
                traceImage.setImageResource(id);
            }
            drawingView.startNew();
        }



    }
    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds){
            mSounds = sounds;
        }
        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = getLayoutInflater();
            return new SoundHolder(inflater,parent);
        }
        @Override
        public void onBindViewHolder(SoundHolder soundHolder, int position){

            Sound sound = mSounds.get(position);
            soundHolder.bindSound(sound);

        }
        @Override
        public int getItemCount(){
            return mSounds.size();
        }
    }
}
