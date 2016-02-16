/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzDAO;

import java.sql.*;
import java.util.List;
import org.luxion.ottzServlet.TradeRecord;

/**
 *
 * @author Leon
 */
public class FilterDAO {
    Connection conn = null;
    
    public FilterDAO(String jdbcDriver, String dbUrl,String ottzUser, String ottzPassword) throws Exception{   
        //物件實體化同時建立connection
        Class.forName(jdbcDriver).newInstance(); //註冊sqlserver driver
        this.conn = DriverManager.getConnection(dbUrl,ottzUser,ottzPassword); //建立connection
    }
    
    public String insertTraining(String bestN_temp, String bestK_temp, String trainStartDate,String trainEndDate, String trainStockSymbol) throws Exception{
        String result; //用於輸出成功敘述   
        int insertRecord = 0;
        Statement stmt = null;
        
        //用轉型做檢查輸入值
        Double bestN_temp2 = Double.valueOf(bestN_temp);
        int bestN = bestN_temp2.intValue();
        double bestK = Double.parseDouble(bestK_temp);

        String sqlInsert = "INSERT INTO FilterTrain VALUES("
                                + "DEFAULT," //createTime default:getdate()
                                + bestN + ","
                                + bestK + ","
                                + "'" + trainStartDate + "',"
                                + "'" + trainEndDate + "',"
                                + "'" + trainStockSymbol + "'"
                                + ")"; 

        try{              
            stmt = conn.createStatement(); //在connection中 建立敘述
            insertRecord = stmt.executeUpdate(sqlInsert); //執行sql敘述 成功則回傳受影響的資料筆數                
        }catch(Exception e){
                throw new Exception(e);
        }finally{
                if(stmt != null)	stmt.close();
        }        
        result =  "<div style='color: red;'>建模資料 寫入完成</div>" + insertRecord + " 條記錄被增加到資料庫中。" + "<br/>執行的 SQL 敘述為: " + sqlInsert + "<br/><br/>";        
        return result;
    }
    
    public String deleteTraining(String dataKey) throws Exception{
        String result; //用於輸出成功敘述
        int deleteRecord = 0;
        
        Statement stmt = null;
        String sqlDelete = "DELETE FROM [dbo].[FilterTrain] WHERE dataKey =" + dataKey;

        try{
            stmt = conn.createStatement();
            deleteRecord = stmt.executeUpdate(sqlDelete);
            
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            if(stmt!=null) stmt.close();
        }
        result =  "<div style='color: red;'>建模資料 刪除完成</div>" + deleteRecord + " 條記錄從資料庫中刪除。" + "<br/>執行的 SQL 敘述為: " + sqlDelete + "<br/><br/>";
        
        return result;
    }
    
    public String insertTesting(String bestN,String bestK,String testStartDate,String testEndDate,String testStockSymbol,String revenuTrade,String testingCode,String filterPictureURL) throws Exception{       
        String result;
        int insertRecord = 0;
        Statement stmt = null;
                
        String sqlInsert = "INSERT INTO [dbo].[FilterTesting] " +
                        "VALUES(" + 
                        "DEFAULT," +
                        "'" + testStartDate + "'," +
                        "'" + testEndDate + "'," +
                        "'" + testStockSymbol + "'," +
                        bestN + "," +
                        bestK + "," + 
                        "'" + testingCode + "'," +
                        revenuTrade +"," +
                        "'" + filterPictureURL + "')";
        try{                           
            stmt = conn.createStatement(); //在connection中 建立敘述
            insertRecord = stmt.executeUpdate(sqlInsert); //執行sql敘述 成功則回傳受影響的資料筆數                
        }catch(Exception e){
            //不做有無重複值的insert check 而直接上拋SQL Exception
               throw new Exception(e);
        }finally{
                if(stmt != null)
                    stmt.close();
        }        
        result =  "<div style='color: red;'>測試資料 寫入完成</div>" + insertRecord + " 條記錄被增加到資料庫中。" + "<br/>執行的 SQL 敘述為: " + sqlInsert + "<br/><br/>";
        return result;
    }
    public String insertTestingTrade(List<TradeRecord> listTrade ,String testingCode) throws Exception{
        //此處使用PreparedStatement而非statement 用來做多筆insert         
        String result;
        PreparedStatement ps = null;
        String query = "INSERT INTO [dbo].[FilterTestingTrade] (BuyDate, BuyPrice, SellDate, SellPrice, TestingCode) VALUES (?, ?, ?, ?, ?)";
        try{
            ps = conn.prepareStatement(query);  
            for(TradeRecord tr: listTrade){
                ps.setString(1, tr.getBuyDate());
                ps.setString(2, tr.getBuyPrice());
                ps.setString(3, tr.getSellDate());
                ps.setString(4, tr.getSellPrice());
                ps.setString(5, testingCode);
                ps.addBatch();
            }
            ps.executeBatch();
        }catch(Exception e){
            throw new Exception(e);
        }
        finally{
            if(ps != null)
                ps.close();
        }
        result = new String("<div style='color: red;'>交易紀錄 寫入完成</div>");       
         
        return result;
    }
    
    public void closeConn() throws SQLException{
         if(conn != null)
            conn.close();
    }
}
