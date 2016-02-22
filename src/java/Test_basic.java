/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;

/**
 *
 * @author Leon
 */
public class Test_basic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Connection conn = null;
        Statement stmtFilterTest = null;
        ResultSet rsFilterTest = null;
        Statement stmtFilterTestTrade = null;
        ResultSet rsFilterTestTrade = null;

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance(); //註冊sqlserver driver
        conn = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=ottzDB", "sa", "rtdx9900");
        stmtFilterTest = conn.createStatement(); //在connection中 建立敘述   
//                String sqlTest = "SELECT * FROM [ottzDB].[dbo].[FilterTesting] ORDER BY createTime DESC";
        String sqlTest = "SELECT FilterTEsting.*, FilterTradeNum.number AS TradeNum FROM [dbo].[FilterTesting] AS FilterTesting "
                + "JOIN "
                + "(SELECT A.TestingCode, count(A.TestingCode) AS number FROM [dbo].[FilterTesting] AS A JOIN [dbo].[FilterTestingTrade] AS B "
                + "ON A.TestingCode = B.TestingCode "
                + "GROUP BY A.TestingCode) AS FilterTradeNum "
                + "ON FilterTesting.TestingCode = FilterTradeNum.TestingCode "
                + "ORDER BY createTime";
        rsFilterTest = stmtFilterTest.executeQuery(sqlTest);
        while (rsFilterTest.next()) {
            String createTime = rsFilterTest.getString("createTime");
            String TestingCode = rsFilterTest.getString("TestingCode");
            String StockSymbol = rsFilterTest.getString("StockSymbol");
            String StartDate = rsFilterTest.getString("StartDate");
            String EndDate = rsFilterTest.getString("EndDate");
            String bestN = rsFilterTest.getString("bestN");
            String bestK = rsFilterTest.getString("bestK");
            String revenuTrade = rsFilterTest.getString("revenuTrade");
            String FilterRplotUrl = rsFilterTest.getString("FilterRplotUrl");
            String tradeNum = rsFilterTest.getString("TradeNum");
        }
    }
}
