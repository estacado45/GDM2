package br.com.gdm.gt.gdm.modelos;

import java.util.Calendar;

public class Data {
 private static Calendar data ;

	public static String dataExtenso() {
		data = Calendar.getInstance();
		String d = (data.get(Calendar.DATE)) + " de "
				+ nomeMes(data.get(Calendar.MONTH)) + " de "
				+ (data.get(Calendar.YEAR));
		return d;
	}

	private static String nomeMes(int mes) {
		String[] meses = new String[12];
		meses[0] = "Janeiro";
		meses[1] = "Fevereiro";
		meses[2] = "Março";
		meses[3] = "Abril";
		meses[4] = "Maio";
		meses[5] = "Junho";
		meses[6] = "Julho";
		meses[7] = "Agosto";
		meses[8] = "Setembro";
		meses[9] = "Outubro";
		meses[10] = "Novembro";
		meses[11] = "Dezembro";
		return meses[mes];
	}

	public static String dataReversa() {
		data = Calendar.getInstance();
		String d = (data.get(Calendar.YEAR)) + "/"
				+ (data.get(Calendar.MONTH) + 1) + "/"
				+ (data.get(Calendar.DATE));
		return d;
	}

	public static String dataAtual() {
		data = Calendar.getInstance();
		String d = (data.get(Calendar.DATE)) + "/"
				+ (data.get(Calendar.MONTH) + 1) + "/"
				+ (data.get(Calendar.YEAR));
		return d;
	}
	
	public static String horaAtual() {
		data = Calendar.getInstance();
		String StrHora = String.valueOf(data.get(Calendar.HOUR_OF_DAY));
		String StrMinuto = String.valueOf(data.get(Calendar.MINUTE));
		if(StrHora.length() == 1){
			StrHora = "0"+StrHora;
		}
		if(StrMinuto.length() == 1){
			StrMinuto = "0"+StrMinuto;
		}
		String d =  StrHora + "h" + StrMinuto;
		return d;
	}
}
