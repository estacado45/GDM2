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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAdapterGerencial;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.banco_dados.daos.CategoriaDAO;
import br.com.gdm.gt.gdm.banco_dados.daos.ProdutoDAO;
import br.com.gdm.gt.gdm.excecoes.GDMExcecao;
import br.com.gdm.gt.gdm.listas.Produtos;
import br.com.gdm.gt.gdm.modelos.Categoria;
import br.com.gdm.gt.gdm.modelos.Produto;

public class FgtCadastroProdutos extends DialogFragment {
    public Produto produto;
    private Toolbar tbrFgtCadastroProduto;
    private EditText edtCodProd, edtDescProd, edtValorProd;
    private Spinner spnCategProd;
    private Button btnConf, btnCancel;


    public FgtCadastroProdutos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View visao = inflater.inflate(R.layout.cadastro_produtos,container);
        final CategoriaDAO categoriaDAO = new CategoriaDAO();
        ArrayList<Categoria> categorias = categoriaDAO.retornaTodasCategorias();
        tbrFgtCadastroProduto = (Toolbar) visao.findViewById(R.id.tbrFgtCadastroProdutos);
        btnConf = (Button) visao.findViewById(R.id.btnConfirmarCadastroProduto);
        btnCancel = (Button) visao.findViewById(R.id.btnCancelarCadastroProduto);
        edtCodProd = (EditText) visao.findViewById(R.id.edtCodigoCadastroProduto);
        edtDescProd = (EditText) visao.findViewById(R.id.edtDescricaoCadastroProduto);
        edtValorProd = (EditText) visao.findViewById(R.id.edtValorCadastroProduto);
        spnCategProd = (Spinner) visao.findViewById(R.id.spnCategoriasCadastroProdutos);
        ArrayAdapter adapter = new ArrayAdapter(MeuContexto.getContexto(), R.layout.spinner_config_itens , categorias);
        spnCategProd.setAdapter(adapter);
        if(produto != null){
            int index = 0;
            for (int i = 0; i < categorias.size(); i++){
                if(produto.getDescCateg().equals(categorias.get(i).getDescricaoCateg())){
                    index = i;
                }
            }
            spnCategProd.setSelection(index);
            edtCodProd.setText(String.valueOf(produto.getCodProduto()));
            edtDescProd.setText(produto.getDescricao());
            edtValorProd.setText(String.valueOf(produto.getValor()));
        }

        btnConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String desc = spnCategProd.getSelectedItem().toString().trim();
                    Categoria categoria = categoriaDAO.retornaCategoriaPorDescricao(desc);
                    Produto prod = new Produto();
                    String codProd,descProd,valorProd;
                    codProd = edtCodProd.getText().toString().trim();
                    descProd = edtDescProd.getText().toString().trim().toUpperCase();
                    valorProd = edtValorProd.getText().toString().trim();
                    ProdutoDAO produtoDAO = new ProdutoDAO();
                    if(verificarCampos(codProd,descProd,valorProd)) {
                        prod.setCodProduto(codProd);
                        prod.setDescricao(descProd);
                        prod.setCodCateg(categoria.getCodCategoria());
                        prod.setValor(Double.parseDouble(valorProd));
                        if (produto != null){
                            produtoDAO.editarProduto(prod, String.valueOf(produto.getCodProduto()));
                            ListaProdutosAdapterGerencial aux = (ListaProdutosAdapterGerencial) FgtProdutosGerencial.obterAdaptadorRcwProdutosGerencial();
                            FgtProdutosGerencial gerencial = new FgtProdutosGerencial();
                            gerencial.preencheLista();
                            aux.atualizarLista();
                        } else {
                            produtoDAO.inserirProduto(prod);
                            ListaProdutosAdapterGerencial aux = (ListaProdutosAdapterGerencial) FgtProdutosGerencial.obterAdaptadorRcwProdutosGerencial();
                            FgtProdutosGerencial gerencial = new FgtProdutosGerencial();
                            gerencial.preencheLista();
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

    private boolean verificarCampos(String codProd, String descProd, String valorProd ){
        boolean retorno = true;
        if(codProd.trim().equals("")){
            edtCodProd.setError("Digite o Código!");
            retorno = false;
        }else if(descProd.trim().equals("")){
            edtDescProd.setError("Digite a Descrição!");
            retorno = false;
        }else if(valorProd.trim().equals("")){
            edtValorProd.setError("Digite o Valor!");
            retorno = false;
        }
        return retorno;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tbrFgtCadastroProduto.setBackgroundResource(R.drawable.toolbar_cantos_arrendondados);
            edtCodProd.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            edtDescProd.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            edtValorProd.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            spnCategProd.setBackgroundResource(R.drawable.caixa_texto_cantos_arrendondados);
            btnConf.setBackgroundResource(R.drawable.botoes_cantos_arrendondados);
            btnCancel.setBackgroundResource(R.drawable.botoes_cantos_arrendondados);
        }
    }
}
