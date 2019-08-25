package br.com.gdm.gt.gdm.banco_dados.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.arquivos.GerenciadorArquivos;
import br.com.gdm.gt.gdm.banco_dados.ConexaoBD;
import br.com.gdm.gt.gdm.arquivos.logs.LogAtividades;
import br.com.gdm.gt.gdm.excecoes.GDMExcecao;
import br.com.gdm.gt.gdm.modelos.Categoria;
import br.com.gdm.gt.gdm.modelos.Produto;

/**
 * Created by estac on 23/04/2018.
 */

public class CategoriaDAO {
    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";
    private static final String TABELA_CATEGORIA = "CATEGORIAS";
    private static final String COD_CATEG = "codCategoria";
    private static final String DESC_CATEG = "descricaoCateg";
    private static LogAtividades logAtv = new LogAtividades();
    private String atv = "";
    private SQLiteDatabase bd = null;
    private ConexaoBD conexao;


    public CategoriaDAO() {
        conexao = ConexaoBD.getInstance();
    }

    public int inserirCategoria(Categoria c){
        int linhaAfetada = 0;
        ContentValues novaCategoria = new ContentValues();
        novaCategoria.put(COD_CATEG,c.getCodCategoria());
        novaCategoria.put(DESC_CATEG,c.getDescricaoCateg());
        if(jaExisteCodCategoria(c.getCodCategoria())){
            throw new GDMExcecao("Código existente em outra categoria!") ;
        }else if(jaExisteDescCategoria(c.getDescricaoCateg())){
            throw new GDMExcecao("Descrição existente em outra categoria!") ;
        }else {
            try {
                bd = ConexaoBD.getInstance().getConexaoEscrita();
                bd.beginTransaction();
                linhaAfetada = (int) bd.insert(TABELA_CATEGORIA, null, novaCategoria);
                bd.setTransactionSuccessful();
                atv = "Categoria " + c.getDescricaoCateg() + " inserida com sucesso!";
                Log.i("Categoria ", "Inserida " + linhaAfetada);

            }catch (SQLiteException e){
                Log.e(TAG_ERRO, e.getMessage());
                atv = "Erro ao inserir categoria "+ c.getDescricaoCateg()+"!";
            }finally {
                fechar();
                try {
                    logAtv.escreveLogAtividades(atv);
                }catch(NullPointerException e){
                    GerenciadorArquivos.criaDiretorioLogs();
                    logAtv.escreveLogAtividades(atv);
                }catch (Exception e) {
                    Log.e(TAG_ERRO, e.getMessage());
                }
                return linhaAfetada;
            }
        }
    }

