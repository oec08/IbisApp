package com.codepath.apps.ibisapp.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codepath.apps.ibisapp.R;

/**
 * Created by ocarty on 10/29/2016.
 */

public class ComposeTweetDialog extends AppCompatDialogFragment {
    private EditText mEditText;

    public ComposeTweetDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        mEditText = (EditText) view.findViewById(R.id.etComposeTweet);
        getDialog().setTitle("Hello");

        return view;
    }
}
