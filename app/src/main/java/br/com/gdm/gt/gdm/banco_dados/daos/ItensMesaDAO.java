package br.com.gdm.gt.gdm.banco_dados.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.banco_dados.ConexaoBD;
import br.com.gdm.gt.gdm.arquivos.logs.LogAtividades;
import br.com.gdm.gt.gdm.modelos.Data;
import br.com.gdm.gt.gdm.modelos.ItensMesa;

/**
 * Created by estac on 21/04/2018.
 */

public class ItensMesaDAO {

    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";
    private static final String TABELA_ITENS_MESA = "ITENS_MESA";
    private static final String ITENS_MESA = "codMesaItem";
    private static final String ITENS_COD_PROD = "codProd";
    private static final String ITENS_DESC_PROD = "descricao";
    private static final String ITENS_VALOR_PROD = "valor";
    private static final String ITENS_QUANT = "quant";
    private static final String ITENS_DETALHES = "detalhes";
    private static final String ITENS_ENTREGUE = "entregue";
    private static LogAtividades logAtv = new LogAtividades();
    private String atv = "";
    private SQLiteDatabase bd = null;

    public String retornaHorarioItem(String codMesa, String codProduto){
        Cursor c = null;
        String horario = "";
        String selecao = ITENS_MESA.concat("=?")+ " and " + ITENS_COD_PROD.concat("=?");
        try{
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_ITENS_MESA, null, selecao, new String[]{codMesa,codProduto}, null, null, null);
            c.moveToFirst();
            horario = (c.getString(c.getColumnIndex(ITENS_DETALHES)));
            bd.setTransactionSuccessful();
        } catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
        }finally{
            c.close();
            fechar();
        }
        return horario;
    }

    public int alteraQtdMais(String codMesa,String codProd,int quant){
        int linhaAfetada,qtd ;
        Cursor c = null;
        linhaAfetada = 0;
        String selecao = ITENS_MESA.concat("=?")+ " and " + ITENS_COD_PROD.concat("=?");
        qtd = 0;
        ContentValues alteraQtd = new ContentValues();
        try{
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            c = bd.query(TABELA_ITENS_MESA, null, selecao, new String[]{codMesa,codProd}, null, null, null);
            c.moveToFirst();
            qtd = (c.getInt(c.getColumnIndex(ITENS_QUANT)) + quant);
            alteraQtd.put(ITENS_QUANT, qtd);
            linhaAfetada = bd.update(TABELA_ITENS_MESA, alteraQtd, selecao, new String[]{codMesa,codProd});
            bd.setTransactionSuccessful();
            atv = "Adicionado mais "+qtd +" "+ codProd +" a Mesa "+ codMesa + " !";
        } catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao adicionar mais "+qtd +" "+ codProd +" a Mesa "+ codMesa + " !";
        }finally{
            c.close();
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            }catch(NullPointerException e){
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            }catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return linhaAfetada;
    }

    public int alteraQtdMenos(String codMesa,String codProd,int quant){
        int linhaAfetada,qtd ;
        Cursor c = null;
        linhaAfetada = 0;
        qtd = 0;
        String selecao = ITENS_MESA.concat("=?")+ " and " + ITENS_COD_PROD.concat("=?");
        ContentValues alteraQtd = new ContentValues();
        try{
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            c = bd.query(TABELA_ITENS_MESA, null, selecao, new String[]{codMesa,codProd}, null, null, null);
            c.moveToFirst();
            qtd = (c.getInt(c.getColumnIndex(ITENS_QUANT)) - quant);
            if(qtd <= 0){
                linhaAfetada =  excluirItenMesa(codMesa, codProd);
                bd.setTransactionSuccessful();
            }else{
                alteraQtd.put(ITENS_QUANT, qtd);
                linhaAfetada = bd.update(TABELA_ITENS_MESA, alteraQtd, selecao, new String[]{codMesa,codProd});
                bd.setTransactionSuccessful();
                atv = "Quantidade "+qtd +" "+ codProd +" transferido com sucesso da mesa: "+codMesa;
            }
        } catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao transferir "+qtd +" "+ codProd +"  da mesa: "+codMesa;
        }finally{
            c.close();
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            }catch(NullPointerException e){
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            }catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return linhaAfetada;
    }
    public void adicionarItenMesa(String mesa, String codProd, Integer quant, boolean entregue) {
        ContentValues valoresItens = new ContentValues();
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            valoresItens.put(ITENS_MESA, mesa);
            valoresItens.put(ITENS_COD_PROD, codProd);
            valoresItens.put(ITENS_QUANT, quant);
            valoresItens.put(ITENS_DETALHES, Data.horaAtual());
            valoresItens.put(ITENS_ENTREGUE, entregue);
            bd.insert(TABELA_ITENS_MESA, null, valoresItens);
            bd.setTransactionSuccessful();
            atv = "Adicionado "+quant +" "+ codProd +" a Mesa "+ mesa + " !";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao adicionar  "+quant +" "+ codProd +" a Mesa "+ mesa + " !";
        }finally{
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            }catch(NullPointerException e){
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            }catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
    }

    public int excluirItenMesa(String mesa, String codProd) {
        int linhaAfetada = 0;
        String selecao = ITENS_MESA.concat("=?")+ " and " + ITENS_COD_PROD.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = bd.delete(TABELA_ITENS_MESA, selecao, new String[]{mesa,codProd});
            bd.setTransactionSuccessful();
            atv = "Item "+ codProd +" excluido com sucesso da mesa: "+mesa;
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao excluir Item "+ codProd +"da mesa: "+mesa;
        }finally{
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            }catch(NullPointerException e){
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            }catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return linhaAfetada;
    }

    public void alterarCodMesa(String codMesaNovo, String codMesaVelho){
        String selecao = ITENS_MESA.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues novoCodMesa = new ContentValues();
            novoCodMesa.put(ITENS_MESA, codMesaNovo);
            bd.update(TABELA_ITENS_MESA, novoCodMesa, selecao, new String[]{codMesaVelho});
            bd.setTransactionSuccessful();
            atv = "codMesa- "+ codMesaVelho + " alterada para- "+ codMesaNovo + " com sucesso!";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao alterar Mesa "+ codMesaVelho + " !";
        } finally {
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            }catch(NullPointerException e){
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            }catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
    }

    public ArrayList<ItensMesa> listarTodosItens(){
        Cursor c = null;
        ArrayList<ItensMesa> itens = new ArrayList<ItensMesa>();
        ItensMesa im ;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_ITENS_MESA, null, null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                im = new ItensMesa();
                im.setCodMesa(c.getString(c.getColumnIndex(ITENS_MESA)));
                im.setCodProd(c.getString(c.getColumnIndex(ITENS_COD_PROD)));
                im.setQuant(c.getInt(c.getColumnIndex(ITENS_QUANT)));
                im.setHorario(c.getString(c.getColumnIndex(ITENS_DETALHES)));
                im.setEntregue(c.getInt(c.getColumnIndex(ITENS_ENTREGUE))==1);
                itens.add(im);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return itens;
    }

    public ArrayList<ItensMesa> listarTodosItensNaoEntregues(){
        Cursor c = null;
        ArrayList<ItensMesa> itens = new ArrayList<ItensMesa>();
        ItensMesa im ;
        String selecao = ITENS_ENTREGUE.concat("=0");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_ITENS_MESA, null, selecao, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                im = new ItensMesa();
                im.setCodMesa(c.getString(c.getColumnIndex(ITENS_MESA)));
                im.setCodProd(c.getString(c.getColumnIndex(ITENS_COD_PROD)));
                im.setQuant(c.getInt(c.getColumnIndex(ITENS_QUANT)));
                im.setHorario(c.getString(c.getColumnIndex(ITENS_DETALHES)));
                im.setEntregue(c.getInt(c.getColumnIndex(ITENS_ENTREGUE))==0);
                itens.add(im);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return itens;
    }

    public ArrayList<ItensMesa> listarItensMesa(String codMesa){
        Cursor c = null;
        ArrayList<ItensMesa> itens = new ArrayList<ItensMesa>();
        ItensMesa im ;
        String selecao = ITENS_MESA.concat("=?");
        String sql = "select p.descricao, p.valor, im.quant, im.codMesaItem ,im.codProd,im.detalhes, im.entregue from itens_mesa im inner join produtos p on p.codProduto = im.codProd where im.codMesaItem = ?";
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.rawQuery(sql, new String[]{codMesa});
     //       c = bd.query(TABELA_ITENS_MESA, null, selecao, new String[]{codMesa}, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                im = new ItensMesa();
                im.setCodMesa(c.getString(c.getColumnIndex(ITENS_MESA)));
                im.setCodProd(c.getString(c.getColumnIndex(ITENS_COD_PROD)));
                im.setQuant(c.getInt(c.getColumnIndex(ITENS_QUANT)));
                im.setDescProd(c.getString(c.getColumnIndex(ITENS_DESC_PROD)));
                im.setValorProd(c.getDouble(c.getColumnIndex(ITENS_VALOR_PROD)));
                im.setHorario(c.getString(c.getColumnIndex(ITENS_DETALHES)));
                im.setEntregue(c.getInt(c.getColumnIndex(ITENS_ENTREGUE))==1);
                itens.add(im);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return itens;
    }

    public boolean jaExisteItem(String codMesa, String codProd){
        Cursor c = null;
        boolean confirmacao = false;
        String selecao = ITENS_MESA.concat("=?")+ " and " + ITENS_COD_PROD.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_ITENS_MESA, null, selecao, new String[]{codMesa,codProd}, null, null, null);
            if(c.moveToFirst()){
                confirmacao = true;
            }
            bd.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return confirmacao;
    }

    public boolean limparItensMesa(){
        boolean confirmacao = false;
        int conf = 0;
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            conf = bd.delete(TABELA_ITENS_MESA,null, null);
            if(conf > 0){
                confirmacao = true;
            }
            else{
                confirmacao = false;
            }
            bd.setTransactionSuccessful();
            atv = "Itens  limpos com sucesso!";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao limpar Itens!";
        } finally {
            bd.endTransaction();
            bd.close();
            try {
                logAtv.escreveLogAtividades(atv);
            }catch(NullPointerException e){
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            }catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return confirmacao;
    }

    private void fechar(){
        if((bd.isOpen())) {
            bd.endTransaction();
            bd.close();
        }
    }
}
