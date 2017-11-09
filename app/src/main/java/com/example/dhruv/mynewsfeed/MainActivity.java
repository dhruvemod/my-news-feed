package com.example.dhruv.mynewsfeed;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private TextView mEmptyTextView;
    public static final String News_API = "https://content.guardianapis.com/search?";
    public static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerNewsAdapter recyclerNewsAdapter;
    private static final int NEWS_LOADER_ID = 1;
    private ProgressBar progressBar;
    private Editable urlText;
    private LinearLayoutManager llm;
    private NetworkInfo info;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycleList);
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        LoaderManager l = getSupportLoaderManager();
        info = connectivityManager.getActiveNetworkInfo();
        mEmptyTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Log.i("success","main me aya");
        if (info != null && info.isConnectedOrConnecting()) {
            l.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
            Log.i("success","intit me aya");
        } else {
            if (recyclerNewsAdapter.getItemCount() == 0) {
                mEmptyTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                mEmptyTextView.setText("No Active Internet Connection found!");
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "No Active Internet Connection found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void updateUI(List<News> bookEv) {
        recyclerNewsAdapter = new RecyclerNewsAdapter(bookEv, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                News b = recyclerNewsAdapter.getItem(position);

                Uri uri = Uri.parse(b.getUrl());
                Intent webview = new Intent(Intent.ACTION_VIEW, uri);
                //checking if app available to launch the intent
                if (webview.resolveActivity(getPackageManager()) != null) {
                    startActivity(webview);
                }
            }
        });
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),llm.getOrientation());
        try {
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(recyclerNewsAdapter);
        } catch (Exception e) {
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.i("success","onCreateLoader  me aya");
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        progressBar.setVisibility(View.INVISIBLE);
        try {
            //removing any pre existing data on the adapter
            updateUI(data);
        } catch (Exception e) {
        }
        //in case of no books available
        if (info == null) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            mEmptyTextView.setText("No active internet connection!");
        }
        if (recyclerNewsAdapter.getItemCount() == 0) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            mEmptyTextView.setText("No News found!");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        try {
            if (recyclerNewsAdapter != null) {
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        getLoaderManager().destroyLoader(NEWS_LOADER_ID);
        super.onDestroy();
    }
}
