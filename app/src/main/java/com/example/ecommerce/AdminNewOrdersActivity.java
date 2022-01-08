package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.ViewHolder.AdminOrderViewHolder;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    RecyclerView ordersList;
    DatabaseReference ordersRef;
    LoadingDialog loadingDialog;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        //initializing the views
        ordersRef =  FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.rcvo);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
        ordersList.setHasFixedSize(true);

        loadingDialog = new LoadingDialog(AdminNewOrdersActivity.this);


              // getSupportActionBar().hide();


        back = findViewById(R.id.backadminorders);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminNewOrdersActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
//        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
//
//        FirebaseRecyclerOptions<AdminOrders> options =
//                new FirebaseRecyclerOptions.Builder<AdminOrders>()
//                .setQuery(ordersRef,AdminOrders.class).build();
//
//        FirebaseRecyclerAdapter<AdminOrders,AdminOrderViewHolder> ordersadapter =
//                new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull AdminOrders model) {
//
//                        Toast.makeText(AdminNewOrdersActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                        holder.phone.setText(model.getPhone());
//                        holder.adrress.setText(model.getAddress());
//                        holder.username.setText(model.getName());
//                        holder.datetime.setText(model.getDate());
//                        holder.totalprice.setText(model.getTotalamount());
//
//
//
//                        holder.btnShow.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                Toast.makeText(AdminNewOrdersActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
//                  return new AdminOrderViewHolder(view);
//                    }
//                };
//        ordersList.setAdapter(ordersadapter);
//       ordersadapter.startListening();

        //cart adapter (getting the cart content from the database to the app )
        loadingDialog.startLoadingDialog();
        FirebaseRecyclerOptions<AdminOrders> o =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef, AdminOrders.class).build();
        Log.d("isDone op","yes");

        FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder> ordersadapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(o) {
                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
                        return new AdminOrderViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull AdminOrders model) {
                       loadingDialog.dismisLoadingDialog();
                        holder.username.setText("User Name : "+model.getName());
                        holder.totalprice.setText("Total price : "+model.getTotal());
                        holder.adrress.setText("User address,city : "+model.getAddress()+", "+model.getCity());
                        holder.datetime.setText("Order date,time : "+model.getDate()+" ,"+model.getTime());
                        holder.phone.setText("User phone number: "+ model.getPhone());
                        holder.state.setText("Order status : "+model.getStatus());
                        holder.btnShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(AdminNewOrdersActivity.this, "clicked.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AdminNewOrdersActivity.this,AdminSeeUserProductActivity.class);
                                i.putExtra("userid", model.getUserid());
                                i.putExtra("username", model.getName());
                                startActivity(i);

                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options [] = new CharSequence[]{
                                        "Yes"
                                        ,
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Have shipped thid order products?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which == 0){

                                            String uid = model.getUserid();
                                            removeOrder(uid);
                                        }else {
                                            finish();

                                        }

                                    }
                                });
                                builder.show();

                            }
                        });

                    }


                };

        ordersList.setAdapter(ordersadapter);
        ordersadapter.startListening();

//        FirebaseRecyclerOptions<AdminOrders> options =
//                new FirebaseRecyclerOptions.Builder<AdminOrders>()
//                        .setQuery(ordersRef, AdminOrders.class).build();
//
//        FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder> ordersadapter =
//                new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
//
//                    @NonNull
//                    @Override
//                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
//                        return new AdminOrderViewHolder(view);
//                    }
//
//                    @Override
//                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull AdminOrders model) {
//                        holder.username.setText("User Name : "+model.getName());
//                        holder.totalprice.setText("Total price : "+model.getTotalamount());
//                        holder.adrress.setText("User address,city : "+model.getAddress()+", "+model.getCity());
//                        holder.datetime.setText("Order date,time : "+model.getDate()+" ,"+model.getTime());
//                        holder.phone.setText("User phone number : "+ model.getPhone());
//
//                        holder.btnShow.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(AdminNewOrdersActivity.this, "Button Show is clicked.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//
//
//                };
//        ordersList.setAdapter(ordersadapter);
//        ordersadapter.startListening();
   }

    private void removeOrder(String uid) {
        ordersRef.child(uid).removeValue();

    }
}