package com.paypal.risk.ganma.experiment.graph.neo4j;

import java.util.Arrays;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.kernel.Traversal;

public abstract class AbstractTransactionNeo4jGraph {
    public static final String ACCT_NUMBER_INDEX_NAME = "accoutNumbers";
    public static final String ACCT_NUMBER = "accountNum";
    public static final String TRANSACTION_ID = "transactionId";
    public static final String TRANSACTION_TIME = "transactionTime";
    public static final String TRANSACTION_AMMOUNT = "ammount";

    GraphDatabaseService graphDB;
    Index<Node> nodeIndex;

    public static enum RelTypes implements RelationshipType {
        SEND,
    }

    public AbstractTransactionNeo4jGraph(GraphDatabaseService graphDB) {
        this.graphDB = graphDB;
        nodeIndex = graphDB.index().forNodes(ACCT_NUMBER_INDEX_NAME);
        registerShutdownHook();
    }

    public void addAccount(Long accountNum) {
        addAccounts(Arrays.asList(accountNum));
    }

    public void addAccounts(List<Long> accountNums) {
        System.out.println("Adding " + accountNums.size() + " accounts");
        Transaction tx = graphDB.beginTx();
        try {
            for (Long accountNum : accountNums) {
                createAndIndexAccount(accountNum);
            }
            tx.success();

        } finally {
            tx.finish();
            System.out.println("accounts added!");
        }
    }

    public void addTransactions(List<TransactionEntity> trans) {
        System.out.println("Adding " + trans.size() + " transaction");
        Transaction tx = graphDB.beginTx();
        try {
            for (TransactionEntity tran : trans) {
                Node senderAcctNode = nodeIndex.get(ACCT_NUMBER, tran.getSenderID()).getSingle();
                Node receiverAcctNode = nodeIndex.get(ACCT_NUMBER, tran.getReceiverID()).getSingle();
                if (senderAcctNode == null || receiverAcctNode == null) {
                    System.out.println("The accout node doesn't exist:" + tran.getSenderID() + "-->"
                            + tran.getReceiverID());
                    continue;
                }
                Relationship rs = senderAcctNode.createRelationshipTo(receiverAcctNode, RelTypes.SEND);
                rs.setProperty(TRANSACTION_ID, tran.getTransactionID());
                rs.setProperty(TRANSACTION_TIME, tran.getTimestamp());
                rs.setProperty(TRANSACTION_AMMOUNT, tran.getAmmount());
            }
            tx.success();
        } finally {
            tx.finish();
            System.out.println("transactions added!");
        }
    }

    public void addTransaction(TransactionEntity tran) {
        addTransactions(Arrays.asList(tran));
    }

    Node createAndIndexAccount(final Long acctNum) {
        Node node = graphDB.createNode();
        node.setProperty(ACCT_NUMBER, acctNum);
        nodeIndex.add(node, ACCT_NUMBER, acctNum);
        return node;
    }

    private void registerShutdownHook() {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDB.shutdown();
            }
        });
    }

    public void traverse(long acctNum, int depth) {
        traverse(acctNum, depth, Direction.BOTH);
    }

    public void traverse(long acctNum, int depth, Direction direct) {
        Node acctNode = nodeIndex.get(ACCT_NUMBER, acctNum).getSingle();
        if (acctNode == null) {
            System.out.println("The account" + acctNum + "doesn't exist");
        }
        int pathNum = 0;
        for (Path position : Traversal.description().depthFirst().relationships(RelTypes.SEND, direct).evaluator(
                Evaluators.toDepth(depth)).traverse(acctNode)) {
            pathNum++;
        }
    }

    public GraphDatabaseService getNeo4jGraphDBService() {
        return graphDB;
    }

    public void setNeo4jGraphDBService(GraphDatabaseService graphDBService) {
        this.graphDB = graphDBService;
    }

}