package com.paypal.risk.ganma.experiment.graph.neo4j;

import org.junit.Before;
import org.junit.Test;

public class TransactionRemoteNeo4jGraphTest {
	AbstractTransactionNeo4jGraph graph;
	@Before
	public void setUp(){
		graph = new TransactionRemoteNeo4jGraph();
	}
		
	@Test
	public void printGraph(){
		graph.traverse(1000000000000000000L + 5,3);
	}
	
	
	
}
