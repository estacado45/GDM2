package br.com.gdm.gt.gdm.banco_dados.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import br.com.gdm.gt.gdm.arquivos.GerenciadorArquivos;
import br.com.gdm.gt.gdm.arquivos.LeitoraProdutos;
import br.com.gdm.gt.gdm.banco_dados.ConexaoBD;
import br.com.gdm.gt.gdm.arquivos.logs.LogAtividades;
import br.com.gdm.gt.gdm.excecoes.GDMExcecao;
import br.com.gdm.gt.gdm.modelos.Produto;

/**
 * Created by estac on 22/04/2018.
 */

public class ProdutoDAO {

    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";
    private static final String TABELA_PRODUTOS = "PRODUTOS";
    private static final String COD_PROD = "codProduto";
    private static final String DESC_PROD = "descricao";
    private static final String DESC_CATEG = "descricaoCateg";
    private static final String CATEG_PROD = "codCateg";
    private static final String VALOR_PROD = "valor";
    private static LogAtividades logAtv = new LogAtividades();
    private String atv = "";
    private SQLiteDatabase bd;
    private ConexaoBD conexao;

    public ProdutoDAO(){
        conexao = ConexaoBD.getInstance();
    }

    public ArrayList<Produto> pesquisaProdutos(String descProduto){
        Cursor c = null ;
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        Produto p ;
        String selecao = DESC_PROD.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_PRODUTOS,null,selecao, new String[]{descProduto.concat("%")},null,null,null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                p = new Produto();
                p.setCodProduto((c.getString(c.getColumnIndex(COD_PROD))));
                p.setCodCateg(c.getInt(c.getColumnIndex(CATEG_PROD)));
                p.setDescCateg(c.getString(c.getColumnIndex(DESC_CATEG)));
                p.setDescricao(c.getString(c.getColumnIndex(DESC_PROD)));
                p.setValor(c.getDouble(c.getColumnIndex(VALOR_PROD)));
                produtos.add(p);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return produtos;
    }

