package com.example.ecommerce;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerce.Model.User;
import com.example.ecommerce.ViewModel.AuthViewModel;
import com.example.ecommerce.prevalent.prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private EditText emailet,passwordet;
    private TextView forgetpass,admin,notadmin;
    private Button loginbtn,fb_btn,google_btn;
    private CheckBox remember_me;
    private AuthViewModel authViewModel;
    private ProgressDialog progressDialog;
     FirebaseAuth firebaseAuth;
     FirebaseUser firebaseUser;
    private TextView signup;
    FirebaseDatabase database;
    ImageView back;



    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
     //   String id = firebaseUser.getUid();
//        checking for users existing : saving users
        if (firebaseUser != null ) {
            database.getReference().child("Users")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
                                User users = dataSnapshot.getValue(User.class);
                                users.setUserId(dataSnapshot.getKey());
                                if((users.getUserId().equals(FirebaseAuth.getInstance().getUid()))){
                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });


            database.getReference().child("Admins")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot :snapshot.getChildren() ) {
                                User users = dataSnapshot.getValue(User.class);
                                users.setUserId(dataSnapshot.getKey());
                                if ((users.getUserId().equals(FirebaseAuth.getInstance().getUid()))) {
                                    Intent intent = new Intent(LoginActivity.this,
                                            AdminCategoryActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //initialize the widgets
         emailet = findViewById(R.id.login_email_et);
         passwordet = findViewById(R.id.login_pass_et);
       forgetpass = findViewById(R.id.login_forget_pass);
       admin = findViewById(R.id.login_Admin_panel);
        notadmin = findViewById(R.id.login_notAdmin_panel);
        loginbtn = findViewById(R.id.login_login_btn);
//       fb_btn = findViewById(R.id.login_fb_btn);
        google_btn = findViewById(R.id.login_google_btn);
      signup = findViewById(R.id.signintxt);

      database = FirebaseDatabase.getInstance();


               // getSupportActionBar().hide();


        back = findViewById(R.id.backlog);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,registerActivity.class);
                startActivity(intent);
            }
        });


//      FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
//          @Override
//          public void onDataChange(@NonNull DataSnapshot snapshot) {
//              User  userdata = snapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).getValue(User.class);
//              prevalent.currentuser = userdata;
//          }
//
//          @Override
//          public void onCancelled(@NonNull DatabaseError error) {
//
//          }
//      });

        //initialize the ViewModel
        authViewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(AuthViewModel.class);

        //initialize the progressDiaolog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing in to your account");
        progressDialog.setMessage("Signing in please wait...");

        //authentication (Login btn event)
        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String isAdmin = loginbtn.getText().toString();

                progressDialog.show();
                String email = emailet.getText().toString();
                String password = passwordet.getText().toString();

                if(TextUtils.isEmpty(email)){
                    emailet.setError("Pleas enter your email");
                    progressDialog.dismiss();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordet.setError("Pleas enter your password");
                    progressDialog.dismiss();
                    return;
                }
                Log.d("login","done3");


                    Log.d("login","done4");

                    authViewModel.signIn(email,password,isAdmin);

                }


        });

        //admin text events
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginbtn.setText("Admin Log in");
                admin.setVisibility(View.GONE);
                notadmin.setVisibility(View.VISIBLE);
            }
        });

        //notadmin text events
        notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginbtn.setText("Log in");
                admin.setVisibility(View.VISIBLE);
                notadmin.setVisibility(View.GONE);
            }
        });

// sign up text events
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this , registerActivity.class);
                startActivity(intent);
            }
        });

    }



}