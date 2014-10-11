<%-- 
    Document   : displayAllDB_testing
    Created on : 2014/3/23, 上午 04:29:15
    Author     : Leon
--%>


<%@page import="java.sql.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display ALL DB</title>
        <link rel="stylesheet" type="text/css" href="../CSS/OTTZ_general.css" />
    </head>
    <body>
        <%
            ServletContext sc = pageContext.getServletContext();
            String dbUrl = sc.getInitParameter("ottzDBurl");
            String ottzUser = sc.getInitParameter("ottzUser");
            String ottzPassword = sc.getInitParameter("ottzPassword");
        %>  
        <!--用於測試輸出的開發用區塊-->
<%--        <div id="devBlock" style="height: 400px;">
            <p>action: <%= request.getParameter("action")%></p>
            <p>TestingCode: <%= request.getParameter("TestingCode")%></p>
            <p>TestingRule: <%= request.getParameter("testingRule")%></p>
            <p>setSearchDate: <%= request.getParameter("setSearchDate")%></p>
            <p>searchStartDate: <%= request.getParameter("searchStartDate")%> searchEndDate: <%= request.getParameter("searchEndDate")%></p>
            <% String testSelect= ("true".equals(request.getParameter("setSearchDate")))? "WHERE StartDate = CONVERT(DATETIME, '"+request.getParameter("searchStartDate")+"') AND EndDate = CONVERT(DATETIME, '"+request.getParameter("searchEndDate")+"') ": ""  ; %>
            <p><%= "sql: " + testSelect%></p>
            <p><%= request.getQueryString()%></p>
        </div>  --%>
        
        
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        <hr style="size:4px">   
        <h1 style="text-align: center">All Testing Data</h1>
        
        <div id="searchDate">
            <form action="displayAllDB_testing.jsp" method="post" id="searchDateForm">
                測試開始日期<input type="date" name="searchStartDate" value="2013-01-01">
                測試結束日期<input type="date" name="searchEndDate" value="2013-12-01">
                <input type="hidden" name="setSearchDate" value="true">
                <input type="submit" value="設定日期">
            </form>
        </div>
         <%
            Connection conn = null; 
            Statement stmtAllTest = null;            
            ResultSet rsAllTest = null;                   

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance(); //註冊sqlserver driver            
            conn = DriverManager.getConnection(dbUrl,ottzUser,ottzPassword);
            stmtAllTest = conn.createStatement(); //在connection中 建立敘述   
