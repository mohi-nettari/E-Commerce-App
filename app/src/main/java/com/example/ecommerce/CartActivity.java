package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Model.product;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CartActivity extends AppCompatActivity {

   private RecyclerView rcv_c;
    private RecyclerView.LayoutManager layoutManager;
    private Button next;
    private TextView totalamount,msg1;
    int totalprice = 0;
    private DatabaseReference prf;
    ImageView back;
    LoadingDialog loadingDialog;

    @Override
    protected void onStart() {
        super.onStart();
        orderstate();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d("isDone s","yes");
//        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("Cart list");
//
//        FirebaseRecyclerOptions<Cart> options =
//                new FirebaseRecyclerOptions.Builder<Cart>()
//                        .setQuery(prf, Cart.class).build();
//        Log.d("isDone op","yes");
//
//        FirebaseRecyclerAdapter<Cart, CartViewHolder> cartadapter =
//                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
//                         holder.txtPquantity.setText("Quantity : "+model.getQuantity());
//                        holder.txtPprice.setText("Price : "+model.getPprice()+" DA");
//                        holder.txtPname.setText("Name : "+model.getPname());
//
////                        int onepp = (Integer.valueOf(model.getPprice()))  * (Integer.valueOf(model.getQuantity()));
////                        totalprice = onepp + totalprice;
////                        totalamount.setText("Total price : " + String.valueOf(totalprice) + " DA");
//
//
//                        Log.d(" Done",model.getPprice());
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                Toast.makeText(CartActivity.this, "item selected", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//
//
//                    @Override
//                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        Log.d("isDone ad oncreatv","yes");
//
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_each_item,parent,false);
//                        CartViewHolder holder = new CartViewHolder(view);
//
//                        return holder;
//
//                    }
//                };
//        Log.d("isDone ad oncreatv done","yes");
//
//        rcv_c.setAdapter(cartadapter);
//        cartadapter.startListening();
//
//        Log.d("isDone onstart","yes");
//
////start","yes");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

//        int nightModeFlags =
//                (CartActivity.this).getResources().getConfiguration().uiMode &
//                        Configuration.UI_MODE_NIGHT_MASK;
//        switch (nightModeFlags) {
//            case Configuration.UI_MODE_NIGHT_YES:
//                if((getSupportActionBar().isShowing())){
//                    getSupportActionBar().hide();
//                }
//                break;
//
//            case Configuration.UI_MODE_NIGHT_NO:
//                if(!(getSupportActionBar().isShowing())){
//                    //  getSupportActionBar().show();
//
//                }
//                break;
//            case Configuration.UI_MODE_NIGHT_UNDEFINED:
//                if(!(getSupportActionBar().isShowing())){
//                  //  getSupportActionBar().show();
//
//                }
//                break;
        //}


        // orderstate();
        prf =  FirebaseDatabase.getInstance().getReference().child("Cart list")
                .child("User view").child(FirebaseAuth.getInstance().getUid())
                .child("Products");
                Log.d("isDone c","yes");

                //initializing the views
        back = findViewById(R.id.backcart);
        rcv_c = findViewById(R.id.rcvc);
        rcv_c.setHasFixedSize(true);
        layoutManager = new  LinearLayoutManager(this);
        rcv_c.setLayoutManager(layoutManager);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });



        loadingDialog = new LoadingDialog(CartActivity.this);
        msg1 = findViewById(R.id.msg1);
        next = findViewById(R.id.nextc);

        totalamount = findViewById(R.id.totalc);

        //back arrow events

        back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(CartActivity.this,HomeActivity.class);
        startActivity(i);


    }
});

//cart adapter (getting the cart content from the database to the app )

        loadingDialog.startLoadingDialog();
        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("Cart list");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(prf, Cart.class).build();
        Log.d("isDone op","yes");

        FirebaseRecyclerAdapter<Cart, CartViewHolder> cartadapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                        loadingDialog.dismisLoadingDialog();
                        holder.txtPquantity.setText("Quantity : "+model.getQuantity());
                        holder.txtPprice.setText("Price : "+model.getPprice()+" DA");
                        holder.txtPname.setText("Name : "+model.getPname());

                        int onepp = (Integer.valueOf(model.getPprice()))  * (Integer.valueOf(model.getQuantity()));
                        totalprice = onepp + totalprice;
                        Log.d(" Done",model.getPprice());
                        totalamount.setText("Total Price  : " + String.valueOf(totalprice) + " DA");

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(CartActivity.this, "item selected", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }


                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        Log.d("isDone ad oncreatv","yes");

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_each_item,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);

                        return holder;

                    }

                };

        rcv_c.setAdapter(cartadapter);
        cartadapter.startListening();

        //END cart adapter (getting the cart content from the database to the app )
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,ConfirmeFinalOrderActivity.class);
                 intent.putExtra("tp",totalamount.getText().toString());
                Toast.makeText(CartActivity.this, totalamount.getText().toString(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

    }

    //this fun will show the user a msg when his order placed successfully and make some views gone
    private void orderstate(){
        DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(FirebaseAuth.getInstance().getUid());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippedstate = snapshot.child("status").getValue().toString();
                    String username = snapshot.child("name").getValue().toString();

                    if(TextUtils.equals(shippedstate,"Shipped")){
                        loadingDialog.dismisLoadingDialog();
                        totalamount.setText("Dear " + username + " your final order has been shipped successfully");
                        totalamount.setTextSize(12);
                        rcv_c.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        next.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "you can purchase more products \n once you received your last order", Toast.LENGTH_SHORT).show();
                        
                        
                        
                    }
                    else if (TextUtils.equals(shippedstate,"Not shipped")) {
                        loadingDialog.dismisLoadingDialog();
                        totalamount.setText(username+" Shipped state : Not shipped yet");
                        totalamount.setTextSize(12);
                        rcv_c.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        msg1.setText("Congratulations\n Your order has been placed successfully.\n soon it woill shipped.");
                        next.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "you can purchase more products \n once you received your last order", Toast.LENGTH_SHORT).show();


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}