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

    public void signUp(String email , String pass ,String username,String isAdmin){
        repository.signUp(email, pass , username,isAdmin);
    }
    public void signIn(String email, String pass,String isAdmin){

        repository.signIn(email, pass , isAdmin);
    }
    public void signOut(){
        repository.signOut();
    }

}

