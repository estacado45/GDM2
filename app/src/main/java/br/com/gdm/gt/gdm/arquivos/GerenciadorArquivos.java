package br.com.gdm.gt.gdm.arquivos;

import android.app.Activity;
import android.util.Log;
import java.io.File;

import br.com.gdm.gt.gdm.atividades.MeuContexto;

/**
 * Created by estac on 26/04/2018.
 */

public class GerenciadorArquivos extends Activity {
    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";
    private static final String caminho = MeuContexto.getContexto().getExternalFilesDir("") + "/br.com.gt.gdm";
    private static File dirPrincipal = null;

    public static void criarDiretorios() {
        criaDiretorioPrincipal();
        criaDiretorioProdutos();
        criaDiretorioLogs();
        criaDiretorioRelatorios();
        criaDiretorioBackup();
    }

    private static void criaDiretorioPrincipal() {
        try {
            dirPrincipal = new File(caminho);
            if (!dirPrincipal.exists()) {
                dirPrincipal.mkdirs();
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    public static void criaDiretorioProdutos(){
        File dirProdutos = null;
        String nomeDirProdutos = "PRODUTOS";
        try {
            dirProdutos = new File(dirPrincipal, nomeDirProdutos);
            if(! dirProdutos.exists()){
                dirProdutos.mkdir();
                criaDocumentoProdutos(dirProdutos);
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    public static void criaDiretorioLogs(){
        File dirLogs = null;
        String nomeDirLogs = "LOGS";
        try {
            dirLogs = new File(dirPrincipal, nomeDirLogs);
            if(! dirLogs.exists()){
                dirLogs.mkdir();
                criaDocumentoLogs(dirLogs);
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    public static void criaDiretorioRelatorios(){
        File dirRelatorios = null;
        String nomeDirRelatorios = "RELATORIOS";
        try {
            dirRelatorios = new File(dirPrincipal, nomeDirRelatorios);
            if(! dirRelatorios.exists()){
                dirRelatorios.mkdir();
                criaDocumentoRelatorios(dirRelatorios);
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    public static void criaDiretorioBackup(){
        File dirBackup = null;
        String nomeDirBackup = "BACKUP";
        try {
            dirBackup = new File(dirPrincipal, nomeDirBackup);
            if(! dirBackup.exists()){
                dirBackup.mkdir();
                criaDocumentoBackup(dirBackup);
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    private static void criaDocumentoProdutos(File diretorio){
        File docProdutos = null;
        String nomeDocProdutos = "listaProdutos.txt";
        try {
            docProdutos = new File(diretorio, nomeDocProdutos);
            if(! docProdutos.exists()){
                docProdutos.createNewFile();
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    private static void criaDocumentoLogs(File diretorio){
        File docLogs = null;
        String nomeDocLogs = "logAtividades.txt";
        try {
            docLogs = new File(diretorio, nomeDocLogs);
            if(! docLogs.exists()){
                docLogs.createNewFile();
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    private static void criaDocumentoRelatorios(File diretorio){
        File docRelatorios = null;
        String nomeDocRelatorios = "relatorios.pdf";
        try {
            docRelatorios = new File(diretorio, nomeDocRelatorios);
            if(! docRelatorios.exists()){
                docRelatorios.createNewFile();
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }

    private static void criaDocumentoBackup(File diretorio){
        File docBackup = null;
        String nomeDocBackup = "backup.bkp";
        try {
            docBackup = new File(diretorio, nomeDocBackup);
            if(! docBackup.exists()){
                docBackup.createNewFile();
            }
        }catch (Exception e){
            Log.e(TAG_ERRO, e.getMessage());
        }
    }
}
