package com.codepath.apps.ibisapp.activities;

import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.utils.TimeFormatter;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetDetailViewActivity extends AppCompatActivity {
    @BindView(R.id.ivProfileImageDetail) ImageView ivProfileImageDetail;
    @BindView(R.id.tvNameDetail) TextView tvNameDetail;
    @BindView(R.id.tvBodyDetail) TextView tvBodyDetail;
    @BindView(R.id.tvUserNameDetail) TextView tvUserNameDetail;
    @BindView(R.id.tvTimeAgoDetail) TextView tvTimeAgoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail_view);
        ButterKnife.bind(this);
        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvNameDetail.setText(tweet.getUser().getName());
        tvBodyDetail.setText(tweet.getBody());
        tvUserNameDetail.setText(tweet.getUser().getScreenName());
        tvTimeAgoDetail.setText(TimeFormatter.getDuration(tweet.getCreatedAt(), TimeFormatter.TWITTER_FORMAT));
        Glide.with(getApplicationContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImageDetail);
    }
}
