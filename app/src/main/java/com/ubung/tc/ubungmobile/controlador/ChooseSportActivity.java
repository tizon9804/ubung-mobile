package com.ubung.tc.ubungmobile.controlador;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ButtonAdapterView;


public class ChooseSportActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sport);
        initGridView();
    }

    private void initGridView() {
        GridView g = (GridView) findViewById(R.id.grid_button_view);
        g.setAdapter(new ButtonAdapterView(this));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChooseSportActivity.this, "" + position + "-" + id, Toast.LENGTH_LONG).show();
                mapIntent(view);
            }
        });
    }

    public void mapIntent(View v){
        Intent t= new Intent(this,LocationActivity.class);
        startActivity(t);
    }

}
