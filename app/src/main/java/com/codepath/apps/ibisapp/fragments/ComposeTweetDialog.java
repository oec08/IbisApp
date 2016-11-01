package com.codepath.apps.ibisapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.codepath.apps.ibisapp.TwitterAppApplication;
import com.codepath.apps.ibisapp.TwitterClient;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.ibisapp.R;
import com.codepath.apps.ibisapp.models.Tweet;
import com.codepath.apps.ibisapp.models.User;
import com.codepath.apps.ibisapp.utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ocarty on 10/29/2016.
 */

public class ComposeTweetDialog extends AppCompatDialogFragment implements TextView.OnEditorActionListener {
    private EditText mEditText;
    private TextView tvName;
    private TextView tvUsername;
    private ImageView ivProfileImage;
    private TextView tvTweetLength;
    private Button btnTweet;
    private TwitterClient client;
    private ImageButton btnCloseDialog;

    public ComposeTweetDialog() {
        // Empty constructor required for DialogFragment
    }
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
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
        btnCloseDialog = (ImageButton)view.findViewById(R.id.btnCloseDialog);
        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mEditText.setOnEditorActionListener(this);

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        setupViews();
        getDialog().setTitle("Compose Tweet");

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();
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
                int charactersLeft = 140 - s.toString().length();
                if(charactersLeft < 0) {
                    btnTweet.setEnabled(false);
                    tvTweetLength.setTextColor(Color.RED);
                }
                else {
                    btnTweet.setEnabled(true);
                    tvTweetLength.setTextColor(Color.BLACK);
                }
            }
        });

        return view;
    }
    private void sendTweet() {
        String composedTweet = mEditText.getText().toString();
        client.doComposeTweet(composedTweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Sending Tweet", response.toString());
                        Tweet tweetJson = Tweet.fromJSON(response);
                        Intent intent = new Intent();
                        intent.putExtra("tweet", Parcels.wrap(tweetJson));
                        getActivity().setResult(RESULT_OK, intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
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
                Glide.with(getContext()).load(User.fromJSONObject(response).getProfileImageUrl()).into(ivProfileImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();
            activity.onFinishEditDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
    }

