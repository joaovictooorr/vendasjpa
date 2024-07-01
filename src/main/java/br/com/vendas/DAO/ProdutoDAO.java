package br.com.vendas.DAO;

import br.com.vendas.domain.Entity.Produto;
import br.com.vendas.generic.GenericDAO;

public class ProdutoDAO extends GenericDAO<Produto, Long> implements IProdutoDAO {
    public ProdutoDAO(){
        super(Produto.class);
    }
}
