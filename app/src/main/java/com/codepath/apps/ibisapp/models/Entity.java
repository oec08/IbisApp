package com.codepath.apps.ibisapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by ocarty on 11/6/2016.
 */
@Parcel
public class Entity {

    String mediaUrl;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public static Entity fromJSONObject(JSONObject jsonObject) {
        Entity entity = new Entity();
        try {
           JSONArray media = jsonObject.optJSONArray("media");
            if(media != null) {
                    for(int i = 0; i < media.length(); i++) {
                        JSONObject mediaObject = (JSONObject) media.get(i);
                        String type = mediaObject.getString("type");
                        if(type.equalsIgnoreCase("photo")) {
                            entity.mediaUrl = mediaObject.getString("media_url");
                            break;
                        }
                    }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }
}