    public boolean jaExisteCodProd(String codProd){
        Cursor c = null;
        boolean confirmacao = false;
        String selecao = COD_PROD.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_PRODUTOS, null, selecao, new String[]{codProd}, null, null, null);
            if(c.moveToFirst()){
                confirmacao = true;
            }
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return confirmacao;
    }

    public boolean jaExisteDescProd(String descProd){
        Cursor c = null ;
        boolean confirmacao = false;
        String selecao = DESC_PROD.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_PRODUTOS, null, selecao, new String[]{descProd}, null, null, null);
            if(c.moveToFirst()){
                confirmacao = true;
            }
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TABELA_PRODUTOS, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return confirmacao;
    }

    public String retornaCodProduto(String desc) {
        Cursor c = null ;
        String where =  DESC_PROD.concat("=?");
        String codigoProduto = "";
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_PRODUTOS, new String[]{COD_PROD}, where , new String[]{desc}, null, null, null);
            if(c.moveToFirst()){
                codigoProduto = c.getString(c.getColumnIndex(COD_PROD));
                bd.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally{
            c.close();
            fechar();
        }
        return codigoProduto;
    }

    public void cadastrarProdutosNaLista() throws IOException {

        ArrayList<Produto> prods;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            prods = LeitoraProdutos.retornalistaProdutos();
            ContentValues aux = new ContentValues();
            for (Produto p : prods) {
                aux.put("codProduto", p.getCodProduto());
                aux.put("descricao", p.getDescricao());
                aux.put("codCateg", p.getCodCateg());
                aux.put("valor", p.getValor());
                bd.insert(TABELA_PRODUTOS, null, aux);
                aux.clear();
            }
            bd.setTransactionSuccessful();

            Log.i(TAG_ERRO, "criou os produtos.");

        } catch (NullPointerException e) {
            Log.e(TAG_ERRO + " prod", e.getMessage());
            GerenciadorArquivos.criaDiretorioProdutos();
            prods = LeitoraProdutos.retornalistaProdutos();
            ContentValues aux = new ContentValues();
            for (Produto p : prods) {
                aux.put("codProduto", p.getCodProduto());
                aux.put("descricao", p.getDescricao());
                aux.put("codCateg", p.getCodCateg());
                aux.put("valor", p.getValor());
                bd.insert("PRODUTOS", null, aux);
                aux.clear();
            }
        } catch (Exception e) {
            Log.e(TAG_ERRO + "(Produtos)", e.getMessage());
        }finally {
            fechar();
        }

    }


    public int inserirProduto(Produto p) {
        int linhaAfetada = 0;
        ContentValues valores = new ContentValues();
        valores.put(COD_PROD, p.getCodProduto());
        valores.put(DESC_PROD, p.getDescricao());
        valores.put(CATEG_PROD, p.getCodCateg());
        valores.put(VALOR_PROD, p.getValor());
        if(jaExisteCodProd(p.getCodProduto())){
            throw new GDMExcecao("Código já Existente!");
        }else if(jaExisteDescProd(p.getDescricao())){
            throw new GDMExcecao("Descrição já Existente!");
        }else{
            try {
                bd = ConexaoBD.getInstance().getConexaoEscrita();
                bd.beginTransaction();
                linhaAfetada = (int) bd.insert(TABELA_PRODUTOS, null, valores);
                bd.setTransactionSuccessful();
                atv = "Produto "+ p.getDescricao()+" inserido com sucesso!";
            } catch (Exception e) {
                Log.e(TAG_ERRO,"PAROU AQUI"+ e.getMessage());
                atv = "Erro ao inserir produto "+ p.getDescricao()+"!";
            } finally {
                fechar();
                try {
                    logAtv.escreveLogAtividades(atv);
                    LeitoraProdutos.inserirListaProdutos(p);
                }catch(NullPointerException e){
                    GerenciadorArquivos.criaDiretorioLogs();
                    logAtv.escreveLogAtividades(atv);
                    GerenciadorArquivos.criaDiretorioProdutos();
                    LeitoraProdutos.inserirListaProdutos(p);
                }catch (Exception e) {
                    Log.e(TAG_ERRO, "PAROU MAIS AQUI"+ e.getMessage());
                }
            }
        }
        return linhaAfetada;
    }

    public int editarProduto(Produto p, String codProdAntigo) {
        int linhaAfetada = 0;
        String selecao = COD_PROD.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues valores = new ContentValues();
            valores.put(COD_PROD, p.getCodProduto());
            valores.put(DESC_PROD, p.getDescricao());
            valores.put(CATEG_PROD, p.getCodCateg());
            valores.put(VALOR_PROD, p.getValor());
            linhaAfetada = bd.update(TABELA_PRODUTOS, valores, selecao, new String[] {codProdAntigo});
            bd.setTransactionSuccessful();
            atv = "Produto editado com sucesso, de : "+ codProdAntigo + " para: " +p.getCodProduto();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao editar produto "+ codProdAntigo + "!";
        } finally {
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
                LeitoraProdutos.alteraListaProdutos(codProdAntigo, p);
            }catch(NullPointerException e){
                GerenciadorArquivos.criaDiretorioLogs();
                logAtv.escreveLogAtividades(atv);
                GerenciadorArquivos.criaDiretorioProdutos();
                LeitoraProdutos.alteraListaProdutos(codProdAntigo, p);
            }catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return linhaAfetada;
    }

    public int excluirProduto(String codProd) {
        int linhaAfetada = 0;
        String selecao = COD_PROD.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = bd.delete(TABELA_PRODUTOS, selecao,new String[] { codProd });
            bd.setTransactionSuccessful();
            atv = "Produto "+ codProd + " excluido com sucesso!";
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao excluir produto "+ codProd ;
        } finally {
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            }catch(NullPointerException e){
                GerenciadorArquivos.criaDiretorioLogs();
                logAtv.escreveLogAtividades(atv);
            }catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return linhaAfetada;
    }

    public Produto retornaProduto(String codProd) {
        Cursor c = null ;
        String where = COD_PROD.concat("=?");
        Produto p = null;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            if (!(codProd.equals(""))) {
                c = bd.query(TABELA_PRODUTOS, null, where, new String[] { codProd },	null, null, null);
                c.moveToFirst();
                p = new Produto();
                p.setCodProduto(c.getString(c.getColumnIndex(COD_PROD)));
                p.setDescricao(c.getString(c.getColumnIndex(DESC_PROD)));
                p.setCodCateg(c.getInt(c.getColumnIndex(CATEG_PROD)));
                p.setValor(c.getDouble(c.getColumnIndex(VALOR_PROD)));
                bd.setTransactionSuccessful();
            }else{
                bd.setTransactionSuccessful();
                return p;
            }
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            p = null;
        }finally{
            c.close();
            fechar();
        }
        return p;
    }

    public ArrayList<Produto> obterProdutos(int categoria) {
        Cursor c = null;
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        Produto p;
        String where = CATEG_PROD.concat("=?");
        String sql = "select p.codProduto, p.descricao, p.codCateg, p.valor, c.descricaoCateg from produtos p inner join categorias c on p.codCateg = c.codCategoria where c.codCategoria = ?";
        bd = ConexaoBD.getInstance().getConexaoLeitura();
        try {
            bd.beginTransaction();
            c = bd.rawQuery(sql,new String[]{String.valueOf(categoria)});
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                p = new Produto();
                p.setCodProduto(c.getString(c.getColumnIndex(COD_PROD)));
                p.setDescricao(c.getString(c.getColumnIndex(DESC_PROD)));
                p.setCodCateg(c.getInt(c.getColumnIndex(CATEG_PROD)));
                p.setDescCateg(c.getString(c.getColumnIndex(DESC_CATEG)));
                p.setValor(c.getDouble(c.getColumnIndex(VALOR_PROD)));
                produtos.add(p);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return produtos;
    }

    public void fechar(){
        if(!(bd == null)){
            if((bd.isOpen())) {
                bd.endTransaction();
                bd.close();
            }
        }
    }
}
