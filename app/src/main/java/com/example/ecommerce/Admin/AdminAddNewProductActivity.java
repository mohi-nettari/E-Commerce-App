package com.example.ecommerce.Admin;

import static android.widget.Toast.makeText;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    Button addP;
    String categoryname,pname,pprice,pdesc,savecurrentdate,savecurrenttime
            ,productrandomkey;
    ImageView productimage,back;
    EditText et_productname,et_productdisc,et_productprice,et_productcate;
    private static final int pickimage =1;
    private Uri imageuri;
    StorageReference Pimagesref;
    private String downloadimageurl;
    private DatabaseReference pref;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        addP = findViewById(R.id.btn_add_pro);
        et_productcate = findViewById(R.id.product_cate_d);
        productimage = findViewById(R.id.add_product);
        et_productname = findViewById(R.id.product_name);
        et_productdisc = findViewById(R.id.product_disc);
        et_productprice =findViewById(R.id.product_price);
        progressDialog = new ProgressDialog(this);
        back = findViewById(R.id.backadninaddp);


                //getSupportActionBar().hide();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });

////storage ref
        Pimagesref =   FirebaseStorage.getInstance().getReference().child("ProductImages");
        pref =   FirebaseDatabase.getInstance().getReference().child("Products");


        categoryname = getIntent().getExtras().get("category").toString();
        et_productcate.setText(categoryname);
        Toast.makeText(AdminAddNewProductActivity.this, categoryname, Toast.LENGTH_SHORT).show();
// pick an image for the product
        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengallery();
            }
        });

        addP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valdateproductdata();
            }
        });


    }

    private void valdateproductdata() {
        pname = et_productname.getText().toString();
        pdesc = et_productdisc.getText().toString();
        pprice = et_productprice.getText().toString();
        categoryname = et_productcate.getText().toString();

        if(imageuri == null){
            Toast.makeText(this, "Product image is mandatory ", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pdesc) || TextUtils.isEmpty(pprice) || TextUtils.isEmpty(pname)  || TextUtils.isEmpty(categoryname)){
            Toast.makeText(this, "Product information are not complete ", Toast.LENGTH_SHORT).show();
        }else {
            storeproductinformation();
        }



    }

    private void storeproductinformation() {

        progressDialog.setTitle("Add new product ");
        progressDialog.setMessage("Pleas wait while we are adding the new product...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM , DD , yyyy");
        savecurrentdate = currentdate.format(calendar.getTime());
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:MM:SS a");
        savecurrenttime = currenttime.format(calendar.getTime());

        productrandomkey = savecurrentdate + savecurrenttime;
        StorageReference filepath = Pimagesref.child(productrandomkey+".jpg");
        final UploadTask uploadTask =filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "ERROR : "+ message , Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               // Toast.makeText(AdminAddNewProductActivity.this, " Image Uploaded " , Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadimageurl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){
                            downloadimageurl = task.getResult().toString();
                           // Toast.makeText(AdminAddNewProductActivity.this, "got image uri successfully", Toast.LENGTH_SHORT).show();

                            saveproductinfo();

                        }
                    }
                });
            }
        });


    }

    private void saveproductinfo() {
        HashMap<String,Object> productmap = new HashMap<>();
        productmap.put("pid",productrandomkey);
        productmap.put("date",savecurrentdate);
        productmap.put("time",savecurrenttime);
        productmap.put("pname",pname);
        productmap.put("pprice",pprice);
        productmap.put("pdesc",pdesc);
        productmap.put("pcategory",categoryname);
        productmap.put("pimage",downloadimageurl);

        pref.child(productrandomkey).updateChildren(productmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();

                        }else{
                            progressDialog.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "ERROR : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void opengallery() {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,pickimage);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pickimage && resultCode == RESULT_OK && data != null){
            imageuri = data.getData();
            productimage.setImageURI(imageuri);
        }
    }
}


