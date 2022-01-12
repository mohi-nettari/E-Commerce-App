package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.HomeActivity;
import com.example.ecommerce.MainActivity;
import com.example.ecommerce.Model.User;
import com.example.ecommerce.R;
import com.example.ecommerce.SettingsActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminSettingsActivity extends AppCompatActivity {
    private CircleImageView proimage_s;
    private EditText name_s,admincode_s,password_S;
    private TextView   phone_s, changeproimage;
    private Uri imageUri;
    private String myurl= "";
    private StorageReference storageproref ;
    private String checker = "";
    private StorageTask uploadtask;
    DatabaseReference reference,reference2;
    FirebaseDatabase database;
    Button update_s;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String id = firebaseUser.getUid();
    User us;
    ImageView back;
    String utype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        storageproref = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        proimage_s = findViewById(R.id.image_settings_admin);
        name_s = findViewById(R.id.name_settings_admin);
        phone_s = findViewById(R.id.phone_settings_admin);
        admincode_s = findViewById(R.id.admin_code_settings_admin);
        password_S = findViewById(R.id.password_settings_admin);
        update_s = findViewById(R.id.updat_settings_admin);
        changeproimage = findViewById(R.id.change_image_settings_admin);

        UserInfoDisplay(proimage_s , phone_s , name_s ,admincode_s,password_S);

        back = findViewById(R.id.backsettings_admin);


        // getSupportActionBar().hide();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSettingsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();

                }else {
                    updateOnlyUserInfo();
                }
            }
        });

        changeproimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(AdminSettingsActivity.this);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            proimage_s.setImageURI(imageUri);
        }else {
            Toast.makeText(this, "Error, Pleas try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminSettingsActivity.this,AdminSettingsActivity.class));
            finish();
        }

    }

    private void updateOnlyUserInfo() {
        if(TextUtils.isEmpty(name_s.getText().toString())){
            Toast.makeText(AdminSettingsActivity.this, "Name is mondatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(admincode_s.getText().toString())){
            Toast.makeText(AdminSettingsActivity.this, "Admin code is mondatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone_s.getText().toString())){
            Toast.makeText(AdminSettingsActivity.this, "Email is mondatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password_S.getText().toString())){
            Toast.makeText(AdminSettingsActivity.this, "Password is mondatory", Toast.LENGTH_SHORT).show();
        }else {

            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Update profile");
            pd.setMessage("Pleas wait ,we are updating your profile");
            pd.setCanceledOnTouchOutside(false);

            pd.show();
             reference = FirebaseDatabase.getInstance().getReference().child("Admins");
             reference2 = FirebaseDatabase.getInstance().getReference().child("Admincode");

            HashMap<String , Object> userhash = new HashMap<>();
            userhash.put("userName",name_s.getText().toString());
            userhash.put("email",phone_s.getText().toString());
            userhash.put("password",password_S.getText().toString());
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference.child(firebaseUser.getUid()).updateChildren(userhash);

            HashMap<String , Object> userhash2 = new HashMap<>();
            userhash2.put("admincode",admincode_s.getText().toString());
            reference2.child("Code").updateChildren(userhash2);

            Toast.makeText(AdminSettingsActivity.this, "your  profile information updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminSettingsActivity.this, AdminCategoryActivity.class));
            pd.dismiss();
            finish();


        }



    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(name_s.getText().toString())){
            Toast.makeText(AdminSettingsActivity.this, "Name is mondatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(admincode_s.getText().toString())){
            Toast.makeText(AdminSettingsActivity.this, "Admin code is mondatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone_s.getText().toString())){
            Toast.makeText(AdminSettingsActivity.this, "Email is mondatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password_S.getText().toString())){
            Toast.makeText(AdminSettingsActivity.this, "Password is mondatory", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadimage();
        }
    }

    private void uploadimage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Update profile");
        pd.setMessage("Pleas wait,we are updating your profile");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        if(imageUri != null){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference fileref = storageproref
                    .child(firebaseUser.getUid()+".jpg");

            uploadtask = fileref.putFile(imageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloaduri = task.getResult();
                        myurl = downloaduri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Admins");
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Admincode");

                        HashMap<String , Object> userhash = new HashMap<>();
                        userhash.put("userName",name_s.getText().toString());
                        userhash.put("email",phone_s.getText().toString());
                        userhash.put("password",password_S.getText().toString());
                        userhash.put("image", myurl);
//                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        reference.child(FirebaseAuth.getInstance().getUid()).updateChildren(userhash);


                        HashMap<String , Object> userhash2 = new HashMap<>();
                        userhash2.put("admincode",admincode_s.getText().toString());
                        reference2.child("Code").updateChildren(userhash2);


                        Toast.makeText(AdminSettingsActivity.this, "your  profile information updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminSettingsActivity.this,AdminCategoryActivity.class));
                        finish();
                    }else {

                        pd.dismiss();
                        Toast.makeText(AdminSettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            Toast.makeText(this, "No profile image selected.", Toast.LENGTH_SHORT).show();
        }

    }

    private void UserInfoDisplay(CircleImageView proimage_s, TextView phone_s, EditText name_s, EditText admincode_s, EditText password_S)
    {

        FirebaseDatabase.getInstance().getReference().child("Admins")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
                            User users = dataSnapshot.getValue(User.class);
                            users.setUserId(dataSnapshot.getKey());
                            if((users.getUserId().equals(FirebaseAuth.getInstance().getUid()))){
                                us = users;
                                Picasso.get().load(us.getImage()).into(proimage_s);
                                name_s.setText(us.getUserName());
                                phone_s.setText(us.getEmail());
                                password_S.setText(us.getPassword());

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

        FirebaseDatabase.getInstance().getReference().child("Admincode")
                .child("Code")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren() ) {
                    String code = dataSnapshot.getValue().toString();
                    admincode_s.setText(code);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid());
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.exists()){
//                    if (snapshot.child("image").exists()){
//                        String image = snapshot.child("image").getValue().toString();
//                        String email = snapshot.child("email").getValue().toString();
//                        String adress = snapshot.child("adress").getValue().toString();
//                        String name = snapshot.child("name").getValue().toString();
//
//                        Picasso.get().load(image).into(proimage_s);
//                        name_s.setText(name);
//                        adress_s.setText(adress);
//                        phone_s.setText(email);
//
//                    }else {
//                        String email = snapshot.child("email").getValue().toString();
//                        String adress = snapshot.child("adress").getValue().toString();
//                        String name = snapshot.child("username").getValue().toString();
//                        name_s.setText(name);
//                        adress_s.setText(adress);
//                        phone_s.setText(email);
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });




}

    }