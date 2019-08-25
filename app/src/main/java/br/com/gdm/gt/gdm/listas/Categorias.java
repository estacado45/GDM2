package br.com.gdm.gt.gdm.listas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.adaptadores.ListaCategoriasAdapter;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroCategorias;
import br.com.gdm.gt.gdm.modelos.Categoria;

public class Categorias extends AppCompatActivity implements ListaCategoriasAdapter.ItemSelecionado{
   private Toolbar tbrCadastrosCategorias = null;
   private RecyclerView rcwListaCategoria = null;
   private static RecyclerView.Adapter adaptadorRcwListaCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorias);
        tbrCadastrosCategorias = (Toolbar) findViewById(R.id.tbrCadastroCategorias);
        rcwListaCategoria = (RecyclerView) findViewById(R.id.rcwCategorias);
        setSupportActionBar(tbrCadastrosCategorias);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preencherLista();
        FloatingActionButton fabNovaCategoria = (FloatingActionButton) findViewById(R.id.fabNovaCategoria);
        fabNovaCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MeuContexto.getContexto(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(Categorias.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    }else{
                        ActivityCompat.requestPermissions(Categorias.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                }else {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    FgtCadastroCategorias cadastroCategorias = new FgtCadastroCategorias();
                    cadastroCategorias.show(ft, "cadastro");
                    ListaCategoriasAdapter aux = (ListaCategoriasAdapter) rcwListaCategoria.getAdapter();
                    aux.atualizarLista();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void preencherLista(){
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ArrayList<Categoria> listaCategorias;
        listaCategorias = categoriaDAO.retornaTodasCategorias();
        rcwListaCategoria.setLayoutManager(new LinearLayoutManager(MeuContexto.getContexto()));
        rcwListaCategoria.setAdapter(new ListaCategoriasAdapter(listaCategorias, this, getSupportFragmentManager() , Categorias.this));
        adaptadorRcwListaCategorias = rcwListaCategoria.getAdapter();
    }

    public static RecyclerView.Adapter obterAdaptadorRcwListaCategorias(){
        return adaptadorRcwListaCategorias;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tbrCadastrosCategorias.setBackgroundResource(R.drawable.toolbar_cantos_arrendondados);
        }
    }

    @Override
    public void itemSelecionado(View view, int position) {

    }
}
