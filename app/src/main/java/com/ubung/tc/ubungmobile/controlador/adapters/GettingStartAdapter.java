package com.ubung.tc.ubungmobile.controlador.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.MainUbungActivity;


public class GettingStartAdapter extends PagerAdapter {

    private MainUbungActivity main;

    public GettingStartAdapter(MainUbungActivity mainUbungActivity) {
        main = mainUbungActivity;
    }

    public int getCount() {
        return 5;
    }


    public Object instantiateItem(View collection, int position) {

        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.layout.inicio_ubung;
                break;
            case 1:
                resId = R.layout.info_app1;
                break;
            case 2:
                resId = R.layout.info_app2;
                break;
            case 3:
                resId = R.layout.user_registation;
                break;
            case 4:
                resId = R.layout.activity_choose_sport;
                break;
        }

        View view = inflater.inflate(resId, null);
        ((ViewPager) collection).addView(view, 0);

        if (position == 4) {
            try {
                main.initView();
            } catch (ParseException e) {
                Log.e("GettingStar",e.getMessage());
            }
        } else if (position == 3) {
            main.initUser_registation();


        }

        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);


    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
