package br.com.gdm.gt.gdm.atividades;

import android.app.Application;
import android.content.Context;

/**
 * Created by estac on 08/03/2018.
 */

public class MeuContexto extends Application {
    private static Context contexto;

    @Override
    public void onCreate() {
        super.onCreate();
        contexto = getApplicationContext();
    }
     public static Context getContexto(){
        return contexto;
     }
}
