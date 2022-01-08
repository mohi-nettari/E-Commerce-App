package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addtocart,mains,plus;
    private ImageView pimage,back;
    private TextView pquantity;
    private TextView pname,pdesc,pprice,ppname2;
    private String pn,pd,pim,pid,quanti,joji,state;
    private int q=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_product_details);
////       //heeeey  joji = PRICE and IDK why i give it a name like this
//
        addtocart = findViewById(R.id.add_to_cart);
       pquantity = findViewById(R.id.quantity2);
        pname = findViewById(R.id.product_name_d);
        pdesc = findViewById(R.id.product_desc_d);
        pprice = findViewById(R.id.product_price_d);
        pimage = findViewById(R.id.product_image_d);
ppname2 = findViewById(R.id.product_name_d3);
        mains = findViewById(R.id.mains);
        plus = findViewById(R.id.plus);


               // getSupportActionBar().hide();


        back = findViewById(R.id.backdetails);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
//
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = q+1;
                pquantity.setText(String.valueOf(q));
            }
        });

        mains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (q <= 1) {
                    Toast.makeText(ProductDetailsActivity.this, "you cant buy less then 1 product.", Toast.LENGTH_SHORT).show();
                }else {
                    q--;
                    pquantity.setText(String.valueOf(q));
                }

            }
        });


        pid = getIntent().getStringExtra("pid");
        pn = getIntent().getStringExtra("name");
        pd = getIntent().getStringExtra("description");
        pim = getIntent().getStringExtra("image");
        joji = getIntent().getStringExtra("joji");

//
        quanti=pquantity.getText().toString();

        ppname2.setText(pn+" Details :");
        pname.setText(pn);
        pprice.setText(joji);
   //     Toast.makeText(ProductDetailsActivity.this, pp, Toast.LENGTH_SHORT).show();
        pdesc.setText(pd);

        Picasso.get()
                .load(pim)
                .into(pimage);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quanti=pquantity.getText().toString();
              //  addingToCart();
                if (state == "Order shipped" ||state == "Order placed") {
                    Toast.makeText(ProductDetailsActivity.this, "You can purchase more product ,\n " +
                            "once your order shipped or confirmed. ", Toast.LENGTH_LONG).show();
                }else {
                    addingToCart();
                }

            }
        });

    }

    // this fun adds a product to the cart and add the product to cart list in firebase
    private void addingToCart() {
        String savecurrentdate,savecurrenttime;

        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd , yyyy");
        savecurrentdate = currentdate.format(calfordate.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calfordate.getTime());

        final DatabaseReference cartref = FirebaseDatabase.getInstance().getReference().child("Cart list");

        final HashMap<String,Object> cartmap = new HashMap<>();
        cartmap.put("pid",pid);
        cartmap.put("pname",pn);
        cartmap.put("pdesc",pd);
        cartmap.put("pprice",joji);
        cartmap.put("date",savecurrentdate);
        cartmap.put("time",savecurrenttime);
        cartmap.put("pimage",pim);
        cartmap.put("quantity",quanti);
        cartmap.put("discount","");

        cartref.child("User view")
                .child(FirebaseAuth.getInstance().getUid())
                .child("Products").child(pid)
                .updateChildren(cartmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    cartref.child("Admin view")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("Products").child(pid)
                            .updateChildren(cartmap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(ProductDetailsActivity.this, "Added to product list.", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                            startActivity(i);

                        }
                    });
                }
            }
        });




    }


    //this fun will give us state of a product (placed or shipped or no)
    private void orderstate(){
        DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(FirebaseAuth.getInstance().getUid());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippedstate = snapshot.child("status").getValue().toString();

                    if(TextUtils.equals(shippedstate,"Shipped")){
                        state="Order shipped";
                    }
                    else if (TextUtils.equals(shippedstate,"Not shipped")) {
                        state="Order placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
