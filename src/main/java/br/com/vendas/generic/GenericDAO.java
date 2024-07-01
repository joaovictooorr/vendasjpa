package br.com.vendas.generic;

import br.com.vendas.domain.exceptions.DAOException;
import br.com.vendas.domain.exceptions.MaisDeUmRegistroException;
import br.com.vendas.domain.exceptions.TipoChaveNaoEncontrado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


public class GenericDAO <T extends Persistence, E extends Serializable> implements IGenericDAO <T,E>{

    protected EntityManagerFactory entityManagerFactory;

    protected EntityManager entityManager;

    private Class<T> persistenteClass;

    public GenericDAO(Class<T> persistenteClass) {
        this.persistenteClass = persistenteClass;
    }

    @Override
    public T cadastrar(T entity) throws TipoChaveNaoEncontrado, DAOException {
        openConnection();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        closeConnection();
        return entity;
    }

    @Override
    public void excluir(T entity) throws DAOException {
        openConnection();
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
        closeConnection();

    }

    @Override
    public T alterar(T entity) throws TipoChaveNaoEncontrado, DAOException {
        openConnection();
        entity = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        closeConnection();
        return entity;
    }

    @Override
    public T consultar(E id) throws MaisDeUmRegistroException, DAOException {
        openConnection();
        T entity = entityManager.find(this.persistenteClass, id);
        entityManager.getTransaction().commit();
        closeConnection();
        return entity;
    }

    @Override
    public Collection<T> buscarTodos() throws DAOException {
        openConnection();
        List<T> list =
                entityManager.createQuery(getSelectSql(), this.persistenteClass).getResultList();
        closeConnection();
        return list;
    }

    protected void openConnection() {
        entityManagerFactory =
                jakarta.persistence.Persistence.createEntityManagerFactory("PROJETO4");

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }
    protected void closeConnection() {
        entityManager.close();
        entityManagerFactory.close();
    }

    private String getSelectSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT obj FROM ");
        sb.append(this.persistenteClass.getSimpleName());
        sb.append(" obj");
        return sb.toString();
    }
}
