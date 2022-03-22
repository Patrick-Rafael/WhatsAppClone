package com.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Model.Mensagem;
import com.bumptech.glide.Glide;
import com.helper.UsuarioFireBase;
import com.patrickrafael.whatsappclone.R;

import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder> {

    private List<Mensagem> mensagems;
    private Context context;
    private static final int TIPO_DESTINATARIO = 1;
    private static final int TIPO_REMETENTE = 0;


    public MensagensAdapter(List<Mensagem> lista, Context c) {
        this.mensagems = lista;
        this.context = c;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        //Verifica qual layout vai retornar
        View item = null;
        if (viewType == TIPO_REMETENTE) {


            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagens_remetente, parent, false);

        } else if (viewType == TIPO_DESTINATARIO) {

            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagens_destinatario, parent, false);

        }

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Mensagem mensagem = mensagems.get(position);

        String msg =mensagem.getMensagem();
        String img =mensagem.getImagem();

        if(img != null ){

            Uri url = Uri.parse( img );
            Glide.with(context).load(url).into(holder.imagem);

            //EsconderTexti
            holder.mensagem.setVisibility(View.GONE);

        }else {
            holder.mensagem.setText(msg);

            //Esconder Imagem

            holder.imagem.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mensagems.size();
    }

    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = mensagems.get(position);

        String idUsuario = UsuarioFireBase.getIdentificadorUsuario();

        if (idUsuario.equals(mensagem.getIdUsuario())) {
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mensagem;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mensagem = itemView.findViewById(R.id.textMensagemTexto);
            imagem = itemView.findViewById(R.id.imageMensagemFoto);
        }
    }

}
