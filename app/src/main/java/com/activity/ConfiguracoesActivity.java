package com.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.helper.Permissao;
import com.patrickrafael.whatsappclone.R;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        //Validar permissões

        Permissao.validarPermissoes(permissoesNecessarias,this, 1);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        //Botão voltar, é necessario alterar o manifest para que a pagina mainActivity seja a pagia "pai".
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaroResultado : grantResults){
            if(permissaroResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();

            }

        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("para utilizar o app, é necessario que as permissões sejam aceitas");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}