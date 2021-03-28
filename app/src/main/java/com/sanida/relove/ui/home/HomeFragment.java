package com.sanida.relove.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sanida.relove.R;
import com.sanida.relove.models.Post;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView homeRecyclerView;
    private PostAdapter homePostAdapter;
    private ArrayList<Post> posts;
    LinearLayoutManager llm;
    private Context context;
    DatabaseReference databaseReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        llm = new LinearLayoutManager(getContext());
        posts = new ArrayList<>();
        context=this.getContext();
        ProgressBar progressBar=root.findViewById(R.id.progress_bar);
        progressBar.setProgress(50,true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        homeRecyclerView=root.findViewById(R.id.post_view);



        databaseReference.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Post post = new Post();
                        post.setTitle(postSnapShot.child("title").getValue().toString());
                        post.setText(postSnapShot.child("text").getValue().toString());
                        post.setImgPath(postSnapShot.child("imgpath").getValue().toString());
                        posts.add(post);



                    }
                }
                progressBar.setProgress(100,true);
                homePostAdapter = new PostAdapter(context, posts);
                homeRecyclerView.setLayoutManager(llm);
                homeRecyclerView.setAdapter(homePostAdapter);
                homePostAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Canceled");
            }
        });






        return root;
    }


}



