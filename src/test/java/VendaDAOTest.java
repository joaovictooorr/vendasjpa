import br.com.vendas.DAO.*;
import br.com.vendas.domain.Entity.Cliente;
import br.com.vendas.domain.Entity.Produto;
import br.com.vendas.domain.Entity.Venda;
import br.com.vendas.domain.exceptions.DAOException;
import br.com.vendas.domain.exceptions.MaisDeUmRegistroException;
import br.com.vendas.domain.exceptions.TipoChaveNaoEncontrado;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class VendaDAOTest {

    private IVendaDAO vendaDao;

    private IVendaDAO vendaExclusaoDao;

    private IClienteDAO clienteDao;

    private IProdutoDAO produtoDao;

    private Random rd;

    private Cliente cliente;

    private Produto produto;

    public VendaDAOTest(){
        this.vendaDao = new VendaDAO();
        vendaExclusaoDao = new VendaExclusaoDAO();
        this.clienteDao = new ClienteDAO();
        this.produtoDao = new ProdutoDAO();
        rd = new Random();

    }

    @Before
    public void init() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException, DAOException {
        this.cliente = cadastrarCliente();
        this.produto = cadastrarProduto("A1", BigDecimal.TEN);
    }

    @After
    public void end() throws DAOException {
        excluirVendas();
        excluirProdutos();
        clienteDao.excluir(this.cliente);
    }

    @Test
    public void pesquisar() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        Venda venda = criarVenda("A1");
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        Venda vendaConsultada = vendaDao.consultar(venda.getId());
        assertNotNull(vendaConsultada);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontrado, DAOException, MaisDeUmRegistroException {
        Venda venda = criarVenda("A2");
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);

        assertTrue(venda.getValorTotal().equals(BigDecimal.valueOf(20)));
        assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));

        Venda vendaConsultada = vendaDao.consultar(venda.getId());
        assertTrue(vendaConsultada.getId() != null);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void cancelarVenda() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException, DAOException {
        String codigoVenda = "A3";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        retorno.setStatus(Venda.Status.CANCELADA);
        vendaDao.cancelarVenda(venda);

        Venda vendaConsultada = vendaDao.consultar(venda.getId());
        assertEquals(codigoVenda, vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CANCELADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        String codigoVenda = "A4";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(produto, 1);

        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        String codigoVenda = "A5";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());


        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);

        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test(expected = DAOException.class)
    public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontrado, DAOException {
        Venda venda = criarVenda("A6");
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);

        Venda venda1 = criarVenda("A6");
        Venda retorno1 = vendaDao.cadastrar(venda1);
        assertNull(retorno1);
        assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerProduto() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        String codigoVenda = "A7";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerApenasUmProduto() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        String codigoVenda = "A8";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerTodosProdutos() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        String codigoVenda = "A9";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerTodosProdutos();
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
        assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void finalizarVenda() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        String codigoVenda = "A10";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        vendaDao.finalizarVenda(venda);

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontrado, MaisDeUmRegistroException,  DAOException {
        String codigoVenda = "A11";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        vendaDao.finalizarVenda(venda);

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());

        vendaConsultada.adicionarProduto(this.produto, 1);

    }


    private void excluirProdutos() throws DAOException {
        Collection<Produto> list = this.produtoDao.buscarTodos();
        list.forEach(prod -> {
            try {
                this.produtoDao.excluir(prod);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });
    }

    private void excluirVendas() throws DAOException {
        Collection<Venda> list = this.vendaExclusaoDao.buscarTodos();
        list.forEach(prod -> {
            try {
                this.vendaExclusaoDao.excluir(prod);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });
    }

    private Produto cadastrarProduto(String codigo, BigDecimal valor) throws TipoChaveNaoEncontrado, MaisDeUmRegistroException, DAOException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto 1");
        produto.setNome("Produto 1");
        produto.setValor(valor);
        produtoDao.cadastrar(produto);
        return produto;
    }

    private Cliente cadastrarCliente() throws TipoChaveNaoEncontrado, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(rd.nextLong());
        cliente.setNome("Rodrigo");
        cliente.setCidade("São Paulo");
        cliente.setEnd("End");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTel(1199999999L);
        clienteDao.cadastrar(cliente);
        return cliente;
    }

    private Venda criarVenda(String codigo) {
        Venda venda = new Venda();
        venda.setCodigo(codigo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(this.cliente);
        venda.setStatus(Venda.Status.INICIADA);
        venda.adicionarProduto(this.produto, 2);
        return venda;
    }


}
