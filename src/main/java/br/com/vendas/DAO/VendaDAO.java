package br.com.vendas.DAO;

import br.com.vendas.domain.Entity.Cliente;
import br.com.vendas.domain.Entity.Produto;
import br.com.vendas.domain.Entity.Venda;
import br.com.vendas.domain.exceptions.DAOException;
import br.com.vendas.domain.exceptions.TipoChaveNaoEncontrado;
import br.com.vendas.generic.GenericDAO;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class VendaDAO extends GenericDAO<Venda,Long> implements IVendaDAO {
    public VendaDAO(Class<Venda> persistenteClass) {
        super(persistenteClass);
    }

    public VendaDAO() {
        super(Venda.class);
    }

    @Override
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontrado, DAOException {
        super.alterar(venda);
    }

    @Override
    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontrado, DAOException {
        super.alterar(venda);
    }

    @Override
    public void excluir(Venda entity) throws DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public Venda cadastrar(Venda entity) throws TipoChaveNaoEncontrado, DAOException {
        try {
            openConnection();
            entity.getProdutos().forEach(prod -> {
                Produto prodJpa = entityManager.merge(prod.getProduto());
                prod.setProduto(prodJpa);
            });
            Cliente cliente = entityManager.merge(entity.getCliente());
            entity.setCliente(cliente);
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            closeConnection();
            return entity;
        } catch (Exception e) {
            throw new DAOException("ERRO SALVANDO VENDA ", e);
        }

    }


    @Override
    public Venda consultarComCollection(Long id) {
        openConnection();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Venda> query = builder.createQuery(Venda.class);
        Root<Venda> root = query.from(Venda.class);
        root.fetch("cliente");
        root.fetch("produtos");
        query.select(root).where(builder.equal(root.get("id"), id));
        TypedQuery<Venda> tpQuery =
                entityManager.createQuery(query);
        Venda venda = tpQuery.getSingleResult();
        closeConnection();
        return venda;
    }
}
