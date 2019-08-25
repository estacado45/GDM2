package br.com.gdm.gt.gdm.modelos;

/**
 * Created by estac on 10/04/2018.
 */

public class Categoria {
    private int codCategoria;
    private String descricaoCateg;

    public String getDescricaoCateg() {
        return descricaoCateg;
    }

    public void setDescricaoCateg(String descricaoCateg) {
        this.descricaoCateg = descricaoCateg;
    }

    public int getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(int codCategoria) {
        this.codCategoria = codCategoria;
    }

    @Override
    public String toString() {
        return getDescricaoCateg().trim();
    }
}
