package com.codepath.apps.ibisapp.models;

import android.os.Build;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by ocarty on 10/28/2016.
 */

/*

 */
// Parse the JSON + Store the data, encapulate state logic or display logic
@Parcel
public class Tweet {
    public String getBody() {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(body, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            return Html.fromHtml(body).toString();
        }

    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public Entity getEntity() {
        return entity;
    }

    public String getCreatedAt() {

        return createdAt;
    }

    // List out the attributes
    String body;
    long uid; // unique id for the tweet
    User user;
    Entity entity;
    String createdAt;

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        // Extract the values from json, store them
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSONObject(jsonObject.getJSONObject("user"));
            tweet.entity = Entity.fromJSONObject(jsonObject.getJSONObject("entities"));
        } catch (JSONException e) {
            e.printStackTrace();
        }





        return tweet;
    }
    // Tweet.fromJson
    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}
