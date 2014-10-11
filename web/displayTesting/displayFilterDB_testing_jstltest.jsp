<%-- 
    Document   : displayFilterDB
    Created on : 2014/1/30, 下午 06:06:21
    Author     : Leon
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display filter db</title>
        <link rel="stylesheet" type="text/css" href="../CSS/OTTZ_general.css" />
    </head>
    <body>                
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        <hr style="size:4px">        
        <h1 style="text-align: center">Filter Testing Data</h1>
        
        <sql:setDataSource driver="com.microsoft.sqlserver.jdbc.SQLServerDriver" user="Leon" password="rtdx9900" 
                           url="jdbc:sqlserver://localhost\SQLSERVER2012;databaseName=ottzDB"
                           scope="session"
                           var="myDS"/>
        <sql:query var="rs" dataSource="jdbc/ottzDB" scope="page">
            SELECT FilterTEsting.*, FilterTradeNum.number AS TradeNum FROM [dbo].[FilterTesting] AS FilterTesting
            JOIN
            (SELECT A.TestingCode, count(A.TestingCode) AS number FROM [dbo].[FilterTesting] AS A JOIN [dbo].[FilterTestingTrade] AS B
            ON A.TestingCode = B.TestingCode
            GROUP BY A.TestingCode) AS FilterTradeNum
            ON FilterTesting.TestingCode = FilterTradeNum.TestingCode
            ORDER BY createTime
        </sql:query>
            
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
        <c:forEach items="${rs.rows}" var="row">
            <%
                String temp;
            %>
            <tr>                
                    <td>${row['createTime']}</td>
                    <td>${row['StockSymbol']}</td>
                    <td>${row['StartDate']}</td>
                    <td>${row['EndDate']}</td>
                    <td>${row['bestN']}</td>
                    <td>${row['bestK']}</td>  
                    <td>${row['revenuTrade']}</td>
                    <td>${row['TradeNum']}</td>
                    <td>
                        <a href='displayRSIDB_testing.jsp?action=showTrade&TestingCode=${row['TestingCode']}'>顯示買賣點</a>
                    </td>
                    <td>
                          <input type='button' value='顯示Plot圖' name='plot1' onclick="changeImg()">
                    </td>
                </tr>
        </c:forEach>
            
        
        <%            
//            while(rsFilterTest.next())
//            {
//                String createTime = rsFilterTest.getString("createTime");
//                String TestingCode = rsFilterTest.getString("TestingCode");
//                String StockSymbol = rsFilterTest.getString("StockSymbol");
//                String StartDate = rsFilterTest.getString("StartDate");
//                String EndDate = rsFilterTest.getString("EndDate");
//                String bestN = rsFilterTest.getString("bestN");
//                String bestK = rsFilterTest.getString("bestK");
//                String revenuTrade = rsFilterTest.getString("revenuTrade");
//                String FilterRplotUrl = rsFilterTest.getString("FilterRplotUrl");
//                String tradeNum = rsFilterTest.getString("TradeNum");
//                out.println("<tr>");
//                out.println("<td>" + createTime + "</td>");
//                out.println("<td>" + StockSymbol + "</td>");
//                out.println("<td>" + StartDate + "</td>");
//                out.println("<td>" + EndDate + "</td>");
//                out.println("<td>" + bestN + "</td>");
//                out.println("<td>" + bestK + "</td>");
//                out.println("<td>" + revenuTrade + "</td>");
//                out.println("<td>" + tradeNum + "</td>");
//                //顯示買賣點的連結按鈕 使用原網頁加入queryString action&TestingCode
//                out.println("<td>"
//                        + "<a href='displayFilterDB_testing.jsp?action=showTrade&TestingCode=" + TestingCode + "'>顯示買賣點</a>"
//                        + "</td>");
//                //顯示R plot的連結按鈕 使用javascript function修改img1的src
//                out.println("<td>"
//                        + "<input type='button' value='顯示Plot圖' name='plot1' onclick=\"changeImg('" + FilterRplotUrl + "')\">" 
//                        + "</td>");
//                out.println("</tr>");
//            }             
        %>            
        </table>
        </div>
        
        <c:if test="${param.action == 'showTrade'}">       
        
            <div>
            <table class="showTable">
                <tr>
                    <th>買點日期</th>
                    <th>買點價格</th>
                    <th>賣點日期</th>
                    <th>賣點價格</th>
                </tr>
            <!--顯示交易資料-->
            
            </table>
            </div>
        </c:if>

        <hr>
        <div id="Rplot_filterTest">            
            <img name="img1" src=""><br><br>
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

