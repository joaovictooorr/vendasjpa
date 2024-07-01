package br.com.vendas.DAO;

import br.com.vendas.domain.Entity.Cliente;
import br.com.vendas.generic.GenericDAO;

public class ClienteDAO extends GenericDAO<Cliente,Long> implements IClienteDAO {
    public ClienteDAO(){
        super(Cliente.class);
    }
}
