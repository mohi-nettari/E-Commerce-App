package com.example.ecommerce;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerce.Model.product;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.ViewModel.AuthViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class AdminCategoryActivity extends AppCompatActivity {

    TextView addC;
    ImageView tshirts,sports,female_d,speakers;
    ImageView glasses,chargers,hats,shoess;
    ImageView head_phoness,phoneex,laptops,mobiles,back;
    private AuthViewModel viewModel;
    Button checkOrders,logout,maintainbtn;
    String admin = "Admin";

    @Override
    protected void onStart() {
        super.onStart();
        //checking for users existing : saving users
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            Intent i = new Intent(AdminCategoryActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

    }
////On key Down Events
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // moveTaskToBack(true);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
//
//        //initialize the widgets
        checkOrders = findViewById(R.id.checkorders);
        logout = findViewById(R.id.logout1);
        maintainbtn = findViewById(R.id.maintainadmin);

        tshirts = findViewById(R.id.tshirts);
        sports = findViewById(R.id.sports);
        female_d = findViewById(R.id.female_dress);

        chargers = findViewById(R.id.chargers);
        speakers = findViewById(R.id.speakers);
        phoneex = findViewById(R.id.phoneex);
//        sweather = findViewById(R.id.sweather);
        glasses = findViewById(R.id.glasses);
//        purses_bags = findViewById(R.id.purses_bags);
        hats = findViewById(R.id.hats);
        shoess = findViewById(R.id.shoes);
        head_phoness = findViewById(R.id.headphone);
//        watches = findViewById(R.id.watches);
        laptops = findViewById(R.id.laptops);
        mobiles = findViewById(R.id.mobiles);

        back = findViewById(R.id.backadmincat);


               // getSupportActionBar().hide();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        //preparing the ViewModel
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(AuthViewModel.class);


        maintainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(AdminCategoryActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.signOut();
            }
        });
//
        //Events for the widgets (catergory images)

    tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Sports");
                startActivity(intent);
            }
        });
//
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Sports");
                startActivity(intent);
            }
        });

//
        female_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","female_dresses");
                startActivity(intent);
            }
        });
//
//
//        //Events for the widgets (catergory images)
//
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });
//
//

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Hats");
                startActivity(intent);
            }
        });
//
//
        shoess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Shoess");
                startActivity(intent);
            }
        });
//
//        //Events for the widgets (catergory images)
//
        head_phoness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Head Phoness");
                startActivity(intent);
            }
        });
////
//
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });

//
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Mobiles");
                startActivity(intent);
            }
        });

        chargers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","chargers");
                startActivity(intent);
            }
        });

        speakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Speakers");
                startActivity(intent);
            }
        });

        phoneex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category","Phone exc");
                startActivity(intent);
            }
        });
        //check orders button
        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent);
                finish();

            }
        });

     //   addC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
//                startActivity(i);
//            }
//        });

//        purses_bags.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
//                intent.putExtra("category","Purses Bags");
//                startActivity(intent);
//            }
//        });
//
////        watches.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
////                intent.putExtra("category","Watches");
////                startActivity(intent);
////            }
////        });
//
////
//        sweather.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
//                intent.putExtra("category","Sweather");
//                startActivity(intent);
//            }
//        });

//
    }


}