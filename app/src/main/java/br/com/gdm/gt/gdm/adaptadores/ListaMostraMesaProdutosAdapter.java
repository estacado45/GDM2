package br.com.gdm.gt.gdm.adaptadores;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.banco_dados.daos.ItensMesaDAO;
import br.com.gdm.gt.gdm.modelos.ItensMesa;

/**
 * Created by estac on 16/05/2019.
 */

public class ListaMostraMesaProdutosAdapter extends RecyclerView.Adapter<ListaMostraMesaProdutosAdapter.ViewHolder>  {
    private ArrayList<ItensMesa> itensMesa;
    private final String TAG_ERRO = "ERRO! ";
    private ItemSelecionado itemSelec = null;
    private FragmentManager fm;

    public ListaMostraMesaProdutosAdapter(ArrayList<ItensMesa> items, FragmentManager fragmentManager, ItemSelecionado selecionado) {
        itensMesa = items;
        fm = fragmentManager;
        itemSelec = selecionado;
    }

    @Override
    public ListaMostraMesaProdutosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mostra_produto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item = itensMesa.get(position);
        holder.txtMostraMesaDescProdAdapter.setText(holder.item.getDescProd());
        holder.txtMostraMesaQtdProdAdapter.setText(String.valueOf(holder.item.getQuant()));
        holder.txtMostraMesaValorProdAdapter.setText(String.valueOf(holder.item.getValorProd()));
        holder.txtMostraMesaTotalProdAdapter.setText(String.valueOf(holder.item.getQuant() * holder.item.getValorProd()));
        if(position == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtMostraMesaDescProdAdapter.setTextColor(Color.BLACK);
            holder.txtMostraMesaDescProdAdapterEst.setTextColor(Color.BLACK);
            holder.txtMostraMesaValorProdAdapter.setTextColor(Color.BLACK);
            holder.txtMostraMesaValorProdAdapterEst.setTextColor(Color.BLACK);
            holder.txtMostraMesaQtdProdAdapter.setTextColor(Color.BLACK);
            holder.txtMostraMesaQtdProdAdapterEst.setTextColor(Color.BLACK);
            holder.txtMostraMesaTotalProdAdapter.setTextColor(Color.BLACK);
            holder.txtMostraMesatotalProdAdapterEst.setTextColor(Color.BLACK);

        }if((position % 2) == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtMostraMesaDescProdAdapter.setTextColor(Color.BLACK);
            holder.txtMostraMesaDescProdAdapterEst.setTextColor(Color.BLACK);
            holder.txtMostraMesaValorProdAdapter.setTextColor(Color.BLACK);
            holder.txtMostraMesaValorProdAdapterEst.setTextColor(Color.BLACK);
            holder.txtMostraMesaQtdProdAdapter.setTextColor(Color.BLACK);
            holder.txtMostraMesaQtdProdAdapterEst.setTextColor(Color.BLACK);
            holder.txtMostraMesaTotalProdAdapter.setTextColor(Color.BLACK);
            holder.txtMostraMesatotalProdAdapterEst.setTextColor(Color.BLACK);

        }else {
            holder.lntContainer.setBackgroundColor(Color.DKGRAY);
            holder.txtMostraMesaDescProdAdapter.setTextColor(Color.WHITE);
            holder.txtMostraMesaDescProdAdapterEst.setTextColor(Color.WHITE);
            holder.txtMostraMesaValorProdAdapter.setTextColor(Color.WHITE);
            holder.txtMostraMesaValorProdAdapterEst.setTextColor(Color.WHITE);
            holder.txtMostraMesaQtdProdAdapter.setTextColor(Color.WHITE);
            holder.txtMostraMesaQtdProdAdapterEst.setTextColor(Color.WHITE);
            holder.txtMostraMesaTotalProdAdapter.setTextColor(Color.WHITE);
            holder.txtMostraMesatotalProdAdapterEst.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return itensMesa.size();
    }

    public void atualizarLista(){
        String nomeMesa = itensMesa.get(0).getCodMesa();
        ItensMesaDAO itensMesaDAO = new ItensMesaDAO();
        itensMesa = itensMesaDAO.listarItensMesa(nomeMesa);
        notifyDataSetChanged();
    }


    public static interface ItemSelecionado {

        public void itemSelecionado(View view, int position);
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(itemSelec != null) {
            itemSelec.itemSelecionado(view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View visao;
        public final LinearLayout lntContainer;
        public final TextView txtMostraMesaDescProdAdapter;
        public final TextView txtMostraMesaDescProdAdapterEst;
        public final TextView txtMostraMesaValorProdAdapter;
        public final TextView txtMostraMesaValorProdAdapterEst;
        public final TextView txtMostraMesaQtdProdAdapter;
        public final TextView txtMostraMesaQtdProdAdapterEst;
        public final TextView txtMostraMesaTotalProdAdapter;
        public final TextView txtMostraMesatotalProdAdapterEst;

        public ItensMesa item;

        public ViewHolder(View view) {
            super(view);
            visao = view;
            lntContainer = (LinearLayout) visao.findViewById(R.id.lntMostraMesaContainer);
            txtMostraMesaDescProdAdapter = (TextView) visao.findViewById(R.id.txtMostraMesaDescProd);
            txtMostraMesaDescProdAdapterEst = (TextView) visao.findViewById(R.id.txtMostraMesaDescProdStr);
            txtMostraMesaValorProdAdapter= (TextView) visao.findViewById(R.id.txtMostraMesaValorProd);
            txtMostraMesaValorProdAdapterEst= (TextView) visao.findViewById(R.id.txtMostraMesaValorProdStr);;
            txtMostraMesaQtdProdAdapter= (TextView) visao.findViewById(R.id.txtMostraMesaQtdProd);;
            txtMostraMesaQtdProdAdapterEst= (TextView) visao.findViewById(R.id.txtMostraMesaQtdProdStr);;
            txtMostraMesaTotalProdAdapter= (TextView) visao.findViewById(R.id.txtMostraMesaTotalProd);;
            txtMostraMesatotalProdAdapterEst= (TextView) visao.findViewById(R.id.txtMostraMesaTotalProdStr);;
            visao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(visao,getAdapterPosition());
                }
            });
        }
    }
}
