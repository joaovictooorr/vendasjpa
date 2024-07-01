import br.com.vendas.DAO.IProdutoDAO;
import br.com.vendas.DAO.ProdutoDAO;
import br.com.vendas.domain.Entity.Produto;
import br.com.vendas.domain.exceptions.DAOException;
import br.com.vendas.domain.exceptions.MaisDeUmRegistroException;
import br.com.vendas.domain.exceptions.TipoChaveNaoEncontrado;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.*;

public class ProdutoDAOTest {

    IProdutoDAO produtoDAO;

    public ProdutoDAOTest(){
        this.produtoDAO = new ProdutoDAO();
    }

    @After
    public void end() throws DAOException {
        Collection<Produto> list = produtoDAO.buscarTodos();
        list.forEach(cli -> {
            try {
                produtoDAO.excluir(cli);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void pesquisar() throws MaisDeUmRegistroException,  DAOException, TipoChaveNaoEncontrado {
        Produto produto = criarProduto("A1");
        assertNotNull(produto);
        Produto produtoDB = this.produtoDAO.consultar(produto.getId());
        assertNotNull(produtoDB);
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontrado, DAOException {
        Produto produto = criarProduto("A2");
        assertNotNull(produto);
    }

    @Test
    public void excluir() throws DAOException, TipoChaveNaoEncontrado, MaisDeUmRegistroException {
        Produto produto = criarProduto("A3");
        assertNotNull(produto);
        this.produtoDAO.excluir(produto);
        Produto produtoBD = this.produtoDAO.consultar(produto.getId());
        assertNull(produtoBD);
    }

    @Test
    public void alterarCliente() throws TipoChaveNaoEncontrado, DAOException, MaisDeUmRegistroException  {
        Produto produto = criarProduto("A4");
        produto.setNome("Rodrigo Pires");
        produtoDAO.alterar(produto);
        Produto produtoBD = this.produtoDAO.consultar(produto.getId());
        assertNotNull(produtoBD);
        Assert.assertEquals("Rodrigo Pires", produtoBD.getNome());
    }

    @Test
    public void buscarTodos() throws DAOException, TipoChaveNaoEncontrado {
        criarProduto("A5");
        criarProduto("A6");
        Collection<Produto> list = produtoDAO.buscarTodos();
        assertTrue(list != null);
        assertTrue(list.size() == 2);

        for (Produto prod : list) {
            this.produtoDAO.excluir(prod);
        }

        list = produtoDAO.buscarTodos();
        assertTrue(list != null);
        assertTrue(list.size() == 0);

    }

    private Produto criarProduto(String codigo) throws TipoChaveNaoEncontrado, DAOException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto 1");
        produto.setNome("Produto 1");
        produto.setValor(BigDecimal.TEN);
        produtoDAO.cadastrar(produto);
        return produto;
    }
}
