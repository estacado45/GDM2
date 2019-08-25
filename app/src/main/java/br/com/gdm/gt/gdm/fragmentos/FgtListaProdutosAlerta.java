package br.com.gdm.gt.gdm.fragmentos;

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
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAdapter;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.banco_dados.daos.ProdutoDAO;
import br.com.gdm.gt.gdm.modelos.Categoria;
import br.com.gdm.gt.gdm.modelos.Produto;

/**
 * Created by estac on 26/03/2018.
 */

public class FgtListaProdutosAlerta extends Fragment {
    private static final String POSICAO_CATEGORIA = "posCateg";
    private static  ArrayList<Categoria> categorias;
    private static CategoriaDAO categoriaDAO = new CategoriaDAO();

    public FgtListaProdutosAlerta() {
    }

    public static FgtListaProdutosAlerta newInstance(int posCateg) {
        FgtListaProdutosAlerta fragment = new FgtListaProdutosAlerta();
        Bundle args = new Bundle();
        categorias = categoriaDAO.retornaTodasCategorias();
        args.putInt(POSICAO_CATEGORIA, posCateg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lista_produtos, container, false);
        TextView txtNomeCateg = (TextView) rootView.findViewById(R.id.txtNomeCategoria);
        RecyclerView rcwListaProd = (RecyclerView) rootView.findViewById(R.id.rcwlistaProdutos);
        rcwListaProd.setLayoutManager(new LinearLayoutManager(getContext()));
        int categoria = categorias.get(getArguments().getInt(POSICAO_CATEGORIA)).getCodCategoria();
        rcwListaProd.setAdapter(new ListaProdutosAdapter(listarProdutos(categoria),getContext()));
        txtNomeCateg.setText(categorias.get(getArguments().getInt(POSICAO_CATEGORIA)).getDescricaoCateg());
    return rootView;
    }

    private ArrayList<Produto> listarProdutos(Integer categoria){
        ArrayList<Produto> lista;
        ProdutoDAO produtoDAO = new ProdutoDAO();
        lista = produtoDAO.obterProdutos(categoria);
    return lista;
    }
}
