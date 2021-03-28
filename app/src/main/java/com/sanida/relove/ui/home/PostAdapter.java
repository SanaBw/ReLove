package com.sanida.relove.ui.home;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sanida.relove.MainActivity;
import com.sanida.relove.R;
import com.sanida.relove.models.Post;
import com.sanida.relove.ui.outfits.OutfitsFragment;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private LayoutInflater layoutInflater;
    private Context context;
    private StorageReference storageReference;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Post post;

        private TextView postTitle;
        private ImageView postImage;


        public ViewHolder(View view) {
            super(view);

            postTitle=view.findViewById(R.id.post_title);
            postImage=view.findViewById(R.id.post_image);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Bundle bdl = new Bundle();
                    bdl.putString("title", post.getTitle());
                    bdl.putString("text", post.getText());
                    MainActivity.navCo.navigate(R.id.postFragment, bdl);
                }
            });

        }


    }

    public PostAdapter(Context context, ArrayList<Post> posts) {
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        this.context = context;
        this.posts = posts;
    }


    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        Post post = posts.get(position);


        holder.post=post;
        holder.postTitle.setText(post.getTitle());
        holder.postTitle.setTextAppearance( R.style.Timeless);


        Glide.with(context)
                .load(storageReference.child(post.getImgPath()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                .into(holder.postImage);

    }


    @Override
    public int getItemCount() {
        return posts.size();
    }



}



