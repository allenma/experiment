package com.paypal.risk.ganma.experiment.graph.neo4j;

import org.junit.Before;
import org.junit.Test;

import com.paypal.risk.ganma.experiment.graph.neo4j.AbstractTransactionNeo4jGraph;
import com.paypal.risk.ganma.experiment.graph.neo4j.TransactionEmbeddedNeo4jGraph;

public class TransactionEmbeddedNeo4jGraphTest {
	AbstractTransactionNeo4jGraph graph;
	@Before
	public void setUp(){
		graph = new TransactionEmbeddedNeo4jGraph();
	}
		
	@Test
	public void printGraph(){
		graph.traverse(1000000000000000000L + 5,3);
	}
	
	
	
}
