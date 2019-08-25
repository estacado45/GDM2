package br.com.gdm.gt.gdm.modelos;


public class ItensMesa {
    private String codProd,codMesa, horario, descProd;
    private int quant;
    private double valorProd;
    private boolean entregue;

    public String getDescProd() {
        return descProd;
    }

    public void setDescProd(String descProd) {
        this.descProd = descProd;
    }

    public double getValorProd() {
        return valorProd;
    }

    public void setValorProd(double valorProd) {
        this.valorProd = valorProd;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public String getCodProd() {
        return codProd;
    }

    public String getCodMesa() {
        return codMesa;
    }

    public void setCodMesa(String codMesa) {
        this.codMesa = codMesa;
    }

    public void setCodProd(String codProd) {
        this.codProd = codProd;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public String toString(){
        return "Produto: "+ getCodProd() +" / Quant: "+getQuant();
    }
}