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
import br.com.gdm.gt.gdm.banco_dados.daos.UsuarioDAO;
import br.com.gdm.gt.gdm.excecoes.GDMExcecao;
import br.com.gdm.gt.gdm.fragmentos.FgtCadastroUsuarios;
import br.com.gdm.gt.gdm.modelos.Usuario;

/**
 * Created by estac on 05/03/2019.
 */

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.ViewHolder> {
    private List<Usuario> usuarios;
    private final String TAG_ERRO = "ERRO! ";
    private final ListaUsuariosAdapter.ItemSelecionado itemSelec;
    private FragmentManager fm;
    private final Context contexto;

    public ListaUsuariosAdapter(List<Usuario> items, ItemSelecionado selecionado, FragmentManager fragmentManager, Context context) {
        usuarios = items;
        itemSelec = selecionado;
        fm = fragmentManager;
        contexto = context;
    }

    @Override
    public ListaUsuariosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_gerencial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListaUsuariosAdapter.ViewHolder holder, final int position) {
        holder.usuario = usuarios.get(position);
        holder.txtCodUsuarioAdapter.setText(holder.usuario.getCodUsuario());
        holder.txtNomeUsuarioAdapter.setText(String.valueOf(holder.usuario.getNome()));
        if(position == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodUsuarioAdapter.setTextColor(Color.BLACK);
            holder.txtCodUsuarioAdapterEst.setTextColor(Color.BLACK);
            holder.txtNomeUsuarioAdapterEst.setTextColor(Color.BLACK);
            holder.txtNomeUsuarioAdapter.setTextColor(Color.BLACK);

        }if((position % 2) == 0){
            holder.lntContainer.setBackgroundColor(Color.LTGRAY);
            holder.txtCodUsuarioAdapter.setTextColor(Color.BLACK);
            holder.txtCodUsuarioAdapterEst.setTextColor(Color.BLACK);
            holder.txtNomeUsuarioAdapterEst.setTextColor(Color.BLACK);
            holder.txtNomeUsuarioAdapter.setTextColor(Color.BLACK);

        }else {
            holder.lntContainer.setBackgroundColor(Color.DKGRAY);
            holder.txtCodUsuarioAdapter.setTextColor(Color.WHITE);
            holder.txtCodUsuarioAdapterEst.setTextColor(Color.WHITE);
            holder.txtNomeUsuarioAdapterEst.setTextColor(Color.WHITE);
            holder.txtNomeUsuarioAdapter.setTextColor(Color.WHITE);

        }
        holder.fabEditarUsuarioAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FgtCadastroUsuarios cadastroUsuarios = new FgtCadastroUsuarios();
                try {
                    cadastroUsuarios.usuario = holder.usuario;
                    cadastroUsuarios.show(fm.beginTransaction(), "cadastro");
                }catch (Exception e){
                    Log.e(TAG_ERRO, e.getMessage());
                }
            }
        });
        holder.fabExcluirUsuarioAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alerta = new AlertDialog.Builder(contexto);
                try {
                   alerta.setTitle(R.string.confirmacao);
                   alerta.setIcon(R.drawable.ic_lixeira);
                   alerta.setMessage("Excluir Usuário?");
                   alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Snackbar.make(holder.visao, R.string.operacao_cancelada, Snackbar.LENGTH_LONG).show();
                       }
                   });
                   alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           UsuarioDAO usuarioDAO = new UsuarioDAO();
                           try {
                               usuarioDAO.excluirUsuario(String.valueOf(holder.usuario.getCodUsuario()));
                               atualizarLista();
                               Snackbar.make(holder.visao, R.string.operacao_realizada, Snackbar.LENGTH_LONG).show();

                           } catch (SQLiteException e) {
                               Toast.makeText(MeuContexto.getContexto(),e.getMessage().toString() +" - " +e.getCause().toString(),Toast.LENGTH_LONG).show();
                           }catch (GDMExcecao e){
                               Toast.makeText(MeuContexto.getContexto(),e.getMessage().toString() ,Toast.LENGTH_LONG).show();

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
        return usuarios.size();
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
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = usuarioDAO.obterUsuarios();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View visao;
        public final LinearLayout lntContainer;
        public final TextView txtCodUsuarioAdapter;
        public final TextView txtCodUsuarioAdapterEst;
        public final TextView txtNomeUsuarioAdapter;
        public final TextView txtNomeUsuarioAdapterEst;
        public final FloatingActionButton fabEditarUsuarioAdapter;
        public final FloatingActionButton fabExcluirUsuarioAdapter;
        public Usuario usuario;

        public ViewHolder(View view) {
            super(view);
            visao = view;
            lntContainer = (LinearLayout) visao.findViewById(R.id.lntConteinerItemUsuarioGer);
            txtCodUsuarioAdapter = (TextView) visao.findViewById(R.id.txtCodUsuario);
            txtCodUsuarioAdapterEst = (TextView) visao.findViewById(R.id.txtCodUsuarioEst);
            txtNomeUsuarioAdapter = (TextView) visao.findViewById(R.id.txtNome);
            txtNomeUsuarioAdapterEst = (TextView) visao.findViewById(R.id.txtNomeEst);
            fabEditarUsuarioAdapter = (FloatingActionButton) visao.findViewById(R.id.fabUsuarioGerEditar);
            fabExcluirUsuarioAdapter = (FloatingActionButton) visao.findViewById(R.id.fabUsuarioGerExcluir);

            visao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(visao,getAdapterPosition());
                }
            });
        }
    }
}
