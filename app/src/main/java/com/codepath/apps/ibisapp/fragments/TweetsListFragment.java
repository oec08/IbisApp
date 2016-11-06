package com.codepath.apps.ibisapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.adapters.TweetsArrayAdapter;
import com.codepath.apps.ibisapp.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ocarty on 11/1/2016.
 */

public class TweetsListFragment extends Fragment {
    TweetsArrayAdapter aTweets;
    ArrayList<Tweet> tweets;
    @BindView(R.id.lvTweets) ListView lvTweets;
    @BindView(R.id.fabComposeTweet) FloatingActionButton btnComposeTweet;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    private Unbinder unbinder;
    private static final int REQUEST_CODE = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweetList) {
        aTweets.addAll(tweetList);
        aTweets.notifyDataSetChanged();
    }
}
