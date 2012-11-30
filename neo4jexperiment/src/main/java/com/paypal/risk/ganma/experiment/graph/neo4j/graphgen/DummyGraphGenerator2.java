package com.paypal.risk.ganma.experiment.graph.neo4j.graphgen;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.lucene.unsafe.batchinsert.LuceneBatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndex;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import com.paypal.risk.ganma.experiment.graph.neo4j.AbstractTransactionNeo4jGraph;
import com.paypal.risk.ganma.experiment.graph.neo4j.AbstractTransactionNeo4jGraph.RelTypes;

public class DummyGraphGenerator2 {
    private GraphGenConfiguration conf;
    private final BatchInserter inserter;
    private final BatchInserterIndex index;
    private final Map<Long, Long> accountNodeMap;

    private static long currTransactionID = 0L;

    public DummyGraphGenerator2(GraphGenConfiguration conf) {
        this.conf = conf;
        inserter = BatchInserters.inserter(conf.getGraphDBName());
        final BatchInserterIndexProvider indexProvider = new LuceneBatchInserterIndexProvider(inserter);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                indexProvider.shutdown();
                inserter.shutdown();
            }
        });

        index = indexProvider.nodeIndex(AbstractTransactionNeo4jGraph.ACCT_NUMBER_INDEX_NAME, MapUtil.stringMap("type",
                "exact"));
        accountNodeMap = new HashMap<Long, Long>(conf.getNodeCnt()*2);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        GraphGenConfiguration conf = GraphGenConfiguration.loadFromFile("GenGraph.properties");
        DummyGraphGenerator2 gen = new DummyGraphGenerator2(conf);
        gen.genDummyGraph();

    }

    public void genDummyGraph() {
        createAccounts();
        createTransactions();
    }

    private void createAccounts() {
        final long baseAcctNum = conf.getBaseAcctNum();
        long accountCnt = conf.getNodeCnt();
        for (long i = 0; i < accountCnt; i++) {
            Long accoutNumber = baseAcctNum + i;
            Map<String, Object> properties = MapUtil.map(AbstractTransactionNeo4jGraph.ACCT_NUMBER, accoutNumber);
            long nodeId = inserter.createNode(properties);
            index.add(nodeId, properties);
            accountNodeMap.put(accoutNumber, nodeId);
            if (accountCnt % 50000 == 0) {
                index.flush();
            }
        }
    }

    private void createTransactions() {
        long tranCnt = conf.getTranCnt();

        for (long i = 0; i < tranCnt; i++) {
            createRandomTransaction();
        }

    }

    private void createRandomTransaction() {
        int accountCnt = conf.getNodeCnt();
        final long baseAcctNum = conf.getBaseAcctNum();
        Random rd = new Random();
        long senderID = rd.nextInt(accountCnt) + baseAcctNum;
        long receiverID = rd.nextInt(accountCnt) + baseAcctNum;
        while (senderID == receiverID) {
            receiverID = rd.nextInt(accountCnt) + baseAcctNum;
        }

        long transactionID = currTransactionID++;
        int ammount = rd.nextInt(10000);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 1, 1);
        long baseTime = calendar.getTimeInMillis();
        long timestamp = baseTime + (long) (Math.random() * (1000L * 60 * 60 * 24 * 365 * 10));

        long nodeId = accountNodeMap.get(senderID);
        long linkNodeId = accountNodeMap.get(receiverID);
        
        
        Map<String, Object> props = MapUtil.map(AbstractTransactionNeo4jGraph.TRANSACTION_ID, transactionID,
                AbstractTransactionNeo4jGraph.ACCT_NUMBER, ammount, AbstractTransactionNeo4jGraph.TRANSACTION_TIME,
                timestamp);
        
        inserter.createRelationship(nodeId, linkNodeId, RelTypes.SEND, props);
    }

}
