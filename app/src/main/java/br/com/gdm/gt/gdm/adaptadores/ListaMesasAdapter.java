package br.com.gdm.gt.gdm.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.modelos.Mesa;


public class ListaMesasAdapter extends RecyclerView.Adapter<ListaMesasAdapter.ViewHolder> {

    private final List<Mesa> valores;
    private final Context contexto;
    private final ItemSelecionado itemSelec;

    public ListaMesasAdapter(List<Mesa> items, Context ctx, ItemSelecionado selecionado) {
        valores = items;
        contexto = ctx;
        itemSelec = selecionado;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String status = "";
        holder.mesa = valores.get(position);
        holder.txtCodMesaAdapter.setText(holder.mesa.getMesa());
        holder.txtDataMesaAdapter.setText(holder.mesa.getData());
        if(holder.mesa.isStatus()){
            status = "Aberta";
        }else{
            status = "Fechada";
        }
        holder.txtStatusMesaAdapter.setText(status);
        if(position == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodMesaAdapter.setTextColor(Color.BLACK);
            holder.txtCodMesaAdapterStr.setTextColor(Color.BLACK);
            holder.txtDataMesaAdapter.setTextColor(Color.BLACK);
            holder.txtStatusMesaAdapter.setTextColor(Color.BLACK);
            holder.txtStatusMesaAdapterStr.setTextColor(Color.BLACK);

        }if((position % 2) == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodMesaAdapter.setTextColor(Color.BLACK);
            holder.txtCodMesaAdapterStr.setTextColor(Color.BLACK);
            holder.txtDataMesaAdapter.setTextColor(Color.BLACK);
            holder.txtStatusMesaAdapter.setTextColor(Color.BLACK);
            holder.txtStatusMesaAdapterStr.setTextColor(Color.BLACK);
        }else {
            holder.lntContainer.setBackgroundColor(Color.DKGRAY);
            holder.txtCodMesaAdapter.setTextColor(Color.WHITE);
            holder.txtCodMesaAdapterStr.setTextColor(Color.WHITE);
            holder.txtDataMesaAdapter.setTextColor(Color.WHITE);
            holder.txtStatusMesaAdapter.setTextColor(Color.WHITE);
            holder.txtStatusMesaAdapterStr.setTextColor(Color.WHITE);
        }
    }

    public static interface ItemSelecionado {

        public void itemSelecionado(View view, int position);

    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(itemSelec != null) {
            itemSelec.itemSelecionado(view, position);
        }
    }

    @Override
    public int getItemCount() {

        return valores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View visao;
        public final LinearLayout lntContainer;
        public final TextView txtCodMesaAdapter;
        public final TextView txtDataMesaAdapter;
        public final TextView txtCodMesaAdapterStr;
        public final TextView txtStatusMesaAdapter;
        public final TextView txtStatusMesaAdapterStr;
        public Mesa mesa;

        public ViewHolder(View view) {
            super(view);
            visao = view;
            lntContainer = (LinearLayout) visao.findViewById(R.id.layout_container);
            txtCodMesaAdapter = (TextView) visao.findViewById(R.id.codMesaAdapter);
            txtDataMesaAdapter = (TextView) visao.findViewById(R.id.dataMesaAdapter);
            txtCodMesaAdapterStr = (TextView) visao.findViewById(R.id.codMesaAdapterStr);
            txtStatusMesaAdapter = (TextView) visao.findViewById(R.id.statusMesaAdapter);
            txtStatusMesaAdapterStr = (TextView) visao.findViewById(R.id.statusMesaAdapterStr);
            visao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(visao,getAdapterPosition());
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtDataMesaAdapter.getText() + "'";
        }
    }
}