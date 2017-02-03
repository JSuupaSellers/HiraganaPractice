package com.jumpingbeanapps.android.hiraganapractice;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Josh on 2/3/2017.
 */

public class QuizActivity extends AppCompatActivity {

    private ArrayList<String> questions;
    private ArrayList<String> letterNames;
    private ArrayList<String> correct;
    private ArrayList<String> incorrect;
    private SoundSetup mSounds;
    private ArrayList<Button> choices;

    private Button a;
    private Button b;
    private Button c;
    private Button d;
    private ImageView questionAsked;
    private String correctAnswer;
    Random rand;

    @Override
    protected void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        choices = new ArrayList<>();
        setContentView(R.layout.layout_quiz);
        rand = new Random();
        a = (Button)findViewById(R.id.a);
        b = (Button)findViewById(R.id.b);
        c = (Button)findViewById(R.id.c);
        d = (Button)findViewById(R.id.d);

        a.setText("");
        b.setText("");
        c.setText("");
        d.setText("");

        choices.add(a);
        choices.add(b);
        choices.add(c);
        choices.add(d);

        questionAsked = (ImageView)findViewById(R.id.question_being_asked);

        mSounds = new SoundSetup(this);
        questions = new ArrayList<>();
        letterNames = new ArrayList<>();
        correct = new ArrayList<>();
        incorrect = new ArrayList<>();

        for(Sound sound : mSounds.getSounds()){
            if(sound != null){
                questions.add(sound.getName());
                letterNames.add(sound.getName());
            }
        }
        int n = rand.nextInt(questions.size());
        int ca = rand.nextInt(3);
        Resources res = getResources();
        int id = res.getIdentifier(questions.get(n),"drawable",getPackageName());
        correctAnswer = questions.get(n);
        questionAsked.setImageResource(id);
        choices.get(ca).setText(questions.get(n));

        for(Button view: choices){
            int rand0 = rand.nextInt(questions.size());
            while(letterNames.get(rand0).contentEquals(questions.get(n))){
                rand0 = rand.nextInt(questions.size());
            }
            if(view.getText() == ""){
                view.setText(letterNames.get(rand0));
            }
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a.getText() == correctAnswer ){
                    if(questions.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Empty!",Toast.LENGTH_SHORT).show();

                    }else{
                        askQuestion(questions);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b.getText() == correctAnswer ){
                    questions.remove(b.getText());
                    if(questions.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Empty!",Toast.LENGTH_SHORT).show();
                    }else{
                        askQuestion(questions);
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c.getText() == correctAnswer ){
                    if(questions.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Empty!",Toast.LENGTH_SHORT).show();
                    }else{
                        askQuestion(questions);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(d.getText() == correctAnswer ){
                    questions.remove(d.getText());
                    if(questions.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Empty!",Toast.LENGTH_SHORT).show();
                    }else{
                        askQuestion(questions);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    public void askQuestion(ArrayList<String> questions){
        for(Button view : choices){
            view.setText("");
        }
        int n = rand.nextInt(questions.size());
        int ca = rand.nextInt(3);

        Resources res = getResources();
        int id = res.getIdentifier(questions.get(n),"drawable",getPackageName());
        correctAnswer = questions.get(n);
        questionAsked.setImageResource(id);
        choices.get(ca).setText(questions.get(n));

        for(Button view: choices){
            int rand0 = rand.nextInt(questions.size());
            while(letterNames.get(rand0).contentEquals(questions.get(n))){
                rand0 = rand.nextInt(questions.size());
            }
            if(view.getText() == ""){
                view.setText(letterNames.get(rand0));
            }
        }
    }
}
