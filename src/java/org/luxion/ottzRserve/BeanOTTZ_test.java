/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzRserve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletContext;
import org.luxion.ottzServlet.TradeRecord;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public abstract class BeanOTTZ_test {
    protected String testStockSymbol; //例2330
    
    protected String testFullDate_start;
    private String testDate_start;
    private String testMonth_start;
    private String testYear_start;
    
    protected String testFullDate_end;
    private String testDate_end;
    private String testMonth_end;
    private String testYear_end;
    
    protected String rplotPicName;
    
    protected List<TradeRecord> listTrade;
    protected String revenueTrade; //在執行excute後實體化
    protected boolean hasTrade = true; //如果buysell[1,1]無值 此flag為false
    
    protected RConnection c = null;
    
    public BeanOTTZ_test() throws Exception{
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
    
    public abstract void excute() throws RserveException, REXPMismatchException, ParseException, Exception;
    
    public void closeRconn() throws RserveException{
        if(c.isConnected())
            c.eval("rm(list=ls())");
            c.close();
    }
    
    public void setRDataSource(String stockSymbol,String trainStartDate,String trainEndDate) throws Exception{
        setTestStockSymbol(stockSymbol);
        setTestStartDate(trainStartDate);
        setTestEndDate(trainEndDate);
        //String trainUrl = "http://ichart.finance.yahoo.com/table.csv?s=1537.TW&a=00&b=01&c=2013&d=06&e=28&f=2013&g=d";
        String testUrl;
        testUrl = this.getYahooFinaceUrl_test();    
        
        try {
            c.assign("TestURL", testUrl);
            c.eval("TestData <- read.csv(TestURL)\n" +
                    "TestData$Date <- as.POSIXlt(TestData$Date)\n" +
                    "TestData <- TestData[order(TestData$Date), ]");
        } catch (RserveException ex) {
            throw new Exception("在set R dataSource階段 發生Rserve Exception:<br/>" + ex);            
        }
    }
    public void setRDataSource(ServletContext sc, String stockSymbol,String testStartDate,String testStartTime, String testEndDate, String testEndTime) throws Exception{
        String ottzDBname = sc.getInitParameter("ottzDBname");
        String ottzUser = sc.getInitParameter("ottzUser");
        String ottzPassword = sc.getInitParameter("ottzPassword");
        String ottzODBC = sc.getInitParameter("ottzODBC");
        
        //for testingcode 可能需要修改
        this.testFullDate_start = testStartDate+" "+testStartTime;
        this.testFullDate_end = testEndDate+" "+testEndTime;
        this.testStockSymbol = stockSymbol;
        try {
            c.eval("conn<-buildConnection('" +ottzODBC+ "','" +ottzUser+ "','"+ottzPassword+"')\n" +
                    "TestData<-getStockDB(conn,'"+testStartDate+" "+testStartTime + "','"+testEndDate+" "+testEndTime+"','"+stockSymbol+"')\n" +
                    "odbcClose(conn)");
        } catch (RserveException ex) {
            throw new Exception("在set R dataSource階段 發生Rserve Exception:<br/>" + ex);            
        }
    }
    
    public void setRplotUrl(String localPath) throws RserveException{
        this.rplotPicName = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String rplotUrl = localPath + this.rplotPicName + ".png";
        c.assign("plotUrl", rplotUrl);
    }
    
    private void setTestStockSymbol(String stockSymbol){
        this.testStockSymbol = stockSymbol;
    }    
    private void setTestStartDate(String testStartDate) throws ParseException{
        //先記錄原日期格式 後將日期分割為year, month, date分別記錄 用來傳給R做撈取yahoo finacial的字串
        this.testFullDate_start = testStartDate;
        String pat = "yyyy-MM-dd"; //設置輸入模式
        SimpleDateFormat sdf = new SimpleDateFormat(pat); //使用輸入模式建立SimpleDateFormate模板
        Date d = sdf.parse(testStartDate); //使用模板將String轉為java Date物件
        Calendar c = Calendar.getInstance(); //實體化Calendar物件 用作取Date物件的年月日值
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH); //在此輸出的month通常要+1 但因為yahoo finance月份也是-1. 因此剛好可以直接放入
        int day = c.get(Calendar.DATE);
        
        this.testYear_start = String.valueOf(year);
        this.testMonth_start = String.valueOf(month);
        this.testDate_start = String.valueOf(day);
    }    
    private void setTestEndDate(String testEndDate) throws ParseException{
        //先記錄原日期格式 後將日期分割為year, month, date分別記錄 用來傳給R做撈取yahoo finacial的字串
        this.testFullDate_end = testEndDate;
        String pat = "yyyy-MM-dd"; //設置輸入模式
        SimpleDateFormat sdf = new SimpleDateFormat(pat); //使用輸入模式建立SimpleDateFormate模板
        Date d = sdf.parse(testEndDate); //使用模板將String轉為java Date物件
        Calendar c = Calendar.getInstance(); //實體化Calendar物件 用作取Date物件的年月日值
        c.setTime(d);
        
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH); //在此輸出的month通常要+1 但因為yahoo finance月份也是-1. 因此剛好可以直接放入
        int day = c.get(Calendar.DATE);
        
        this.testYear_end = String.valueOf(year);
        this.testMonth_end = String.valueOf(month);
        this.testDate_end = String.valueOf(day);
    }
    
    public  String getTestStockSymbol(){
        return this.testStockSymbol;
    }
    public String getTestStartDate(){
        return this.testFullDate_start;
    }
    public String getTestEndDate(){
        return this.testFullDate_end;
    }
    public List<TradeRecord> getRecordTrade(){
        return this.listTrade;
    }
    public String getRevenuTrade(){
        return this.revenueTrade;
    }
    public boolean getHasTrade(){
        return this.hasTrade;
    } 
    
    public abstract String getTestingCode();
    
    protected String getYahooFinaceUrl_test(){
        String testUrl = "http://ichart.finance.yahoo.com/table.csv?" +
                "s=" + testStockSymbol + ".TW" +
                "&a=" + testMonth_start + 
                "&b=" + testDate_start +
                "&c=" + testYear_start +
                "&d=" + testMonth_end + 
                "&e=" + testDate_end + 
                "&f=" + testYear_end + "&g=d";
        return testUrl;
    }    
}


