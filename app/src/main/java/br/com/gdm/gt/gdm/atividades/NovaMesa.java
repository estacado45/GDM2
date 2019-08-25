package br.com.gdm.gt.gdm.atividades;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAdapter;
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAlertaAdapter;
import br.com.gdm.gt.gdm.banco_dados.BancoGDM;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.banco_dados.daos.MesaDAO;
import br.com.gdm.gt.gdm.banco_dados.daos.ProdutoDAO;
import br.com.gdm.gt.gdm.fragmentos.FgtListaMesas;
import br.com.gdm.gt.gdm.modelos.Categoria;
import br.com.gdm.gt.gdm.modelos.Data;
import br.com.gdm.gt.gdm.modelos.Mesa;
import br.com.gdm.gt.gdm.modelos.Produto;

public class NovaMesa extends AppCompatActivity {

    private static final String TAG_INFO = "INFORMAÇÃO!";
    private static final String TAG_ERRO = "ERRO!";
    private Toolbar tbrNovaMesa = null;
    private ListaProdutosAlertaAdapter lpa = null;
    private Mesa mesa = null;
    public static Produto produtoAux = null;
    private EditText edtCodMesa = null;
    private EditText edtAuxQtd = null;
    private EditText edtQtdProd = null;
    private ImageButton btnQtdMenos = null;
    private ImageButton btnQtdMais = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_confirmar:
                    incluirNovaMesa();
                    Intent i = new Intent(NovaMesa.this, MostraMesa.class);
                    i.putExtra("codMesa", mesa.getMesa());
                    startActivity(i);
                    finish();
                    return true;
                case R.id.navigation_cancelar:
                      finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_mesa);
        tbrNovaMesa = (Toolbar) findViewById(R.id.tbrNovaMesa);
        setSupportActionBar(tbrNovaMesa);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navNovaMesa);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        controleTela();
    }

    private void controleTela(){
        edtCodMesa = (EditText) findViewById(R.id.edtCodNovaMesa);
        edtQtdProd = (EditText) findViewById(R.id.edtNovaMesaQtd);
        edtAuxQtd = (EditText) findViewById(R.id.edtAuxQtd);
        ViewPager vpNovaMesa = (ViewPager) findViewById(R.id.vpNovaMesaContainer);
        btnQtdMenos = (ImageButton) findViewById(R.id.btnNovaMesaMenosQtd);
        btnQtdMais = (ImageButton) findViewById(R.id.btnNovaMesaMaisQtd);
        edtQtdProd.setEnabled(false);
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        lpa = new ListaProdutosAlertaAdapter(getSupportFragmentManager(), categoriaDAO.retornaTotalCategorias());
        vpNovaMesa.setAdapter(lpa);

        btnQtdMenos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int qtd = Integer.parseInt(edtQtdProd.getText().toString());
                if(qtd > 1){
                   qtd--;
                    edtQtdProd.setText(String.valueOf(qtd));
                }
            }
        });

        btnQtdMais.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int qtd = Integer.parseInt(edtQtdProd.getText().toString());
                qtd++;
                edtQtdProd.setText(String.valueOf(qtd));
            }
        });
    }

    private void incluirNovaMesa(){
        String mesaStr = edtCodMesa.getText().toString().toUpperCase().trim();
        if(verificaCampos(mesaStr)){
            mesa = new Mesa();
            mesa.setMesa(mesaStr);
            mesa.setData(Data.dataAtual());
            mesa.setStatus(true);
            MesaDAO mesaDAO = new MesaDAO();
            mesaDAO.novaMesa(mesa,produtoAux.getCodProduto(),Integer.parseInt(edtQtdProd.getText().toString()),false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pesquisar_produtos,menu);
        SearchManager pesquisarManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView srvPesquisar;
        srvPesquisar = (SearchView) menu.findItem(R.id.icPesquisarProdutos).getActionView();
        srvPesquisar.setSearchableInfo(pesquisarManager.getSearchableInfo(getComponentName()));
        srvPesquisar.setQueryHint("Pesquisar Produtos....");
        srvPesquisar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MeuContexto.getContexto(),query,Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MeuContexto.getContexto(),newText,Toast.LENGTH_LONG).show();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.closeButton){
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tbrNovaMesa.setBackgroundResource(R.drawable.toolbar_cantos_arrendondados);
            edtCodMesa.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            edtQtdProd.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            edtAuxQtd.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            btnQtdMais.setBackgroundResource(R.drawable.botoes_cantos_arrendondados);
            btnQtdMenos.setBackgroundResource(R.drawable.botoes_cantos_arrendondados);
        }
    }

    private boolean verificaCampos(String codMesa){
        boolean retorno = true;
        if(codMesa.equals("")){
            edtCodMesa.setError("Digite o Nome da Mesa!");
            retorno = false;
        }
        if (produtoAux == null){
            Toast.makeText(MeuContexto.getContexto(),"Selecione um Produto!",Toast.LENGTH_LONG).show();
            retorno = false;
        }
        return retorno;
    }
}
