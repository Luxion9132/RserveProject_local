<%-- 
    Document   : displayFilter_test
    Created on : 2014/4/19, 下午 05:57:29
    Author     : Leon
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Filter Main Testing</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/OTTZ_general.css" />
    </head>
    <body>        
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        <hr style="size:4px">
        
        <h1 align="center">Filter Testing</h1>   
        
        <!--table 1. 測試參數-->
        <table class="showTable">
            <tr>
                <th>Best N</th>                
                <th>Best K</th>
            </tr>
            <tr>
                <td>${BestN}</td>
                <td>${BestK}</td> 
            </tr>
            
            <tr>
                <th>測試股票Symbol</th><th>測試開始日期</th><th>測試結束日期</th>                
            </tr>
            <tr>
                <td>${testStockSymbol}</td>
                <td>${testStartDatetime}</td>
                <td>${testEndDatetime}</td> 
            </tr>
        </table>
        
        <c:if test="${hasTrade}">        
        <table class="showTable">
            <tr>
                <th>買點日期</th>
                <th>買點價格</th>
                <th>賣點日期</th>
                <th>賣點價格</th>
            </tr>          
            <c:forEach var="tra" items="${trade}">
                <tr>
                    <td><c:out value="${tra.buyDate}" default="none"/></td>
                    <td><c:out value="${tra.buyPrice}" default="none"/></td>
                    <td><c:out value="${tra.sellDate}" default="none"/></td>
                    <td><c:out value="${tra.sellPrice}" default="none"/></td>
                </tr>
            </c:forEach>
            <tr><th>預計交易平均獲利(不計有買無賣)</th></tr>
            <tr><td>${revenue}</td></tr>
        </table>
        </c:if>   
            
        <c:if test="${not hasTrade}">
            <p style="font-size: 18px; margin: 50px">測試時間範圍內 無符合的買賣點</p>
        </c:if> 
            
        <!--以下區塊設置R plot圖片位置與顯示按鈕-->
        <div id="Rplot_filterTest">
            <img name="img1" src=""><br><br>
            <input type="button" value="顯示Plot圖" name="plot1" onclick="changeImg()">        

            <script type="text/javascript">
                function changeImg(){
                    document.images['img1'].src="${pageContext.request.contextPath}${RplotUrl}";
                     }
            </script>
        </div>
                     
        <form action="OperateDBFilter" method="post">
            <input type='hidden' name='action' value='addFilterTest'/>
            <input type='hidden' name='bestN' value='${BestN}'>
            <input type='hidden' name='bestK' value='${BestK}'>
            <input type="hidden" name="testStockSymbol" value="${testStockSymbol}">
            <input type="hidden" name="testStartDatetime" value="${testStartDatetime}">
            <input type="hidden" name="testEndDatetime" value="${testEndDatetime}">
            <input type="hidden" name="revenue" value="${revenue}">
            <input type="hidden" name="RplotUrl" value="${RplotUrl}">            
            <input type="hidden" name="hasTrade" value="${hasTrade}">
            <input type="hidden" name="testingCode" value="${testingCode}">
            <input type="submit" value="寫入測試資料">
        </form>
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
