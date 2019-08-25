package br.com.gdm.gt.gdm.banco_dados.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import br.com.gdm.gt.gdm.banco_dados.ConexaoBD;
import br.com.gdm.gt.gdm.arquivos.logs.LogAtividades;
import br.com.gdm.gt.gdm.modelos.Servico;

/**
 * Created by estac on 23/04/2018.
 */

public class ServicoDAO {
    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";
    private static final String TABELA_SERVICOS = "SERVICOS";
    private static final String SERVICO_COD_MESA = "codMesaServ";
    private static final String SERVICO_DESC = "descricaoServ";
    private static final String SERVICO_VALOR = "valorServ";
    private static LogAtividades logAtv = new LogAtividades();
    private String atv = "";
    private SQLiteDatabase bd = null;

    public int inserirServico(Servico s){
        long linhaAfetada = 0;
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues novoServico = new ContentValues();
            int i = 1;
            String aux = s.getDescricaoServ().concat("(1)");
            boolean jatem = false;
            if(jaExisteDescServ(aux)){
                jatem = true;
                while(jatem){
                    aux = s.getDescricaoServ().concat("("+i+")");
                    jatem = jaExisteDescServ(aux);
                    i++;
                }
            }
            novoServico.put(SERVICO_COD_MESA, s.getCodMesa());
            novoServico.put(SERVICO_DESC, aux);
            novoServico.put(SERVICO_VALOR, s.getValorServ());
            linhaAfetada = bd.insert(TABELA_SERVICOS, null, novoServico);
            bd.setTransactionSuccessful();
            atv = "Servico "+ s.getDescricaoServ()+" inserido com sucesso na mesa: "+ s.getCodMesa();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao inserir servicona mesa: "+ s.getCodMesa()+" !";
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
        return (int) linhaAfetada;
    }

    public int alterarServico(Servico s,String codMesaServAnt){
        long linhaAfetada = 0;
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues alterarServico = new ContentValues();
            int i = 1;
            String aux = s.getDescricaoServ().concat("(1)");
            boolean jatem = false;
            if(jaExisteDescServ(aux)){
                jatem = true;
                while(jatem){
                    aux = s.getDescricaoServ().concat("("+i+")");
                    jatem = jaExisteDescServ(aux);
                    i++;
                }
            }
            String where = SERVICO_DESC.concat("=?")+" and " + SERVICO_COD_MESA.concat("=?");
            alterarServico.put(SERVICO_COD_MESA, s.getCodMesa());
            alterarServico.put(SERVICO_DESC, aux);
            alterarServico.put(SERVICO_VALOR, s.getValorServ());
            linhaAfetada = bd.update(TABELA_SERVICOS, alterarServico, where ,new String[]{s.getDescricaoServ(),codMesaServAnt});
            bd.setTransactionSuccessful();
            atv = "Servico "+ s.getDescricaoServ() +" alterado com sucessona mesa: "+ s.getCodMesa()+"!";
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao alterar servico na mesa: "+ s.getCodMesa()+"!";
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
        return (int) linhaAfetada;
    }

    public int excluirServico(String codMesa,String descServ, Double valorAntigo){
        int linhaAfetada ;
        linhaAfetada = 0;
        Cursor c;
        String selecao = "codMesaServico=? and descServico=? and valorServico=?";
        String selecao2 = "codMesaServico=? and descServico=? and _id=?";

        try{
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            c = bd.query(TABELA_SERVICOS, null, selecao, new String[] {codMesa, descServ,String.valueOf(valorAntigo) }, null, null, null);
            c.moveToFirst();
            linhaAfetada = bd.delete(TABELA_SERVICOS, selecao2,new String[] {codMesa, descServ, String.valueOf(c.getInt(c.getColumnIndex("_id")))});
            bd.setTransactionSuccessful();
            atv = "Servico "+ descServ +" excluido com sucesso na mesa: "+ codMesa+"!";
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao excluir servico na mesa: "+ codMesa+"!";
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
        return linhaAfetada;
    }

    public ArrayList<Servico> listarTodosServicos(){
        Cursor c ;
        ArrayList<Servico> servicos = new ArrayList<Servico>();
        Servico s ;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_SERVICOS, null, null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                s = new Servico();
                s.setCodMesa(c.getString(c.getColumnIndex(SERVICO_COD_MESA)));
                s.setDescricaoServ(c.getString(c.getColumnIndex(SERVICO_DESC)));
                s.setValorServ(c.getDouble(c.getColumnIndex(SERVICO_VALOR)));
                servicos.add(s);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            bd.endTransaction();
            bd.close();
        }
        return servicos;
    }

    public ArrayList<Servico> retornaServicosMesa(String mesa){
        Cursor c ;
        ArrayList<Servico> servicos = new ArrayList<Servico>();
        Servico s;
        String selecao = SERVICO_COD_MESA.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_SERVICOS, null, selecao, new String[]{mesa}, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                s = new Servico();
                s.setCodMesa(c.getString(c.getColumnIndex(SERVICO_COD_MESA)));
                s.setDescricaoServ(c.getString(c.getColumnIndex(SERVICO_DESC)));
                s.setValorServ(c.getDouble(c.getColumnIndex(SERVICO_VALOR)));
                servicos.add(s);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            bd.endTransaction();
            bd.close();
        }
        return servicos;
    }

    public Servico retornaServico(String descServ){
        Cursor c ;
        Servico s = null;
        String selecao = SERVICO_DESC.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_SERVICOS, null, selecao, new String[]{descServ}, null, null, null);
            c.moveToFirst();
            s = new Servico();
            s.setCodMesa(c.getString(c.getColumnIndex(SERVICO_COD_MESA)));
            s.setDescricaoServ(c.getString(c.getColumnIndex(SERVICO_DESC)));
            s.setValorServ(c.getDouble(c.getColumnIndex(SERVICO_VALOR)));
            bd.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            bd.endTransaction();
            bd.close();
        }
        return s;
    }

    public ArrayList<Servico> retornaServicosMesaFechadas(String mesa){
        Cursor c ;
        ArrayList<Servico> servicos = new ArrayList<Servico>();
        Servico s;
        String selecao = SERVICO_COD_MESA.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_SERVICOS, null, selecao, new String[]{mesa}, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                s = new Servico();
                s.setCodMesa(c.getString(c.getColumnIndex(SERVICO_COD_MESA)));
                s.setDescricaoServ(c.getString(c.getColumnIndex(SERVICO_DESC)));
                s.setValorServ(c.getDouble(c.getColumnIndex(SERVICO_VALOR)));
                servicos.add(s);
                c.moveToNext();
            }
            bd.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            bd.endTransaction();
            bd.close();
        }
        return servicos;
    }

    public boolean jaExisteDescServ(String descServ){
        Cursor c ;
        boolean confirmacao = false;
        String selecao = SERVICO_DESC.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_SERVICOS, null, selecao, new String[]{descServ}, null, null, null);
            if(c.moveToFirst()){
                confirmacao = true;
            }
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
        }finally {
            bd.endTransaction();
            bd.close();
        }
        return confirmacao;
    }
}
