package com.paypal.risk.ganma.experiment.graph.neo4j.pfTest;

public class PFTestResult {
    private long endTime;
    private long startTime;
    
    private long acctNum;

    

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getTakeTime(){
        return endTime - startTime;
    }

    public long getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(long acctNum) {
        this.acctNum = acctNum;
    }
    @Override
    public String toString(){
        return acctNum + " started:" + startTime + ", takes:" + getTakeTime() + "ms.";
    }
}
