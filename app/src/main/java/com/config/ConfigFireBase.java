package com.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFireBase {


    private static DatabaseReference dataBase;
    private static FirebaseAuth auth;

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



}
