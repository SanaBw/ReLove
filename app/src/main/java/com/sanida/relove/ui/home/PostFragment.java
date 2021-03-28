package com.sanida.relove.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanida.relove.R;


public class PostFragment extends Fragment {
    String title ,text;
    TextView FragmentPostText,FragmentPostTitle;


    public PostFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.title = bundle.getString("title");
            this.text = bundle.getString("text");
        }

        FragmentPostText=view.findViewById(R.id.fragment_post_text);
        FragmentPostTitle=view.findViewById(R.id.fragment_post_title);

        FragmentPostTitle.setTextAppearance( R.style.Timeless);
        FragmentPostText.setTextAppearance( R.style.Timeless);

        FragmentPostText.setText(text);
        FragmentPostTitle.setText(title);

        return view;
    }
}