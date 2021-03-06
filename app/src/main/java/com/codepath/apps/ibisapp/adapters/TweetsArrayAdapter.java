package com.codepath.apps.ibisapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.activities.ProfileActivity;
import com.codepath.apps.ibisapp.fragments.ComposeTweetDialog;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.utils.PatternEditableBuilder;
import com.codepath.apps.ibisapp.utils.StringUtils;
import com.codepath.apps.ibisapp.utils.TimeFormatter;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;
import java.util.regex.Pattern;

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
        ImageView tvMediaImage;
        ImageButton replyButton;
        TextView tvUserName;
        TextView tvName;
        TextView tvBody;
        TextView tvTimeAgo;
    }

    // Override and setup custom template
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
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
            viewHolder.tvMediaImage = (ImageView)convertView.findViewById(R.id.ivImage);
            viewHolder.replyButton = (ImageButton)convertView.findViewById(R.id.ibReplyBtn);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTimeAgo.setText(TimeFormatter.getDuration(tweet.getCreatedAt(), TimeFormatter.TWITTER_FORMAT));
        viewHolder.tvName.setText(tweet.getUser().getName());
        String userNameWithHashtag = StringUtils.addHashtagToString(tweet.getUser().getScreenName());
        viewHolder.tvUserName.setText(userNameWithHashtag);
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.tvProfileImage);
        if(tweet.getEntity().getMediaUrl()!= null) {
            Glide.with(getContext()).load(tweet.getEntity().getMediaUrl()).into(viewHolder.tvMediaImage);
            viewHolder.tvMediaImage.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.tvMediaImage.setVisibility(View.GONE);
        }

        viewHolder.tvProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(tweet.getUser()));
                intent.putExtra("screen_name", tweet.getUser().getScreenName());
                getContext().startActivity(intent);
            }
        });

        viewHolder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fm = FragmentManager();
//                ComposeTweetDialog composeTweetDialog = new ComposeTweetDialog();
//                composeTweetDialog.show(fm, "fragment_compose_tweet");
            }
        });
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent intent = new Intent(getContext(), ProfileActivity.class);
                                intent.putExtra("hashtag_user", text);
                                getContext().startActivity(intent);
                            }
                        }).into(viewHolder.tvBody);

        return convertView;
    }
}
