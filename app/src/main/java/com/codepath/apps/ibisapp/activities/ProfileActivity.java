package com.codepath.apps.ibisapp.activities;

import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    UserTimelineFragment fragmentUserTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterAppApplication.getRestClient();
        final User userPage = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        String hashtagUser = getIntent().getStringExtra("hashtag_user");

        if(userPage != null) {
            getSupportActionBar().setTitle(StringUtils.addHashtagToString(userPage.getScreenName()));
            populateProfileHeader(userPage);
        }
        else if(hashtagUser != null) {
            client.getUserTimeline(1, false, hashtagUser, new JsonHttpResponseHandler() {
                // Success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    for(int i =0; i < response.length(); i++) {
                        try {
                            if(response.get(i).equals("user")) {
                                user = User.fromJSONObject(response.getJSONObject(i));
                                getSupportActionBar().setTitle(StringUtils.addHashtagToString(user.getScreenName()));
                                populateProfileHeader(user);
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
        else {
            client.getAccountDetails(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSONObject(response);
                    getSupportActionBar().setTitle(StringUtils.addHashtagToString(user.getScreenName()));
                    populateProfileHeader(user);
                    fragmentUserTimeline = UserTimelineFragment.newInstance(user.getScreenName());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, fragmentUserTimeline);
                    ft.commit();
                }
            });
        }


        // Get screen name
        String screenName = getIntent().getStringExtra("screen_name");

        if(hashtagUser != null) {
            hashtagUser = hashtagUser.substring(1);
            fragmentUserTimeline = UserTimelineFragment.newInstance(hashtagUser);
            client.getUserByScreenName(hashtagUser, new JsonHttpResponseHandler() {
                // Success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        user = User.fromJSONObject(response);
                        getSupportActionBar().setTitle(StringUtils.addHashtagToString(user.getScreenName()));
                        populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
        else if(screenName!= null){
            fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }

        // Display user fragment within this activity
        if(fragmentUserTimeline != null ) {
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
