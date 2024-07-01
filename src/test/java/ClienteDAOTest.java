import br.com.vendas.DAO.ClienteDAO;
import br.com.vendas.DAO.IClienteDAO;
import br.com.vendas.domain.Entity.Cliente;
import br.com.vendas.domain.exceptions.DAOException;
import br.com.vendas.domain.exceptions.MaisDeUmRegistroException;
import br.com.vendas.domain.exceptions.TipoChaveNaoEncontrado;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class ClienteDAOTest {

    private IClienteDAO clienteDAO;
    private Random random;

    public ClienteDAOTest(){
        this.clienteDAO = new ClienteDAO();
        random = new Random();
    }

    @After
    public void end() throws DAOException{
        Collection<Cliente> list = clienteDAO.buscarTodos();
        list.forEach(cliente -> {
            try {
                clienteDAO.excluir(cliente);
            } catch (DAOException e){
                e.printStackTrace();
            }
        });
    }



    @Test
    public void pesquisarCliente() throws TipoChaveNaoEncontrado, DAOException, MaisDeUmRegistroException {
        Cliente cliente = criarCliente();
        clienteDAO.cadastrar(cliente);

        Cliente clienteConsultado = clienteDAO.consultar(cliente.getId());
        Assert.assertNotNull(clienteConsultado);

    }
    @Test
    public void salvarCliente() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        Cliente cliente = criarCliente();
        Cliente retorno =  clienteDAO.cadastrar(cliente);
        Assert.assertNotNull(retorno);

        Cliente clienteConsultado =  clienteDAO.consultar(retorno.getId());
        Assert.assertNotNull(clienteConsultado);

        clienteDAO.excluir(cliente);

        Cliente clienteConsultado1 =  clienteDAO.consultar(retorno.getId());
        Assert.assertNull(clienteConsultado1);
    }

    @Test
    public void excluirCliente() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException, DAOException {
        Cliente cliente = criarCliente();
        Cliente retorno = clienteDAO.cadastrar(cliente);
        Assert.assertNotNull(retorno);

        Cliente clienteConsultado = clienteDAO.consultar(cliente.getId());
        Assert.assertNotNull(clienteConsultado);

        clienteDAO.excluir(cliente);
        clienteConsultado = clienteDAO.consultar(cliente.getId());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void alterarCliente() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException, DAOException {
        Cliente cliente = criarCliente();
        Cliente retorno = clienteDAO.cadastrar(cliente);
        Assert.assertNotNull(retorno);

        Cliente clienteConsultado = clienteDAO.consultar(cliente.getId());
        Assert.assertNotNull(clienteConsultado);

        clienteConsultado.setNome("bia");
        clienteDAO.alterar(clienteConsultado);

        Cliente clienteAlterado = clienteDAO.consultar(clienteConsultado.getId());
        Assert.assertNotNull(clienteAlterado);
        Assert.assertEquals("bia", clienteAlterado.getNome());

        clienteDAO.excluir(cliente);
        clienteConsultado = clienteDAO.consultar(clienteAlterado.getId());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void buscarTodos() throws TipoChaveNaoEncontrado, DAOException {
        Cliente cliente = criarCliente();
        Cliente retorno =  clienteDAO.cadastrar(cliente);
        Assert.assertNotNull(retorno);

        Cliente cliente1 = criarCliente();
        Cliente retorno1 =  clienteDAO.cadastrar(cliente1);
        Assert.assertNotNull(retorno1);

        Collection<Cliente> list =  clienteDAO.buscarTodos();
        assertTrue(list != null);
        assertTrue(list.size() == 2);

        list.forEach(cli -> {
            try {
                clienteDAO.excluir(cli);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });

        Collection<Cliente> list1 =  clienteDAO.buscarTodos();
        assertTrue(list1 != null);
        assertTrue(list1.size() == 0);
    }

    private Cliente criarCliente() {
        Cliente cliente = new Cliente();
        cliente.setCpf(random.nextLong());
        cliente.setNome("joao");
        cliente.setCidade("São Paulo");
        cliente.setEnd("End");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTel(1199999999L);
        return cliente;
    }




}
