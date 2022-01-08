package com.example.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.ecommerce.Model.User;
import com.example.ecommerce.Model.product;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.ViewModel.AuthViewModel;
import com.example.ecommerce.prevalent.prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
         implements NavigationView.OnNavigationItemSelectedListener  {

    private AuthViewModel viewModel;

    private DrawerLayout mNavD;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference pref;
    private RecyclerView rcv;
    LoadingDialog loadingDialog;
    private RecyclerView.LayoutManager layoutManager;
    private CircleImageView profilepic;
    View headerView;
    TextView username;
    User us;
    private String type ;
    StorageReference Pimagesref;

    //On key Down Events
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // moveTaskToBack(true);
            if (type == "admin"){
                Intent intent = new Intent(HomeActivity.this,AdminCategoryActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("done","start");
        loadingDialog.startLoadingDialog();
        pref = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
        FirebaseRecyclerOptions<product> options =
                new FirebaseRecyclerOptions.Builder<product>()
                .setQuery(pref,product.class).build();

        FirebaseRecyclerAdapter<product, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull product model) {
                        loadingDialog.dismisLoadingDialog();
                        holder.productname.setText("Name : " + model.getPname());
                        holder.productprice.setText("Price : " + model.getPprice()+" DA");
                      //  holder.productdesc.setText("Description : " +model.getPdesc());


                        Picasso.get()
                                .load(model.getPimage())
                                .into(holder.productimage);


                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                if(type=="admin"){
                                    new AlertDialog.Builder(HomeActivity.this)
                                            .setTitle("Delete")
                                            .setMessage("Are you sure want to delete this product?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {




                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    database.getReference().child("Products").child(model.getPid())
                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(HomeActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                                                                FirebaseStorage firebaseStorage;
                                                                firebaseStorage = FirebaseStorage.getInstance();
                                                                firebaseStorage.getReference().child("ProductImages")
                                                                        .child(model.getPid()+".jpg").delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){

                                                                            Toast.makeText(HomeActivity.this, "image deleted", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });

                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();

                                        }
                                    }).show();


                                    return false;
                                }else {
                                    Toast.makeText(HomeActivity.this, "nooot Lonnnnng", Toast.LENGTH_SHORT).show();

                                }
                                return true;

                            }
                        });



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(type=="admin"){
                                    Intent intent = new Intent(HomeActivity.this , AdminMaintainProductActivity.class);
                                    intent.putExtra("joji",model.getPprice());
                                    intent.putExtra("image", model.getPimage());
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("name", model.getPname());
                                    intent.putExtra("description" ,model.getPdesc());
                                    intent.putExtra("categ" ,model.getPcategory());
                                    startActivity(intent);
                                }else {

                                    Intent intent = new Intent(HomeActivity.this , ProductDetailsActivity.class);
                                    intent.putExtra("joji",model.getPprice());
                                    intent.putExtra("image", model.getPimage());
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("name", model.getPname());
                                    intent.putExtra("description" ,model.getPdesc());
                                    startActivity(intent);
                                }

//

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;

                    }
                };
rcv.setAdapter(adapter);
adapter.startListening();

