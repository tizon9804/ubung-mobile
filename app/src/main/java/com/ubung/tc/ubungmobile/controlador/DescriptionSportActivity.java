package com.ubung.tc.ubungmobile.controlador;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ubung.tc.ubungmobile.R;


public class DescriptionSportActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_sport);
        initButtons();
    }

    private void initButtons() {
        final Button start=(Button) findViewById(R.id.btn_continue);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 nextActivity();
            }
        });
        start.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    start.setTextColor(getResources().getColor(R.color.holo_green_light));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    start.setTextColor(getResources().getColor(R.color.white));
                }
                return false;
            }
        });

    }

    private void nextActivity() {
        Intent i= new Intent(this,LocationActivity.class);
        startActivity(i);
    }


}
