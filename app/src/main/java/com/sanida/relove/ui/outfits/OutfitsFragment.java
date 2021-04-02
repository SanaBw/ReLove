package com.sanida.relove.ui.outfits;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sanida.relove.R;
import com.sanida.relove.models.Outfit;
import com.sanida.relove.models.Shop;
import com.sanida.relove.ui.shop.ShopAdapter;

import java.util.ArrayList;

public class OutfitsFragment extends Fragment {

    private RecyclerView outfitRecyclerView;
    private OutfitAdapter outfitAdapter;
    private ArrayList<Outfit> outfits;
    private Context context;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_outfits, container, false);

        outfits = new ArrayList<>();
        context=this.getContext();
        ProgressBar progressBar=root.findViewById(R.id.progress_bar3);
        progressBar.setProgress(50,true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        outfitRecyclerView=root.findViewById(R.id.outfit_view);

        databaseReference.child("outfits").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Outfit outfit = new Outfit();
                        outfit.setName(postSnapShot.child("name").getValue().toString());
                        outfit.setDescription(postSnapShot.child("description").getValue().toString());
                        outfit.setPrice(postSnapShot.child("price").getValue().toString());
                        outfit.setImgPath(postSnapShot.child("imgpath").getValue().toString());

                        outfits.add(outfit);
                    }
                }
                progressBar.setProgress(100,true);
                outfitAdapter = new OutfitAdapter(context, outfits);
                outfitRecyclerView.setAdapter(outfitAdapter);
                outfitAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);

                outfitRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Canceled");
            }
        });


        return root;

    }
}