package com.example.dhruv.mynewsfeed;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by dhruv on 11/8/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    public NewsLoader(Context context) {
        super(context);

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i("success","loader onstart me aya");

    }

    @Override
    public List<News> loadInBackground() {
        Log.i("success","loadinback me aya");

        List<News> list=QueryHandler.fetchNews();
        try{
            Log.i("success",String.valueOf(list));

        }catch (Exception e){

        }
        return list;
    }
}
