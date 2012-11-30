package com.paypal.risk.ganma.experiment.graph.neo4j.pfTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PFTestConfiguration {
    private int goDepth = 3;
    private String direction = "BOTH";
    private int requests = 10;
    private boolean parallel = false;
    
    private long started_accountNum = 1000000000000000000L;
    private long ended_accountNum = 1000000000000000010L;
    
    private String outputFile;
    
    private String graphServiceURI;
    
    public int getGoDepth() {
        return goDepth;
    }
    public void setGoDepth(int goDepth) {
        this.goDepth = goDepth;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public int getRequests() {
        return requests;
    }
    public void setRequests(int requests) {
        this.requests = requests;
    }
    public long getStarted_accountNum() {
        return started_accountNum;
    }
    public void setStarted_accountNum(long startedAccountNum) {
        started_accountNum = startedAccountNum;
    }
    public long getEnded_accountNum() {
        return ended_accountNum;
    }
    public void setEnded_accountNum(long endedAccountNum) {
        ended_accountNum = endedAccountNum;
    }
    
    
    
    public boolean isParallel() {
        return parallel;
    }
    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }
    public String getGraphServiceURI() {
        return graphServiceURI;
    }
    public void setGraphServiceURI(String graphServiceURI) {
        this.graphServiceURI = graphServiceURI;
    }
    
    public String getOutputFile() {
        return outputFile;
    }
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
    public static PFTestConfiguration loadFromFile(String filePath){
        PFTestConfiguration conf = new PFTestConfiguration();
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(filePath));
            conf.setDirection(prop.getProperty("direction", "BOTH"));
            conf.setStarted_accountNum(Long.parseLong(prop.getProperty("started_accountNum", "1000000000000000000")));
            conf.setEnded_accountNum(Long.parseLong(prop.getProperty("ended_accountNum", "1000000000000000010")));
            conf.setGoDepth(Integer.parseInt(prop.getProperty("depth", "2")));
            conf.setRequests(Integer.parseInt(prop.getProperty("requests", "2")));
            conf.setOutputFile(prop.getProperty("resultOutPut"));
            conf.setGraphServiceURI(prop.getProperty("graphServiceURI","http://localhost:7474/db/data"));
            conf.setParallel(Boolean.parseBoolean(prop.getProperty("parallel","false")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conf;
    }
    
    
}
