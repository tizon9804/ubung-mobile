package com.ubung.tc.ubungmobile.controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ubung.tc.ubungmobile.R;


public class MainUbungActivity extends Activity {

    // final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ubung);
        initButtons();
    }

    private void initButtons() {
        final Button start = (Button) findViewById(R.id.btn_start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   v.startAnimation(animAlpha);
                nextActivity();
            }
        });
        start.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start.setTextColor(getResources().getColor(R.color.holo_green_light));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    start.setTextColor(getResources().getColor(R.color.white));
                }
                return false;
            }
        });

    }

    private void nextActivity() {
        Intent i = new Intent(this, ChooseSportActivity.class);
        startActivity(i);
    }


}
