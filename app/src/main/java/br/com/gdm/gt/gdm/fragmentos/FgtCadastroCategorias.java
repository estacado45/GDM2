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
import br.com.gdm.gt.gdm.adaptadores.ListaCategoriasAdapter;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.excecoes.GDMExcecao;
import br.com.gdm.gt.gdm.listas.Categorias;
import br.com.gdm.gt.gdm.modelos.Categoria;

public class FgtCadastroCategorias extends DialogFragment {
    public Categoria categoria;
    private Toolbar tbrFgtCadCateg;
    private EditText edtCodCateg, edtDescCateg;
    private Button btnConf, btnCancel;

    public FgtCadastroCategorias() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View visao = inflater.inflate(R.layout.cadastro_categorias,container);
        tbrFgtCadCateg = (Toolbar) visao.findViewById(R.id.tbrFgtCadCateg);
        btnConf = (Button) visao.findViewById(R.id.btnConfCadastroCategorias);
        btnCancel = (Button) visao.findViewById(R.id.btnCancelCadastroCategorias);
        edtCodCateg = (EditText) visao.findViewById(R.id.edtCodigoCadastroCategoria);
        edtDescCateg = (EditText) visao.findViewById(R.id.edtDescricaoCadastroCategoria);
        if(categoria != null){
            edtCodCateg.setText(String.valueOf(categoria.getCodCategoria()));
            edtDescCateg.setText(categoria.getDescricaoCateg());
        }

        btnConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Categoria categ = new Categoria();
                    String codCateg,descCateg;
                    codCateg = edtCodCateg.getText().toString().trim();
                    descCateg = edtDescCateg.getText().toString().trim().toUpperCase();
                    CategoriaDAO categoriaDAO = new CategoriaDAO();
                    if(verificarCampos(codCateg,descCateg)) {
                        categ.setCodCategoria(Integer.parseInt(codCateg));
                        categ.setDescricaoCateg(descCateg);
                        if (categoria != null) {
                            categoriaDAO.editarCategoria(categ, String.valueOf(categoria.getCodCategoria()));
                            ListaCategoriasAdapter aux = (ListaCategoriasAdapter) Categorias.obterAdaptadorRcwListaCategorias();
                            aux.atualizarLista();
                        } else {
                           categoriaDAO.inserirCategoria(categ);
                            ListaCategoriasAdapter aux = (ListaCategoriasAdapter) Categorias.obterAdaptadorRcwListaCategorias();
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

    private boolean verificarCampos(String codCateg , String descCateg){
        boolean retorno = true;
        if(codCateg.trim().equals("")){
            edtCodCateg.setError("Digite o Código!");
            retorno = false;
        }else if(descCateg.trim().equals("")){
            edtDescCateg.setError("Digite a Descrição!");
            retorno = false;
        }
        return retorno;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tbrFgtCadCateg.setBackgroundResource(R.drawable.toolbar_cantos_arrendondados);
            edtCodCateg.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            edtDescCateg.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            btnCancel.setBackgroundResource(R.drawable.botoes_cantos_arrendondados);
            btnConf.setBackgroundResource(R.drawable.botoes_cantos_arrendondados);
        }
    }
}
