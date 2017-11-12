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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private TextView mEmptyTextView;
    private EditText section;
    private Button button;
    private String word;
    private int count;
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
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.searchButton);
        section = findViewById(R.id.sectionSearch);
        recyclerView = (RecyclerView) findViewById(R.id.recycleList);
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        final LoaderManager l = getSupportLoaderManager();
        info = connectivityManager.getActiveNetworkInfo();
        word = "world";
        //setting up the button which will search the particular section for the user
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = connectivityManager.getActiveNetworkInfo();
                mEmptyTextView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                urlText = section.getText();
                word = urlText.toString();
                //Checking if the EditText returns null or not
                if (TextUtils.isEmpty(urlText)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Enter section first", Toast.LENGTH_SHORT).show();
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    Log.i("count check", String.valueOf(count));
                    if (count > 0) {
                        if (info != null && info.isConnectedOrConnecting()) {
                            l.restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                        }
                    }
                    if (info != null && info.isConnectedOrConnecting()) {
                        l.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
                        count++;
                    } else {
                        if (recyclerNewsAdapter == null && info == null) {
                            progressBar.setVisibility(View.INVISIBLE);
                            mEmptyTextView.setVisibility(View.VISIBLE);
                            mEmptyTextView.setText("No active internet connection found");
                        } else if (info != null) {
                            if (recyclerNewsAdapter.getItemCount() == 0) {
                                mEmptyTextView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                mEmptyTextView.setText("No Active Internet Connection found!");
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "No Active Internet Connection found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    private void updateUI(List<News> newsList) {
        recyclerNewsAdapter = new RecyclerNewsAdapter(newsList, new CustomItemClickListener() {
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
        //Adding dividers between the two news in the recyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
        try {
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(recyclerNewsAdapter);
        } catch (Exception e) {
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, word);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        progressBar.setVisibility(View.INVISIBLE);
        try {
            //updating the UI
            updateUI(data);
        } catch (Exception e) {
        }
        //in case of no news available
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
        count = 0;
        try {
            if (recyclerNewsAdapter != null) {
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        count = 0;
        getLoaderManager().destroyLoader(NEWS_LOADER_ID);
        super.onDestroy();
    }
}
