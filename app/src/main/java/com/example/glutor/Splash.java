package com.example.glutor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    //variables
    Animation topanim,bottomanim;
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_splash);


        //Animations
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);


        //hooks
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        imageView.setAnimation(topanim);
        textView.setAnimation(bottomanim);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(Splash.this,MainActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(imageView,"logo_image");
                pairs[1] = new Pair<View,String>(textView,"logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash.this,pairs);
                startActivity(intent,options.toBundle());


            }
        },2200);

    }
}