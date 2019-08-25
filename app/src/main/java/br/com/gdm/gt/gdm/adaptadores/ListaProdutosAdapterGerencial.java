package br.com.gdm.gt.gdm.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.banco_dados.daos.ProdutoDAO;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroCategorias;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroProdutos;
import br.com.gdm.gt.gdm.modelos.Produto;


public class ListaProdutosAdapterGerencial extends RecyclerView.Adapter<ListaProdutosAdapterGerencial.ViewHolder> {

    private List<Produto> produtos;
    private final Context contexto;
    private final ListaProdutosAdapterGerencial.ItemSelecionado itemSelecionado;
    private final String TAG_ERRO = "ERRO! ";
    private FragmentManager fm;

    public ListaProdutosAdapterGerencial(List<Produto> itens, ListaProdutosAdapterGerencial.ItemSelecionado selecionado, Context ctx , FragmentManager fragmentManager) {
        produtos = itens;
        contexto = ctx;
        itemSelecionado = selecionado;
        fm = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto_gerencial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.prodItem = produtos.get(position);
        holder.txtCodProdAdapter.setText(holder.prodItem.getCodProduto());
        holder.txtDescProdAdapter.setText(holder.prodItem.getDescricao());
        holder.txtCategProdAdapter.setText(String.valueOf(holder.prodItem.getDescCateg()));
        holder.txtValorProdAdpter.setText(String.valueOf(holder.prodItem.getValor()));
        if(position == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtDescProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtCategProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtValorProdAdpterStr.setTextColor(Color.BLACK);
            holder.txtCodProdAdapter.setTextColor(Color.BLACK);
            holder.txtDescProdAdapter.setTextColor(Color.BLACK);
            holder.txtCategProdAdapter.setTextColor(Color.BLACK);
            holder.txtValorProdAdpter.setTextColor(Color.BLACK);

        }if((position % 2) == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtDescProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtCategProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtValorProdAdpterStr.setTextColor(Color.BLACK);
            holder.txtCodProdAdapter.setTextColor(Color.BLACK);
            holder.txtDescProdAdapter.setTextColor(Color.BLACK);
            holder.txtCategProdAdapter.setTextColor(Color.BLACK);
            holder.txtValorProdAdpter.setTextColor(Color.BLACK);
        }else {
            holder.lntContainer.setBackgroundColor(Color.DKGRAY);
            holder.txtCodProdAdapterStr.setTextColor(Color.WHITE);
            holder.txtDescProdAdapterStr.setTextColor(Color.WHITE);
            holder.txtCategProdAdapterStr.setTextColor(Color.WHITE);
            holder.txtValorProdAdpterStr.setTextColor(Color.WHITE);
            holder.txtCodProdAdapter.setTextColor(Color.WHITE);
            holder.txtDescProdAdapter.setTextColor(Color.WHITE);
            holder.txtCategProdAdapter.setTextColor(Color.WHITE);
            holder.txtValorProdAdpter.setTextColor(Color.WHITE);
        }

        holder.fabEditarProdutoAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FgtCadastroProdutos cadastroProdutos = new FgtCadastroProdutos();
                try {
                    cadastroProdutos.produto = holder.prodItem;
                    cadastroProdutos.show(fm.beginTransaction(), "cadastro");
                }catch (Exception e){
                    Log.e(TAG_ERRO, e.getMessage());
                }
            }
        });
        holder.fabExcluirProdutoAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alerta = new AlertDialog.Builder(contexto);
                try {
                    alerta.setTitle(R.string.confirmacao);
                    alerta.setIcon(R.drawable.ic_lixeira);
                    alerta.setMessage("Excluir Produto?");
                    alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(holder.visao, R.string.operacao_cancelada, Snackbar.LENGTH_LONG).show();
                        }
                    });
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProdutoDAO produtoDAO = new ProdutoDAO();
                            try {
                                produtoDAO.excluirProduto(String.valueOf(holder.prodItem.getCodProduto()));
                                Snackbar.make(holder.visao, R.string.operacao_realizada, Snackbar.LENGTH_LONG).show();

                            } catch (SQLiteException e) {

                            }
                        }
                    });
                    alerta.show();
                }catch (Exception e){

                }
            }
        });
    }

    public static interface ItemSelecionado {

        public void itemSelecionado(View view, int position);
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(itemSelecionado != null) {
            itemSelecionado.itemSelecionado(view, position);
        }
    }

    public void atualizarLista(){
        ProdutoDAO  produtoDAO = new ProdutoDAO();
        produtos = produtoDAO.obterProdutos(1);
        notifyDataSetChanged();
        notifyAll();
    }


    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View visao;
        public final LinearLayout lntContainer;
        public final TextView txtCodProdAdapterStr;
        public final TextView txtDescProdAdapterStr;
        public final TextView txtCategProdAdapterStr;
        public final TextView txtValorProdAdpterStr;
        public final TextView txtCodProdAdapter;
        public final TextView txtDescProdAdapter;
        public final TextView txtCategProdAdapter;
        public final TextView txtValorProdAdpter;
        public final FloatingActionButton fabEditarProdutoAdapter;
        public final FloatingActionButton fabExcluirProdutoAdapter;
        public Produto prodItem;

        public ViewHolder(View view) {
            super(view);
            visao = view;
            lntContainer = (LinearLayout) visao.findViewById(R.id.layout_container_lista_produtos);
            txtCodProdAdapterStr = (TextView) visao.findViewById(R.id.txtListaProdCodStr);
            txtDescProdAdapterStr = (TextView) visao.findViewById(R.id.txtListaProdDescStr);
            txtCategProdAdapterStr = (TextView) visao.findViewById(R.id.txtListaProdCategStr);
            txtValorProdAdpterStr = (TextView) visao.findViewById(R.id.txtListaProdValorStr);
            txtCodProdAdapter = (TextView) visao.findViewById(R.id.txtListaProdCod);
            txtDescProdAdapter = (TextView) visao.findViewById(R.id.txtListaProdDesc);
            txtCategProdAdapter = (TextView) visao.findViewById(R.id.txtListaProdCateg);
            txtValorProdAdpter = (TextView) visao.findViewById(R.id.txtListaProdValor);
            fabEditarProdutoAdapter = (FloatingActionButton) visao.findViewById(R.id.fabProdGerEditar);
            fabExcluirProdutoAdapter = (FloatingActionButton) visao.findViewById(R.id.fabProdGerExcluir);
        }
    }
}