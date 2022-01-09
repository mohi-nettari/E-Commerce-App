package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.ViewModel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {

    private Button creataccbtn,fb_btn,google_btn;
    private EditText nameet,emailet,passet,passet2,admincode;
    private TextView admin,notadmin;
    private FirebaseAuth firebaseAuth;
    private AuthViewModel viewModel;
   private ProgressDialog progressDialog;
   private FirebaseUser firebaseUser ;
   private TextView signin;
   ImageView back;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //checking for users existing : saving users
        if (firebaseUser != null) {
            Intent i = new Intent(registerActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("onCreate","Done");
        //initialize the widgets
        setContentView(R.layout.activity_register);


               // getSupportActionBar().hide();

        creataccbtn = findViewById(R.id.register_create_btn);
        nameet = findViewById(R.id.register_name_et);
        emailet = findViewById(R.id.register_num_et);
        passet = findViewById(R.id.register_pass_et);
        passet2 = findViewById(R.id.register_pass2_et);
        signin = findViewById(R.id.signintxt);
        admin = findViewById(R.id.create_Admin_panel);
        notadmin = findViewById(R.id.create_notAdmin_panel);
        admincode = findViewById(R.id.register_admincode_et);

        back = findViewById(R.id.backreg);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        //preparing the ViewModel
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(AuthViewModel.class);



        //preparing the progressDiaolog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating New Acocunt");
        progressDialog.setMessage("Signing Up Please Wait...");

        // creating a new user button events
        creataccbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                String creatkind =creataccbtn.getText().toString();
                String email = emailet.getText().toString();
                String password = passet.getText().toString();
                String password2 = passet2.getText().toString();
                String userName = nameet.getText().toString();

                String code = admincode.getText().toString();
                if (creatkind.equals("Admin Create Account")){
                    if(TextUtils.isEmpty(code)){
                        nameet.setError("Please Enter Your Name");
                        progressDialog.dismiss();
                    }

                }
                if(TextUtils.isEmpty(userName)){
                    nameet.setError("Please Enter Your Name");
                    progressDialog.dismiss();

                }
                if (TextUtils.isEmpty(email)|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailet.setError("Please Enter a Valid Email");
                    progressDialog.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    passet.setError("Enter your Password");
                    progressDialog.dismiss();
                    return;
                }
                if ( password.length() < 6){
                    passet.setError("your Password should contain at least 6 characters");
                    progressDialog.dismiss();
                    return;
                }
                if (!(password2.equals(password))){
                    passet2.setError("the Passwords you entered are not the same");
                    progressDialog.dismiss();
                    return;
                }

                    viewModel.signUp(email,password,userName,creatkind,code);




            }
        });

        // sign in text events
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        });

        //admin text events
        notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creataccbtn.setText("Create Account");
                admin.setVisibility(View.VISIBLE);
                notadmin.setVisibility(View.GONE);
                admincode.setVisibility(View.GONE);
            }
        });
        //admin text events
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creataccbtn.setText("Admin Create Account");
                admin.setVisibility(View.GONE);
                notadmin.setVisibility(View.VISIBLE);
                admincode.setVisibility(View.VISIBLE);
            }
        });


    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent

                            // after email is sent just logout the user and finish this activity
                            startActivity(new Intent(registerActivity.this, LoginActivity.class));
                            Toast.makeText(registerActivity.this, "verification email is sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Toast.makeText(registerActivity.this, "verification email didn't sent", Toast.LENGTH_SHORT).show();
                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }
}