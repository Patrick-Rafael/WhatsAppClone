package com.helper;

import com.config.ConfigFireBase;
import com.google.firebase.auth.FirebaseAuth;

public class UsuarioFireBase {

    public static String getIdentificadorUsuario(){

        FirebaseAuth usuario = ConfigFireBase.getFireBaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64(email);

        return  identificadorUsuario;
    }


}
