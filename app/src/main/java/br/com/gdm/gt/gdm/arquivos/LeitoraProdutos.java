package br.com.gdm.gt.gdm.arquivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.modelos.Produto;

public class LeitoraProdutos extends Activity {
	private static String caminho = MeuContexto.getContexto().getExternalFilesDir("")+ "/br.com.gt.gdm/PRODUTOS";
	private static File f = null;
	private static File arq = null;
    private static final String NOME_ARQUIVO = "listaProdutos.txt";
	@SuppressLint("LongLogTag")

	public static void criaArquivo() {
		try {
			if(f == null){
				f = new File(caminho);
				if (f.exists()) {
					f.mkdirs();
					f.createNewFile();
					arq = new File(f, NOME_ARQUIVO);
					arq.createNewFile();

				}
			}
		}catch (Exception e) {
			Log.e("Erro====>('Metodo criaArquivo Cria')", e.getMessage());
		}
	}

	public static BufferedReader abreListaProdutos() throws FileNotFoundException, IOException{
		if(arq == null){
			arq = new File(new File(caminho), NOME_ARQUIVO);
			arq.createNewFile();
		}

		return  new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
	}

	public static ArrayList<Produto> retornalistaProdutos() throws FileNotFoundException,IOException {
		BufferedReader ListaProdutos = null;
		String linha = "";  
		ArrayList<Produto> produtos = null;
		Produto p = null;
			String [] aux;
			produtos = new ArrayList<>();
			ListaProdutos = abreListaProdutos();
			linha = ListaProdutos.readLine(); 
			while (linha != null){   
				aux = linha.split("/");
				p = new Produto();
				p.setCodProduto(String.valueOf(aux[0].toString()));
				p.setDescricao(aux[1].toString().toUpperCase());
				p.setCodCateg(Integer.parseInt(aux[2]));
				p.setValor(Double.parseDouble(aux[3].toString()));
				produtos.add(p);
				linha = ListaProdutos.readLine();
			}
		return produtos;
	}

	@SuppressLint("LongLogTag")
	public static void inserirListaProdutos(Produto p) {
		BufferedReader ListaProdutos = null;
		BufferedWriter bw = null;
		File aux = null;
		try{
			if (f.exists()) {
				aux = new File(f, NOME_ARQUIVO);
				aux.createNewFile();
			}
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(aux)));
			String linha,novaLinha;
			linha = "";
			novaLinha = "";	
			ListaProdutos = abreListaProdutos();
			linha = ListaProdutos.readLine(); 
			while (linha != null){
				bw.append(linha);
				bw.newLine();
				linha = ListaProdutos.readLine();				
			}
			bw.newLine();
			novaLinha = p.getCodProduto().trim()+"/"+p.getDescricao().trim()+"/"+ String.valueOf(p.getCodCateg())+"/"+p.getValor();
			bw.append(novaLinha);
			aux.renameTo(arq);
		}catch(Exception e){
			Log.e("Erro====>",e.getMessage());
		}finally{
			try {
				bw.close();
				ListaProdutos.close();
			} catch (IOException e) {
				Log.e("Erro====>('Metodo inserirListaProdutos')", e.getMessage());
			}
		}
	}
	
	@SuppressLint("LongLogTag")
	public static void alteraListaProdutos(String codProdAntigo, Produto prodAlterado) {
		BufferedReader ListaProdutos = null;
		BufferedWriter bw = null;
		File aux = null;
		try{
			if (f.exists()) {
				aux = new File(f, NOME_ARQUIVO);
				aux.createNewFile();
			}
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(aux)));
			String linha,novaLinha;
			linha = "";
			novaLinha = "";	
			Produto p = prodAlterado;
			ListaProdutos = abreListaProdutos();
			linha = ListaProdutos.readLine(); 
			while (linha != null){
				if(linha.contains(codProdAntigo)){		
					novaLinha = p.getCodProduto().trim()+"/"+p.getDescricao().trim()+"/"+String.valueOf(p.getCodCateg())+"/"+p.getValor();
					bw.append(novaLinha);
					bw.newLine();
					linha = ListaProdutos.readLine();
				}else{
					bw.append(linha);
					bw.newLine();
					linha = ListaProdutos.readLine();				
				}
			}
			aux.renameTo(arq);
		}catch(Exception e){
			Log.e("Erro====>",e.getMessage());
		}finally{
			try {
				bw.close();
				ListaProdutos.close();
			} catch (IOException e) {
				Log.e("Erro====>('Metodo alteraListaProdutos')", e.getMessage());
			}
		}
	}

	@SuppressLint("LongLogTag")
	public static void apagarListaProdutos() {
		BufferedWriter bw = null;
	    BufferedReader br = null;
	    String linha = "";
	    File aux = null;
	    try {   
		if (f.exists()) {
			aux = new File(f, NOME_ARQUIVO);
			aux.createNewFile();
		}
			br = abreListaProdutos();
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(aux)));
			bw.append(linha);
			aux.renameTo(arq);
		}catch (Exception e) {
			Log.e("Erro====>('Metodo apagarListaProdutos')", e.getMessage());
		}
	    finally{
			try {
				bw.close();
				br.close();
			} catch (IOException e) {
				Log.e("Erro====>('Metodo apagarListaProdutos')", e.getMessage());
			}
		}
	}

	public static ArrayList<Integer> retornaCodCategoriasLista() throws IOException {
		ArrayList<Integer> codCategorias = new ArrayList<>();
	    String[] auxiliar;
		BufferedReader ListaProdutos = null;
		String linha = "";
		ListaProdutos = abreListaProdutos();
		linha = ListaProdutos.readLine();
		while (linha != null) {
			auxiliar = linha.split("/");
			codCategorias.add(Integer.parseInt(auxiliar[2]));
		}
	return codCategorias;
	}
}