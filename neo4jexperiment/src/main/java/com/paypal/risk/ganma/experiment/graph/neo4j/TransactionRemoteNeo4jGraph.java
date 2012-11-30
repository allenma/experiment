package com.paypal.risk.ganma.experiment.graph.neo4j;

import org.neo4j.rest.graphdb.RestGraphDatabase;

public class TransactionRemoteNeo4jGraph extends AbstractTransactionNeo4jGraph{

	
	public static final String REMOTE_GRAPH_URL = "http://stage2vm4887.sc4.paypal.com:7474/db/data";

	public TransactionRemoteNeo4jGraph() {
		this(REMOTE_GRAPH_URL);
	}

	public TransactionRemoteNeo4jGraph(String graph_url) {
		super(new RestGraphDatabase(graph_url));
	}

	
}
