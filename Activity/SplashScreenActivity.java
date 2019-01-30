package com.example.nabab.tourmate.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nabab.tourmate.R;

public class SplashScreenActivity extends AppCompatActivity {
    private Button splashScreenLoginBtn, splashScreenSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    /*splashScreenLoginBtn = findViewById(R.id.splashScreenLoginBtnId);
    splashScreenSignUpBtn = findViewById(R.id.splashScreenSignUpBtnId);*/

    /*splashScreenLoginBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        }
    });

    splashScreenSignUpBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
        }
    });*/

    }
}
