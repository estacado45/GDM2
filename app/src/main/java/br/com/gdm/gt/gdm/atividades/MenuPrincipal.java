package br.com.gdm.gt.gdm.atividades;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.arquivos.GerenciadorArquivos;
import br.com.gdm.gt.gdm.arquivos.LeitoraProdutos;
import br.com.gdm.gt.gdm.banco_dados.daos.ProdutoDAO;
import br.com.gdm.gt.gdm.listas.Categorias;
import br.com.gdm.gt.gdm.fragmentos.FgtListaMesas;
import br.com.gdm.gt.gdm.listas.Produtos;
import br.com.gdm.gt.gdm.listas.ProdutosGer;
import br.com.gdm.gt.gdm.listas.Usuarios;
import br.com.gdm.gt.gdm.modelos.Produto;

public class MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   private Toolbar toolbar;
    private final int PERMISSAO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MeuContexto.getContexto(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MenuPrincipal.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    }else{
                        ActivityCompat.requestPermissions(MenuPrincipal.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSAO);
                    }
                }else{
                    Intent i = new Intent(MenuPrincipal.this, NovaMesa.class);
                    startActivity(i);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fltContainer,new FgtListaMesas()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSAO:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(MenuPrincipal.this, NovaMesa.class);
                    startActivity(i);
                }
            }default:{
                Intent i = new Intent(MenuPrincipal.this, NovaMesa.class);
                startActivity(i);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        SearchManager srmPesquisaMan = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView srvPesquisar;
        srvPesquisar = (SearchView) menu.findItem(R.id.icPesquisar).getActionView();
        srvPesquisar.setSearchableInfo(srmPesquisaMan.getSearchableInfo(getComponentName()));
        srvPesquisar.setQueryHint(getResources().getString(R.string.pesquisar));
        srvPesquisar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                   getSupportFragmentManager().beginTransaction().replace(R.id.fltContainer,new FgtListaMesas()).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icListaMesasAbertas) {
            Toast.makeText(MenuPrincipal.this," Listar Mesas Abertas",Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.icListaMesasTodas) {
            Toast.makeText(MenuPrincipal.this," Listar Todas as Mesas",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.icMesas) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fltContainer,new FgtListaMesas()).commit();
            toolbar.setTitle(R.string.mesas);
            Toast.makeText(MenuPrincipal.this,"Mesa",Toast.LENGTH_LONG).show();
        } else if (id == R.id.icProdutos) {
            toolbar.setTitle(R.string.produtos);
            Toast.makeText(MenuPrincipal.this,"Produto",Toast.LENGTH_LONG).show();
            Intent i = new Intent(MenuPrincipal.this, ProdutosGer.class);
            startActivity(i);

        } else if (id == R.id.icListaProdAtrasados) {
            toolbar.setTitle(R.string.pedidos_atrasados);
            Toast.makeText(MenuPrincipal.this,"Pedidos Atrasados",Toast.LENGTH_LONG).show();

        } else if (id == R.id.icRelatorios) {
            Toast.makeText(MenuPrincipal.this,"Relatorios",Toast.LENGTH_LONG).show();
            Intent i = new Intent(MenuPrincipal.this, Categorias.class);
            startActivity(i);
        } else if (id == R.id.icTrocarUsuario) {
            Toast.makeText(MenuPrincipal.this,"Trocar de Usuario",Toast.LENGTH_LONG).show();
            Intent i = new Intent(MenuPrincipal.this, Usuarios.class);
            startActivity(i);

        } else if (id == R.id.icEncerrarDia) {
            toolbar.setTitle(R.string.encerrar_dia);
            ProdutoDAO produtoDAO = new ProdutoDAO();
            try {
                produtoDAO.cadastrarProdutosNaLista();
            } catch (IOException e) {
                Log.i("teste", "cadastrou");

            }
            Toast.makeText(MenuPrincipal.this,"Encerrar Dia",Toast.LENGTH_LONG).show();

        }else if (id == R.id.icConfig) {
            Toast.makeText(MenuPrincipal.this,"Configuracões",Toast.LENGTH_LONG).show();
            try {
                ArrayList<Produto> produtos =  LeitoraProdutos.retornalistaProdutos();
                ProdutoDAO produtoDAO = new ProdutoDAO();
                for (Produto p : produtos){
                    int i = 0;
                    Log.i("Contador", String.valueOf(i));
                    produtoDAO.inserirProduto(p);
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (id == R.id.icCalculadora) {
            Toast.makeText(MenuPrincipal.this,"Calculadora",Toast.LENGTH_LONG).show();
            ArrayList<HashMap<String, Object>> itens = new ArrayList<HashMap<String,Object>>();
            final PackageManager pm = getPackageManager();
            List<PackageInfo> pacs = pm.getInstalledPackages(0);
            for(PackageInfo pi : pacs){
                if((pi.packageName.toString().toLowerCase().contains("calcul"))||(pi.packageName.toString().toLowerCase().contains("calc"))){
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("appNome", pi.applicationInfo.loadLabel(pm));
                    map.put("pacoteNome", pi.packageName);
                    itens.add(map);
                }
            }
            if(itens.size()>=1){
                String nomePacote = (String) itens.get(0).get("pacoteNome");
                Intent i = pm.getLaunchIntentForPackage(nomePacote);
                if(i!= null){
                    try {
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(MenuPrincipal.this,"Erro ao abrir a calculadora!",Toast.LENGTH_LONG).show();
                        Log.e("('Metodo Calculadora')", e.getMessage());							}
                }
            }
        }else if (id == R.id.icDesligar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.finishAffinity();
            }else {
                this.finish();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            toolbar.setBackgroundResource(R.drawable.toolbar_cantos_arrendondados);
        }
    }
}