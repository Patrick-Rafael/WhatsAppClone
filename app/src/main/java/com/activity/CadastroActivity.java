package com.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.Model.Usuario;
import com.config.ConfigFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.helper.Base64Custom;
import com.helper.UsuarioFireBase;
import com.patrickrafael.whatsappclone.R;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoSenha, campoEmail;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        ips();


    }


    public void ips() {

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);

    }

    public void ValidarUsuario(View view) {


        String textNome = campoNome.getText().toString();
        String textEmail = campoEmail.getText().toString();
        String textSenha = campoSenha.getText().toString();

        if (!textNome.isEmpty()) {

            if (!textEmail.isEmpty()) {

                if (!textSenha.isEmpty()) {

                    Usuario usuario = new Usuario();

                    usuario.setNome(textNome);
                    usuario.setEmail(textEmail);
                    usuario.setSenha(textSenha);

                    CadastrarUsuario(usuario);


                } else {
                    Toast.makeText(CadastroActivity.this, "Preencha a senha", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(CadastroActivity.this, "Preencha o Email", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(CadastroActivity.this, "Preencha o nome", Toast.LENGTH_LONG).show();
        }


    }

    public void CadastrarUsuario(Usuario usuario) {

        autenticacao = ConfigFireBase.getFireBaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()

        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar Usuário", Toast.LENGTH_LONG).show();

                    UsuarioFireBase.atualizarNomeDoUsuario(usuario.getNome());

                    finish();

                    try {

                        String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setId(idUsuario);
                        usuario.salvar();

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                } else {
                    String excecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor, digite um e-mail válido";

                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Esta conta já foi cadastrada";

                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();

                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_LONG).show();

                }

            }
        });


    }
}