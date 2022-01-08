package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmeFinalOrderActivity extends AppCompatActivity {

    EditText etname,etphone,etadress,etcity;
    TextView txttotal;
    Button btnconfirm;
    String totalp;
    String userId;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirme_final_order);
        userId = FirebaseAuth.getInstance().getUid();
        etname = findViewById(R.id.etname);
        etphone = findViewById(R.id.etphone);
        etadress = findViewById(R.id.etadress);
        etcity = findViewById(R.id.etcity);
        btnconfirm = findViewById(R.id.btnconfirm);

        totalp = getIntent().getStringExtra("tp");
        txttotal = findViewById(R.id.txttotalconfirm);
        txttotal.setText( totalp );


               // getSupportActionBar().hide();

        back = findViewById(R.id.backconfirme);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmeFinalOrderActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void check() {
        if(TextUtils.isEmpty(etname.getText().toString())){

            Toast.makeText(ConfirmeFinalOrderActivity.this, "Pleas provide your full name", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(etphone.getText().toString())){

            Toast.makeText(ConfirmeFinalOrderActivity.this, "Pleas provide your full phone number", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(etphone.getText().toString())){

            Toast.makeText(ConfirmeFinalOrderActivity.this, "Pleas provide your full home address ", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(etadress.getText().toString())){

            Toast.makeText(ConfirmeFinalOrderActivity.this, "Pleas provide your full city name ", Toast.LENGTH_SHORT).show();

        }else{
            confirmorder();
        }

    }

    private void confirmorder() {
        final String savecurrentdate,savecurrenttime;

        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd , yyyy");
        savecurrentdate = currentdate.format(calfordate.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calfordate.getTime());
        final DatabaseReference ordersref = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(FirebaseAuth.getInstance().getUid());

        final HashMap<String,Object> ordersmap = new HashMap<>();
        ordersmap.put("total",totalp);
        ordersmap.put("userid",userId);
        ordersmap.put("name",etname.getText().toString());
        ordersmap.put("phone",etphone.getText().toString());
        ordersmap.put("address",etadress.getText().toString());
        ordersmap.put("city",etcity.getText().toString());
        ordersmap.put("date",savecurrentdate);
        ordersmap.put("time",savecurrenttime);
        ordersmap.put("status","Not shipped");

        ordersref.updateChildren(ordersmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cart list")
                            .child("User view")
                            .child(FirebaseAuth.getInstance().getUid())
                             .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmeFinalOrderActivity.this, "Your order has been confirmed successfully", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ConfirmeFinalOrderActivity.this,HomeActivity.class);
                                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();

                                    }

                                }
                            });
                }
            }
            });


    }
    }