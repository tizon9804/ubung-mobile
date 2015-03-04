package com.ubung.tc.ubungmobile.controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

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
        Boolean devuelta = getIntent().getBooleanExtra("last", false);
        if (devuelta)
            myPager.setCurrentItem(4);
        else
            myPager.setCurrentItem(0);


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


}
