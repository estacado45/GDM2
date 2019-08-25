package br.com.gdm.gt.gdm.modelos;

/**
 * Created by estac on 21/04/2018.
 */

public class Pagamento {
    private String codMesa;
    private Double valorDinheiro, valorCartao;

    public String getCodMesa() {
        return codMesa;
    }

    public void setCodMesa(String codMesa) {
        this.codMesa = codMesa;
    }

    public Double getValorDinheiro() {
        return valorDinheiro;
    }

    public void setValorDinheiro(Double valorDinheiro) {
        this.valorDinheiro = valorDinheiro;
    }

    public Double getValorCartao() {
        return valorCartao;
    }

    public void setValorCartao(Double valorCartao) {
        this.valorCartao = valorCartao;
    }

    public String toString(){
        return "R$: " + valorDinheiro + " / R$(cartão): " +valorCartao;
    }
}
