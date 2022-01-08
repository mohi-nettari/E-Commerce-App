package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {

  private EditText product_cate_d,product_name_d2,product_price_d2,product_desc_d;
   TextView product_name_d3;
    private  ImageView product_image_d,back;
    private Button update;
    private String pn,pp,pd,pim,pid,quanti,joji,state,categ;
    String categoryname,pname,pprice,pdesc,pcateg;
    private static final int pickimage =1;
    private Uri imageuri;
    StorageReference Pimagesref;
    private String downloadimageurl;
    private DatabaseReference pref;
    private ProgressDialog progressDialog;
    Boolean checker;

    //On key Down Events
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // moveTaskToBack(true);
            Intent intent = new Intent(AdminMaintainProductActivity.this,HomeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        ///storage ref
        Pimagesref =   FirebaseStorage.getInstance().getReference().child("ProductImages");

        //initializing the widgets
        product_name_d3 = findViewById(R.id.product_name_d3);
        product_cate_d = findViewById(R.id.product_cate_d);
        product_name_d2 = findViewById(R.id.product_name_d2);
        product_price_d2 = findViewById(R.id.product_price_d2);
        product_desc_d = findViewById(R.id.product_desc_d);
        product_image_d = findViewById(R.id.product_image_d);
        update = findViewById(R.id.add_to_cart);

        pid = getIntent().getStringExtra("pid");
        pref =   FirebaseDatabase.getInstance().getReference().child("Products").child(pid);
        pn = getIntent().getStringExtra("name");
        pd = getIntent().getStringExtra("description");
        pim = getIntent().getStringExtra("image");
        joji = getIntent().getStringExtra("joji");
        categ = getIntent().getStringExtra("categ");

        showproductdetails();


               // getSupportActionBar().hide();


        back = findViewById(R.id.backadminmaintain);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMaintainProductActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });


        //when admin clicks on product image so he can change it

        product_image_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = true;
                opengallery();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valdateproductdata();
            }
        });


    }

    private void showproductdetails(){
        product_name_d2.setText(pn);
        product_price_d2.setText(joji);
        product_desc_d.setText(pd);
        product_cate_d.setText(categ);
        product_name_d3.setText("Update "+pn+" details: ");
        Picasso.get()
                .load(pim)
                .into(product_image_d);

    }


    private void opengallery() {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,pickimage);

    }

//
private void valdateproductdata() {
    pname = product_name_d2.getText().toString();
    pdesc = product_desc_d.getText().toString();
    pprice = product_price_d2.getText().toString();
    pcateg = product_cate_d.getText().toString();

    if (TextUtils.isEmpty(pdesc) || TextUtils.isEmpty(pprice) || TextUtils.isEmpty(pname) || TextUtils.isEmpty(pcateg) ){
        Toast.makeText(this, "Product information are not complete ", Toast.LENGTH_SHORT).show();
    }else {

            saveproductinfo();

    }

}



//
//    private void storeproductinformation() {
//
//        progressDialog.setTitle("Add new product ");
//        progressDialog.setMessage("Pleas wait while we are adding the new product...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
////
//        StorageReference filepath = Pimagesref.child(imageuri.getLastPathSegment()+pid + ".jpg");
//        final UploadTask uploadTask =filepath.putFile(imageuri);
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message = e.toString();
//                Toast.makeText(AdminMaintainProductActivity.this, "ERROR : "+ message , Toast.LENGTH_SHORT).show();
//
//                progressDialog.dismiss();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AdminMaintainProductActivity.this, " Image Uploaded " , Toast.LENGTH_SHORT).show();
//
//                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//
//                        if(!task.isSuccessful()){
//                            throw task.getException();
//                        }
//                        downloadimageurl = filepath.getDownloadUrl().toString();
//                        return filepath.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//
//                        if(task.isSuccessful()){
//                            downloadimageurl = task.getResult().toString();
//                            Toast.makeText(AdminMaintainProductActivity.this, "got image uri successfully", Toast.LENGTH_SHORT).show();
//
//                            saveproductinfo();
//
//                        }
//                    }
//                });
//            }
//        });
//
//
//    }



    private void saveproductinfo() {
        HashMap<String,Object> productmap = new HashMap<>();
        productmap.put("pid",pid);
        productmap.put("pname",pname);
        productmap.put("pprice",pprice);
        productmap.put("pdesc",pdesc);
        productmap.put("pcategory",categoryname);
      //  productmap.put("pimage",downloadimageurl);

        pref.updateChildren(productmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(AdminMaintainProductActivity.this,HomeActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            Toast.makeText(AdminMaintainProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();

                        }else{
                            progressDialog.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminMaintainProductActivity.this, "ERROR : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == pickimage && resultCode == RESULT_OK && data != null){
//            imageuri = data.getData();
//            product_image_d.setImageURI(imageuri);
//        }
//    }

}