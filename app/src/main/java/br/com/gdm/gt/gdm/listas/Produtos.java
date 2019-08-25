package br.com.gdm.gt.gdm.listas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.adaptadores.ListaCategoriasAdapter;
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAdapterGerencial;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.banco_dados.daos.ProdutoDAO;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroCategorias;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroProdutos;
import br.com.gdm.gt.gdm.modelos.Produto;

public class Produtos extends AppCompatActivity implements ListaProdutosAdapterGerencial.ItemSelecionado{
    private RecyclerView rcwProdutos = null;
    private static RecyclerView.Adapter adaptadorRcwProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produtos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrProdutos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcwProdutos = (RecyclerView)findViewById(R.id.rcwProdutos);
        preencherLista();
        FloatingActionButton fabNovoProduto = (FloatingActionButton) findViewById(R.id.fabNovoProduto);
        fabNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MeuContexto.getContexto(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(Produtos.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    }else{
                        ActivityCompat.requestPermissions(Produtos.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                }else {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    FgtCadastroProdutos cadastroProdutos = new FgtCadastroProdutos();
                    cadastroProdutos.show(ft, "cadastro");
                }
            }
        });
    }

    public void preencherLista(){
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ArrayList<Produto> produtos = produtoDAO.obterProdutos(1);
        rcwProdutos.setLayoutManager(new LinearLayoutManager(MeuContexto.getContexto()));
        rcwProdutos.setAdapter(new ListaProdutosAdapterGerencial(produtos, this, Produtos.this, getSupportFragmentManager()));
        adaptadorRcwProdutos = rcwProdutos.getAdapter();
    }

    public static RecyclerView.Adapter obterAdaptadorRcwProdutos(){
        return adaptadorRcwProdutos;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemSelecionado(View view, int position) {

    }
}
