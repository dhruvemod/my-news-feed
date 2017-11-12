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
    }

    @Override
    public List<News> loadInBackground() {
        List<News> list=QueryHandler.fetchNews(section);
        return list;
    }
}
