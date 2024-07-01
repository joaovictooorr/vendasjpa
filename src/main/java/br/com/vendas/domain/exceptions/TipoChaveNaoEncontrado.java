package br.com.vendas.domain.exceptions;

public class TipoChaveNaoEncontrado extends Exception{
    private static final long serialVersionUID = -1389494676398525746L;

    public TipoChaveNaoEncontrado(String msg) {
        this(msg, null);
    }

    public TipoChaveNaoEncontrado(String msg, Throwable e) {
        super(msg, e);
    }
}
