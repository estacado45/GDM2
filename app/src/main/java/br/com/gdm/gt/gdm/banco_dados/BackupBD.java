package br.com.gdm.gt.gdm.banco_dados;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;

public class BackupBD extends Activity {
	private String caminho = Environment.getExternalStorageDirectory()+ "/Android/data/com.gt.gdm/backup";
	private File f = null;
	private File arq = null;
	private final String TAG = "Erro!";

	@SuppressLint("LongLogTag")
	public void criaArquivo() {
		try {
			if(f == null){
				f = new File(caminho);
				f.mkdirs();
				f.createNewFile();
				if(f.exists()) {
					arq = new File(f, "BDGDM.bkp");
					arq.createNewFile();
				}
			}
		}catch (FileNotFoundException e) {
			Log.e(TAG +"('Metodo criaArquivo')", e.getMessage());
		}catch (Exception e) {
			Log.e(TAG +"('Metodo criaArquivo')", e.getMessage());
		}
	}

	
	@SuppressLint("LongLogTag")
	public void criarBackup() {
		try {
			InputStream origemArqBackup = new FileInputStream(new File(Environment.getDataDirectory()+ "/data/gt.gdm/databases/BDGDM"));
			OutputStream destinoArqBackup = new FileOutputStream(new File(arq.getPath()));
			byte[] buf = new byte[1024];
			int tam;
			while ((tam = origemArqBackup.read(buf)) > 0) {
				destinoArqBackup.write(buf, 0, tam);
			}
			origemArqBackup.close();
			destinoArqBackup.close();
		} catch (Exception e) {
			Log.e(TAG + "('Metodo criarBackup')", e.getMessage());
		}
	}

	@SuppressLint("LongLogTag")
	public void restaurarBackup() {
		try {
			InputStream origemArqBackup = new FileInputStream(new File(Environment.getExternalStorageDirectory()+ "/Android/data/com.gt.gdm/backup/BDGDM"));
			OutputStream destinoArqBackup = new FileOutputStream(new File(Environment.getDataDirectory()+ "/data/gt.gdm/databases/BDGDM"));
			byte[] buf = new byte[1024];
			int tam;
			while ((tam = origemArqBackup.read(buf)) > 0) {
				destinoArqBackup.write(buf, 0, tam);
			}
			origemArqBackup.close();
			destinoArqBackup.close();
		} catch (Exception e) {
			Log.e(TAG+ "('Metodo restaurarBackup')", e.getMessage());
		}
	}
}