//check if the user is an admin or no
        if (type=="admin"){
            Intent intent = new Intent(HomeActivity.this,AdminCategoryActivity.class);
            startActivity(intent);
            finish();
        }
        //checking for users existing : saving users
        if (firebaseUser == null) {
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("done","create");
        database = FirebaseDatabase.getInstance();

        //getting user type
        getuType();
        //getSupportActionBar().hide();

        loadingDialog = new LoadingDialog(HomeActivity.this);

        FirebaseAuth.getInstance().getCurrentUser().getUid();

        //preparing the widgets
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        Log.d("onc","done");
        database = FirebaseDatabase.getInstance();

        //profilepic = findViewById(R.id.profile_image_nav);
        pref = FirebaseDatabase.getInstance().getReference().child("Products");

        mNavD= findViewById(R.id.drawer_layout);
        NavigationView mnav = findViewById(R.id.nav_view);
        Log.d("onc","done");
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(type == "admin")) {
                    Intent i = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(HomeActivity.this, "Admin didn't has a cart.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView  navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mnav.setNavigationItemSelectedListener(this);
        View headerView = mnav.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username_nav);
        CircleImageView userimage = headerView.findViewById(R.id.profile_image_nav);
        Log.d("onc","done3");



       rcv = findViewById(R.id.rcv_menu);
       rcv.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);

        rcv.setLayoutManager(layoutManager);

        //preparing the ViewModel
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(AuthViewModel.class);

        if(!(type == "admin")) {

            FirebaseDatabase.getInstance().getReference().child("Users")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
                                User users = dataSnapshot.getValue(User.class);
                                users.setUserId(dataSnapshot.getKey());
                                Log.d("id ", users.getUserId().toString());
                                if((users.getUserId().
                                        equals(FirebaseAuth.getInstance().getUid()))){

                                    us = users;
                                    CircleImageView userimage = headerView.findViewById(R.id.profile_image_nav);

//                                if(us.getImage() != "" && us.getImage() != null ){
                                    Picasso.get()
                                            .load(us.getImage())
                                            .placeholder(R.drawable.profile)
                                            .into(userimage);
//                                }
//                                Glide.with(HomeActivity.this)
//                                        .load(us.getImage())
//                                        .into(profilepic);

                                    username.setText(us.getUserName());

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

        }else {
            FirebaseDatabase.getInstance().getReference().child("Admins")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
                                User users = dataSnapshot.getValue(User.class);
                                users.setUserId(dataSnapshot.getKey());
                                Log.d("id ", users.getUserId().toString());
                                if((users.getUserId().
                                        equals(FirebaseAuth.getInstance().getUid()))){

                                    us = users;
                                    CircleImageView userimage = headerView.findViewById(R.id.profile_image_nav);

//                                if(us.getImage() != "" && us.getImage() != null ){
                                    Picasso.get()
                                            .load(us.getImage())
                                            .placeholder(R.drawable.profile)
                                            .into(userimage);
//                                }
//                                Glide.with(HomeActivity.this)
//                                        .load(us.getImage())
//                                        .into(profilepic);

                                    username.setText(us.getUserName());

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_settings){
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:
                if(!(type == "admin")) {
                    Intent i = new Intent(HomeActivity.this,CartActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(HomeActivity.this, "The Admin didn't has a cart.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_search:
                Intent isearch = new Intent(HomeActivity.this,SearchForProductActivity.class);
                startActivity(isearch);
                break;

            case R.id.nav_categories:
                Toast.makeText(this, "Nav categories Is clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                Toast.makeText(this, "Nav settings Is clicked", Toast.LENGTH_SHORT).show();
              Intent in = new Intent(HomeActivity.this, SettingsActivity.class);
               startActivity(in);
                break;

            case R.id.nav_logout:
                if(!(type == "admin")) {
                    Toast.makeText(this, "Nav logout Is clicked", Toast.LENGTH_SHORT).show();
                    viewModel.signOut();

                }else {
                    Intent i = new Intent(HomeActivity.this,AdminCategoryActivity.class);
                    startActivity(i);
                }

                break;

        }
        return true;
    }


    public void getuType(){
        database.getReference().child("Admins")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
                            User users = dataSnapshot.getValue(User.class);
                            users.setUserId(dataSnapshot.getKey());
                            if((users.getUserId().equals(FirebaseAuth.getInstance().getUid()))){

                                type = "admin";
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

    }
//
//    public void getusernameandimage(String uid){
//
//        FirebaseDatabase.getInstance().getReference().child("Users")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
//                            User users = dataSnapshot.getValue(User.class);
//                            users.setUserId(dataSnapshot.getKey());
//                            Log.d("id ", users.getUserId().toString());
//                            if((users.getUserId().
//                                    equals(FirebaseAuth.getInstance().getUid()))){
//
//                                us = users;
//                                CircleImageView userimage = headerView.findViewById(R.id.profile_image_nav);
//
////                                if(us.getImage() != "" && us.getImage() != null ){
//                                Picasso.get()
//                                        .load(us.getImage())
//                                        .placeholder(R.drawable.profile)
//                                        .into(userimage);
////                                }
////                                Glide.with(HomeActivity.this)
////                                        .load(us.getImage())
////                                        .into(profilepic);
//
//                                username.setText(us.getUserName());
//
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//
//                });
//    }
////
//    FirebaseDatabase.getInstance().getReference().child("Users")
//                .addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
//                User users = dataSnapshot.getValue(User.class);
//                users.setUserId(dataSnapshot.getKey());
//                Log.d("id ", users.getUserId().toString());
//                if((users.getUserId().
//                        equals(FirebaseAuth.getInstance().getUid()))){
//
//                    us = users;
//                    CircleImageView userimage = headerView.findViewById(R.id.profile_image_nav);
//
////                                if(us.getImage() != "" && us.getImage() != null ){
//                    Picasso.get()
//                            .load(us.getImage())
//                            .placeholder(R.drawable.profile)
//                            .into(userimage);
////                                }
////                                Glide.with(HomeActivity.this)
////                                        .load(us.getImage())
////                                        .into(profilepic);
//
//                    username.setText(us.getUserName());
//
//                }
//            }
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//
//    });
//
//        FirebaseDatabase.getInstance().getReference().child("Admins")
//                .addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
//                User users = dataSnapshot.getValue(User.class);
//                users.setUserId(dataSnapshot.getKey());
//                Log.d("id ", users.getUserId().toString());
//                if((users.getUserId().
//                        equals(FirebaseAuth.getInstance().getUid()))){
//
//                    us = users;
//                    CircleImageView userimage = headerView.findViewById(R.id.profile_image_nav);
//
////                                if(us.getImage() != "" && us.getImage() != null ){
//                    Picasso.get()
//                            .load(us.getImage())
//                            .placeholder(R.drawable.profile)
//                            .into(userimage);
////                                }
////                                Glide.with(HomeActivity.this)
////                                        .load(us.getImage())
////                                        .into(profilepic);
//
//                    username.setText(us.getUserName());
//
//                }
//            }
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//
//    });
    public void getadminnameandimage(){

        FirebaseDatabase.getInstance().getReference().child("Admins")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
                            User users = dataSnapshot.getValue(User.class);
                            users.setUserId(dataSnapshot.getKey());
                            Log.d("id ", users.getUserId().toString());
                            if((users.getUserId().
                                    equals(FirebaseAuth.getInstance().getUid()))){

                                us = users;
                                CircleImageView userimage = headerView.findViewById(R.id.profile_image_nav);

//                                if(us.getImage() != "" && us.getImage() != null ){
                                Picasso.get()
                                        .load(us.getImage())
                                        .placeholder(R.drawable.profile)
                                        .into(userimage);
//                                }
//                                Glide.with(HomeActivity.this)
//                                        .load(us.getImage())
//                                        .into(profilepic);

                                username.setText(us.getUserName());

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
    }


}