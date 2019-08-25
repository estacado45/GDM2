package br.com.gdm.gt.gdm.arquivos.logs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import br.com.gdm.gt.gdm.atividades.MeuContexto;

public class LogAtividades extends Activity {
	private String caminho = MeuContexto.getContexto().getExternalFilesDir("")+ "/br.com.gt.gdm/LOGS";
	private File f = null;
	private File arq = null;
	private static final String NOME_ARQUIVO = "logAtividades.txt";
	public void criaArquivo() {
		try {
			if(f == null){
				f = new File(caminho);
				f.mkdirs();
				f.createNewFile();
				if (f.exists()) {
					arq = new File(f,NOME_ARQUIVO);
					arq.createNewFile();
				}
			}
		}catch (Exception e) {
			Log.e("Erro====>('Metodo criaArquivo LOG')", e.getMessage());
		}
	}

	public void escreveLogAtividades(String atividade) {
		BufferedWriter bw = null;
	    BufferedReader br = null;
	    String linha = "";
	    File aux = null;
	    try {
		if (f.exists()) {
			if(arq == null){
				arq = new File(f, NOME_ARQUIVO);
				arq.createNewFile();
			}
			aux = new File(f, "logAtvAux.txt");
			aux.createNewFile();
		}else{
			f = new File(caminho);
			f.createNewFile();
			if(arq == null){
				arq = new File(f, NOME_ARQUIVO);
				arq.createNewFile();
			}
			aux = new File(f, "logAtvAux.txt");
			aux.createNewFile();
		}
			br = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(aux)));
			linha = br.readLine();
			while(linha != null){
				bw.append(linha);
				bw.newLine();
				linha = br.readLine();
			}
			bw.append(retornaDataHora());
			bw.append(",");
			bw.append(atividade);
			bw.newLine();
			bw.append("-");
			aux.renameTo(arq);
		}catch (Exception e) {
			Log.e("Erro====>('Metodo escreveLogAtividades')", e.getMessage());
		}finally{
			try {
				bw.close();
				br.close();
			} catch (IOException e) {
				Log.e("Erro====>('Metodo escreveLogAtividades')", e.getMessage());
			}
		}
	}

	public ArrayList<LogAux> lerLogAtividades() {

		BufferedReader br = null;
		String linha = "";
		LogAux logAux = null;
		ArrayList<LogAux> msgs = null;
		try {
			msgs = new ArrayList<LogAux>();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
			linha = br.readLine();
			while (linha != null) {
				if (!linha.equals("-")) {
					logAux = new LogAux();
					String[]auxLinha = linha.split(",");
					logAux.setData(auxLinha[0].toString());
					logAux.setHora(auxLinha[1].toString());
					logAux.setMsg(auxLinha[2].toString());
					msgs.add(logAux);
					linha = br.readLine();
				}else{
					linha = br.readLine();
				}
			}
			return msgs;
		} catch (Exception e) {
			Log.e("Erro====>('Metodo lerLogAtividades')", e.getMessage());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				Log.e("Erro====>('Metodo lerLogAtividades')",e.getMessage());
			}
		}
		return msgs;
	}

	public void apagarLogAtividades() {
		BufferedWriter bw = null;
	    BufferedReader br = null;
	    String linha = "";
	    File aux = null;
	    try {
		if (f.exists()) {
			aux = new File(f, "logAtvAux.txt");
			aux.createNewFile();
		}
			br = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(aux)));
			bw.append(linha);
			aux.renameTo(arq);
		}catch (Exception e) {
			Log.e("Erro====>('Metodo apagarLogAtividades')", e.getMessage());
		}
	    finally{
			try {
				bw.close();
				br.close();
			} catch (IOException e) {
				Log.e("Erro====>('Metodo apagarLogAtividades')", e.getMessage());
			}
		}
	}

	public String retornaDataHora() {

		Calendar cal = Calendar.getInstance();
		String dataAtual, hora, dataHora;
		dataAtual = cal.get(Calendar.DATE) + "/"
				+ (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
		final Integer horaAtual = cal.get(Calendar.HOUR_OF_DAY);
		final Integer minutoAtual = cal.get(Calendar.MINUTE);

		String strHora = String.valueOf(horaAtual);
		String strMinuto = String.valueOf(minutoAtual);

		if (strHora.length() == 1) {
			strHora = "0" + strHora;
		}
		if (strMinuto.length() == 1) {
			strMinuto = "0" + strMinuto;
		}
		hora = strHora + "h" + strMinuto;
		dataHora = dataAtual + "," + hora;
		return dataHora;
	}

}
