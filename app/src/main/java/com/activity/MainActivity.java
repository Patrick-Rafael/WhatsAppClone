package com.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.config.ConfigFireBase;
import com.fragments.ContatosFragment;
import com.fragments.ConversasFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.patrickrafael.whatsappclone.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfigFireBase.getFireBaseAutenticacao();


        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);

        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        //Configurar abas

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(

                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversasFragment.class)
                        .add("Contatos", ContatosFragment.class)
                        .create()

        );
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.smartTabLayout);
        viewPagerTab.setViewPager(viewPager);

        //Config Search view
        searchView = findViewById(R.id.materialSerarchPrincipal);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment fragment = (ConversasFragment) adapter.getPage( 0 );
                fragment.recarregarConversas();
            }
        });


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //Usar o fragment dentro da activity
                ConversasFragment fragment = (ConversasFragment) adapter.getPage( 0 );
                if (newText != null && !newText.isEmpty() ){
                    fragment.pesquisarConversas(newText.toLowerCase(Locale.ROOT));
                }

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuSair:

                deslogarUsuario();
                finish();
                break;
            case R.id.menuConfigurações:
                abrirConfig();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void deslogarUsuario() {

        try {
            autenticacao.signOut();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void abrirConfig() {

        Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
        startActivity(intent);

    }
}