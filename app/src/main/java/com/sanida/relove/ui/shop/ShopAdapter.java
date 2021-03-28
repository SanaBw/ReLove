package com.sanida.relove.ui.shop;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sanida.relove.MainActivity;
import com.sanida.relove.R;
import com.sanida.relove.models.DatabaseHelper;
import com.sanida.relove.models.Shop;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.sanida.relove.MainActivity.cartImage;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder>{

    private ArrayList<Shop> shops;
    private LayoutInflater layoutInflater;
    public Context context;
    private StorageReference storageReference;
    public static Boolean shopItemIsShowing;
    public static Dialog dialog;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Shop shop;

        private TextView shopPrice, shopName;
        private ImageView shopImage;


        public ViewHolder(View itemView) {
            super(itemView);

            shopPrice=itemView.findViewById(R.id.shop_price);
            shopName=itemView.findViewById(R.id.shop_name);
            shopImage=itemView.findViewById(R.id.shop_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMyDialog(context, shopImage, shop);
                }
            });
        }
    }

    public ShopAdapter( Context context, ArrayList<Shop> shops) {
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        this.context = context;
        this.shops = shops;
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {
        Shop shop = shops.get(position);


        holder.shop=shop;
        holder.shopName.setText(shop.getName());
        holder.shopPrice.setText(shop.getPrice());
        holder.shopName.setTextAppearance( R.style.Timeless);
        holder.shopPrice.setTextAppearance( R.style.Timeless);

        Glide.with(context)
                .load(storageReference.child(shop.getImgPath()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .into(holder.shopImage);

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    private void showMyDialog(Context context, ImageView image, Shop shop) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.shop_item);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        TextView itemName = dialog.findViewById(R.id.item_name);
        TextView itemDescription = dialog.findViewById(R.id.item_description);
        ImageView itemImage = dialog.findViewById(R.id.item_image);
        Button addToCart = dialog.findViewById(R.id.add_btn);

        itemName.setText(shop.getName()+ ": "+shop.getPrice());
        itemDescription.setText(shop.getDescription());
        itemImage.setImageDrawable(image.getDrawable());


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                Boolean existsInDb=false;
                ArrayList<Shop> items = databaseHelper.getItems();
                for (Shop s: items) {
                    if (s.getName().equals(shop.getName())){
                        existsInDb=true;
                        Toast.makeText(context, "You already added this item!", Toast.LENGTH_LONG).show();
                    }
                }

                if (existsInDb==false){
                    boolean success = databaseHelper.addToCart(shop);

                    if (success){
                        Toast.makeText(context, "Added to cart!", Toast.LENGTH_LONG).show();
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
                        cartImage.startAnimation(animation);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cartImage.clearAnimation();
                            }
                        }, 1000);
                    } else
                    {
                        Toast.makeText(context, "Error. Try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }


}
