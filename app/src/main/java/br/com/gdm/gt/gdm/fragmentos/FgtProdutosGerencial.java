package br.com.gdm.gt.gdm.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAdapterGerencial;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.banco_dados.daos.ProdutoDAO;
import br.com.gdm.gt.gdm.modelos.Categoria;
import br.com.gdm.gt.gdm.modelos.Produto;

/**
 * Created by estac on 26/03/2018.
 */

public class FgtProdutosGerencial extends Fragment implements ListaProdutosAdapterGerencial.ItemSelecionado{
    private static final String POSICAO_CATEGORIA = "posCateg";
    private static  ArrayList<Categoria> categorias;
    private static CategoriaDAO categoriaDAO = new CategoriaDAO();
    private TextView txtNomeCateg;
    private RecyclerView rcwListaProd;
    private static RecyclerView.Adapter adaptadorRcwProdutosGerencial;

    public FgtProdutosGerencial() {
    }

    public static FgtProdutosGerencial newInstance(int posCateg) {
        FgtProdutosGerencial fragment = new FgtProdutosGerencial();
        Bundle args = new Bundle();
        categorias = categoriaDAO.retornaTodasCategorias();
        args.putInt(POSICAO_CATEGORIA, posCateg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_produtos_gerencial, container, false);
        txtNomeCateg = (TextView) rootView.findViewById(R.id.txtNomeCategoria);
        rcwListaProd = (RecyclerView) rootView.findViewById(R.id.rcwProdutos);
        preencheLista();
        return rootView;
    }

    public void preencheLista(){
        rcwListaProd.setLayoutManager(new LinearLayoutManager(getContext()));
        int categoria = categorias.get(getArguments().getInt(POSICAO_CATEGORIA)).getCodCategoria();
        rcwListaProd.setAdapter(new ListaProdutosAdapterGerencial(listarProdutos(categoria), this , getContext(),getFragmentManager()));
        txtNomeCateg.setText(categorias.get(getArguments().getInt(POSICAO_CATEGORIA)).getDescricaoCateg());
        adaptadorRcwProdutosGerencial = rcwListaProd.getAdapter();
    }

    public  static RecyclerView.Adapter obterAdaptadorRcwProdutosGerencial(){
        return adaptadorRcwProdutosGerencial;
    }

    @Override
    public void itemSelecionado(View view, int position) {

    }

    private ArrayList<Produto> listarProdutos(Integer categoria){
        ArrayList<Produto> lista;
        ProdutoDAO produtoDAO = new ProdutoDAO();
        lista = produtoDAO.obterProdutos(categoria);
        return lista;
    }
}
