package com.paypal.risk.ganma.experiment.graph.neo4j.pfTest;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.Direction;

import com.paypal.risk.ganma.experiment.graph.neo4j.AbstractTransactionNeo4jGraph;
import com.paypal.risk.ganma.experiment.graph.neo4j.TransactionRemoteNeo4jGraph;

public class GraphPerformanceTest implements Runnable {
    PFTestConfiguration conf;
    AbstractTransactionNeo4jGraph graph;

    ExecutorService executor;
    PFTestResults results;

    public void runTest() {
        int requests = conf.getRequests();
        for (int i = 0; i < requests; i++) {
            executor.execute(this);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        results.print();
    }

    private long getRamdomAcct() {
        Random rd = new Random();
        long start = conf.getStarted_accountNum();
        long end = conf.getEnded_accountNum();
        int count = (int) (end - start);
        long acct = rd.nextInt(count) + start;
        return acct;
    }

    public GraphPerformanceTest() {
        conf = PFTestConfiguration.loadFromFile("Performance.properties");
        graph = new TransactionRemoteNeo4jGraph(conf.getGraphServiceURI());
        if (conf.isParallel()) {
            executor = Executors.newFixedThreadPool(conf.getRequests());
        } else {
            executor = Executors.newSingleThreadExecutor();
        }
        results = new PFTestResults(conf);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new GraphPerformanceTest().runTest();
    }

    @Override
    public void run() {
        long acctNum = getRamdomAcct();
        int depth = conf.getGoDepth();
        Direction direct = Direction.valueOf(conf.getDirection());

        PFTestResult result = new PFTestResult();
        result.setStartTime(System.currentTimeMillis());
        result.setAcctNum(acctNum);
        graph.traverse(acctNum, depth, direct);
        result.setEndTime(System.currentTimeMillis());
        results.addResult(result);
    }

}
