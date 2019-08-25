package br.com.gdm.gt.gdm.listas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
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
import br.com.gdm.gt.gdm.adaptadores.ListaUsuariosAdapter;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.banco_dados.daos.UsuarioDAO;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroCategorias;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroUsuarios;
import br.com.gdm.gt.gdm.modelos.Categoria;
import br.com.gdm.gt.gdm.modelos.Usuario;

public class Usuarios extends AppCompatActivity implements ListaUsuariosAdapter.ItemSelecionado{
    private Toolbar tbrUsuarios;
    private RecyclerView rcwUsuarios = null;
    private static RecyclerView.Adapter adaptadorRcwUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuarios);
        tbrUsuarios = (Toolbar) findViewById(R.id.tbrUsuarios);
        rcwUsuarios = (RecyclerView) findViewById(R.id.rcwUsuarios);
        setSupportActionBar(tbrUsuarios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preencherLista();

        FloatingActionButton fabNovoUsuario = (FloatingActionButton) findViewById(R.id.fabNovoUsuario);
        fabNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MeuContexto.getContexto(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(Usuarios.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    }else{
                        ActivityCompat.requestPermissions(Usuarios.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                }else {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    FgtCadastroUsuarios cadastroUsuarios = new FgtCadastroUsuarios();
                    cadastroUsuarios.show(ft, "cadastro");
                    ListaUsuariosAdapter aux = (ListaUsuariosAdapter) rcwUsuarios.getAdapter();
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
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<Usuario> listaUsuarios;
        listaUsuarios = usuarioDAO.obterUsuarios();
        rcwUsuarios.setLayoutManager(new LinearLayoutManager(MeuContexto.getContexto()));
        rcwUsuarios.setAdapter(new ListaUsuariosAdapter(listaUsuarios, this, getSupportFragmentManager() , Usuarios.this));
        adaptadorRcwUsuarios = rcwUsuarios.getAdapter();
    }

    public static RecyclerView.Adapter obterAdaptadorRcwUsuarios(){
        return adaptadorRcwUsuarios;
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tbrUsuarios.setBackgroundResource(R.drawable.toolbar_cantos_arrendondados);
        }
    }

    @Override
    public void itemSelecionado(View view, int position) {

    }
}
