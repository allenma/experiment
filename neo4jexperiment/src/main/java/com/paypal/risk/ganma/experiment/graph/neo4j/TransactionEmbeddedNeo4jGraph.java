package com.paypal.risk.ganma.experiment.graph.neo4j;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class TransactionEmbeddedNeo4jGraph extends AbstractTransactionNeo4jGraph {
	
	public static final String TRANSACTION_GRAPH_NAME = "graphdbs";

	private String graphName;
	
	public TransactionEmbeddedNeo4jGraph() {

		this(TRANSACTION_GRAPH_NAME);
	}

	public TransactionEmbeddedNeo4jGraph(String graphName) {
		super(new GraphDatabaseFactory().newEmbeddedDatabase(graphName));

		this.graphName = graphName;

	}
	
	public TransactionEmbeddedNeo4jGraph(GraphDatabaseService gdbService){
	    super(gdbService);
	}

	public void clean() {
		deleteFileOrDirectory(new File(graphName));
	}

	private static void deleteFileOrDirectory(final File file) {
		if (!file.exists()) {
			return;
		}

		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				deleteFileOrDirectory(child);
			}
		} else {
			file.delete();
		}
	}

	public String getGraphName() {
		return graphName;
	}


}
