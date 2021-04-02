package com.sanida.relove;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sanida.relove.models.DatabaseHelper;
import com.sanida.relove.models.Shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public NavHostFragment navHostFragment;
    public static NavController navCo;
    public static ImageView cartImage;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        cartImage = findViewById(R.id.cart_image_main);

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navCo = navHostFragment.getNavController();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.navigation_home):{
                        navCo.navigate(R.id.navigation_home);
                        break;
                    }
                    case (R.id.navigation_chat):{
                        navCo.navigate(R.id.navigation_chat);
                        break;
                    }
                    case (R.id.navigation_outfits):{
                        navCo.navigate(R.id.navigation_outfits);
                        break;
                    }
                    case (R.id.navigation_shop):{
                        navCo.navigate(R.id.navigation_shop);
                        break;
                    }

                }

                return true;
            }
        });

        //Firebase
        //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Toolbar modification
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                ArrayList<Shop> items = databaseHelper.getItems();

                OpenCart(items);

                databaseHelper.close();
            }


        });
    }

    private void OpenCart(ArrayList<Shop> items){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cart);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        TextView cartText = dialog.findViewById(R.id.cart_text);
        Button checkout = dialog.findViewById(R.id.checkout_btn);
        TextView cartTitle = dialog.findViewById(R.id.cart_title);
        cartTitle.setTextAppearance( R.style.RetroholicFont);

        String cartString = "";

        for (Shop s:items) {
            cartString += s.getName() + ": " + s.getPrice()+"\n\n";
        }

        if (cartString.equals("")){
            cartString = "Your cart is empty";
        }
            cartText.setText(cartString);


    }

}