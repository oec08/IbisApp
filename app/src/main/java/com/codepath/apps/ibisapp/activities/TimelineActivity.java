package com.codepath.apps.ibisapp.activities;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.TwitterAppApplication;
import com.codepath.apps.ibisapp.TwitterClient;
import com.codepath.apps.ibisapp.adapters.TweetsArrayAdapter;
import com.codepath.apps.ibisapp.fragments.ComposeTweetDialog;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    public TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private FloatingActionButton btnComposeTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        btnComposeTweet = (FloatingActionButton)findViewById(R.id.fabComposeTweet);
        // Create the arraylist from data source
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);

        lvTweets.setAdapter(aTweets);
        client = TwitterAppApplication.getRestClient(); // Singleton client
        populateTimeline(1);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if(page > 0) {
                    page = page - 1;
                }
                final int  offset = page;
                Handler handler = new Handler();
                Runnable runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        // Do something here on the main thread
                        Log.d("Waiting a second", "Called on main thread");
                        ArrayList<Long> longList = new ArrayList<Long>();
                        for(Tweet a : tweets) {
                           longList.add(a.getUid());
                        }

                        Collections.min(longList);
                        populateTimeline(tweets.get(tweets.size() - 1).getUid());
                    }
                };
                handler.postDelayed(runnableCode, 1000);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        btnComposeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComposeTweetDialog();
            }
        });


    }
    private void showComposeTweetDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialog composeTweetDialog = new ComposeTweetDialog();
        composeTweetDialog.show(fm, "fragment_compose_tweet");
    }

    // Send an API request to get the timeline json
    // fill the listview as well by creating the tweet object from json
    private void populateTimeline(long lastUid) {
        client.getHomeTimeLine( lastUid, new JsonHttpResponseHandler() {
         // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                // JSON HERE
                // DESERIALIZE JSON
                // CREATE MODELS
                // LOAD MODEL DATA INTO LISTVIEW
                aTweets.addAll(Tweet.fromJsonArray(response));
                aTweets.notifyDataSetChanged();
                Log.d("DEBUG", aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
