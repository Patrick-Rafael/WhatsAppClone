package com.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if(Build.VERSION.SDK_INT >= 21 ){

            List<String> listaPermissoes = new ArrayList<>();

            //Percorrer permissoes passadas, verificar cada uma

            for(String permissao : permissoes ){

               Boolean temPermissao =  ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
               if(!temPermissao) listaPermissoes.add(permissao);



            }

            //Caso a lista esteja vazia, não é necessario solicitar permissoes
            if( listaPermissoes.isEmpty()) return true;
            String[] novasPermissoes = new String[ listaPermissoes.size() ];
            listaPermissoes.toArray(novasPermissoes);
            //Solicitar permissao
            ActivityCompat.requestPermissions(activity,novasPermissoes,requestCode);

        }

        return true;

    }
}
