package com.sanida.relove.ui.shop;

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
import com.sanida.relove.models.Shop;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    private RecyclerView shopRecyclerView;
    private ShopAdapter shopAdapter;
    private ArrayList<Shop> shops;
    private Context context;
    DatabaseReference databaseReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop, container, false);

        shops = new ArrayList<>();
        context=this.getContext();
        ProgressBar progressBar=root.findViewById(R.id.progress_bar2);
        progressBar.setProgress(50,true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        shopRecyclerView=root.findViewById(R.id.outfit_view);

        databaseReference.child("shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Shop shop = new Shop();
                        shop.setName(postSnapShot.child("name").getValue().toString());
                        shop.setDescription(postSnapShot.child("description").getValue().toString());
                        shop.setPrice(postSnapShot.child("price").getValue().toString());
                        shop.setImgPath(postSnapShot.child("imgpath").getValue().toString());

                        shops.add(shop);
                    }
                }
                progressBar.setProgress(100,true);
                shopAdapter = new ShopAdapter(context, shops);
                shopRecyclerView.setAdapter(shopAdapter);
                shopAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);

                shopRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Canceled");
            }
        });


        return root;
    }
}