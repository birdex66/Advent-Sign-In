package com.example.adventsignin;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.atomic.AtomicBoolean;

public class Confirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.confirmed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.goodbye), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        View r = findViewById(R.id.goodbye);
        AtomicBoolean f = new AtomicBoolean(false);
        r.setOnTouchListener((v,event) -> {
            f.set(true);
            startActivity(new Intent(this,WelcomeActivity.class));
            finish();
            return true;
        });
        new CountDownTimer(10000,1000){
            @Override
            public void onTick(long millisUntilFinished) {return;}

            @Override
            public void onFinish(){
                if(!f.get()) startActivity(new Intent(Confirm.this,WelcomeActivity.class));
                finish();
            }
        }.start();
    }
}
