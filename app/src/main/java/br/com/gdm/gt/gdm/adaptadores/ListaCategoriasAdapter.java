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
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.excecoes.GDMExcecao;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroCategorias;
import br.com.gdm.gt.gdm.modelos.Categoria;

/**
 * Created by estac on 05/03/2019.
 */

public class ListaCategoriasAdapter extends RecyclerView.Adapter<ListaCategoriasAdapter.ViewHolder> {
    private List<Categoria> valores;
    private final String TAG_ERRO = "ERRO! ";
    private final ListaCategoriasAdapter.ItemSelecionado itemSelec;
    private FragmentManager fm;
    private final Context contexto;

    public ListaCategoriasAdapter(List<Categoria> items, ItemSelecionado selecionado, FragmentManager fragmentManager, Context context) {
        valores = items;
        itemSelec = selecionado;
        fm = fragmentManager;
        contexto = context;
    }

    @Override
    public ListaCategoriasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria_gerencial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListaCategoriasAdapter.ViewHolder holder, final int position) {
        holder.categoria = valores.get(position);
        holder.txtCodCategoriaAdapter.setText(String.valueOf(holder.categoria.getCodCategoria()));
        holder.txtDescCategoriaAdapter.setText(holder.categoria.getDescricaoCateg());
        if(position == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodCategoriaAdapterEst.setTextColor(Color.BLACK);
            holder.txtCodCategoriaAdapter.setTextColor(Color.BLACK);
            holder.txtDescCategoriaAdapterEst.setTextColor(Color.BLACK);
            holder.txtDescCategoriaAdapter.setTextColor(Color.BLACK);

        }if((position % 2) == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodCategoriaAdapterEst.setTextColor(Color.BLACK);
            holder.txtCodCategoriaAdapter.setTextColor(Color.BLACK);
            holder.txtDescCategoriaAdapterEst.setTextColor(Color.BLACK);
            holder.txtDescCategoriaAdapter.setTextColor(Color.BLACK);

        }else {
            holder.lntContainer.setBackgroundColor(Color.DKGRAY);
            holder.txtCodCategoriaAdapterEst.setTextColor(Color.WHITE);
            holder.txtCodCategoriaAdapter.setTextColor(Color.WHITE);
            holder.txtDescCategoriaAdapterEst.setTextColor(Color.WHITE);
            holder.txtDescCategoriaAdapter.setTextColor(Color.WHITE);

        }
        holder.fabEditarCategoriaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FgtCadastroCategorias cadastroCategorias = new FgtCadastroCategorias();
                try {
                    cadastroCategorias.categoria = holder.categoria;
                    cadastroCategorias.show(fm.beginTransaction(), "cadastro");
                }catch (Exception e){
                    Log.e(TAG_ERRO, e.getMessage());
                }
            }
        });
        holder.fabExcluirCategoriaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alerta = new AlertDialog.Builder(contexto);
                try {
                   alerta.setTitle(R.string.confirmacao);
                   alerta.setIcon(R.drawable.ic_lixeira);
                   alerta.setMessage("Excluir Categoria?");
                   alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Snackbar.make(holder.visao, R.string.operacao_cancelada, Snackbar.LENGTH_LONG).show();
                       }
                   });
                   alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           CategoriaDAO categoriaDAO = new CategoriaDAO();
                           try {
                               categoriaDAO.excluirCategoria(String.valueOf(holder.categoria.getCodCategoria()));
                               atualizarLista();
                               Snackbar.make(holder.visao, R.string.operacao_realizada, Snackbar.LENGTH_LONG).show();

                           } catch (SQLiteException e) {
                               Toast.makeText(MeuContexto.getContexto(),e.getMessage().toString() +" - " +e.getCause().toString(),Toast.LENGTH_LONG).show();
                           }catch (GDMExcecao e){
                               Toast.makeText(MeuContexto.getContexto(),e.getMessage().toString() +" - " +e.getCause().toString(),Toast.LENGTH_LONG).show();

                           }
                       }
                   });
                   alerta.show();
               }catch(Exception e){
                    Toast.makeText(MeuContexto.getContexto(),e.getMessage().toString() +" - " +e.getCause().toString(),Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return valores.size();
    }

    public static interface ItemSelecionado {

        public void itemSelecionado(View view, int position);
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(itemSelec != null) {
            itemSelec.itemSelecionado(view, position);
        }
    }

    public void atualizarLista(){
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        valores = categoriaDAO.retornaTodasCategorias();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View visao;
        public final LinearLayout lntContainer;
        public final TextView txtCodCategoriaAdapter;
        public final TextView txtDescCategoriaAdapter;
        public final TextView txtCodCategoriaAdapterEst;
        public final TextView txtDescCategoriaAdapterEst;
        public final FloatingActionButton fabEditarCategoriaAdapter;
        public final FloatingActionButton fabExcluirCategoriaAdapter;
        public Categoria categoria;

        public ViewHolder(View view) {
            super(view);
            visao = view;
            lntContainer = (LinearLayout) visao.findViewById(R.id.lntConteinerItemCategGer);
            txtCodCategoriaAdapter = (TextView) visao.findViewById(R.id.txtCodCategoria);
            txtDescCategoriaAdapter = (TextView) visao.findViewById(R.id.txtDescricaoCateg);
            txtCodCategoriaAdapterEst = (TextView) visao.findViewById(R.id.txtCodCategoriaEst);
            txtDescCategoriaAdapterEst = (TextView) visao.findViewById(R.id.txtDescCategoriaEst);
            fabEditarCategoriaAdapter = (FloatingActionButton) visao.findViewById(R.id.fabCategGerEditar);
            fabExcluirCategoriaAdapter = (FloatingActionButton) visao.findViewById(R.id.fabCategGerExcluir);

            visao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(visao,getAdapterPosition());
                }
            });
        }
    }
}
