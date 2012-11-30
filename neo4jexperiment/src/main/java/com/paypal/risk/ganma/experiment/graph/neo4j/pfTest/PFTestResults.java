package com.paypal.risk.ganma.experiment.graph.neo4j.pfTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class PFTestResults {
    List<PFTestResult> results = new ArrayList<PFTestResult>();
    PFTestConfiguration conf;
    
    public PFTestResults(PFTestConfiguration conf){
        this.conf = conf;
    }
    synchronized public void addResult(PFTestResult result){
        results.add(result);
    }
    
    public void print(){
        
        long total = 0L;
        long max = 0L;
        for(PFTestResult result: results){
            total += result.getTakeTime();
            if(result.getTakeTime() > max){
                max = result.getTakeTime();
            }
        }
        int size = results.size();
        double avg = (double)total/size;
        PrintStream output = getPrintStream();
        
        output.println("Graph traverse depth:" + conf.getGoDepth());
        output.println("Traverse direction:" + conf.getDirection());
        output.println("Requests number per second:" + size);
        output.println("Avarage time:" + avg + "ms");
        output.println("Max time:" + max + "ms");
        output.println("");
        output.println("--------------------------");
        for(PFTestResult result: results){
            output.println(result);
        }
    }
    
    private PrintStream getPrintStream(){
        String fileName = conf.getOutputFile();
        PrintStream result = System.out;
        if(fileName == null || fileName.isEmpty()){
            return result;
        }
        try{
        File file = new File(fileName);
        if(!file.exists()){
            file.createNewFile();
        }
            result = new PrintStream(new FileOutputStream(file));
        
        }catch(Exception e){
            
        }
        return result;
        
    }
}
