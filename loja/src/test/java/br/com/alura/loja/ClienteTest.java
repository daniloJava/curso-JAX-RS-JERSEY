package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

import com.thoughtworks.xstream.XStream;



public class ClienteTest {
	
	private HttpServer server;
	private Client client;
	private WebTarget target;


	@Before
	public void startaServidor(){
		server = Servidor.inicializaServidor();
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		//Criando um cliente Rest
		this.client = ClientBuilder.newClient();
		//Usando a Uri Base do servido
		this.target = client.target("http://localhost:8080");
		
	}
	
	@After
	public void mataServidor(){
		server.stop();
		
	}
	

	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		//Vazendo uma requisição ao serviço.
		String conteudo = target.path("/carrinho/1").request().get(String.class);
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
		
	}
	
	@Test
	public void testaQueSalvaUmCarrinhoPeloPost(){
        
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        String xml = carrinho.toXML();
        
        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
        //Agora pega dependendo do status. 201 para criar
        Response response = target.path("/carrinho").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
        
        //recuperando a location do recurso criado
        String headerString = response.getHeaderString("Location");
        
        //recupera novamente se a url anterior recebeu foi criado.
		String carrinhoNew = client.target(headerString).request().get(String.class);
		Assert.assertTrue(carrinhoNew.contains("Tablet")); // verifica se tem Tablet criado.
		
		
	}
}
