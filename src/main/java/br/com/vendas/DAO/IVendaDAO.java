package br.com.vendas.DAO;

import br.com.vendas.domain.Entity.Venda;
import br.com.vendas.domain.exceptions.DAOException;
import br.com.vendas.domain.exceptions.TipoChaveNaoEncontrado;
import br.com.vendas.generic.IGenericDAO;

public interface IVendaDAO extends IGenericDAO<Venda,Long> {
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontrado, DAOException;
    public void cancelarVenda(Venda venda) throws  TipoChaveNaoEncontrado, DAOException;
    public Venda consultarComCollection(Long id);

}