    public int editarCategoria(Categoria c, String codCategEditar){
        int linhaAfetada = 0;
        String selecao = COD_CATEG.concat("=?");
        ContentValues editarCategoria = new ContentValues();
        editarCategoria.put(COD_CATEG,c.getCodCategoria());
        editarCategoria.put(DESC_CATEG,c.getDescricaoCateg());
        Categoria categoria = retornaCategoriaPorCodigo(codCategEditar);
        if(!(categoria.getCodCategoria() == Integer.parseInt(editarCategoria.get(COD_CATEG).toString().trim()))) {
            if (jaExisteCodCategoria(c.getCodCategoria())) {
                throw new GDMExcecao("Código existente em outra categoria!");
            }
        }
        if(!(categoria.getDescricaoCateg().equals(editarCategoria.get(DESC_CATEG).toString().trim()))){
            if(jaExisteDescCategoria(c.getDescricaoCateg())){
                throw new GDMExcecao("Descrição existente em outra categoria!", new Throwable("Valor de Campo já utilizado em outra categoria!")) ;
            }
        }
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = (int) bd.update(TABELA_CATEGORIA, editarCategoria, selecao, new String[]{String.valueOf(codCategEditar)});
            bd.setTransactionSuccessful();
            atv = "Categoria "+ c.getDescricaoCateg()+" editada com sucesso!";
        }catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao editar categoria "+ c.getDescricaoCateg()+"!";
        }finally {
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

    public int excluirCategoria(String codCategExcluir){
        int linhaAfetada = 0;
        String selecao = COD_CATEG.concat("=?");
            if(existeProdutoNaCategoria(Integer.parseInt(codCategExcluir))){
                throw new GDMExcecao("Impossível Excluir a Categoria.", new Throwable("Existem Produtos Cadastrados Nela."));
            }
           try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = (int) bd.delete(TABELA_CATEGORIA, selecao, new String[]{String.valueOf(codCategExcluir)});
            bd.setTransactionSuccessful();
            atv = "Categoria "+ codCategExcluir+" excluida com sucesso!";
        }catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao excluir categoria "+ codCategExcluir+"!";
        }finally {
               fechar();
               try {
                logAtv.escreveLogAtividades(atv);
            } catch (NullPointerException e) {
                GerenciadorArquivos.criaDiretorioLogs();
                logAtv.escreveLogAtividades(atv);
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
            return linhaAfetada;
        }
    }

    public Categoria retornaCategoriaPorCodigo(String codCateg) {
        Categoria categoria = new Categoria();
        Cursor cursor = null;
        String selecao = COD_CATEG.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            cursor = bd.query(TABELA_CATEGORIA, null, selecao, new String[]{String.valueOf(codCateg)}, null, null, null, null);
            cursor.moveToFirst();
            categoria.setCodCategoria(cursor.getInt(cursor.getColumnIndex(COD_CATEG)));
            categoria.setDescricaoCateg(cursor.getString(cursor.getColumnIndex(DESC_CATEG)));
            bd.setTransactionSuccessful();
            atv = "Categoria " + codCateg + " retornada com sucesso!";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao retornar categoria " + codCateg + "!";
        } finally {
            cursor.close();
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            } catch (NullPointerException e) {
                GerenciadorArquivos.criaDiretorioLogs();
                logAtv.escreveLogAtividades(atv);
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return categoria;
    }

    public Categoria retornaCategoriaPorDescricao(String descCateg) {
        Categoria c = new Categoria();
        Cursor cursor = null;
        String selecao = DESC_CATEG.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            cursor = bd.query(TABELA_CATEGORIA, null, selecao, new String[]{String.valueOf(descCateg)}, null, null, null, null);
            cursor.moveToFirst();
            c.setCodCategoria(cursor.getInt(cursor.getColumnIndex(COD_CATEG)));
            c.setDescricaoCateg(cursor.getString(cursor.getColumnIndex(DESC_CATEG)));
            bd.setTransactionSuccessful();
            atv = "Categoria " + descCateg + " retornada com sucesso!";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao retornar categoria " + descCateg + "!";
        } finally {
            cursor.close();
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            } catch (NullPointerException e) {
                GerenciadorArquivos.criaDiretorioLogs();
                logAtv.escreveLogAtividades(atv);
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return c;
    }

    public ArrayList<Categoria> retornaTodasCategorias() {
        ArrayList<Categoria> categorias = new ArrayList<>();
        Categoria c;
        Cursor cursor = null;
        try {
            bd = conexao.getConexaoLeitura();
            bd.beginTransaction();
            cursor = bd.query(TABELA_CATEGORIA, null, null, null, null, null, null, null);
            cursor.moveToFirst();
            for (int i = 0; i< cursor.getCount(); i++){
                c = new Categoria();
                c.setCodCategoria(cursor.getInt(cursor.getColumnIndex(COD_CATEG)));
                c.setDescricaoCateg(cursor.getString(cursor.getColumnIndex(DESC_CATEG)));
                categorias.add(c);
                cursor.moveToNext();
            }
            bd.setTransactionSuccessful();
            atv = "Categorias retornadas com sucesso!";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao retornar categorias !";
        } finally {
            cursor.close();
            fechar();
        }
            try {
                logAtv.escreveLogAtividades(atv);
            } catch (NullPointerException e) {
                GerenciadorArquivos.criaDiretorioLogs();
                // logAtv.escreveLogAtividades(atv);
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        return categorias;
    }

    private boolean jaExisteCodCategoria(Integer codigoCategoria){
        boolean retorno = false;
        String selecao = COD_CATEG.concat("=?");
        Cursor cursor = null;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            cursor = bd.query(TABELA_CATEGORIA, null, selecao, new String[]{String.valueOf(codigoCategoria)}, null, null, null, null);
            if(cursor.moveToFirst()){
                retorno = true;
            }else{
                retorno = false;
            }
            bd.setTransactionSuccessful();
        }catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            cursor.close();
            fechar();

        }
        return retorno;
    }

    private boolean jaExisteDescCategoria(String descricaoCategoria){
        boolean retorno = false;
        String selecao = DESC_CATEG.concat("=?");
        Cursor cursor = null;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            cursor = bd.query(TABELA_CATEGORIA, null, selecao, new String[]{descricaoCategoria}, null, null, null, null);
            if(cursor.moveToFirst()){
                retorno = true;
            }else {
                retorno = false;
            }
            bd.setTransactionSuccessful();
        }catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            cursor.close();
            fechar();
        }
        return retorno;
    }

    private boolean existeProdutoNaCategoria(int codCateg){
        boolean retorno = true;
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ArrayList<Produto> produtos = produtoDAO.obterProdutos(codCateg);
        if(produtos.size() < 0){
            retorno = false;
        }
     return retorno;
    }

    public int retornaTotalCategorias(){
        int numCategs = 0;
        Cursor cursor = null;
        String sql = "select count(*) as total from categorias";
        try {
            bd = conexao.getConexaoLeitura();
            bd.beginTransaction();
            cursor = bd.rawQuery(sql, null);
            cursor.moveToFirst();
            numCategs = cursor.getInt(cursor.getColumnIndex("total"));
            bd.setTransactionSuccessful();
            atv = "numero de Categorias retornadas com sucesso!";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao retornar numeero de categorias !";
        } finally {
            cursor.close();
            fechar();
        }
        try {
            logAtv.escreveLogAtividades(atv);
        } catch (NullPointerException e) {
            GerenciadorArquivos.criaDiretorioLogs();
            logAtv.escreveLogAtividades(atv);
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
        }
        return numCategs;
    }

    private void fechar(){
        if((bd.isOpen())) {
            bd.endTransaction();
            bd.close();
        }
    }
}

