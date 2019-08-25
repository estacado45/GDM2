package br.com.gdm.gt.gdm.fragmentos;


import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.adaptadores.ListaUsuariosAdapter;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.banco_dados.daos.UsuarioDAO;
import br.com.gdm.gt.gdm.excecoes.GDMExcecao;
import br.com.gdm.gt.gdm.listas.Usuarios;
import br.com.gdm.gt.gdm.modelos.Usuario;

public class FgtCadastroUsuarios extends DialogFragment {
    public Usuario usuario;
    private Toolbar tbrFgtCadUsuario;
    private EditText edtCodigoUsuario, edtNomeUsuario, edtSenhaUsuario,edtConfSenhaUsuario;
    private Button btnConf, btnCancel;

    public FgtCadastroUsuarios() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View visao = inflater.inflate(R.layout.cadastro_usuarios,container);
        tbrFgtCadUsuario = (Toolbar) visao.findViewById(R.id.tbrFgtCadUsuarios);
        btnConf = (Button) visao.findViewById(R.id.btnConfCadastroUsuarios);
        btnCancel = (Button) visao.findViewById(R.id.btnCancelCadastroUsuarios);
        edtCodigoUsuario = (EditText) visao.findViewById(R.id.edtCadastroCodigoUsuario);
        edtNomeUsuario = (EditText) visao.findViewById(R.id.edtCadastroNomeUsuario);
        edtSenhaUsuario = (EditText) visao.findViewById(R.id.edtCadastroSenhaUsuario);
        edtConfSenhaUsuario = (EditText) visao.findViewById(R.id.edtCadastroConfSenhaUsuario);
        if(usuario != null){
            edtCodigoUsuario.setText(usuario.getCodUsuario());
            edtNomeUsuario.setText(usuario.getNome());
        }

        btnConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Usuario usr = new Usuario();
                    String codigo, nome,senha,confSenha;
                    codigo = edtCodigoUsuario.getText().toString().trim();
                    nome = edtNomeUsuario.getText().toString().trim();
                    senha = edtSenhaUsuario.getText().toString().trim();
                    confSenha = edtConfSenhaUsuario.getText().toString().trim();
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    if(verificarCampos(codigo, nome, senha, confSenha)) {
                        usr.setCodUsuario(codigo);
                        usr.setNome(nome);
                        usr.setSenha(senha);
                        if (usuario != null) {
                            usr.setAdm(usuario.isAdm());
                            usr.setMaster(usuario.isMaster());
                            usuarioDAO.alteraUsuario(usr, usuario);
                            ListaUsuariosAdapter aux = (ListaUsuariosAdapter) Usuarios.obterAdaptadorRcwUsuarios();
                            aux.atualizarLista();
                        } else {
                            usr.setAdm(false);
                            usr.setMaster(false);
                            usuarioDAO.cadastrarUsuario(usr);
                            ListaUsuariosAdapter aux = (ListaUsuariosAdapter) Usuarios.obterAdaptadorRcwUsuarios();
                            aux.atualizarLista();
                        }
                        Snackbar.make(visao, "Confirmado!", Snackbar.LENGTH_LONG).show();
                        dismiss();
                    }
                }catch (SQLiteException e){
                    Toast.makeText(MeuContexto.getContexto(),R.string.alerta_erro + e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }catch (GDMExcecao e){
                    Toast.makeText(MeuContexto.getContexto(),R.string.alerta_erro + e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(MeuContexto.getContexto(),R.string.alerta_erro + e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MeuContexto.getContexto(),"Cancelar!",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        return visao;
    }

    private boolean verificarCampos(String codigo, String nome , String senha, String confSenha) {
        boolean retorno = true;

        if (codigo.trim().equals("")) {
            edtCodigoUsuario.setError("Digite o Código!");
            retorno = false;
        }
        if (nome.trim().equals("")) {
            edtNomeUsuario.setError("Digite o Nome!");
            retorno = false;
        } else if (senha.trim().equals("")) {
            edtSenhaUsuario.setError("Digite a Senha!");
            retorno = false;
        } else if (confSenha.trim().equals("")) {
            edtConfSenhaUsuario.setError("Digite a Confirmação da Senha!");
            retorno = false;
        }
        if (!(senha.trim().equals(confSenha.trim()))) {
            edtConfSenhaUsuario.setError("Senhas não conferem!");
            retorno = false;
        }
        return retorno;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tbrFgtCadUsuario.setBackgroundResource(R.drawable.toolbar_cantos_arrendondados);
            edtCodigoUsuario.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            edtNomeUsuario.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            edtSenhaUsuario.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            edtConfSenhaUsuario.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            btnCancel.setBackgroundResource(R.drawable.botoes_cantos_arrendondados);
            btnConf.setBackgroundResource(R.drawable.botoes_cantos_arrendondados);
        }
    }
}
