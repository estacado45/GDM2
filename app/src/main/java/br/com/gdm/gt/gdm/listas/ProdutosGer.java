package br.com.gdm.gt.gdm.listas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAdapterGerencial;
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAuxGerAdapter;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.banco_dados.daos.ProdutoDAO;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroProdutos;
import br.com.gdm.gt.gdm.modelos.Categoria;
import br.com.gdm.gt.gdm.modelos.Produto;

public class ProdutosGer extends AppCompatActivity implements ListaProdutosAdapterGerencial.ItemSelecionado{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produtos_gerencial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrProdutosGer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preencherLista();
        FloatingActionButton fabNovoProduto = (FloatingActionButton) findViewById(R.id.fabNovoProdutoGer);
        fabNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MeuContexto.getContexto(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(ProdutosGer.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    }else{
                        ActivityCompat.requestPermissions(ProdutosGer.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
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
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ArrayList<Categoria> categorias = categoriaDAO.retornaTodasCategorias();
        ListaProdutosAuxGerAdapter lpag = new ListaProdutosAuxGerAdapter(getSupportFragmentManager(),categorias.size());
        ViewPager vpnProdutosGerencial = (ViewPager) findViewById(R.id.vpnProdutosGerencial);
        vpnProdutosGerencial.setAdapter(lpag);
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
