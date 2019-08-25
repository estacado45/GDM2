package br.com.gdm.gt.gdm.banco_dados;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by estac on 19/04/2018.
 */

public class ConexaoBD {
    private static final String TAG_INFO = "INFORMAÇÃO!";
    private static final String TAG_ERRO = "ERRO!";
    private static ConexaoBD conexaoBD;
    private BancoGDM banco = null;
    private SQLiteDatabase conexaoEscrita, conexaoLeitura;

    private ConexaoBD() {
        try {
            if (banco == null) {
                banco = new BancoGDM();
             }
        }catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    public static ConexaoBD getInstance() {
        if (conexaoBD == null) {
            conexaoBD = new ConexaoBD();
        }
        return conexaoBD;
    }

    public SQLiteDatabase getConexaoEscrita() {
        try {
               if((conexaoEscrita == null) || (!conexaoEscrita.isOpen())) {
                   conexaoEscrita = banco.getWritableDatabase();
               }
        }catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }
        return conexaoEscrita;
    }

    public SQLiteDatabase getConexaoLeitura() {
        try {
            if((conexaoLeitura ==  null) || (!conexaoLeitura.isOpen())) {
                conexaoLeitura = banco.getReadableDatabase();
            }
        }catch (SQLiteException e) {
            Log.e(TAG_ERRO, e.getMessage());
        }
    return conexaoLeitura;
    }

    public void fecharConexaoEscrita() {
        if (conexaoEscrita.isOpen()) {
            conexaoEscrita.close();
        }
    }

    public void fecharConexaoLeitura() {
        if (conexaoLeitura.isOpen()) {
            conexaoLeitura.close();
        }
    }
}