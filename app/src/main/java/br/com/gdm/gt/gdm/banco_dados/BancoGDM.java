package br.com.gdm.gt.gdm.banco_dados;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import br.com.gdm.gt.gdm.arquivos.GerenciadorArquivos;
import br.com.gdm.gt.gdm.arquivos.LeitoraProdutos;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.criptografia.Criptografia;
import br.com.gdm.gt.gdm.modelos.Produto;

public class BancoGDM extends SQLiteOpenHelper {
    private static final String TAG_INFO = "INFORMAÇÃO!";
    private static final String TAG_ERRO = "ERRO!";
    private static final String NOME_BANCO = "BDGDM.db";
    private static final int VERSAO_BD = 1;

    public BancoGDM() {
        super(MeuContexto.getContexto(), NOME_BANCO, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String criarTbl = "CREATE TABLE IF NOT EXISTS ";
        final String primaria = " (_id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        final String colunas_produtos = " codProduto TEXT, descricao TEXT, codCateg INTEGER, valor DOUBLE) ";
        final String colunas_mesas = " mesa TEXT, data DATE ,status BOOLEAN) ";
        final String colunas_usuarios = " codUsuario TEXT, nome TEXT, senha TEXT, admin BOOLEAN, master BOOLEAN) ";
        final String colunas_itens_mesa = " codMesaItem TEXT, codProd TEXT, quant INTEGER, detalhes TEXT, entregue BOOLEAN) ";
        final String colunas_servicos = " codMesaServ TEXT, descricaoServ TEXT, valorServ DOUBLE) ";
        final String colunas_pagamentos = " codMesaPgto TEXT, valorDinheiro DOUBLE, valorCartao DOUBLE) ";
        final String colunas_categorias = " codCategoria INTEGER, descricaoCateg TEXT) ";

        try {
            GerenciadorArquivos.criarDiretorios();

            db.beginTransaction();
            db.execSQL(criarTbl.concat("PRODUTOS").concat(primaria).concat(colunas_produtos));
            db.execSQL(criarTbl.concat("MESAS").concat(primaria).concat(colunas_mesas));
            db.execSQL(criarTbl.concat("ITENS_MESA").concat(primaria).concat(colunas_itens_mesa));
            db.execSQL(criarTbl.concat("USUARIOS").concat(primaria).concat(colunas_usuarios));
            db.execSQL(criarTbl.concat("SERVICOS").concat(primaria).concat(colunas_servicos));
            db.execSQL(criarTbl.concat("PAGAMENTOS").concat(primaria).concat(colunas_pagamentos));
            db.execSQL(criarTbl.concat("CATEGORIAS").concat(primaria).concat(colunas_categorias));
            Log.i(TAG_INFO, "criou as tabelas.");

            cadastrarCategoriasPreEstabelecidas(db);
            cadastrarUsuariosPreEstabelecidos(db);
            cadastrarProdutosNaLista(db);

        } catch (SQLiteException e) {
            Log.e(TAG_ERRO + "sql", e.getMessage());
        } catch (IOException e) {
            Log.e(TAG_ERRO + "sql", e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        destruirBanco();
        onCreate(db);
    }

    private void destruirBanco() {
        try {
            close();
            MeuContexto.getContexto().deleteDatabase(getDatabaseName());
        } catch (Exception e) {
            Log.e(TAG_ERRO + "des", e.getMessage());
        }
    }

    public void cadastrarProdutosNaLista(SQLiteDatabase bd) throws IOException {

        ArrayList<Produto> prods;
        try {
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
            Log.i(TAG_INFO, "criou os produtos.");

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
        }

    }

    private void cadastrarCategoriasPreEstabelecidas(SQLiteDatabase bd){
        ContentValues categorias = new ContentValues();
        categorias.put("codCategoria", 1);
        categorias.put("descricaoCateg", "BEBIDAS");
        bd.insert("categorias", null, categorias);
        categorias.clear();
        categorias.put("codCategoria", 2);
        categorias.put("descricaoCateg", "PORCOES");
        bd.insert("categorias", null, categorias);
        categorias.clear();
        categorias.put("codCategoria", 3);
        categorias.put("descricaoCateg", "REFEICOES");
        bd.insert("categorias", null, categorias);
        categorias.clear();
        categorias.put("codCategoria", 4);
        categorias.put("descricaoCateg", "DIVERSOS");
        bd.insert("CATEGORIAS", null, categorias);
        Log.i(TAG_INFO, "criou as categorias.");
        categorias.clear();
    }

    private void cadastrarUsuariosPreEstabelecidos(SQLiteDatabase bd){

        ContentValues usuario = new ContentValues();
        usuario.put("codUsuario","gerMas01");
        usuario.put("NOME", "MANUTENCAO");
        usuario.put("SENHA", Criptografia.criptografar("katita"));
        usuario.put("MASTER", true);
        usuario.put("ADMIN", true);
        bd.insert("USUARIOS", null, usuario);
        usuario.clear();

        usuario.put("codUsuario","adm01");
        usuario.put("NOME", "ADMIN");
        usuario.put("SENHA", Criptografia.criptografar("master"));
        usuario.put("MASTER", false);
        usuario.put("ADMIN", true);
        bd.insert("USUARIOS", null, usuario);
        usuario.clear();
        bd.setTransactionSuccessful();
        Log.i(TAG_INFO, "criou os usuarios.");
    }

}