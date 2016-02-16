
import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Leon
 */
public class Test_DateFormate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        String testFullDate_start = "2013-1-1 8:00";
        String testFullDate_end = "2013-12-1";
        String testStockSymbol = "2330";
        String bestN = "2";
        String bestK = "0.1";
        
        
        
        Test_DateFormate td = new Test_DateFormate();       
        
        String test1 = td.replaceDate(testFullDate_start);
        String test2 = td.replaceDate(testFullDate_end);
        
        String tc = td.setTestingCode(testFullDate_start, testFullDate_end, testStockSymbol, bestN, bestK);
    }
    private String replaceDate(String str){
        String temp = str.replaceAll("-", "").replace(" ", "").replace(":", "");        
        return temp;
    }
     private String setTestingCode(String testStartDate, String testEndDate, String testStockSymbol, String bestN, String bestK) throws ParseException{
        //用SimpleDateFormat先將String轉Date, 再次將Date轉String, 目標2014-1-8 -> 20140108
//        SimpleDateFormat testGetFormat = new SimpleDateFormat("yyyy-MM-dd");
//        java.util.Date dStart = testGetFormat.parse(testStartDate);
//        java.util.Date dEnd = testGetFormat.parse(testEndDate);
//
//        SimpleDateFormat testSetFormat = new SimpleDateFormat("yyyyMMdd");
//        String testStartDateInput = testSetFormat.format(dStart);
//        String testEndDateInput = testSetFormat.format(dEnd);
        //testingCode組合: 開始日期+結束日期+股票代號+bestN+bestK
        String testStartDate_eliminate = testStartDate.replaceAll("-", "").replace(" ", "").replace(":", "");
        String testEndDate_eliminate = testEndDate.replaceAll("-", "").replace(" ", "").replace(":", "");
         
        String testingCode_temp = testStartDate_eliminate + testEndDate_eliminate + testStockSymbol + bestN + bestK;   
        return testingCode_temp;
    }  
}
