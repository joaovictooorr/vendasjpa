package br.com.vendas.generic;

import br.com.vendas.domain.exceptions.DAOException;
import br.com.vendas.domain.exceptions.MaisDeUmRegistroException;
import br.com.vendas.domain.exceptions.TipoChaveNaoEncontrado;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericDAO <T extends Persistence, E extends Serializable>{
    public T cadastrar(T entity) throws TipoChaveNaoEncontrado, DAOException;
    public void excluir(T entity) throws DAOException;
    public T alterar(T entity) throws TipoChaveNaoEncontrado, DAOException;
    public T consultar(E id) throws MaisDeUmRegistroException, DAOException;
    public Collection<T> buscarTodos() throws DAOException;
}