//                String sqlTest = "SELECT * FROM [ottzDB].[dbo].[AllTesting] ORDER BY createTime DESC";
            String sqlTest = "SELECT createTime,StartDate,EndDate,StockSymbol,TestingCode,revenuTrade, RplotUrl,AllTesting.TradeNum,TestingRule FROM "
                            +"( "
                            +"SELECT F.createTime,F.StartDate,F.EndDate,F.StockSymbol,F.TestingCode,F.revenuTrade,F.FilterRplotUrl AS RplotUrl,F.TradeNum,'Filter' AS TestingRule "
                            +"FROM  "
                            +"(SELECT FilterTEsting.*, FilterTradeNum.number AS TradeNum FROM [dbo].[FilterTesting] AS FilterTesting "
                            +"JOIN "
                            +"(SELECT A.TestingCode, count(A.TestingCode) AS number FROM [dbo].[FilterTesting] AS A JOIN [dbo].[FilterTestingTrade] AS B "
                            +"ON A.TestingCode = B.TestingCode "
                            +"GROUP BY A.TestingCode) AS FilterTradeNum "
                            +"ON FilterTesting.TestingCode = FilterTradeNum.TestingCode) AS F "
                            +"UNION\t "
                            +"SELECT M.createTime,M.StartDate,M.EndDate,M.StockSymbol,M.TestingCode,M.revenuTrade,M.MARplotUrl AS RplotUrl,M.TradeNum,'MA' AS TestingRule  FROM  "
                            +"(SELECT MATesting.*, MATradeNum.number AS TradeNum FROM [ottzDB].[dbo].[MATesting] AS MATesting "
                            +"join (SELECT A.TestingCode, count(A.TestingCode) as number FROM "
                            +"[ottzDB].[dbo].[MATesting] AS A INNER JOIN [dbo].[MATestingTrade] AS B "
                            +"ON A.TestingCode = B.TestingCode "
                            +"GROUP BY A.TestingCode) AS MATradeNum "
                            +"ON MATesting.TestingCode = MATradeNum.TestingCode) AS M "
                            +"UNION\t "
                            +"SELECT R.createTime,R.StartDate,R.EndDate,R.StockSymbol,R.TestingCode,R.revenuTrade,R.RSIRplotUrl AS RplotUrl,R.TradeNum,'RSI' AS TestingRule FROM "
                            +"(SELECT RSITesting.*, RSITradeNum.number AS TradeNum FROM [ottzDB].[dbo].[RSITesting] AS RSITesting  "
                            +"join (SELECT A.TestingCode, count(A.TestingCode) as number FROM  "
                            +"[ottzDB].[dbo].[RSITesting] AS A INNER JOIN [dbo].[RSITestingTrade] AS B "
                            +"ON A.TestingCode = B.TestingCode "
                            +"GROUP BY A.TestingCode) AS RSITradeNum "
                            +"ON RSITesting.TestingCode = RSITradeNum.TestingCode)AS R "
                            +")AS AllTesting  "
