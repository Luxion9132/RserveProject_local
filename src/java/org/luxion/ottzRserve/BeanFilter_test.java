/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzRserve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.luxion.ottzServlet.TradeRecord;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;

/**
 *
 * @author Leon
 */
public class BeanFilter_test extends BeanOTTZ_test {
    private String bestN;   //可能是使用者輸入或來自train資料庫的參數
    private String bestK;
    
    /*回傳參數
     * revenueTrade 交易平均收益 不記有買無賣
     * recordTrade: 各筆買賣交易紀錄的二維陣列
     * filterTestPicName 年月日分秒組成的檔案名稱 不含路徑與類型 如20140225071245
     */
    
    private String testingCode; //用於資料庫寫入時使用的primary Key 由startdate enddate bestN bestK組成
    
    public BeanFilter_test() throws Exception{
    
    }
    
    @Override
    public void excute() throws RserveException, REXPMismatchException, ParseException, Exception{
        //執行這項前須已執行過setTestStockSymbol,setTestEndDate, setTestStartDate,setBestK,setBestN
        try{    
            REXP x;
            String checkUrl = c.eval("plotUrl").asString();
            String checkBestn = c.eval("Bestn").asString();
            String checkBestk = c.eval("Bestk").asString();
            String checkTestData = c.eval("exists('TestData')").asString();
            
//            c.eval("filterTest <- filterTesting(\"http://ichart.finance.yahoo.com/table.csv?s=1537.TW&a=0&b=1&c=2013&d=11&e=1&f=2013&g=d\", 20, 0.1, 'C:/Users/Leon/Documents/NetBeansProjects/OTTZ_ver4/web/Rimage/Filter/20140407231228.png')"); 
            c.eval("filterTest <- filterTesting(TestData, Bestn, Bestk, plotUrl)");
            
            
            //檢查是否有交易點,有可能有一筆買點無賣點 故檢查賣點日期[1,3]是否為NA, NA是R的null值
            String check1 = c.eval("filterTest$buysell[1,3]").asString();
            boolean checkBuysell = "NA".equals(c.eval("filterTest$buysell[1,3]").asString());            
            
            int nRow;
            int ncol;
            if(checkBuysell){
                this.hasTrade = false;
                this.revenueTrade = "0";
            }
            else{
                //-----------紀錄此測試交易平均收益參數
                this.revenueTrade = c.eval("filterTest$revenue").asString();
            
                nRow = c.eval("nrow(filterTest$buysell)").asInteger();
                ncol = c.eval("ncol(filterTest$buysell)").asInteger();
                
                //R的基本單位是vector, 故此處一次取一條vector放入二維陣列 再迴圈輸入arrayList
                String[][] arrayTrade = new String[nRow][ncol];
                for(int i =1; i <= nRow; i++)
                {
                    x = c.eval("as.character(filterTest$buysell[" + i + ",])"); //此處取得的是R的vector(單維陣列)
                    arrayTrade[i-1] = x.asStrings();
                }
                
                listTrade = new ArrayList<TradeRecord>();
                TradeRecord tr;
                for(String[] s: arrayTrade){
                    tr = new TradeRecord();
                    tr.setBuyDate(s[0]);
                    tr.setBuyPrice(s[1]);
                    tr.setSellDate(s[2]);
                    tr.setSellPrice(s[3]);
                    listTrade.add(tr);
                }
                
            }
            //產生testingCode
            this.testingCode = setTestingCode(testFullDate_start, testFullDate_end, testStockSymbol, bestN, bestK);

        }catch(RserveException e){
            throw new Exception("發生Rserve Exception: " + e);
        }
        
    }
   
    public void setBestN(String bestN) throws RserveException{        
        this.bestN = bestN;
        c.eval("Bestn<-" + this.bestN);
    }
    public void setBestK(String bestK) throws RserveException{        
        this.bestK = bestK;
        c.eval("Bestk<-" + this.bestK);
    }
    /**
     *
     * @param testStartDate 測試開始日期, ex:2014-1-8
     * @param testEndDate 測試結束日期, ex:2014-12-5
     * @param testStockSymbol 股票代號, ex: 2330
     * @param bestN 最佳N ex:2
     * @param bestK 最佳K ex:0.12
     * @return
     * @throws ParseException
     */
    private String setTestingCode(String testStartDate, String testEndDate, String testStockSymbol, String bestN, String bestK) throws ParseException{
//        //用SimpleDateFormat先將String轉Date, 再次將Date轉String, 目標2014-1-8 -> 20140108
//        SimpleDateFormat testGetFormat = new SimpleDateFormat("yyyy-MM-dd");
//        java.util.Date dStart = testGetFormat.parse(testStartDate);
//        java.util.Date dEnd = testGetFormat.parse(testEndDate);
//
//        SimpleDateFormat testSetFormat = new SimpleDateFormat("yyyyMMdd");
//        String testStartDateInput = testSetFormat.format(dStart);
//        String testEndDateInput = testSetFormat.format(dEnd);
//        //testingCode組合: 開始日期+結束日期+股票代號+bestN+bestK
        String testStartDate_eliminate = testStartDate.replaceAll("-", "").replace(" ", "").replace(":", "");
        String testEndDate_eliminate = testEndDate.replaceAll("-", "").replace(" ", "").replace(":", "");
         
        String testingCode_temp = testStartDate_eliminate + testEndDate_eliminate + testStockSymbol + bestN + bestK;   
        return testingCode_temp;
    }      
    
    public String getFilterPictureURL()
    {
        return "/Rimage/Filter/" + this.rplotPicName + ".png";
    }    
    public String getBestN(){
        return this.bestN;                
    }
    public String getBestK(){
        return this.bestK;
    }   
    @Override
    public String getTestingCode(){
        return this.testingCode;                
    }         
       
}

