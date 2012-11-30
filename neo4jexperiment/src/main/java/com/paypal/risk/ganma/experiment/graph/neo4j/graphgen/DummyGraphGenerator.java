package com.paypal.risk.ganma.experiment.graph.neo4j.graphgen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import com.paypal.risk.ganma.experiment.graph.neo4j.AbstractTransactionNeo4jGraph;
import com.paypal.risk.ganma.experiment.graph.neo4j.TransactionEmbeddedNeo4jGraph;
import com.paypal.risk.ganma.experiment.graph.neo4j.TransactionEntity;
import com.paypal.risk.ganma.experiment.graph.neo4j.TransactionRemoteNeo4jGraph;

public class DummyGraphGenerator {
    private long baseAcctNum = 1000000000000000000L;
    // private long baseTransID = 1000000000000000000L;
    private int batchSize = 2000;
    private int nodeCnt = 0;
    private long tranCnt = 0;

    private static long currTransactionID = 0L;

    public DummyGraphGenerator(int accountCnts, long tranCnts) {
        this.nodeCnt = accountCnts;
        this.tranCnt = tranCnts;
    }

    public DummyGraphGenerator() {

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        int nodeCnt = 0;
        long tranCnt = 0;
        if (args.length == 2) {
            nodeCnt = Integer.valueOf(args[0]);
            tranCnt = Integer.valueOf(args[1]);
            DummyGraphGenerator gen = new DummyGraphGenerator(nodeCnt, tranCnt);
            gen.genDummyGraph();
            // graph.printAccountTrans(1000000000000000000L,3);
            // gen.creatDummyRemoteGraph();
        }
        

    }

    public AbstractTransactionNeo4jGraph genDummyGraph() {
        TransactionEmbeddedNeo4jGraph graph = new TransactionEmbeddedNeo4jGraph();
        
        createAccounts(graph);
        createTransactions(graph);
        // graph.printAccountTrans(1000000000000000000L);
        return graph;
    }
    
    public AbstractTransactionNeo4jGraph genDummyGraph_batch() {
        GraphDatabaseService gds = BatchInserters.batchDatabase( "graphdbs/transactionGraph" );
        TransactionEmbeddedNeo4jGraph graph = new TransactionEmbeddedNeo4jGraph(gds);
        createAccounts(graph);
        createTransactions(graph);
        // graph.printAccountTrans(1000000000000000000L);
        return graph;
    }

    public TransactionRemoteNeo4jGraph genDummyRemoteGraph() {
        TransactionRemoteNeo4jGraph graph = new TransactionRemoteNeo4jGraph();
        createAccounts(graph);
        createTransactions(graph);
        // graph.printAccountTrans(1000000000000000000L);
        return graph;
    }

    private void createTransactions(AbstractTransactionNeo4jGraph graph) {
        long batches = tranCnt / batchSize;
        long left = tranCnt % batchSize;

        if (left != 0) {
            batches++;
        }

        for (long i = 0; i < batches; i++) {
            List<TransactionEntity> transactions = new ArrayList<TransactionEntity>();
            long realTransInBatch = batchSize;
            if (i == batches - 1 && left != 0) {
                realTransInBatch = left;
            }
            for (int j = 0; j < realTransInBatch; j++) {
                transactions.add(createRandomTransaction());
            }
            graph.addTransactions(transactions);
        }

    }

    private void createAccounts(AbstractTransactionNeo4jGraph graph) {
        long acctNum = baseAcctNum;

        long batches = nodeCnt / batchSize;
        long left = nodeCnt % batchSize;
        if (left != 0) {
            batches++;
        }

        for (long i = 0; i < batches; i++) {
            List<Long> batchAccouts = new ArrayList<Long>();
            long realAccountsInBatch = batchSize;
            if (i == batches - 1 && left != 0) {
                realAccountsInBatch = left;
            }
            for (int j = 0; j < realAccountsInBatch; j++) {
                batchAccouts.add(acctNum);
                acctNum++;
            }
            graph.addAccounts(batchAccouts);
        }

    }

    private TransactionEntity createRandomTransaction() {
        Random rd = new Random();
        long senderID = rd.nextInt(nodeCnt) + baseAcctNum;
        long receiverID = rd.nextInt(nodeCnt) + baseAcctNum;
        while (senderID == receiverID) {
            receiverID = rd.nextInt(nodeCnt) + baseAcctNum;
        }
        long transactionID = currTransactionID++;
        int ammount = rd.nextInt(10000);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 1, 1);
        long baseTime = calendar.getTimeInMillis();
        long timestamp = baseTime + (long) (Math.random() * (1000L * 60 * 60 * 24 * 365 * 10));
        TransactionEntity tran = new TransactionEntity();
        tran.setSenderID(senderID);
        tran.setReceiverID(receiverID);
        tran.setTransactionID(transactionID);
        tran.setTimestamp(timestamp);
        tran.setAmmount(ammount);

        return tran;
    }

}
