package com.codepath.apps.ibisapp.activities;

import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.TwitterAppApplication;
import com.codepath.apps.ibisapp.TwitterClient;
import com.codepath.apps.ibisapp.fragments.UserTimelineFragment;
import com.codepath.apps.ibisapp.models.User;
import com.codepath.apps.ibisapp.utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterAppApplication.getRestClient();
        User userPage = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        if(userPage != null) {
            getSupportActionBar().setTitle(StringUtils.addHashtagToString(userPage.getScreenName()));
            populateProfileHeader(userPage);
        }
        else {
            client.getAccountDetails(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSONObject(response);
                    getSupportActionBar().setTitle(StringUtils.addHashtagToString(user.getScreenName()));
                    populateProfileHeader(user);
                }
            });
        }
        // Get screen name
        String screenName = getIntent().getStringExtra("screen_name");

        UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
        // Display user fragment within this activity
        if(savedInstanceState == null ) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }

    }

    public void populateProfileHeader(User user) {
        TextView tvName = (TextView)findViewById(R.id.tvName);
        TextView tvTagLine = (TextView)findViewById(R.id.tvTagLine);
        TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Glide.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

    }
}
