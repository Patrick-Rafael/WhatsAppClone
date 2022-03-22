package com.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.Mensagem;
import com.Model.Usuario;
import com.adapter.MensagensAdapter;
import com.bumptech.glide.Glide;
import com.config.ConfigFireBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.helper.Base64Custom;
import com.helper.UsuarioFireBase;
import com.patrickrafael.whatsappclone.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioDestinatario;
    private FloatingActionButton buttonMenssagem;
    private EditText editMensagem;
    private String idUsuarioRementente, idUsuarioDestinatario;
    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapterMensagens;
    private List<Mensagem> mensagens = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;


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

        //Configurar adapter Mensagens
        adapterMensagens = new MensagensAdapter(mensagens, getApplicationContext());

        //Configurar Recycler Mensagens
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapterMensagens);

        database = ConfigFireBase.getFireBaseDataBase();
        mensagensRef = database.child("mensagens")
                .child(idUsuarioRementente)
                .child(idUsuarioDestinatario);


    }

    public void enviarMensagem(View view) {

        String textoMensagem = editMensagem.getText().toString();
        if (!textoMensagem.isEmpty()) {


            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idUsuarioRementente);
            mensagem.setMensagem(textoMensagem);

            //Salvar Mensagem para o Remetente
            salvarMensagem(idUsuarioRementente, idUsuarioDestinatario, mensagem);

            //Salvar Mensagem para o Destinatario
            salvarMensagem(idUsuarioDestinatario, idUsuarioRementente, mensagem);



        } else {
            Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();

        }

    }

    private void salvarMensagem(String idRementente, String idDestinatario, Mensagem mensagem) {

        DatabaseReference database = ConfigFireBase.getFireBaseDataBase();
        DatabaseReference mensagensRef  = database.child("mensagens");

                mensagensRef.child(idRementente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        //Limpar texto

        editMensagem.setText("");


    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagem();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagem(){

       childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Mensagem mensagem = snapshot.getValue(Mensagem.class);
               mensagens.add(mensagem);
               adapterMensagens.notifyDataSetChanged();
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

    }

    public void ids() {

        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageViewChat);
        buttonMenssagem = findViewById(R.id.fabEnviar);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

    }
}