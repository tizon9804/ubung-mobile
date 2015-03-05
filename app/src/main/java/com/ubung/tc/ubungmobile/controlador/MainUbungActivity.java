package com.ubung.tc.ubungmobile.controlador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;


import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ButtonAdapterView;
import com.ubung.tc.ubungmobile.controlador.adapters.GettingStartAdapter;
import com.ubung.tc.ubungmobile.modelo.InterfazUbung;
import com.ubung.tc.ubungmobile.modelo.Singleton;


public class MainUbungActivity extends Activity {

    public static final String POSITION = "position";
    public static final String ID = "id";
    public static final String USER = "usuario";

    private InterfazUbung singleton;
    protected boolean active = true;
    protected int ubungTime = 1000;
    private Thread ubungThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Singleton singleton = Singleton.getInstance();
        singleton.inicializar(this.getApplicationContext());
        this.singleton = singleton;

        if(singleton.darPropietario()!=null){
            animationUbung();
        }
        else {
            setContentView(R.layout.activity_main_ubung);
            GettingStartAdapter adapter = new GettingStartAdapter(this);
            ViewPager myPager = (ViewPager) findViewById(R.id.gettingstartpager);
            myPager.setAdapter(adapter);
            myPager.setCurrentItem(0);

        }
    }

    private void animationUbung() {

        setContentView(R.layout.inicio_ubung);
        TextView deslice= (TextView)findViewById(R.id.deslice_inicio);
        deslice.setText("");
        ubungThread = new Thread(){
            @Override
            public void run(){
                try{
                    int waited = 0;
                    while(active && (waited < ubungTime)){
                        sleep(100);
                        if(active){
                            waited += 100;
                        }

                    }
                } catch(InterruptedException e){

                } finally{
                    openMap();
                }

            }
        };
        ubungThread.start();
    }

    public void initGridView() {


            //String mDrawableName = "myimg";
            // int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
            GridView g = (GridView) findViewById(R.id.grid_button_view);

            //  Toast.makeText(this, usuario, Toast.LENGTH_LONG).show();
            g.setAdapter(new ButtonAdapterView(this));
            g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String usuario = ((EditText) findViewById(R.id.user_name)).getText().toString().trim();
                    intentDescription(position, id, usuario);
                }
            });


    }

    public void intentDescription(int position, long id, String usuario) {
        Intent t = new Intent(this, DescriptionSportActivity.class);
        t.putExtra(POSITION, position+"");
        t.putExtra(ID, id+"");
        t.putExtra(USER, usuario);
        startActivity(t);
    }


    public void initUser_registation() {
        EditText user= (EditText)findViewById(R.id.user_name);
        user.requestFocus();
        user.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                return true;
            }
        });
//Todo implementar que el usuario no se repita con los del exterior
    }


    private void openMap(){
        finish();
        startActivity(new Intent(this,LocationActivity.class));
    }


    @Override
    public void onResume(){
    super.onResume();
    }
}
