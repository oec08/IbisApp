package com.codepath.apps.ibisapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.TwitterAppApplication;
import com.codepath.apps.ibisapp.TwitterClient;
import com.codepath.apps.ibisapp.activities.TimelineActivity;
import com.codepath.apps.ibisapp.activities.TweetDetailViewActivity;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by ocarty on 11/1/2016.
 */

public class HomeTimelineFragment extends TweetsListFragment {
    public TwitterClient client;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    private Unbinder unbinder;

    public static HomeTimelineFragment newInstance(Tweet tweet) {
        HomeTimelineFragment homeTimelineFragment = new HomeTimelineFragment();
        Bundle args = new Bundle();
        args.putParcelable("tweet", Parcels.wrap(tweet));
        homeTimelineFragment.setArguments(args);
        return homeTimelineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterAppApplication.getRestClient(); // Singleton client
        populateTimeline(1, false);
        Tweet tweet  = getArguments().getParcelable("tweet");
        if(tweet != null) {
            postNewTweet(tweet);
        }
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
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                        long currentMin = tweets.get(tweets.size() - 1).getUid();
                        if(currentMin > 0) {
                            currentMin = currentMin - 1;
                        }
                        populateTimeline(currentMin, false);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        btnComposeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComposeTweetDialog();
            }
        });
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), TweetDetailViewActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweets.get(position)));
                startActivity(intent);
            }
        });
        return v;
    }
    private void showComposeTweetDialog() {
        FragmentManager fm = getFragmentManager();
        ComposeTweetDialog composeTweetDialog = new ComposeTweetDialog();
        composeTweetDialog.show(fm, "fragment_compose_tweet");
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lvTweets.setAdapter(aTweets);
    }


    // Send an API request to get the timeline json
    // fill the listview as well by creating the tweet object from json
    private void populateTimeline(long lastUid, final boolean isSwipeToRefresh) {
        client.getHomeTimeLine( lastUid, isSwipeToRefresh,  new JsonHttpResponseHandler() {
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
                    aTweets.notifyDataSetChanged();
                }
                else {
                    addAll(Tweet.fromJsonArray(response));
               }
               Log.d("DEBUG", aTweets.toString());
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