//                          +"WHERE StartDate = CONVERT(DATETIME, '2013-1-1') AND EndDate = CONVERT(DATETIME, '2013-10-1') "
                            + (("true".equals(request.getParameter("setSearchDate")))? "WHERE StartDate = CONVERT(DATETIME, '"+request.getParameter("searchStartDate")+"') AND EndDate = CONVERT(DATETIME, '"+request.getParameter("searchEndDate")+"') ": "")
                            //如果有設時間條件 加入上列where敘述, 否則為空字串
                            +"ORDER BY revenuTrade DESC ";
            
            rsAllTest = stmtAllTest.executeQuery(sqlTest);
        %>
        <div>
        <table class="showTable">
            <tr>
                <th>測試方法</th>
                <th>資料建立時間</th>
                <th>股票代號</th>
                <th>測試開始日期</th>
                <th>測試結束日期</th>      
                <th>交易收益平均</th>
                <th>交易次數</th>
                <th>顯示</th>
            </tr>
        <!--顯示測試資料table-->    
        <%            
            while(rsAllTest.next())
            {
                String createTime = rsAllTest.getString("createTime");
                String TestingCode = rsAllTest.getString("TestingCode");
                String StockSymbol = rsAllTest.getString("StockSymbol");
                String StartDate = rsAllTest.getString("StartDate");
                String EndDate = rsAllTest.getString("EndDate");
                String revenuTrade = rsAllTest.getString("revenuTrade");
                String rPlotUrl = rsAllTest.getString("RplotUrl");
                String tradeNum = rsAllTest.getString("TradeNum");
                String testingRule = rsAllTest.getString("TestingRule");
                out.println("<tr>");
                out.println("<td>" + testingRule + "</td>");
                out.println("<td>" + createTime + "</td>");
                out.println("<td>" + StockSymbol + "</td>");
                out.println("<td>" + StartDate + "</td>");
                out.println("<td>" + EndDate + "</td>");
                out.println("<td>" + revenuTrade + "</td>");
                out.println("<td>" + tradeNum + "</td>");
                //顯示買賣點的連結按鈕 使用原網頁加入queryString action&TestingCode
                out.println("<td>"
                        + "<a href='displayAllDB_testing.jsp?action=showTrade&TestingCode=" + TestingCode + "&testingRule="+ testingRule + "'>顯示買賣點</a>"
                        + "</td>");
                //顯示R plot的連結按鈕 使用javascript function修改img1的src
                out.println("<td>"
                        + "<input type='button' value='顯示Plot圖' name='plot1' onclick=\"changeImg('" + rPlotUrl + "')\">" 
                        + "</td>");
                out.println("</tr>");
            }
            //此處先保留conn未關閉
            if(rsAllTest!=null) rsAllTest.close();
            if(stmtAllTest!=null) stmtAllTest.close();
        %>        
        </table>
        </div>
        
        <!--顯示交易table-->
        <%
            if("showTrade".equals(request.getParameter("action"))){
                Statement stmtAllTestTrade = null;
                ResultSet rsTestTrade = null;
                stmtAllTestTrade = conn.createStatement();
                //以下依據各testingRule, 至不同的table撈取資料
                if("Filter".equals(request.getParameter("testingRule"))){                    
                    String sqlTestTrade = "SELECT StartDate, EndDate, StockSymbol, bestN,bestK,BuyDate,BuyPrice,SellDate,SellPrice" +
                                           " FROM FilterTestingTrade AS A JOIN FilterTesting AS B" +
                                           " ON A.TestingCode = B.TestingCode" +
                                           " WHERE A.TestingCode = '" + request.getParameter("TestingCode") + "'";                    
                    rsTestTrade = stmtAllTestTrade.executeQuery(sqlTestTrade);                    
                }
                else if("MA".equals(request.getParameter("testingRule"))){
                    String sqlTestTrade = "SELECT StartDate, EndDate, StockSymbol, bestS,bestL,BuyDate,BuyPrice,SellDate,SellPrice" +
                                        " FROM MATestingTrade AS A JOIN MATesting AS B" +
                                        " ON A.TestingCode = B.TestingCode" +
                                        " WHERE A.TestingCode = '" + request.getParameter("TestingCode") + "'";  
                    rsTestTrade = stmtAllTestTrade.executeQuery(sqlTestTrade);
                }
                else if("RSI".equals(request.getParameter("testingRule"))){
                    String sqlTestTrade = "SELECT StartDate, EndDate, StockSymbol, bestS,bestL,BuyDate,BuyPrice,SellDate,SellPrice" +
                                        " FROM RSITestingTrade AS A JOIN RSITesting AS B" +
                                        " ON A.TestingCode = B.TestingCode" +
                                        " WHERE A.TestingCode = '" + request.getParameter("TestingCode") + "'";                    
                    rsTestTrade = stmtAllTestTrade.executeQuery(sqlTestTrade);
                }
                    
            
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
                    if(rsTestTrade == null){
                        out.println("<tr Style='color: red'><td colspan='4'>no match testing rule!!</td></tr>");
                    }
                    else{
                        while(rsTestTrade.next()){
                            String BuyDate = rsTestTrade.getString("BuyDate");
                            String BuyPrice = rsTestTrade.getString("BuyPrice");
                            String SellDate = rsTestTrade.getString("SellDate");
                            String SellPrice = rsTestTrade.getString("SellPrice");                        
                            out.println("<tr>");
                            out.println("<td>" + BuyDate + "</td>");
                            out.println("<td>" + BuyPrice + "</td>");
                            out.println("<td>" + SellDate + "</td>");
                            out.println("<td>" + SellPrice + "</td>");                                     
                            out.println("</tr>");
                        }
                    }   
                    if(rsTestTrade != null) rsTestTrade.close();
                    if(stmtAllTestTrade != null) stmtAllTestTrade.close();
                }//if showTrade end here            
            %>
                </table>
            </div>     
                 
                 

        <div id="Rplot_filterTest">            
            <img name="img1" src=""><br><br>
<!--            <input type="button" value="顯示Plot圖" name="plot1" onclick="changeImg()">        -->

            <script type="text/javascript">
                function changeImg(url){
                    document.images['img1'].src="../"+url;
                     }
            </script>
        </div>        
        <%
            
            if(conn!=null) conn.close();
        %>
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
