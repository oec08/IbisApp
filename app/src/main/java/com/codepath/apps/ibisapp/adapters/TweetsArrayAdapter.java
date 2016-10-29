package com.codepath.apps.ibisapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.utils.TimeFormatter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ocarty on 10/28/2016.
 */
// Taking the tweet objects and turning them into Views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,0, tweets);
    }

    // Override and setup custom template
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView tvProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvBody = (TextView)convertView.findViewById(R.id.tvBody);
        TextView tvTimeAgo = (TextView)convertView.findViewById(R.id.tvTimeAgo);


        tvTimeAgo.setText(TimeFormatter.getDuration(tweet.getCreatedAt(), TimeFormatter.TWITTER_FORMAT));
        tvName.setText(tweet.getUser().getName());
        String userNameWithHashtag = "@" + tweet.getUser().getScreenName();
        tvUserName.setText(userNameWithHashtag);
        tvBody.setText(tweet.getBody());
        tvProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(tvProfileImage);

        return convertView;
    }
}
