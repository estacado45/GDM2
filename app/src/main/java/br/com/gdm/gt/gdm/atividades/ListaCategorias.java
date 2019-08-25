package br.com.gdm.gt.gdm.atividades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.com.gdm.gt.gdm.R;

public class ListaCategorias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_categorias);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCategoria);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            }
        });
    }

}
