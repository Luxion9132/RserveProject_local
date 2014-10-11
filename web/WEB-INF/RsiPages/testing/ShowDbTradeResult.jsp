<%-- 
    Document   : ShowDbTradeResult
    Created on : 2014/4/20, 上午 05:48:42
    Author     : Leon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DB Trade Result</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/OTTZ_general.css" />
    </head>
    <body>
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        <hr style="size:4px">
        
        <h1 align="center">DB Trade Result MA Testing</h1>   
        ${resultInsertTesting}
        ${resultInsertTestingTrade}
   
        
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
