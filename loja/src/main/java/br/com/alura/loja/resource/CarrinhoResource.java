package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

import com.thoughtworks.xstream.XStream;

/**Criando um recurso web,
 * todo recurso JAX-RS será anotado com a anotação Path
 * 
 * @author tapower
 *
 */
@Path("carrinho")
public class CarrinhoResource {

	/**@GET - formato da requisição do recurso
	 * @Produces(MediaType.APPLICATION_XML) - tipo de retorno dos dados.
	 * @QueryParam("id") long id - encaminhar um dado por parametro porem em webservices tem a questao dos caches /carrinho?id=10
	 * @PathParam("id") long id) - infomrar ao metodo o parametro que será pego /carrinho/1
	 * @Path("{id}") receber um id pela URI.
	 * 
	 */
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@PathParam("id") long id){
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toXML();
	}
	
	
//	@Path("{id}")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public String buscaJSon(@PathParam("id") long id){
//		Carrinho carrinho = new CarrinhoDAO().busca(id);
//		return carrinho.toJson();
//	}
	
	/**Classe Reponse tem os recursos necessarios para informar um status para o cliente.
	 * 
	 * @param conteudo - conteudo da criaão do carrinho.
	 * @return Response- retorno 201 informando a URL que foi criada o recurso
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
    public Response adiciona(Carrinho carrinho) {
        new CarrinhoDAO().adiciona(carrinho);
        URI uri = URI.create("/carrinho/"+ carrinho.getId());
		return Response.created(uri).build();
    }
	
	/**Metodo delete eliminar um recurso.
	 * 
	 * @param id
	 * @param produtoId
	 * @return
	 */
	@Path("{id}/produtos/{produtoId}")
    @DELETE
    public Response removeProduto(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
        carrinho.remove(produtoId);
        return Response.ok().build();
    }
	
	/**Put para atualizar um recurso, nesse caso apenas a quantidade
	 * 
	 * @param id
	 * @param produtoId
	 * @param conteudo
	 * @return
	 */
	@Path("{id}/produtos/{produtoId}/quantidade")
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response alteraProduto(@PathParam("id") long id, @PathParam("produtoId") long produtoId, Produto produto) {
        Carrinho carrinho = new CarrinhoDAO().busca(id);
        carrinho.trocaQuantidade(produto);
        return Response.ok().build();
    }
	
	
}
