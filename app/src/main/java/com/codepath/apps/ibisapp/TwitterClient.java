package com.codepath.apps.ibisapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 *
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "2yc8SOx24HMcUb9ngDCyT84XL";
	public static final String REST_CONSUMER_SECRET = "fqtkm1diM3A23opx8D032kmbw0l73GXJCadSXbzahDJN0qHEh8";
	public static final String REST_CALLBACK_URL = "oauth://cpibisapp";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeLine(long lastUid,boolean isSwipeToRefresh,  AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if(lastUid == 1) {
			params.put("since_id", lastUid);
		}
		else if(isSwipeToRefresh) {
			params.put("since_id", lastUid);
		}
		else {
			params.put("max_id", lastUid);
		}
		// Execute the request
		getClient().get(apiUrl, params, handler);
	}

	public void getAccountDetails(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, handler);
	}

	public void getUserByScreenName(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("screen_name",screenName);
		getClient().get(apiUrl, params, handler);
	}

	public void doComposeTweet(String composedTweet, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", composedTweet);
		getClient().post(apiUrl, params, handler);
	}

	public void getMentionsTimeline(long lastUid, boolean isSwipeToRefresh, JsonHttpResponseHandler jsonHttpResponseHandler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if(lastUid == 1) {
			params.put("since_id", lastUid);
		}
		else if(isSwipeToRefresh) {
			params.put("since_id", lastUid);
		}
		else {
			params.put("max_id", lastUid);
		}
		// Execute the request
		getClient().get(apiUrl, params, jsonHttpResponseHandler);
	}

	public void getUserTimeline(long lastUid, boolean isSwipeToRefresh, String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if(lastUid == 1) {
			params.put("since_id", lastUid);
		}
		else if(isSwipeToRefresh) {
			params.put("since_id", lastUid);
		}
		else {
			params.put("max_id", lastUid);
		}
		params.put("screen_name", screenName);
		// Execute the request
		getClient().get(apiUrl, params, handler);

	}

	// Compose tweet

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
