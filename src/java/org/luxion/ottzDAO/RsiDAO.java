/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.luxion.ottzServlet.TradeRecord;

/**
 *
 * @author Leon
 */
public class RsiDAO {
    
    Connection conn = null;
    
    public RsiDAO(String jdbcDriver, String dbUrl,String ottzUser, String ottzPassword) throws Exception{
        //物件實體化同時建立connection
        Class.forName(jdbcDriver).newInstance(); //註冊sqlserver driver
        this.conn = DriverManager.getConnection(dbUrl,ottzUser,ottzPassword); //建立connection
    }
    
    public String insertTraining(String bestS_temp, String bestL_temp, String trainStartDate,String trainEndDate, String trainStockSymbol) throws Exception{
        String result; //用於輸出成功敘述   
        int insertRecord = 0;
        Statement stmt = null;
        
        //用轉型做檢查輸入值
        Double dBestS = Double.parseDouble(bestS_temp);
        int bestS = dBestS.intValue();
        Double dBestL = Double.parseDouble(bestL_temp);
        int bestL = dBestL.intValue();

        String sqlInsert = "INSERT INTO RSITrain VALUES("
                                + "DEFAULT," //createTime default:getdate()
                                + bestS + ","
                                + bestL + ","
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
        String sqlDelete = "DELETE FROM [dbo].[RSITrain] WHERE dataKey =" + dataKey;
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
     
    public String insertTesting(String bestS,String bestL,String testStartDate,String testEndDate,String testStockSymbol,String revenuTrade,String testingCode,String rsiPictureURL) throws Exception{       
        String result;
        int insertRecord = 0;
        Statement stmt = null;
                
        String sqlInsert = "INSERT INTO [dbo].[RSITesting] " +
                        "VALUES(" + 
                        "DEFAULT," +
                        "'" + testStartDate + "'," +
                        "'" + testEndDate + "'," +
                        "'" + testStockSymbol + "'," +
                        bestS + "," +
                        bestL + "," + 
                        "'" + testingCode + "'," +
                        revenuTrade +"," +
                        "'" + rsiPictureURL + "')";
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
        String query ="INSERT INTO [dbo].[RSITestingTrade] (BuyDate, BuyPrice, SellDate, SellPrice, TestingCode) VALUES (?, ?, ?, ?, ?)";
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
