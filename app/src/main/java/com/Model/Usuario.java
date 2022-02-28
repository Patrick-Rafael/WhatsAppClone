package com.Model;

import com.config.ConfigFireBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.helper.UsuarioFireBase;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String nome;
    private String email;
    private String senha;
    private String id;
    private String foto;

    public Usuario() {
    }

    public void salvar() {

        DatabaseReference fireBaseRef = ConfigFireBase.getFireBaseDataBase();
        DatabaseReference usuario = fireBaseRef.child("usuarios").child(getId());

        usuario.setValue(this);
    }

    public void atualizar(){

        String idDoUsuario = UsuarioFireBase.getIdentificadorUsuario();
        DatabaseReference firebaseRef = ConfigFireBase.getFireBaseDataBase();

        DatabaseReference usuaruiRef = firebaseRef.child("usuarios")
                .child(idDoUsuario);


        Map<String, Object> valoresusuario = converterParaMap();

        usuaruiRef.updateChildren(valoresusuario);


    }

    @Exclude
    public Map<String, Object> converterParaMap(){

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getFoto());

        return usuarioMap;

    }



    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
