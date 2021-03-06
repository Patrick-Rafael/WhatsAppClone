package com.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.Model.Usuario;
import com.bumptech.glide.Glide;
import com.config.ConfigFireBase;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.helper.Base64Custom;
import com.helper.Permissao;
import com.helper.UsuarioFireBase;
import com.patrickrafael.whatsappclone.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };

    private ImageButton buttonGaleria, buttonCamera;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_Galeria = 200;
    private CircleImageView circleImageViewPerfil;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private EditText editeNomePerfil;
    private Usuario usuarioLogado;
    private ImageView imageAtualizarNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);


        //Configura????es iniciais
        storageReference = ConfigFireBase.getFirebaseStorage();
        identificadorUsuario = UsuarioFireBase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFireBase.getDadosUsuaruiLogado();
        //Validar permiss??es

        buttonCamera = findViewById(R.id.imageButtonCamera);
        buttonGaleria = findViewById(R.id.imageButtonGaleria);
        circleImageViewPerfil = findViewById(R.id.fotoPerfilCircle);
        editeNomePerfil = findViewById(R.id.editNomeUsuarioPerfil);
        imageAtualizarNome = findViewById(R.id.imageAtualizarNome);


        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configura????es");
        setSupportActionBar(toolbar);

        //Bot??o voltar, ?? necessario alterar o manifest para que a pagina mainActivity seja a pagia "pai".
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados do Usuario
        FirebaseUser usuario = UsuarioFireBase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();

        if (url != null) {
            Glide.with(ConfiguracoesActivity.this).load(url).into(circleImageViewPerfil);

        } else {
            circleImageViewPerfil.setImageResource(R.drawable.padrao);
        }

        editeNomePerfil.setText(usuario.getDisplayName());


        imageAtualizarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editeNomePerfil.getText().toString();
                boolean retorno = UsuarioFireBase.atualizarNomeDoUsuario(nome);
                if (retorno) {

                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualizar();
                    Toast.makeText(ConfiguracoesActivity.this, "Nome alterado com sucesso", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {

                }
                startActivityForResult(intent, SELECAO_CAMERA);

            }
        });

        buttonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {

                }
                startActivityForResult(intent, SELECAO_Galeria);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                switch (requestCode) {

                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_Galeria:
                        Uri localImagemSelecionada = data.getData();

                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                        break;
                }

                if (imagem != null) {

                    circleImageViewPerfil.setImageBitmap(imagem);

                    //Recuperar dados da imagem
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagem firebase
                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            // .child(identificadorUsuario)
                            .child(identificadorUsuario + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivity.this,
                                    "Erro ao fazer upload da imagem", Toast.LENGTH_LONG).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ConfiguracoesActivity.this,
                                    "Sucesso ao fazer upload da imagem", Toast.LENGTH_LONG).show();


                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFotoUsuario(url);
                                }
                            });

                        }
                    });


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void atualizarFotoUsuario(Uri url) {
        boolean retorno = UsuarioFireBase.atualizarFotoUsuario(url);
        if (retorno) {
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();

            Toast.makeText(ConfiguracoesActivity.this, "Sua foto foi alterada", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaroResultado : grantResults) {
            if (permissaroResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();

            }

        }

    }

    private void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permiss??es Negadas");
        builder.setMessage("para utilizar o app, ?? necessario que as permiss??es sejam aceitas");
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