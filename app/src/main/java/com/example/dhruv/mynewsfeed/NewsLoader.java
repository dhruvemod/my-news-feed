package com.example.dhruv.mynewsfeed;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by dhruv on 11/8/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    String section;

    public NewsLoader(Context context,String mSec) {
        super(context);
        section=mSec;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i("success","loader onstart me aya");

    }

    @Override
    public List<News> loadInBackground() {

        List<News> list=QueryHandler.fetchNews(section);
        try{
            Log.i("success",String.valueOf(list));

        }catch (Exception e){

        }
        return list;
    }
}
