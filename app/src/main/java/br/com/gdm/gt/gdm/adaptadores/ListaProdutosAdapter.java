package br.com.gdm.gt.gdm.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.atividades.NovaMesa;
import br.com.gdm.gt.gdm.modelos.Produto;


public class ListaProdutosAdapter extends RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder> {

    private final List<Produto> produtos;
    private final Context contexto;
    private int checado = -1;

    public ListaProdutosAdapter(List<Produto> itens, Context ctx) {
        produtos = itens;
        contexto = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_produto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.prodItem = produtos.get(position);
        holder.txtCodProdAdapter.setText(holder.prodItem.getCodProduto());
        holder.txtDescProdAdapter.setText(holder.prodItem.getDescricao());
        holder.txtCategProdAdapter.setText(String.valueOf(holder.prodItem.getDescCateg()));
        holder.txtValorProdAdpter.setText(String.valueOf(holder.prodItem.getValor()));
        holder.rdoListaProdCheckItem.setChecked(checado == position);
        if(position == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodProdAdapter.setTextColor(Color.BLACK);
            holder.txtDescProdAdapter.setTextColor(Color.BLACK);
            holder.txtCategProdAdapter.setTextColor(Color.BLACK);
            holder.txtValorProdAdpter.setTextColor(Color.BLACK);
            holder.txtCodProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtDescProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtCategProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtValorProdAdpterStr.setTextColor(Color.BLACK);

        }if((position % 2) == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodProdAdapter.setTextColor(Color.BLACK);
            holder.txtDescProdAdapter.setTextColor(Color.BLACK);
            holder.txtCategProdAdapter.setTextColor(Color.BLACK);
            holder.txtValorProdAdpter.setTextColor(Color.BLACK);
            holder.txtCodProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtDescProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtCategProdAdapterStr.setTextColor(Color.BLACK);
            holder.txtValorProdAdpterStr.setTextColor(Color.BLACK);

        }else {
            holder.lntContainer.setBackgroundColor(Color.DKGRAY);
            holder.txtCodProdAdapter.setTextColor(Color.WHITE);
            holder.txtDescProdAdapter.setTextColor(Color.WHITE);
            holder.txtCategProdAdapter.setTextColor(Color.WHITE);
            holder.txtValorProdAdpter.setTextColor(Color.WHITE);
            holder.txtCodProdAdapterStr.setTextColor(Color.WHITE);
            holder.txtDescProdAdapterStr.setTextColor(Color.WHITE);
            holder.txtCategProdAdapterStr.setTextColor(Color.WHITE);
            holder.txtValorProdAdpterStr.setTextColor(Color.WHITE);
        }

        holder.rdoListaProdCheckItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NovaMesa.produtoAux = holder.prodItem;
                checado = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View visao;
        public final LinearLayout lntContainer;
        public final TextView txtCodProdAdapter;
        public final TextView txtDescProdAdapter;
        public final TextView txtCategProdAdapter;
        public final TextView txtValorProdAdpter;
        public final TextView txtCodProdAdapterStr;
        public final TextView txtDescProdAdapterStr;
        public final TextView txtCategProdAdapterStr;
        public final TextView txtValorProdAdpterStr;
        public final RadioButton rdoListaProdCheckItem;
        public Produto prodItem;

        public ViewHolder(View view) {
            super(view);
            visao = view;
            lntContainer = (LinearLayout) visao.findViewById(R.id.layout_container_lista_produtos);
            txtCodProdAdapter = (TextView) visao.findViewById(R.id.txtListaProdCod);
            txtDescProdAdapter = (TextView) visao.findViewById(R.id.txtListaProdDesc);
            txtCategProdAdapter = (TextView) visao.findViewById(R.id.txtListaProdCateg);
            txtValorProdAdpter = (TextView) visao.findViewById(R.id.txtListaProdValor);
            txtCodProdAdapterStr = (TextView) visao.findViewById(R.id.txtListaProdCodStr);
            txtDescProdAdapterStr = (TextView) visao.findViewById(R.id.txtListaProdDescStr);
            txtCategProdAdapterStr = (TextView) visao.findViewById(R.id.txtListaProdCategStr);
            txtValorProdAdpterStr = (TextView) visao.findViewById(R.id.txtListaProdValorStr);
            rdoListaProdCheckItem = (RadioButton) visao.findViewById(R.id.rdoListaProdCheckItem);
        }

        @Override
        public String toString() {
            return prodItem.toString();
        }
    }
}