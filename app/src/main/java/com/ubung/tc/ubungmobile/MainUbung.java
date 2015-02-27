package com.ubung.tc.ubungmobile;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import android.widget.AdapterView;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.ubung.tc.ubungmobile.adapters.ButtonAdapterView;


public class MainUbung extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ubung);
        initGridView();
    }

    private void initGridView() {
        GridView g = (GridView) findViewById(R.id.grid_button_view);
        g.setAdapter(new ButtonAdapterView(this));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainUbung.this,""+position+"-"+id,Toast.LENGTH_LONG).show();
             mapIntent(view);
            }
        });
    }

    public void mapIntent(View v){
        Intent t= new Intent(this,LocationActivity.class);
        startActivity(t);
    }

}
