package com.jumpingbeanapps.android.hiraganapractice;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import static android.R.attr.typeface;

public class MainActivity extends AppCompatActivity {

    private Button drawButton;
    private TextView title;
    private Button quizButton;
    private Button pronuncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView)findViewById(R.id.title);
        drawButton = (Button)findViewById(R.id.draw);
        quizButton = (Button)findViewById(R.id.quiz);
        pronuncButton = (Button)findViewById(R.id.pronunciating);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/njnaruto.ttf");
        title.setTypeface(custom_font);
        drawButton.setTypeface(custom_font);
        quizButton.setTypeface(custom_font);
        pronuncButton.setTypeface(custom_font);
    }
    public void openDraw(View view){
        Intent intent = new Intent(this,PracticeDrawingActivity.class);
        startActivity(intent);
    }
    public void openPronunc(View view){
        Intent intent = new Intent(this,PronunciateActivity.class);
        startActivity(intent);
    }
    public void openQuiz(View view){
        Intent intent = new Intent(this,QuizActivity.class);
        startActivity(intent);
    }
}
