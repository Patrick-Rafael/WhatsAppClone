package com.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.config.ConfigFireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.patrickrafael.whatsappclone.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfigFireBase.getFireBaseAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);

        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_principal, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){

            case R.id.menuSair:

                deslogarUsuario();
                finish();


            break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void deslogarUsuario(){

        try {
            autenticacao.signOut();

        }catch (Exception e){
            e.printStackTrace();

        }

    }
}