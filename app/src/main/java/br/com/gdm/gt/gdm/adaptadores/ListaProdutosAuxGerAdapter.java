package br.com.gdm.gt.gdm.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.gdm.gt.gdm.fragmentos.FgtListaProdutosAlerta;
import br.com.gdm.gt.gdm.fragmentos.FgtProdutosGerencial;

/**
 * Created by estac on 27/03/2018.
 */

public class ListaProdutosAuxGerAdapter extends FragmentPagerAdapter {
    private int numViews;

    public ListaProdutosAuxGerAdapter(FragmentManager fm, int nViews) {
        super(fm);
        numViews = nViews;
    }

    @Override
    public Fragment getItem(int position) {
        return FgtProdutosGerencial.newInstance(position);
    }

    @Override
    public int getCount() {
        return numViews;
    }
}
