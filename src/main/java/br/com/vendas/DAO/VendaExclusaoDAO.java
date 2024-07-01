package br.com.vendas.DAO;

import br.com.vendas.domain.Entity.Venda;
import br.com.vendas.domain.exceptions.DAOException;
import br.com.vendas.domain.exceptions.TipoChaveNaoEncontrado;
import br.com.vendas.generic.GenericDAO;

public class VendaExclusaoDAO extends GenericDAO<Venda, Long> implements IVendaDAO {

    public VendaExclusaoDAO(){
        super(Venda.class);
    }

    @Override
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontrado, DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontrado, DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public Venda consultarComCollection(Long id) {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }
}
