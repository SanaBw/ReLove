package com.sanida.relove.ui.outfits;

import android.app.Dialog;
import android.content.Context;
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
import com.sanida.relove.R;
import com.sanida.relove.models.DatabaseHelper;
import com.sanida.relove.models.Outfit;
import com.sanida.relove.models.Shop;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.sanida.relove.MainActivity.cartImage;

public class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.ViewHolder>{

    private ArrayList<Outfit> outfits;
    private LayoutInflater layoutInflater;
    public Context context;
    private StorageReference storageReference;
    public static Boolean shopItemIsShowing;
    public static Dialog dialog;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Outfit outfit;

        private TextView outfitPrice, outfitName;
        private ImageView outfitImage;


        public ViewHolder(View itemView) {
            super(itemView);

            outfitPrice=itemView.findViewById(R.id.shop_price);
            outfitName=itemView.findViewById(R.id.shop_name);
            outfitImage=itemView.findViewById(R.id.shop_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMyDialog(context, outfitImage, outfit);
                }
            });
        }
    }

    public OutfitAdapter(Context context, ArrayList<Outfit> outfits) {
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        this.context = context;
        this.outfits = outfits;
    }

    @NonNull
    @Override
    public OutfitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitAdapter.ViewHolder holder, int position) {
        Outfit outfit = outfits.get(position);


        holder.outfit=outfit;
        holder.outfitName.setText(outfit.getName());
        holder.outfitPrice.setText(outfit.getPrice());
        holder.outfitName.setTextAppearance( R.style.Timeless);
        holder.outfitPrice.setTextAppearance( R.style.Timeless);

        Glide.with(context)
                .load(storageReference.child(outfit.getImgPath()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                .into(holder.outfitImage);

    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    private void showMyDialog(Context context, ImageView image, Outfit outfit) {
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

        itemName.setText(outfit.getName()+ ": "+outfit.getPrice());
        itemDescription.setText(outfit.getDescription());
        itemImage.setImageDrawable(image.getDrawable());


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                Boolean existsInDb=false;
                ArrayList<Shop> items = databaseHelper.getItems();
                for (Shop s: items) {
                    if (s.getName().equals(outfit.getName())){
                        existsInDb=true;
                        Toast.makeText(context, "You already added this item!", Toast.LENGTH_LONG).show();
                    }
                }

                if (existsInDb==false){
                    boolean success = databaseHelper.addToCart(outfit);

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
