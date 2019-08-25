package br.com.gdm.gt.gdm.banco_dados.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import br.com.gdm.gt.gdm.banco_dados.ConexaoBD;
import br.com.gdm.gt.gdm.arquivos.logs.LogAtividades;
import br.com.gdm.gt.gdm.modelos.Pagamento;

/**
 * Created by estac on 24/04/2018.
 */

public class PagamentoDAO {
    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";
    private static final String TABELA_PAGAMENTO = "PAGAMENTOS";
    private static final String COD_MESA_PGTO = "codMesaPgto";
    private static final String VR_DINHEIRO_PGTO = "valorDinheiro";
    private static final String VR_CARTAO_PGTO = "valorCartao";
    private static LogAtividades logAtv = new LogAtividades();
    private String atv = "";
    private SQLiteDatabase bd = null;

    public int inserirPagamento(Pagamento pgto){
        int linhaAfetada = 0;
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues novoPagamento = new ContentValues();
            novoPagamento.put(COD_MESA_PGTO, pgto.getCodMesa());
            novoPagamento.put(VR_DINHEIRO_PGTO,pgto.getValorDinheiro());
            novoPagamento.put(VR_CARTAO_PGTO,pgto.getValorCartao());
            linhaAfetada = (int) bd.insert(TABELA_PAGAMENTO, null, novoPagamento);
            bd.setTransactionSuccessful();
            atv = "Pagamento da mesa "+ pgto.getCodMesa()+" realizado com sucesso!";
        }catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao realizar pagamento da mesa "+ pgto.getCodMesa()+"!";
        }finally {
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
            return linhaAfetada;
        }
    }

    public int editarPagamento(Pagamento pgto, String codMesaPgtoEditar){
        int linhaAfetada = 0;
        String selecao = COD_MESA_PGTO.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            ContentValues editarPgto = new ContentValues();
            editarPgto.put(COD_MESA_PGTO,pgto.getCodMesa());
            editarPgto.put(VR_DINHEIRO_PGTO,pgto.getValorDinheiro());
            editarPgto.put(VR_CARTAO_PGTO,pgto.getValorCartao());
            linhaAfetada = (int) bd.update(TABELA_PAGAMENTO, editarPgto, selecao, new String[]{String.valueOf(codMesaPgtoEditar)});
            bd.setTransactionSuccessful();
            atv = "Pagamento da mesa "+ pgto.getCodMesa()+" editado com sucesso!";
        }catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao editar Pagamento da mesa "+ pgto.getCodMesa()+"!";
        }finally {
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
            return linhaAfetada;
        }
    }

    public int excluirPagamento(String codMesaPgtoExcluir){
        int linhaAfetada = 0;
        String selecao = COD_MESA_PGTO.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = (int) bd.delete(TABELA_PAGAMENTO, selecao, new String[]{String.valueOf(codMesaPgtoExcluir)});
            bd.setTransactionSuccessful();
            atv = "Pagamento da mesa "+ codMesaPgtoExcluir+ "excluido com sucesso!";
        }catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao excluir Pagamento da mesa "+ codMesaPgtoExcluir+"!";
        }finally {
            bd.endTransaction();
            bd.close();
            try {
                logAtv.escreveLogAtividades(atv);
            } catch (NullPointerException e) {
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
            return linhaAfetada;
        }
    }

    public Pagamento retornaCategoriaPorCodigo(String codMesaPgto) {
        Pagamento pgto = new Pagamento();
        Cursor cursor;
        String selecao = COD_MESA_PGTO.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            cursor = bd.query(TABELA_PAGAMENTO, null, selecao, new String[]{String.valueOf(codMesaPgto)}, null, null, null, null);
            pgto.setCodMesa(cursor.getString(cursor.getColumnIndex(COD_MESA_PGTO)));
            pgto.setValorDinheiro(cursor.getDouble(cursor.getColumnIndex(VR_DINHEIRO_PGTO)));
            pgto.setValorCartao(cursor.getDouble(cursor.getColumnIndex(VR_CARTAO_PGTO)));
            bd.setTransactionSuccessful();
            atv = "Pagamento da mesa " + codMesaPgto + " retornado com sucesso!";
        } catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao retornar Pagamento da mesa " + codMesaPgto + "!";
        } finally {
            bd.endTransaction();
            bd.close();
            try {
                logAtv.escreveLogAtividades(atv);
            } catch (NullPointerException e) {
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
            }
        }
        return pgto;
    }
}
