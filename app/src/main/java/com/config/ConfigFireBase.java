package com.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFireBase {


    private static DatabaseReference dataBase;
    private static FirebaseAuth auth;
    private static StorageReference storage;

    //Instancia do FireBaseDataBase
    public static DatabaseReference getFireBaseDataBase(){
        if(dataBase == null){
            dataBase = FirebaseDatabase.getInstance().getReference();
        }

        return dataBase;
    }



    //Instancia do FireBaseAuth

    public static FirebaseAuth getFireBaseAutenticacao(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();

        }
        return auth;
    }

    public static StorageReference getFirebaseStorage(){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;

    }

}
