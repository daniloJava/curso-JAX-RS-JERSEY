package br.com.alura.loja;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Servidor {
	
	public static void main(String[] args) throws IOException {
		HttpServer server = inicializaServidor();
		System.out.println("Servidor Rodando");
		System.in.read();
		server.stop();
		
	}

	public static HttpServer inicializaServidor() {
		//Url base do servidor e a porta padão
		URI uri = URI.create("http://localhost:8080/");
		//configuração do pacote qye o JAX-RS irá Buscar para usar como serviço
		ResourceConfig config = new ResourceConfig().packages("br.com.alura.loja");
		//cria servidor
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
		return server;
	}

}
