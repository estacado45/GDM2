package br.com.gdm.gt.gdm.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.adaptadores.ListaMesasAdapter;
import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.atividades.MostraMesa;
import br.com.gdm.gt.gdm.banco_dados.daos.MesaDAO;
import br.com.gdm.gt.gdm.modelos.Data;
import br.com.gdm.gt.gdm.modelos.Mesa;


public class FgtListaMesas extends Fragment implements ListaMesasAdapter.ItemSelecionado{

    private static ArrayList<Mesa> lista  = new ArrayList<Mesa>();
    private RecyclerView rcwListaMesas = null;

    public static FgtListaMesas newInstance(int columnCount) {
        FgtListaMesas fragment = new FgtListaMesas();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        MesaDAO mesaDAO = new MesaDAO();
        lista = mesaDAO.obterMesas();
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            rcwListaMesas = (RecyclerView) view;
            rcwListaMesas.setLayoutManager(new LinearLayoutManager(context));
            rcwListaMesas.setAdapter(new ListaMesasAdapter(lista, MeuContexto.getContexto(),this));
        }
        return view;
    }

    @Override
    public void itemSelecionado(View view, int position) {
        Mesa m = lista.get(position);
        Intent intent = new Intent(MeuContexto.getContexto(), MostraMesa.class);
        intent.putExtra("codMesa",m.getMesa());
        startActivity(intent);
    }
}