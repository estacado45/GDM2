package br.com.gdm.gt.gdm.fragmentos;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.gdm.gt.gdm.R;
import br.com.gdm.gt.gdm.atividades.MeuContexto;
import br.com.gdm.gt.gdm.modelos.ItensMesa;

/**
 * Created by estac on 25/05/2019.
 */

public class FgtAcaoItem extends DialogFragment {
    public ItensMesa itemMesa = null;
    private Spinner spnFgtAcaoItemAdicionar = null;
    private Spinner spnFgtAcaoItemSubtrair = null;
    private Button btnFgtAcaoItemAdicionar = null;
    private Button btnFgtAcaoItemSubtrair = null;
    private Button btnFgtAcaoItemExcluir = null;
    private Button btnFgtAcaoItemDetalhes = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final String[] quantidade = {"1","2","3","4","5","6","7","8","9","10","11","12"};
        super.onCreateView(inflater, container, savedInstanceState);
        final View visao = inflater.inflate(R.layout.acao_item,container);
        spnFgtAcaoItemAdicionar = (Spinner) visao.findViewById(R.id.spnFgtAcaoItemAdicionar);
        spnFgtAcaoItemSubtrair = (Spinner) visao.findViewById(R.id.spnFgtAcaoItemSubtrair);
        btnFgtAcaoItemAdicionar = (Button) visao.findViewById(R.id.btnFgtAcaoItemAdicionar);
        btnFgtAcaoItemSubtrair = (Button) visao.findViewById(R.id.btnFgtAcaoItemSubtrair);
        btnFgtAcaoItemExcluir = (Button) visao.findViewById(R.id.btnFgtAcaoItemExcluir);
        btnFgtAcaoItemDetalhes = (Button) visao.findViewById(R.id.btnFgtAcaoItemDetalhes);
        spnFgtAcaoItemAdicionar.setAdapter(new ArrayAdapter<String>(MeuContexto.getContexto(),android.R.layout.simple_spinner_dropdown_item, quantidade));
        spnFgtAcaoItemSubtrair.setAdapter(new ArrayAdapter<String>(MeuContexto.getContexto(),android.R.layout.simple_spinner_dropdown_item, quantidade));

        Toast.makeText(MeuContexto.getContexto(),"ITEM: "+itemMesa.getDescProd(),Toast.LENGTH_LONG).show();

        btnFgtAcaoItemAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeuContexto.getContexto(),btnFgtAcaoItemAdicionar.getText().toString() + "qtd: " + spnFgtAcaoItemAdicionar.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }
        });

        btnFgtAcaoItemSubtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeuContexto.getContexto(),btnFgtAcaoItemSubtrair.getText().toString() + "qtd: " + spnFgtAcaoItemSubtrair.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }
        });
        btnFgtAcaoItemExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeuContexto.getContexto(),btnFgtAcaoItemExcluir.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
        btnFgtAcaoItemDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeuContexto.getContexto(),btnFgtAcaoItemDetalhes.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
        return visao;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams parametros = getDialog().getWindow().getAttributes();
        parametros.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams)parametros);
    }
}
