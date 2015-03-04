package com.ubung.tc.ubungmobile.controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ButtonAdapterView;
import com.ubung.tc.ubungmobile.controlador.adapters.GettingStartAdapter;
import com.ubung.tc.ubungmobile.modelo.InterfazUbung;
import com.ubung.tc.ubungmobile.modelo.Singleton;


public class MainUbungActivity extends Activity {

    private InterfazUbung singleton;

    // final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Singleton singleton = Singleton.getInstance();
        singleton.inicializar(this.getApplicationContext());
        this.singleton = singleton;

        setContentView(R.layout.activity_main_ubung);
        GettingStartAdapter adapter = new GettingStartAdapter(this);
        ViewPager myPager = (ViewPager) findViewById(R.id.gettingstartpager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);

    }

    public void initGridView() {
        GridView g = (GridView) findViewById(R.id.grid_button_view);
        g.setAdapter(new ButtonAdapterView(this));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(ChooseSportActivity.this, "" + position + "-" + id, Toast.LENGTH_LONG).show();
                intent(position, id);
            }
        });

    }

    public void intent(int position, long id) {
        Intent t = new Intent(this, DescriptionSportActivity.class);
        t.putExtra("position", position);
        t.putExtra("id", id);
        startActivity(t);
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }



}
