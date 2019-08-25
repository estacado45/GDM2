package br.com.gdm.gt.gdm.atividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.adaptadores.ListaCategoriasAdapter;
import br.com.gdm.gt.gdm.adaptadores.ListaMostraMesaProdutosAdapter;
import br.com.gdm.gt.gdm.banco_dados.daos.ItensMesaDAO;
import br.com.gdm.gt.gdm.fragmentos.FgtAcaoItem;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroCategorias;
import br.com.gdm.gt.gdm.listas.Categorias;
import br.com.gdm.gt.gdm.modelos.Data;
import br.com.gdm.gt.gdm.modelos.ItensMesa;

/**
 * Created by estac on 09/07/2018.
 */

public class MostraMesa extends AppCompatActivity implements ListaMostraMesaProdutosAdapter.ItemSelecionado{

    private String codigoMesa;
    private Toolbar tbrMostraMesa = null;
    private ArrayList<ItensMesa> itensMesas = null;
    public static RecyclerView rcwMostraMesaProdutos = null;
    private TextView txtNumItens = null;
    private TextView txtValorTxServ = null;
    public  TextView txtValorTotal = null;
    public static double auxTotal = 0;
    private CheckBox cbxTaxaServico = null;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_adicionar:
                    finish();
                    return true;
                case R.id.navigation_fechar:
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostra_mesa);
        Intent i = getIntent();
        codigoMesa = i.getStringExtra("codMesa").trim();
        String titulo = "Mesa: " + codigoMesa;
        tbrMostraMesa = (Toolbar) findViewById(R.id.tbrMostraMesa);
        setSupportActionBar(tbrMostraMesa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbrMostraMesa.setTitle(titulo);
        tbrMostraMesa.setSubtitle("Status: " + "Aberta" + " - " + Data.dataAtual());
        rcwMostraMesaProdutos = (RecyclerView) findViewById(R.id.rcwMostraMesa);
        txtNumItens = (TextView) findViewById(R.id.txtMostraMesaNumItens);
        txtValorTotal = (TextView) findViewById(R.id.txtMostraMesaValorTotal);
        txtValorTxServ = (TextView) findViewById(R.id.txtMostraMesaTxServ);
        cbxTaxaServico = (CheckBox) findViewById(R.id.cbxMostraMesaTxServ);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navMostraMesa);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        preencherLista();
        cbxTaxaServico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                double valorTxServ = 0;
                double total = 0;
                if(isChecked){
                    valorTxServ = auxTotal * 0.1;
                    total = auxTotal + valorTxServ;
                    txtValorTxServ.setText(String.valueOf(valorTxServ));
                    txtValorTotal.setText(String.valueOf(total));
                } else{
                    txtValorTotal.setText(String.valueOf(auxTotal));
                    txtValorTxServ.setText("0.0");
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

    private void preencherLista(){
        ItensMesaDAO itensMesaDAO = new ItensMesaDAO();
        auxTotal = 0;
        itensMesas =  itensMesaDAO.listarItensMesa(codigoMesa);
        rcwMostraMesaProdutos.setLayoutManager(new LinearLayoutManager(MeuContexto.getContexto()));
        rcwMostraMesaProdutos.setAdapter(new ListaMostraMesaProdutosAdapter(itensMesas, getSupportFragmentManager(),this));
        txtNumItens.setText(String.valueOf(itensMesas.size()));
        for(ItensMesa im : itensMesas){
            auxTotal += im.getQuant() * im.getValorProd();
        }
        txtValorTotal.setText(String.valueOf(auxTotal));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void itemSelecionado(View view, int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FgtAcaoItem acaoNoItem = new FgtAcaoItem();
        acaoNoItem.itemMesa = itensMesas.get(position);
        acaoNoItem.show(ft,"acao");
        Toast.makeText(MostraMesa.this, " iTEM -> "+ itensMesas.get(position).getDescProd(),Toast.LENGTH_LONG).show();
    }
}
