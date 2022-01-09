package com.example.ecommerce.ViewModel;

import android.app.Application;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce.Repository.AuthRepo;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel{



    //private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseUser currentUser;
    private AuthRepo repository;

//    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
//        return firebaseUserMutableLiveData;
//    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public AuthViewModel(@NonNull Application application) {
        super(application);

        repository = new AuthRepo (application);
        currentUser = repository.getCurrentUser();
       // firebaseUserMutableLiveData = repository.getFirebaseUserMutableLiveData();
    }

    public void signUp(String email , String pass ,String username,String isAdmin,String admincode){
        repository.signUp(email, pass , username,isAdmin,admincode);
    }
    public void signIn(String email, String pass,String isAdmin,String AdminCode){

        repository.signIn(email, pass , isAdmin,AdminCode);
    }
    public void signOut(){
        repository.signOut();
    }

}

