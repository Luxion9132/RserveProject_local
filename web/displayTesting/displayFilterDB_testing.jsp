<%-- 
    Document   : displayFilterDB
    Created on : 2014/1/30, 下午 06:06:21
    Author     : Leon
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display filter db</title>
        <link rel="stylesheet" type="text/css" href="../CSS/OTTZ_general.css" />
<!--        <style type="text/css">
            @import url("../CSS/OTTZ_general.css");
        </style>-->
    </head>
    <body>
        <!--置入預設setting參數-->
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
        <h1 style="text-align: center">Filter Testing Data</h1>
        <%
            Connection conn = null; 
            Statement stmtFilterTest = null;
            ResultSet rsFilterTest = null;
            Statement stmtFilterTestTrade = null;
            ResultSet rsFilterTestTrade = null;            
            
            //註記: 此處try catch被分割 目前測試發現無法catch exception
            try{
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance(); //註冊sqlserver driver
//                conn = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLSERVER2012;databaseName=" + ottzDBname 
//                                                ,ottzUser
//                                                ,ottzPassword); //建立connection
                conn = DriverManager.getConnection(dbUrl,ottzUser,ottzPassword);
                stmtFilterTest = conn.createStatement(); //在connection中 建立敘述   
//                String sqlTest = "SELECT * FROM [ottzDB].[dbo].[FilterTesting] ORDER BY createTime DESC";
                String sqlTest = "SELECT FilterTEsting.*, FilterTradeNum.number AS TradeNum FROM [dbo].[FilterTesting] AS FilterTesting " +
                                    "JOIN "+
                                    "(SELECT A.TestingCode, count(A.TestingCode) AS number FROM [dbo].[FilterTesting] AS A JOIN [dbo].[FilterTestingTrade] AS B " +
                                    "ON A.TestingCode = B.TestingCode "+
                                    "GROUP BY A.TestingCode) AS FilterTradeNum "+
                                    "ON FilterTesting.TestingCode = FilterTradeNum.TestingCode "+
                                    "ORDER BY createTime";
                rsFilterTest = stmtFilterTest.executeQuery(sqlTest);
        %>
        <div>
        <table class="showTable">
            <tr>
                <th>資料建立時間</th>
                <th>股票代號</th>
                <th>測試開始日期</th>
                <th>測試結束日期</th>      
                <th>N值</th>
                <th>K值</th>
                <th>交易收益平均</th>
                <th>交易次數</th>
                <th>顯示</th>
            </tr>
        <!--顯示測試資料table-->    
        <%            
            while(rsFilterTest.next())
            {
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
                out.println("<tr>");
                out.println("<td>" + createTime + "</td>");
                out.println("<td>" + StockSymbol + "</td>");
                out.println("<td>" + StartDate + "</td>");
                out.println("<td>" + EndDate + "</td>");
                out.println("<td>" + bestN + "</td>");
                out.println("<td>" + bestK + "</td>");
                out.println("<td>" + revenuTrade + "</td>");
                out.println("<td>" + tradeNum + "</td>");
                //顯示買賣點的連結按鈕 使用原網頁加入queryString action&TestingCode
                out.println("<td>"
                        + "<a href='displayFilterDB_testing.jsp?action=showTrade&TestingCode=" + TestingCode + "'>顯示買賣點</a>"
                        + "</td>");
                //顯示R plot的連結按鈕 使用javascript function修改img1的src
                out.println("<td>"
                        + "<input type='button' value='顯示Plot圖' name='plot1' onclick=\"changeImg('" + FilterRplotUrl + "')\">" 
                        + "</td>");
                out.println("</tr>");
            }             
        %>            
        </table>
        </div>
        <c:if test="${param.action == 'showTrade'}">       
        
            <!--當使用者點選顯示買賣點連結時顯示該TestingCode關聯的交易資料-->
            <%
//                if("showTrade".equals(request.getParameter("action"))){
                    stmtFilterTestTrade = conn.createStatement(); //在connection中 建立敘述   
                    String sqlTestTrade = "SELECT StartDate, EndDate, StockSymbol, bestN,bestK,BuyDate,BuyPrice,SellDate,SellPrice" +
                                            " FROM FilterTestingTrade AS A JOIN FilterTesting AS B" +
                                            " ON A.TestingCode = B.TestingCode" +
                                            " WHERE A.TestingCode = '" + request.getParameter("TestingCode") + "'";                    
                    rsFilterTestTrade = stmtFilterTestTrade.executeQuery(sqlTestTrade);
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
                    while(rsFilterTestTrade.next()){
                        
                        String BuyDate = rsFilterTestTrade.getString("BuyDate");
                        String BuyPrice = rsFilterTestTrade.getString("BuyPrice");
                        String SellDate = rsFilterTestTrade.getString("SellDate");
                        String SellPrice = rsFilterTestTrade.getString("SellPrice");                        
                        out.println("<tr>");
                        out.println("<td>" + BuyDate + "</td>");
                        out.println("<td>" + BuyPrice + "</td>");
                        out.println("<td>" + SellDate + "</td>");
                        out.println("<td>" + SellPrice + "</td>");                                     
                        out.println("</tr>");
                    }                
            %>
            
            </table>
            </div>
        </c:if>
            
        <%
            }
            catch(SQLException e){
                out.println("SQL Exception: " + e);
            }
            finally{
                if(rsFilterTest != null) rsFilterTest.close();
                if(stmtFilterTest != null) stmtFilterTest.close();
                if(rsFilterTestTrade != null) rsFilterTestTrade.close();
                if(stmtFilterTestTrade != null) stmtFilterTestTrade.close();
                if(conn != null) conn.close();
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

