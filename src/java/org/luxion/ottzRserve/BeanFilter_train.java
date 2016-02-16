/*
 * 4/8
 * 修改為使用RjavaOTTZ package模式
 */
package org.luxion.ottzRserve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
/**
 *
 * @author Leon
 */
public class BeanFilter_train extends BeanOTTZ_train{
    private String bestN;
    private String bestK;
    
    public BeanFilter_train() throws Exception{
        
    }
    
    @Override
    public void excute() throws REXPMismatchException, Exception{
        try{
            REXP x;      
//            String havaData = c.eval("exists('TrainData')").asString();
            c.eval("filterTrain <- filterTraining(TrainData)");
            
            //-----------紀錄最佳N K參數            
            x = c.eval("filterTrain$Bestn");
            this.bestN = x.asString();
            x = c.eval("filterTrain$Bestk");
            this.bestK = x.asString();
        }catch(RserveException e){
            throw new Exception("發生Rserve Exception: " + e);
        }
    }
    public String getBestN(){
        return this.bestN;
    }
    public String getBestK(){
        return this.bestK;
    }
    
}