package com.codepath.apps.ibisapp.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.TwitterClient;
import com.codepath.apps.ibisapp.fragments.HomeTimelineFragment;
import com.codepath.apps.ibisapp.fragments.MentionsTimelineFragment;
import com.codepath.apps.ibisapp.adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.ibisapp.models.Tweet;
import org.parceler.Parcels;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {
    public TwitterClient client;
    @Nullable @BindView(R.id.lvTweets) ListView lvTweets;
    @Nullable @BindView(R.id.fabComposeTweet) FloatingActionButton btnComposeTweet;
    @Nullable @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @BindView(R.id.viewpager) ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        vpPager = (ViewPager)findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem menuItem) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
                return  HomeTimelineFragment.newInstance(tweet);
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
