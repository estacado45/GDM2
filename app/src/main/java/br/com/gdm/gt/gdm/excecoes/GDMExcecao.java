package br.com.gdm.gt.gdm.excecoes;

/**
 * Created by estac on 02/04/2019.
 */

public class GDMExcecao extends RuntimeException {

    public GDMExcecao(String message) {
        super(message);
    }

    public GDMExcecao(String message, Throwable cause) {
        super(message, cause);
    }
}
