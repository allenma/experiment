package com.paypal.risk.ganma.experiment.graph.neo4j;

import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.server.configuration.Configurator;
import org.neo4j.server.configuration.ServerConfigurator;

public class TransactionGraphServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// let the database accept remote neo4j-shell connections
		GraphDatabaseAPI graphdb = (GraphDatabaseAPI) new TransactionEmbeddedNeo4jGraph().getNeo4jGraphDBService();
		ServerConfigurator config;
		config = new ServerConfigurator( graphdb );
		// let the server endpoint be on a custom port
		config.configuration().setProperty(
		        Configurator.WEBSERVER_PORT_PROPERTY_KEY, 7575 );
		 
		WrappingNeoServerBootstrapper srv;
		srv = new WrappingNeoServerBootstrapper( graphdb, config );
		srv.start();
	}

}
