package com.codepath.apps.ibisapp.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.TwitterAppApplication;
import com.codepath.apps.ibisapp.TwitterClient;
import com.codepath.apps.ibisapp.adapters.TweetsArrayAdapter;
import com.codepath.apps.ibisapp.fragments.ComposeTweetDialog;
import com.codepath.apps.ibisapp.fragments.HomeTimelineFragment;
import com.codepath.apps.ibisapp.fragments.MentionsTimelineFragment;
import com.codepath.apps.ibisapp.fragments.TweetsListFragment;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    public TwitterClient client;
//    private TweetsArrayAdapter aTweets;
//    private ArrayList<Tweet> tweets;
    @Nullable @BindView(R.id.lvTweets) ListView lvTweets;
    @Nullable @BindView(R.id.fabComposeTweet) FloatingActionButton btnComposeTweet;
    @Nullable @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    private static final int REQUEST_CODE = 10;
    private TweetsListFragment fragmentTweetsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        ViewPager vpPager = (ViewPager)findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);

        tabStrip.setViewPager(vpPager);
        // ButterKnife.bind(this);
//        client = TwitterAppApplication.getRestClient();
//        if(savedInstanceState == null) {
//            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
//        }
        // Create the arraylist from data source
//        tweets = new ArrayList<>();
//        aTweets = new TweetsArrayAdapter(this, tweets);
//        lvTweets.setAdapter(aTweets);
//        populateTimeline(1, false);
        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.d("DEBUG", tweets.toString());
//                if(tweets.size() > 0) {
//                    populateTimeline(tweets.get(0).getUid(), true);
//                }
//            }
//        });
//        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
//                getResources().getColor(android.R.color.holo_green_dark),
//                getResources().getColor(android.R.color.holo_orange_light),
//                getResources().getColor(android.R.color.holo_red_light));

//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to your AdapterView
//                Handler handler = new Handler();
//                Runnable runnableCode = new Runnable() {
//                    @Override
//                    public void run() {
//                        // Do something here on the main thread
//                        Log.d("Waiting a second", "Called on main thread");
//                        long currentMin = tweets.get(tweets.size() - 1).getUid();
//                        if(currentMin > 0) {
//                            currentMin = currentMin - 1;
//                        }
//                        populateTimeline(currentMin, false);
//                    }
//                };
//                handler.postDelayed(runnableCode, 1000);
//                return true; // ONLY if more data is actually being loaded; false otherwise.
//            }
//        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // REQUEST_CODE is defined above
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//            Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
//            aTweets.insert(tweet, 0);
//            aTweets.notifyDataSetChanged();
//
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem menuItem) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            }
            else if(position == 1) {
                return new MentionsTimelineFragment();
            }
            else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
