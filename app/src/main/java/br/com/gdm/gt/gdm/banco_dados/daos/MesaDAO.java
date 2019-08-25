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
import br.com.gdm.gt.gdm.modelos.Mesa;

/**
 * Created by estac on 19/04/2018.
 */

public class MesaDAO {
    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";
    private static final String TABELA_MESAS = "MESAS";
    private static final String MESA = "mesa";
    private static final String MESA_DATA = "data";
    private static final String MESA_STATUS = "status";
    private static LogAtividades logAtv = new LogAtividades();
    private String atv = "";
    private SQLiteDatabase bd = null;

    public void novaMesa(Mesa m,String codProduto, Integer quantProd, boolean entregue) {
        ItensMesaDAO itemDAO = new ItensMesaDAO();
        boolean certo = true;
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues valoresMesa = new ContentValues();
            valoresMesa.put(MESA, m.getMesa().toUpperCase());
            valoresMesa.put(MESA_STATUS, m.isStatus());
            valoresMesa.put(MESA_DATA, Data.dataAtual());
            bd.insert(TABELA_MESAS, null, valoresMesa);
            bd.setTransactionSuccessful();
            Log.i(TAG_INFORMACAO,"Mesa "+m.getMesa()+" inserida!");
            atv = "Nova mesa adicionada com o codigo: " + m.getMesa();
        } catch (SQLiteException e) {
            certo = false;
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao adicionar Nova mesa  com o codigo: " + m.getMesa();
        }finally{
            fechar();
            if(certo){
                itemDAO.adicionarItenMesa(m.getMesa(),codProduto,quantProd,entregue);
            }
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

    public boolean jaExiste(String codMesa){
        Cursor c = null;
        boolean confirmacao = false;
        String selecao = MESA.concat("=?") + " and " + MESA_STATUS.concat("=1");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_MESAS, null, selecao, new String[]{codMesa}, null, null, null);
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

    public boolean estaAberta(String codMesa){
        Cursor c = null;
        boolean confirmacao = false;
        String selecao = MESA.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_MESAS, null, selecao, new String[]{codMesa}, null, null, null);
            c.moveToFirst();
            if(c.getInt(c.getColumnIndex(MESA_STATUS)) == 1){
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

    public boolean jaExisteTodas(String codMesa){
        Cursor c = null;
        boolean confirmacao = false;
        String selecao = MESA.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_MESAS, null, selecao, new String[]{codMesa}, null, null, null);
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

    public boolean limparMesas(){
        boolean confirmacao = false;
        int conf = 0;
        ItensMesaDAO itemDAO = new ItensMesaDAO();
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            itemDAO.limparItensMesa();
            conf = bd.delete(TABELA_MESAS,null, null);
            if(conf > 0){
                confirmacao = true;
            }
            else{
                confirmacao = false;
            }
            bd.setTransactionSuccessful();
            atv = "Mesas limpas com sucesso!";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao limpar mesas!";
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
        return confirmacao;
    }

    public ArrayList<Mesa> pesquisaMesa(String codMesa){
        Cursor c = null;
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        Mesa m ;
        String selecao = MESA.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_MESAS,null, selecao, new String[]{codMesa.concat("%")},null,null,null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                m = new Mesa();
                m.setMesa((c.getString(c.getColumnIndex(MESA))));
                m.setData(c.getString(c.getColumnIndex(MESA_DATA)));
                m.setStatus(c.getInt(c.getColumnIndex(MESA_STATUS))==1);
                if(m.isStatus()){
                    mesas.add(m);
                }
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return mesas;
    }

    public ArrayList<Mesa> obterMesas() {
        Cursor c = null;
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        Mesa m ;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_MESAS, null, null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                m = new Mesa();
                m.setMesa((c.getString(c.getColumnIndex(MESA))));
                m.setData(c.getString(c.getColumnIndex(MESA_DATA)));
                m.setStatus(c.getInt(c.getColumnIndex(MESA_STATUS))==1);
                mesas.add(m);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return mesas;
    }

    public ArrayList<Mesa> obterMesasAbertas() {
        Cursor c = null;
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        Mesa m ;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_MESAS, null, null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                m = new Mesa();
                m.setMesa((c.getString(c.getColumnIndex(MESA))));
                m.setData(c.getString(c.getColumnIndex(MESA_DATA)));
                m.setStatus(c.getInt(c.getColumnIndex(MESA_STATUS))==1);
                if(m.isStatus()){
                    mesas.add(m);
                }
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            c.close();
            fechar();
        }
        return mesas;
    }

    public int alteraCodMesa(String codMesa,String codMesaVelho){
        int linhaAfetada = 0;
        String selecao = MESA.concat("=?");
        ItensMesaDAO itemDAO = new ItensMesaDAO();
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues novoCodMesa = new ContentValues();
            novoCodMesa.put(MESA, codMesa);
            linhaAfetada = bd.update(TABELA_MESAS, novoCodMesa, selecao, new String[] { codMesaVelho });
            itemDAO.alterarCodMesa(codMesa,codMesaVelho);

            bd.setTransactionSuccessful();
            atv = "Mesa- "+ codMesaVelho + " alterada para- "+ codMesa + " com sucesso!";
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao alterar Mesa "+ codMesa + " !";
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
        return linhaAfetada;
    }

    public int fecharMesa(String codMesa){
        int linhaAfetada,i;
        linhaAfetada = 0;
        i = 1;
        String aux = "";
        String selecao = MESA.concat("=?") + " and " + MESA_STATUS.concat("=1");

        if(!codMesa.contains("(1)")){
            aux = codMesa.concat("(1)");
        } else{
            aux = codMesa;
        }
        boolean jatem = false;
        ContentValues fecharMesa = new ContentValues();
        try{
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            fecharMesa.put(MESA_STATUS, false);

            if(jaExisteTodas(aux)){
                jatem = true;
                while(jatem){
                    aux = codMesa.concat("("+i+")");
                    jatem = jaExisteTodas(aux);
                    i++;
                }
                alteraCodMesa(aux, codMesa);
                linhaAfetada = bd.update(TABELA_MESAS, fecharMesa, selecao, new String[]{aux});
                bd.setTransactionSuccessful();
            }else{
                alteraCodMesa(aux, codMesa);
                linhaAfetada = bd.update(TABELA_MESAS, fecharMesa, selecao, new String[]{aux});
                bd.setTransactionSuccessful();
                atv = "Mesa "+ codMesa + " fechada com sucesso!";
            }
        } catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao fechar a mesa "+ codMesa + " !";
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

    public int reabrirMesa(String codMesa){
        int linhaAfetada = 0;
        String selecao = MESA.concat("=?") + " and " + MESA_STATUS.concat("=0");
        try{
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues reabrirMesa = new ContentValues();

            reabrirMesa.put(MESA_STATUS, true);
            linhaAfetada = bd.update(TABELA_MESAS, reabrirMesa, selecao, new String[]{codMesa});
            bd.setTransactionSuccessful();
            atv = "Mesa "+ codMesa + " reaberta com sucesso!";
        } catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao reabrir mesa "+ codMesa + " !";
        }finally {
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

    public int excluirMesa(String codMesa){
        int linhaAfetada = 0;
        String selecao = MESA.concat("=?");
        try{
            atv = "Mesa "+ codMesa + "com o valor de R$: " + "" + " excluida com sucesso!";
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = bd.delete(TABELA_MESAS, selecao, new String[]{codMesa});
            bd.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao excluir mesa "+ codMesa + "!";
        } finally{
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

    private void fechar(){
        if((bd.isOpen())) {
            bd.endTransaction();
            bd.close();
        }
    }
}