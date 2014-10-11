<%-- 
    Document   : displayMADB_testing
    Created on : 2014/3/11, 下午 05:53:08
    Author     : Leon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display MA DB</title>
        <link rel="stylesheet" type="text/css" href="../CSS/OTTZ_general.css" />
<!--        <style type="text/css">
            @import url("../CSS/OTTZ_general.css");
        </style>-->
    </head>
    <body>
        <%
            ServletContext sc = pageContext.getServletContext();
            String dbUrl = sc.getInitParameter("ottzDBurl");
            String ottzUser = sc.getInitParameter("ottzUser");
            String ottzPassword = sc.getInitParameter("ottzPassword");
        %>   
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        <hr style="size:4px">   
        <h1 style="text-align: center">MA Testing Data</h1>
        <%
            Connection conn = null; 
            Statement stmtMATest = null;
            ResultSet rsMATest = null;
            Statement stmtMATestTrade = null;
            ResultSet rsMATestTrade = null;            
            
            //註記: 此處try catch被分割 目前測試發現無法catch exception
            try{
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance(); //註冊sqlserver driver
                conn = DriverManager.getConnection(dbUrl,ottzUser,ottzPassword);
                stmtMATest = conn.createStatement(); //在connection中 建立敘述   
//                String sqlTest = "SELECT * FROM [ottzDB].[dbo].[MATesting] ORDER BY createTime DESC";
                String sqlTest = "SELECT MATesting.*, MATradeNum.number AS TradeNum FROM [ottzDB].[dbo].[MATesting] AS MATesting " + 
                                  "join (SELECT A.TestingCode, count(A.TestingCode) as number FROM " + 
                                    "[ottzDB].[dbo].[MATesting] AS A INNER JOIN [dbo].[MATestingTrade] AS B "+
                                    "ON A.TestingCode = B.TestingCode "+
                                    "GROUP BY A.TestingCode) AS MATradeNum "+
                                    "ON MATesting.TestingCode = MATradeNum.TestingCode "+
                                    "ORDER BY MATesting.createTime DESC";
                rsMATest = stmtMATest.executeQuery(sqlTest);
        %>
        <div>
        <table class="showTable">
            <tr>
                <th>資料建立時間</th>
                <th>股票代號</th>
                <th>測試開始日期</th>
                <th>測試結束日期</th>      
                <th>S值</th>
                <th>L值</th>
                <th>交易收益平均</th>
                <th>交易次數</th>
                <th>顯示</th>
            </tr>
        <!--顯示測試資料table-->    
        <%            
            while(rsMATest.next())
            {
                String createTime = rsMATest.getString("createTime");
                String TestingCode = rsMATest.getString("TestingCode");
                String StockSymbol = rsMATest.getString("StockSymbol");
                String StartDate = rsMATest.getString("StartDate");
                String EndDate = rsMATest.getString("EndDate");
                String bestS = rsMATest.getString("bestS");
                String bestL = rsMATest.getString("bestL");
                String revenuTrade = rsMATest.getString("revenuTrade");
                String MARplotUrl = rsMATest.getString("MARplotUrl");
                String tradeNum = rsMATest.getString("TradeNum");
                out.println("<tr>");
                out.println("<td>" + createTime + "</td>");
                out.println("<td>" + StockSymbol + "</td>");
                out.println("<td>" + StartDate + "</td>");
                out.println("<td>" + EndDate + "</td>");
                out.println("<td>" + bestS + "</td>");
                out.println("<td>" + bestL + "</td>");
                out.println("<td>" + revenuTrade + "</td>");
                out.println("<td>" + tradeNum + "</td>");
                //顯示買賣點的連結按鈕 使用原網頁加入queryString action&TestingCode
                out.println("<td>"
                        + "<a href='displayMADB_testing.jsp?action=showTrade&TestingCode=" + TestingCode + "'>顯示買賣點</a>"
                        + "</td>");
                //顯示R plot的連結按鈕 使用javascript function修改img1的src
                out.println("<td>"
                        + "<input type='button' value='顯示Plot圖' name='plot1' onclick=\"changeImg('" + MARplotUrl + "')\">" 
                        + "</td>");
                out.println("</tr>");
            }             
        %>            
        </table>
        </div>
        <!--當使用者點選顯示買賣點連結時顯示該TestingCode關聯的交易資料-->
        <%
            if("showTrade".equals(request.getParameter("action"))){
                stmtMATestTrade = conn.createStatement(); //在connection中 建立敘述   
                String sqlTestTrade = "SELECT StartDate, EndDate, StockSymbol, bestS,bestL,BuyDate,BuyPrice,SellDate,SellPrice" +
                                        " FROM MATestingTrade AS A JOIN MATesting AS B" +
                                        " ON A.TestingCode = B.TestingCode" +
                                        " WHERE A.TestingCode = '" + request.getParameter("TestingCode") + "'";                    
                rsMATestTrade = stmtMATestTrade.executeQuery(sqlTestTrade);
        %>
        <div>
             <table class="showTable">
                 <tr>
                     <th>買點日期</th>
                     <th>買點價格</th>
                     <th>賣點日期</th>
                     <th>賣點價格</th>
                 </tr>
        <!--顯示交易資料-->
        <%
                while(rsMATestTrade.next()){

                    String BuyDate = rsMATestTrade.getString("BuyDate");
                    String BuyPrice = rsMATestTrade.getString("BuyPrice");
                    String SellDate = rsMATestTrade.getString("SellDate");
                    String SellPrice = rsMATestTrade.getString("SellPrice");                        
                    out.println("<tr>");
                    out.println("<td>" + BuyDate + "</td>");
                    out.println("<td>" + BuyPrice + "</td>");
                    out.println("<td>" + SellDate + "</td>");
                    out.println("<td>" + SellPrice + "</td>");                                     
                    out.println("</tr>");
                }
            }
        %>
             </table>
        </div>
        <%
            }
            catch(SQLException e){
                out.println("SQL Exception: " + e);
            }
            finally{
                if(rsMATest != null) rsMATest.close();
                if(stmtMATest != null) stmtMATest.close();
                if(rsMATestTrade != null) rsMATestTrade.close();
                if(stmtMATestTrade != null) stmtMATestTrade.close();
                conn.close();
            }            
        %>
        <hr>
        <div id="Rplot_filterTest">            
            <img name="img1" src=""><br><br>
<!--            <input type="button" value="顯示Plot圖" name="plot1" onclick="changeImg()">        -->

            <script type="text/javascript">
                function changeImg(url){
                    document.images['img1'].src="../"+url;
                     }
            </script>
        </div>        
        
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
