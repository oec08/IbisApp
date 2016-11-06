package com.codepath.apps.ibisapp.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by ocarty on 10/28/2016.
 */
@Parcel
public class User {
    // List attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    private int followersCount;
    private int followingCount;


    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    // Deserialize the user json <=> User
    public static User fromJSONObject(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.tagline = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
