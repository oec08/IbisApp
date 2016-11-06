package com.codepath.apps.ibisapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.TwitterAppApplication;
import com.codepath.apps.ibisapp.TwitterClient;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by ocarty on 11/1/2016.
 */

public class UserTimelineFragment extends TweetsListFragment {
    public TwitterClient client;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterAppApplication.getRestClient(); // Singleton client
        populateTimeline(1, false);
    }
    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        unbinder = ButterKnife.bind(this, v);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("DEBUG", tweets.toString());
                if(tweets.size() > 0) {
                    populateTimeline(tweets.get(0).getUid(), true);
                }
            }
        });
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                Handler handler = new Handler();
                Runnable runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        // Do something here on the main thread
                        Log.d("Waiting a second", "Called on main thread");
                        long currentMin = tweets.get(tweets.size() - 1).getUid();
                        if(currentMin > 0) {
                            currentMin = currentMin - 1;
                        }
                        populateTimeline(currentMin, false);
                    }
                };
                handler.postDelayed(runnableCode, 1000);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        return v;
    }

    // Send an API request to get the timeline json
    // fill the listview as well by creating the tweet object from json
    private void populateTimeline(long lastUid, final boolean isSwipeToRefresh) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(lastUid, isSwipeToRefresh, screenName, new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                if(!aTweets.isEmpty() && isSwipeToRefresh) {
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject tweetJson = response.getJSONObject(i);
                            aTweets.insert(Tweet.fromJSON(tweetJson), i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
                else {
                addAll(Tweet.fromJsonArray(response));
                }
                aTweets.notifyDataSetChanged();
                Log.d("DEBUG", aTweets.toString());
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle   savedInstanceState) {
        lvTweets.setAdapter(aTweets);
    }
}
