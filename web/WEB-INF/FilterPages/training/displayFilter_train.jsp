<%-- 
    Document   : displayFilter_train
    Created on : 2014/4/4, 下午 12:53:37
    Author     : Leon

    the view used from FilterTrain_Servlet.java
    attr:
        Bestn, Bestk
    param:
        trainStockSymbol, trainStartDate, trainEndDate
        
--%>

<%@page import="org.luxion.ottzRserve.BeanFilter_train"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Filter Main Training</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/OTTZ_general.css" />
    </head>
    <body>        
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        <hr style="size:4px">
        
        <h1 align="center">Filter Training</h1>   
        <hr/>
        
        <table class="showTable">
            <tr>
                <th>建模股票Symbol</th><th>建模開始日期</th><th>建模結束日期</th>                
            </tr>
            <tr>
                <td>${param.trainStockSymbol}</td>
                <td>${trainStartDatetime}</td>
                <td>${trainEndDatetime}</td> 
            </tr>
            <tr>
                <th>Best N</th>                
                <th>Best K</th>
            </tr>
            <tr>
                <td>${Bestn}</td>
                <td>${Bestk}</td>                   
            </tr>                       
        </table>
            
        <form action='OperateDBFilter' method="post">
            <input type='hidden' name='action' value='addFilterTrain'/>
            <input type='hidden' name='bestN' value='${Bestn}'>
            <input type='hidden' name='bestK' value='${Bestk}'>
            <input type="hidden" name="trainStockSymbol" value="${param.trainStockSymbol}">
            <input type="hidden" name="trainStartDate" value="${trainStartDatetime}">
            <input type="hidden" name="trainEndDate" value="${trainEndDatetime}">
            <input type='submit' value='寫入此筆資料'>
            <input type="button" value='回上頁' onclick="history.go(-1); " />
        </form>
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
