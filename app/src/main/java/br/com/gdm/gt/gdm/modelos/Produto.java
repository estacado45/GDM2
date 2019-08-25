package br.com.gdm.gt.gdm.modelos;


public class Produto {

    private String codProduto;
    private String descricao;
    private String descCateg;
    private int codCateg;
    private double valor;

    public void setCodProduto(String codProduto) {
        this.codProduto = codProduto;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescCateg() {
        return descCateg;
    }

    public void setDescCateg(String descCateg) {
        this.descCateg = descCateg;
    }


    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCodProduto() {
        return codProduto;
    }

    public String getDescricao() {
        return descricao;
    }


    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return getCodProduto()+"/"+getDescricao()+"/"+getValor();
    }

    public int getCodCateg() {
        return codCateg;
    }

    public void setCodCateg(int codCateg) {
        this.codCateg = codCateg;
    }
}
