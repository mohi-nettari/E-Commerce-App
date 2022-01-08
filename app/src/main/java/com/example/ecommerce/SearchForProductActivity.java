package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Model.product;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class SearchForProductActivity extends AppCompatActivity {

    ImageView back;
    ImageView searchbtn;
    RecyclerView searchlist;
    EditText searchet;
    String searchinput;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_product);

        //initializing the widgets
        back = findViewById(R.id.back3);
        searchbtn = findViewById(R.id.searchbtn);
        searchet = findViewById(R.id.searchet);

        searchlist = findViewById(R.id.searchres);
        searchlist.setLayoutManager(new GridLayoutManager(this,2));
        searchlist.setHasFixedSize(true);

        loadingDialog = new LoadingDialog(SearchForProductActivity.this);

        back = findViewById(R.id.backsearch);


               // getSupportActionBar().hide();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchForProductActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchinput = searchet.getText().toString();
                onStart();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingDialog.startLoadingDialog();
       DatabaseReference pref = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<product> options =
                new FirebaseRecyclerOptions.Builder<product>()
                        .setQuery(pref.orderByChild("pname").startAt(searchinput),product.class).build();

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

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchForProductActivity.this , ProductDetailsActivity.class);
                                intent.putExtra("joji",model.getPprice());
                                intent.putExtra("image", model.getPimage());
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("name", model.getPname());
                                intent.putExtra("description" ,model.getPdesc());
                                startActivity(intent);
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
        searchlist.setAdapter(adapter);
        adapter.startListening();
    }
}