/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzRserve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public abstract class BeanOTTZ_train {
    /* 測試建模資料參數: 股票代號與日期 
     * 日期月份減1, 例a=0&b=1&c=2013 是為2013年1月1號, (含2013/1/1)     
     */
    private String trainStockSymbol;
    private String trainDate_start;
    private String trainMonth_start; //注意1月的話此處為0, 12月的話應為11
    private String trainYear_start;
    private String trainDate_end;
    private String trainMonth_end;
    private String trainYear_end;
    
    protected RConnection c = null;
    
    public BeanOTTZ_train() throws Exception{
        try{
            this.c = new RConnection();
            c.eval("library(\"OTTS\")\n" +
                    "library(\"RjavaOTTZ\")\n" +
                    "library(\"RODBC\") \n" +
                    "library(\"DBI\") \n" +
                    "library(\"sqldf\")");
        }catch(Exception e){
            throw new Exception("在Rserve Connection實體化階段 發生Rserve Exception:<br/>請檢查是否開啟Rserve!!<br/>" + e);            
        }
    }
    
    public abstract void excute() throws REXPMismatchException, Exception;
    
    public void closeRconn() throws RserveException{
        if(c.isConnected())
            c.eval("rm(list=ls())");
            c.close();
    }
    
    public void setRDataSource(String stockSymbol,String trainStartDate,String trainEndDate) throws Exception{
        setTrainStockSymbol(stockSymbol);
        setTrainStartDate(trainStartDate);
        setTrainEndDate(trainEndDate);
        //String trainUrl = "http://ichart.finance.yahoo.com/table.csv?s=1537.TW&a=00&b=01&c=2013&d=06&e=28&f=2013&g=d";
        String trainUrl;        
        trainUrl = getYahooFinaceUrl_train();        
        try {
            c.assign("TrainURL", trainUrl);
            c.eval("TrainData <- read.csv(TrainURL)\n" +
                    "TrainData$Date <- as.POSIXlt(TrainData$Date)\n" +
                    "TrainData <- TrainData[order(TrainData$Date), ]");
        } catch (RserveException ex) {
            throw new Exception("在set R dataSource階段 發生Rserve Exception:<br/>" + ex);            
        }
    }
    public void setRDataSource(ServletContext sc, String stockSymbol,String trainStartDate,String trainStartTime, String trainEndDate, String trainEndTime) throws Exception{
        String ottzDBname = sc.getInitParameter("ottzDBname");
        String ottzUser = sc.getInitParameter("ottzUser");
        String ottzPassword = sc.getInitParameter("ottzPassword");
        String ottzODBC = sc.getInitParameter("ottzODBC");
        
        String temp = "conn<-buildConnection('" +ottzODBC+ "','" +ottzUser+ "','"+ottzPassword+"')\n" +
                    "TrainData<-getStockDB(conn,'"+trainStartDate+" "+trainStartTime + "','"+trainEndDate+" "+trainEndTime+"','"+stockSymbol+"')\n" +
                    "odbcClose(conn)";
        
        try {
//            c.assign("TrainURL", trainUrl);
            c.eval("conn<-buildConnection('" +ottzODBC+ "','" +ottzUser+ "','"+ottzPassword+"')\n" +
                    "TrainData<-getStockDB(conn,'"+trainStartDate+" "+trainStartTime + "','"+trainEndDate+" "+trainEndTime+"','"+stockSymbol+"')\n" +
                    "odbcClose(conn)");
        } catch (RserveException ex) {
            throw new Exception("在set R dataSource階段 發生Rserve Exception:<br/>" + ex);            
        }
    }
    
    /**
     *設置trainStockSymbol
     * @param stockSymbol 例: 2330
     */
    private void setTrainStockSymbol(String stockSymbol){
       this.trainStockSymbol = stockSymbol;
    }    
    /**
     *將字串日期分割為年,月,日 分別儲存為
     * trainYear_start
     * trainMonth_start
     * trainDate_start
     * @param trainEndDate　例: 2013-01-10 
     * @throws ParseException
     */
    private void setTrainStartDate(String trainStartDate) throws ParseException{
        String pat = "yyyy-MM-dd"; //設置輸入模式
        SimpleDateFormat sdf = new SimpleDateFormat(pat); //使用輸入模式建立SimpleDateFormate模板
        Date d = sdf.parse(trainStartDate); //使用模板將String轉為java Date物件
        Calendar c = Calendar.getInstance(); //實體化Calendar物件 用作取Date物件的年月日值
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH); //在此輸出的month通常要+1 但因為yahoo finance月份也是-1. 因此剛好可以直接放入
        int day = c.get(Calendar.DATE);
        
        this.trainYear_start = String.valueOf(year);
        this.trainMonth_start = String.valueOf(month);
        this.trainDate_start = String.valueOf(day);
    }   
    /**
     *將字串日期分割為年,月,日 分別儲存為
     * trainYear_end
     * trainMonth_end
     * trainDate_end
     * @param trainEndDate　例: 2013-08-22 
     * @throws ParseException
     */
    private void setTrainEndDate(String trainEndDate) throws ParseException{
        String pat = "yyyy-MM-dd"; //設置輸入模式
        SimpleDateFormat sdf = new SimpleDateFormat(pat); //使用輸入模式建立SimpleDateFormate模板
        Date d = sdf.parse(trainEndDate); //使用模板將String轉為java Date物件
        Calendar c = Calendar.getInstance(); //實體化Calendar物件 用作取Date物件的年月日值
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH); //在此輸出的month通常要+1 但因為yahoo finance月份也是-1. 因此剛好可以直接放入
        int day = c.get(Calendar.DATE);
        
        this.trainYear_end = String.valueOf(year);
        this.trainMonth_end = String.valueOf(month);
        this.trainDate_end = String.valueOf(day);
    }
    
    
    public String getYahooFinaceUrl_train(){
        String trainUrl = "http://ichart.finance.yahoo.com/table.csv?" + 
                    "s=" + trainStockSymbol + ".TW" +
                    "&a=" + trainMonth_start + 
                    "&b=" + trainDate_start +
                    "&c=" + trainYear_start +
                    "&d=" + trainMonth_end + 
                    "&e=" + trainDate_end + 
                    "&f=" + trainYear_end + "&g=d";
        return trainUrl;
    }    
}
