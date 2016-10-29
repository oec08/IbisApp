package com.codepath.apps.ibisapp.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.ibisapp.R;

/**
 * Created by ocarty on 10/29/2016.
 */

public class ComposeTweetDialog extends AppCompatDialogFragment {
    private EditText mEditText;
    private TextView tvTweetLength;
    private Button btnTweet;

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
}
