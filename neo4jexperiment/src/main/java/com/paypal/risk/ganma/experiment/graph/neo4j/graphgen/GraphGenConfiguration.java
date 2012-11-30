package com.paypal.risk.ganma.experiment.graph.neo4j.graphgen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.paypal.risk.ganma.experiment.graph.neo4j.pfTest.PFTestConfiguration;

public class GraphGenConfiguration {
    private String graphDBName;
    
    private long baseAcctNum = 0L;
    private int nodeCnt;
    private long tranCnt;
    public long getBaseAcctNum() {
        return baseAcctNum;
    }
    public void setBaseAcctNum(long baseAcctNum) {
        this.baseAcctNum = baseAcctNum;
    }

    public int getNodeCnt() {
        return nodeCnt;
    }
    public void setNodeCnt(int nodeCnt) {
        this.nodeCnt = nodeCnt;
    }
    public long getTranCnt() {
        return tranCnt;
    }
    public void setTranCnt(long tranCnt) {
        this.tranCnt = tranCnt;
    }
    public String getGraphDBName() {
        return graphDBName;
    }
    public void setGraphDBName(String graphName) {
        this.graphDBName = graphName;
    }
    
    public static GraphGenConfiguration loadFromFile(String filePath){
        GraphGenConfiguration conf = new GraphGenConfiguration();
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(filePath));
            conf.setGraphDBName(prop.getProperty("graphDBName", "graphdbs"));
            conf.setBaseAcctNum(Long.parseLong(prop.getProperty("baseAcctNum", "1000000000000000000")));
            conf.setNodeCnt(Integer.parseInt(prop.getProperty("nodeCnt", "100")));
            conf.setTranCnt(Long.parseLong(prop.getProperty("tranCnt", "200")));
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
