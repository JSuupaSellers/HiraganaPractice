package com.jumpingbeanapps.android.hiraganapractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button drawButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void openDraw(View view){
        Intent intent = new Intent(this,PracticeDrawingActivity.class);
        startActivity(intent);
    }
    public void openPronunc(View view){
        Intent intent = new Intent(this,PronunciateActivity.class);
        startActivity(intent);
    }
}
