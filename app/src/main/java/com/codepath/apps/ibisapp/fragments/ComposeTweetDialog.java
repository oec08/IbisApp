package com.codepath.apps.ibisapp.fragments;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import com.codepath.apps.ibisapp.TwitterAppApplication;
import com.codepath.apps.ibisapp.TwitterClient;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.models.User;
import com.codepath.apps.ibisapp.utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ocarty on 10/29/2016.
 */

public class ComposeTweetDialog extends AppCompatDialogFragment {
    private EditText mEditText;
    private TextView tvName;
    private TextView tvUsername;
    private ImageView ivProfileImage;
    private TextView tvTweetLength;
    private Button btnTweet;
    public TwitterClient client;

    public ComposeTweetDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        mEditText = (EditText) view.findViewById(R.id.etComposeTweet);
        btnTweet = (Button)view.findViewById(R.id.btnTweet);
        tvTweetLength = (TextView)view.findViewById(R.id.tvTweetLength);
        tvName = (TextView)view.findViewById(R.id.tvName);
        tvUsername = (TextView)view.findViewById(R.id.tvUsername);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        setupViews();
        getDialog().setTitle("Compose Tweet");

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String length = 140 - s.toString().length() + "/140";
                tvTweetLength.setText(length);
            }
        });

        return view;
    }
    private void setupViews() {
        client = TwitterAppApplication.getRestClient();

        client.getAccountDetails(new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                tvName.setText(User.fromJSONObject(response).getName());
                tvUsername.setText(StringUtils.addHashtagToString(User.fromJSONObject(response).getScreenName()));
                Picasso.with(getContext()).load(User.fromJSONObject(response).getProfileImageUrl()).into(ivProfileImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

}
