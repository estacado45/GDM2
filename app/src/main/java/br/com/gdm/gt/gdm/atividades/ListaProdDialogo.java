package br.com.gdm.gt.gdm.atividades;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.adaptadores.ListaProdutosAlertaAdapter;

/**
 * Created by estac on 28/03/2018.
 */

public class ListaProdDialogo extends DialogFragment {
    private static ViewPager container;
    private ListaProdutosAlertaAdapter lpa;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lista_produtos_alerta,container,false);
        setaAtributos(view);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabVoltarNovaMesa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void setaAtributos(View v){
        container = (ViewPager) v.findViewById(R.id.viewPagerContainer);
        lpa = new ListaProdutosAlertaAdapter(getChildFragmentManager(), 4);
        container.setAdapter(lpa);
    }
}


