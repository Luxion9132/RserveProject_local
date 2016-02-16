/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzRserve;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public class BeanRsi_train extends BeanOTTZ_train {
    private String bestS;
    private String bestL;
    
    public BeanRsi_train() throws Exception{
        
    }
    
    @Override
    public void excute() throws REXPMismatchException, Exception {
        try{
            REXP x;      
            String havaData = c.eval("exists('TrainData')").asString();
            String[] temp = c.eval("as.character(TrainData[1,])").asStrings();
            
            c.eval("rsiTrain <- rsiTraining(TrainData)");
            
            //-----------紀錄最佳參數            
            x = c.eval("rsiTrain$Bests");
            this.bestS = x.asString();
            x = c.eval("rsiTrain$Bestl");
            this.bestL = x.asString();
        }catch(RserveException e){
            throw new Exception("發生Rserve Exception: " + e);
        }
    }
    public String getBestS(){
        return this.bestS;
    }
    public String getBestL(){
        return this.bestL;
    }
    
}
