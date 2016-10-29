package com.codepath.apps.ibisapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

    private static class ViewHolder {
        ImageView tvProfileImage;
        TextView tvUserName;
        TextView tvName;
        TextView tvBody;
        TextView tvTimeAgo;
    }

    // Override and setup custom template
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            viewHolder.tvProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimeAgo = (TextView)convertView.findViewById(R.id.tvTimeAgo);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTimeAgo.setText(TimeFormatter.getDuration(tweet.getCreatedAt(), TimeFormatter.TWITTER_FORMAT));
        viewHolder.tvName.setText(tweet.getUser().getName());
        String userNameWithHashtag = "@" + tweet.getUser().getScreenName();
        viewHolder.tvUserName.setText(userNameWithHashtag);
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.tvProfileImage);

        return convertView;
    }
}
