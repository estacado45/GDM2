package br.com.gdm.gt.gdm.banco_dados.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import br.com.gdm.gt.gdm.banco_dados.ConexaoBD;
import br.com.gdm.gt.gdm.arquivos.logs.LogAtividades;
import br.com.gdm.gt.gdm.criptografia.Criptografia;
import br.com.gdm.gt.gdm.excecoes.GDMExcecao;
import br.com.gdm.gt.gdm.modelos.Usuario;

/**
 * Created by estac on 23/04/2018.
 */

public class UsuarioDAO {

    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";
    private static final String TABELA_USUARIO = "USUARIOS";
    private static final String USUARIOS_COD = "codUsuario";
    private static final String USUARIOS_NOME = "nome";
    private static final String USUARIOS_SENHA = "senha";
    private static final String USUARIOS_ADM = "admin";
    private static final String USUARIOS_MASTER = "master";
    private static LogAtividades logAtv = new LogAtividades();
    private String atv = "";
    private SQLiteDatabase bd;


    public int alteraUsuario(Usuario novoUsuario, Usuario UsuarioAntigo ){
        int linhaAfetada = 0;
        String selecao = "nome=? and senha=?";
        if(jaExisteCodUsuario(novoUsuario.getCodUsuario())){
            throw new GDMExcecao("Código existente em outro usuário.");
        }
        try {
            ContentValues alteraUsuario = new ContentValues();
            alteraUsuario.put(USUARIOS_COD, novoUsuario.getCodUsuario());
            alteraUsuario.put(USUARIOS_NOME, novoUsuario.getNome());
            alteraUsuario.put(USUARIOS_SENHA, Criptografia.criptografar(novoUsuario.getSenha()));
            alteraUsuario.put(USUARIOS_ADM, novoUsuario.isAdm());
            alteraUsuario.put(USUARIOS_MASTER, novoUsuario.isMaster());
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = bd.update(TABELA_USUARIO, alteraUsuario, selecao, new String[]{UsuarioAntigo.getNome(), UsuarioAntigo.getSenha()});
            bd.setTransactionSuccessful();
            atv = "Usuario alterado com sucesso,de: " + UsuarioAntigo.getNome() + " para: " + novoUsuario.getNome();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao alterar usuario.";
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

    public int cadastrarUsuario(Usuario u){
        int linhaAfetada = 0;
        ContentValues cadastraUsuario = new ContentValues();
        cadastraUsuario.put(USUARIOS_COD,u.getCodUsuario());
        cadastraUsuario.put(USUARIOS_NOME, u.getNome());
        cadastraUsuario.put(USUARIOS_SENHA, Criptografia.criptografar(u.getSenha()));
        cadastraUsuario.put(USUARIOS_ADM, u.isAdm());
        cadastraUsuario.put(USUARIOS_MASTER, u.isMaster());
        if(jaExisteCodUsuario(u.getCodUsuario())) {
            throw new GDMExcecao("Código existente em outro usuário.");
        }
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = (int) bd.insert(TABELA_USUARIO, null, cadastraUsuario);
            bd.setTransactionSuccessful();
            atv = "Usuario cadastrado com sucesso,com o nome: "+u.getNome();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao cadastrar usuario.";
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

    public ArrayList<Usuario> obterUsuarios() {
        Cursor c = null ;
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        Usuario u;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_USUARIO, null, null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                u = new Usuario();
                u.setCodUsuario(c.getString(c.getColumnIndex(USUARIOS_COD)));
                u.setNome(c.getString(c.getColumnIndex(USUARIOS_NOME)));
                u.setSenha(c.getString(c.getColumnIndex(USUARIOS_SENHA)));
                u.setAdm(c.getInt(c.getColumnIndex(USUARIOS_ADM))==1);
                u.setMaster(c.getInt(c.getColumnIndex(USUARIOS_MASTER))==1);
                if(!(u.isMaster())){
                    usuarios.add(u);
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
        return usuarios;
    }

    public int excluirUsuario(String codUsuarioExcluir){
        int linhaAfetada = 0;
        String selecao = USUARIOS_COD.concat("=?");
        Usuario usuario = retornaUsuario(codUsuarioExcluir);
        if(usuario.isAdm()){
            throw new GDMExcecao("Esse usuário é administrador e não pode ser excluído!");
        }
        try {
            bd = ConexaoBD.getInstance().getConexaoEscrita();
            bd.beginTransaction();
            linhaAfetada = (int) bd.delete(TABELA_USUARIO, selecao, new String[]{String.valueOf(codUsuarioExcluir)});
            bd.setTransactionSuccessful();
            atv = "Categoria " + codUsuarioExcluir + " excluida com sucesso!";
        }catch (SQLiteException e){
            Log.e(TAG_ERRO, e.getMessage());
            atv = "Erro ao excluir categoria "+ codUsuarioExcluir+"!";
        }finally {
            fechar();
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

    private boolean jaExisteCodUsuario(String codigoUsuario){
        boolean retorno = false;
        String selecao = USUARIOS_COD.concat("=?");
        Cursor cursor = null;
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            cursor = bd.query(TABELA_USUARIO, null, selecao, new String[]{String.valueOf(codigoUsuario)}, null, null, null, null);
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

    public Usuario retornaUsuario(String codUsuario) {
        Cursor c = null ;
        Usuario usuario = null;
        String selecao = USUARIOS_COD.concat("=?");
        try {
            bd = ConexaoBD.getInstance().getConexaoLeitura();
            bd.beginTransaction();
            c = bd.query(TABELA_USUARIO, null, selecao,new String[]{(codUsuario)}, null, null,null);
            c.moveToFirst();
            usuario = new Usuario();
            usuario.setCodUsuario(c.getString(c.getColumnIndex(USUARIOS_COD)));
            usuario.setNome(c.getString(c.getColumnIndex(USUARIOS_NOME)));
            usuario.setSenha(c.getString(c.getColumnIndex(USUARIOS_SENHA)));
            usuario.setAdm(c.getInt(c.getColumnIndex(USUARIOS_ADM))==1);
            usuario.setMaster(c.getInt(c.getColumnIndex(USUARIOS_MASTER))==1);
            c.close();
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            usuario = null;
        }finally {
            c.close();
            fechar();
        }
        return usuario;
    }

    public boolean login(String nome, String senha){
        Cursor c = null;
        boolean confirmacao = false;
        String selecao = "nome=? and senha=?";
        bd = ConexaoBD.getInstance().getConexaoLeitura();
        try {
            bd.beginTransaction();
            c = bd.query(TABELA_USUARIO, null, selecao, new String[]{nome, Criptografia.criptografar(senha)}, null, null, null);
            if(c.moveToFirst()){
                confirmacao = true;
                bd.setTransactionSuccessful();
            }
            atv = "Login realizado pelo usuario: "+c.getString(c.getColumnIndex(USUARIOS_NOME));
            c.close();
        } catch (Exception e) {
            Log.e(TAG_ERRO + "(ATV USR)", e.getMessage());
            atv = "Falha ao tentar realizar login pelo usuario";
        }finally{
            c.close();
            fechar();
            try {
                logAtv.escreveLogAtividades(atv);
            }catch(NullPointerException e){
                logAtv.criaArquivo();
                logAtv.escreveLogAtividades(atv);
            }catch (Exception e) {
                Log.e(TAG_ERRO+ "(ATV USR)", e.getMessage());
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