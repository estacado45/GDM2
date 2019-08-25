package br.com.gdm.gt.gdm.modelos;

/**
 * Created by estac on 23/04/2018.
 */

public class Servico {
    private String codMesa,descricaoServ;
    private Double valorServ;

    public String getCodMesa() {
        return codMesa;
    }

    public void setCodMesa(String codMesa) {
        this.codMesa = codMesa;
    }

    public String getDescricaoServ() {
        return descricaoServ;
    }

    public void setDescricaoServ(String descricaoServ) {
        this.descricaoServ = descricaoServ;
    }

    public Double getValorServ() {
        return valorServ;
    }

    public void setValorServ(Double valorServ) {
        this.valorServ = valorServ;
    }
}