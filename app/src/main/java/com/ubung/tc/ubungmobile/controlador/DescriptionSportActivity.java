package com.ubung.tc.ubungmobile.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubung.tc.ubungmobile.R;


public class DescriptionSportActivity extends ActionBarActivity {

    // -----------------------------------------------------
// CONSTRUCTOR
// -----------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_sport);
        InitLabels();
        initButtons();
    }
    // -----------------------------------------------------
// INICIALIZADORES
// -----------------------------------------------------

    private void InitLabels() {

        Intent t=getIntent();

        String id=t.getStringExtra(MainUbungActivity.ID);
        String position=t.getStringExtra(MainUbungActivity.POSITION);
        String user=t.getStringExtra(MainUbungActivity.USER);
        String deporte="Basketball";
        String descripcion="hola";
        //Todo obtener el deporte escogido dado el id o posicion
        TextView txtv=(TextView)findViewById(R.id.title_description_sport);
        txtv.setText("!Enhorabuena "+ user + "Tu deporte es "+ deporte + "!");
        ImageView image= (ImageView)findViewById(R.id.image_sport_description);
        Integer imagePath= getResources().getIdentifier("basket" , "drawable", getPackageName());
        image.setImageResource(imagePath );
        TextView txtdesc=(TextView)findViewById(R.id.description_sport);
        txtdesc.setText("Descripción:\n"+descripcion);


    }

    private void initButtons() {
        final Button start = (Button) findViewById(R.id.btn_continue);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    // -----------------------------------------------------
// CONEXION SIGUIENTE ACTIVIDAD
// -----------------------------------------------------

    private void nextActivity() {
        Intent i = new Intent(this, LocationActivity.class);
        startActivity(i);
    }


}
