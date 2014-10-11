<%-- 
    Document   : mainMenu
    Created on : 2014/1/28, 下午 12:29:15
    Author     : Leon
    3/11 棄用OTTZ_general.css的div class, 改用MainmenuStyle1.css
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
//    request.getContextPath();
    //${pageContext.request.contextPath}
%>

<div>
    <ul id="navigation">
        <li><a href="/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/training/selectPage_train.jsp">Training</a></li>
        
        <li><a href="${pageContext.request.contextPath}/testing/selectPage_test.jsp">Testing</a></li>
        <li class="sub"><a>Display Training Data</a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/displayTraining/displayFilterDB_training.jsp">Filter Training Data</a></li>
                <li><a href="${pageContext.request.contextPath}/displayTraining/displayMaDB_training.jsp">MA Training Data</a></li>
                <li><a href="${pageContext.request.contextPath}/displayTraining/displayRsiDB_training.jsp">RSI Training Data</a></li>
           </ul>
        </li>
        <li class="sub"><a>Display Testing Data</a>
            <ul>                
                <li><a href="${pageContext.request.contextPath}/displayTesting/displayFilterDB_testing.jsp">Filter Testing Data</a></li>
                <li><a href="${pageContext.request.contextPath}/displayTesting/displayMADB_testing.jsp">MA Testing Data</a></li>
                <li><a href="${pageContext.request.contextPath}/displayTesting/displayRSIDB_testing.jsp">RSI Testing Data</a></li>
                <li><a href="${pageContext.request.contextPath}/displayTesting/displayAllDB_testing.jsp">All Testing Data</a></li>
            </ul>
        </li>
    </ul>
</div>

<div style="clear: both;"></div>
