package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSeeUserProductActivity extends AppCompatActivity {

    RecyclerView rcvProducts;
    DatabaseReference userproductsRef;
    TextView userproducts,totalamount;
    String userid ,username;
    int totalprice = 0;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_see_user_product);

        //initializing the views
        userid = getIntent().getStringExtra("userid");
        username = getIntent().getStringExtra("username");

        userproductsRef =  FirebaseDatabase.getInstance().getReference().child("Cart list").child("Admin view")
                .child(userid).child("Products");

        userproducts = findViewById(R.id.userproducts);
        totalamount = findViewById(R.id.usertotal);


            //    getSupportActionBar().hide();

        back = findViewById(R.id.backadminseep);
//
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSeeUserProductActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });



        userproducts.setText(username + " Products : " );
        rcvProducts = findViewById(R.id.rcvuserproduct);
        rcvProducts.setLayoutManager(new LinearLayoutManager(this));
        rcvProducts.setHasFixedSize(true);
        FirebaseRecyclerOptions<Cart> o =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(userproductsRef, Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> productsadapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(o) {
                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_each_item,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);

                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                        holder.txtPquantity.setText("Quantity : "+model.getQuantity());
                        holder.txtPprice.setText("Price : "+model.getPprice()+" DA");
                        holder.txtPname.setText("Name : "+model.getPname());

                        int onepp = (Integer.valueOf(model.getPprice()))  * (Integer.valueOf(model.getQuantity()));
                        totalprice = onepp + totalprice;
                        Log.d(" Done",model.getPprice());
                        totalamount.setText("Total Price  : " + String.valueOf(totalprice) + " DA");

                    }



                };

        rcvProducts.setAdapter(productsadapter);
        productsadapter.startListening();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}