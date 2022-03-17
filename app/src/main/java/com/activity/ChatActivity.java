package com.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.Mensagem;
import com.Model.Usuario;
import com.bumptech.glide.Glide;
import com.config.ConfigFireBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.helper.Base64Custom;
import com.helper.UsuarioFireBase;
import com.patrickrafael.whatsappclone.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioDestinatario;
    private FloatingActionButton buttonMenssagem;
    private EditText editMensagem;
    private String idUsuarioRementente, idUsuarioDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbarChat);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Config inicial
        ids();

        //Recupera dados do usuario remetente
        idUsuarioRementente = UsuarioFireBase.getIdentificadorUsuario();

        //Pega dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
            textViewNome.setText(usuarioDestinatario.getNome());
            //Recuperar foto
            String foto = usuarioDestinatario.getFoto();
            if (foto != null) {
                Uri url = Uri.parse(usuarioDestinatario.getFoto());
                Glide.with(ChatActivity.this).load(url).into(circleImageViewFoto);
            } else {
                circleImageViewFoto.setImageResource(R.drawable.padrao);
            }

            //Recuperar dados usuario destinatario
            idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());

        }


    }

    public void enviarMensagem(View view) {

        String textoMensagem = editMensagem.getText().toString();
        if (!textoMensagem.isEmpty()) {


            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idUsuarioRementente);
            mensagem.setMensagem(textoMensagem);

            //Salvar Mensagem para o Remetente
            salvarMensagem(idUsuarioRementente, idUsuarioDestinatario, mensagem);

        } else {
            Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();

        }

    }

    private void salvarMensagem(String idRementente, String idDestinatario, Mensagem mensagem) {

        DatabaseReference database = ConfigFireBase.getFireBaseDataBase();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRementente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        //Limpar texto

        editMensagem.setText("");


    }

    public void ids() {

        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageViewChat);
        buttonMenssagem = findViewById(R.id.fabEnviar);
        editMensagem = findViewById(R.id.editMensagem);

    }
}