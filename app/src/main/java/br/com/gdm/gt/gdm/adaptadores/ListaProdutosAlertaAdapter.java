package br.com.gdm.gt.gdm.adaptadores;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.gdm.gt.gdm.fragmentos.FgtListaProdutosAlerta;

/**
 * Created by estac on 27/03/2018.
 */

public class ListaProdutosAlertaAdapter extends FragmentPagerAdapter {
    private int numViews;

    public ListaProdutosAlertaAdapter(FragmentManager fm, int nViews) {
        super(fm);
        numViews = nViews;
    }

    @Override
    public Fragment getItem(int position) {
        return FgtListaProdutosAlerta.newInstance(position);
    }

    @Override
    public int getCount() {
        return numViews;
    }
}
