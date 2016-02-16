/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzRserve;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.luxion.ottzServlet.TradeRecord;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public class BeanMa_test extends BeanOTTZ_test{
    private String bestS;
    private String bestL;
    private String testingCode; 
    
    public BeanMa_test() throws Exception{
        
    }

    @Override
    public void excute() throws RserveException, REXPMismatchException, ParseException, Exception {
        try{    
            REXP x;
            c.eval("maTest <- maTesting(TestData, Bests, Bestl, plotUrl)");
            boolean checkBuysell = "NA".equals(c.eval("maTest$buysell[1,3]").asString());    
            int nRow;
            int ncol;
            if(checkBuysell){
                this.hasTrade = false;
                this.revenueTrade = "0";
            } else{
                //-----------紀錄此測試交易平均收益參數
                this.revenueTrade = c.eval("maTest$revenue").asString();

                nRow = c.eval("nrow(maTest$buysell)").asInteger();
                ncol = c.eval("ncol(maTest$buysell)").asInteger();

                //R的基本單位是vector, 故此處一次取一條vector放入二維陣列 再迴圈輸入arrayList
                String[][] arrayTrade = new String[nRow][ncol];
                for(int i =1; i <= nRow; i++)
                {
                    x = c.eval("as.character(maTest$buysell[" + i + ",])"); //此處取得的是R的vector(單維陣列)
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
                //產生testingCode
                this.testingCode = setTestingCode(testFullDate_start, testFullDate_end, testStockSymbol, bestS, bestL);
            }
        }catch(RserveException e){
            throw new Exception("發生Rserve Exception: " + e);
        }
    }
    
    public void setBestS(String bestS) throws RserveException{        
        this.bestS = bestS;
        c.eval("Bests<-" + this.bestS);
    }
    public void setBestL(String bestL) throws RserveException{        
        this.bestL = bestL;
        c.eval("Bestl<-" + this.bestL);
    }

    private String setTestingCode(String testStartDate, String testEndDate, String testStockSymbol, String bestS, String bestL) {
        String testStartDate_eliminate = testStartDate.replaceAll("-", "").replace(" ", "").replace(":", "");
        String testEndDate_eliminate = testEndDate.replaceAll("-", "").replace(" ", "").replace(":", "");
         
        String testingCode_temp = testStartDate_eliminate + testEndDate_eliminate + testStockSymbol + bestS + bestL;   
        return testingCode_temp;
    }

    public String getTestingCode() {
        return this.testingCode;
    }
    public String getBestS(){
        return this.bestS;
    }
    public String getBestL(){
        return this.bestL;
    }
    public String getMaPictureURL()
    {
        return "/Rimage/MA/" + this.rplotPicName + ".png";
    }  
    
    
}
