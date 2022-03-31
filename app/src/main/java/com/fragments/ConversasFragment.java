package com.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.Model.Conversa;
import com.Model.Usuario;
import com.activity.ChatActivity;
import com.adapter.ContatosAdapter;
import com.adapter.ConversasAdapter;
import com.config.ConfigFireBase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.helper.RecyclerItemClickListener;
import com.helper.UsuarioFireBase;
import com.patrickrafael.whatsappclone.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ConversasFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerListaConversas;
    private ConversasAdapter adapterConversa;
    private List<Conversa> listaConversa = new ArrayList<>();
    private DatabaseReference database;
    private  DatabaseReference conversasRef;
    private ChildEventListener childEventListenerConversas;


    public ConversasFragment() {

    }

    public static ConversasFragment newInstance(String param1, String param2) {
        ConversasFragment fragment = new ConversasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);


        //Adapter

        adapterConversa = new ConversasAdapter(listaConversa, getActivity());

        //Recycler
        recyclerListaConversas = view.findViewById(R.id.recyclerListaConversas);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerListaConversas.setLayoutManager(layoutManager);
        recyclerListaConversas.setHasFixedSize(true);
        recyclerListaConversas.setAdapter(adapterConversa);
        //Configura conversasRef

        database = ConfigFireBase.getFireBaseDataBase();
        String identificadorUsuario = UsuarioFireBase.getIdentificadorUsuario();

        conversasRef =  database.child("conversas").child(identificadorUsuario);

        //Clique
        recyclerListaConversas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerListaConversas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Conversa conversaSelecionada = listaConversa.get(position);

                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                i.putExtra("chatContato", conversaSelecionada.getUsuarioExibido() );
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public void pesquisarConversas(String texto){

        List<Conversa> listaConversasBusca = new ArrayList<>();

        for(Conversa conversa : listaConversa){
            String nome = conversa.getUsuarioExibido().getNome().toLowerCase(Locale.ROOT);
            String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase(Locale.ROOT);

            if( nome.contains(texto) || ultimaMensagem.contains(texto)  ){
                listaConversasBusca.add(conversa);
            }
        }

        adapterConversa = new ConversasAdapter(listaConversasBusca, getActivity());
        recyclerListaConversas.setAdapter(adapterConversa);
        adapterConversa.notifyDataSetChanged();
    }

    public void recarregarConversas(){

        adapterConversa = new ConversasAdapter(listaConversa, getActivity());
        recyclerListaConversas.setAdapter(adapterConversa);
        adapterConversa.notifyDataSetChanged();

    }

    public void recuperarConversas(){

        childEventListenerConversas =  conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                //Recuperar conversas

                Conversa conversa = snapshot.getValue(Conversa.class);

                listaConversa.add(conversa);

                adapterConversa.notifyDataSetChanged();

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

}