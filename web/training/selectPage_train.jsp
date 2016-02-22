<%-- 
    Document   : SelectPageFilter
    Created on : 2013/12/1, 下午 04:23:37
    Author     : Leon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Filter Rule Select Train</title>
        
        <script type="text/javascript" src="../jquery.min.js"></script>
        <script type="text/javascript">
            $(function() {        
                $("#toggleFilterUseYahoo").click(function(){
                    $("#formFilterUseYahoo").slideToggle();
                })
                $("#toggleFilterUseDB").click(function(){
                    $("#formFilterUseDB").slideToggle();
                })
                
                $("#toggleMaUseYahoo").click(function(){
                    $("#formMaUseYahoo").slideToggle();
                })
                $("#toggleMaUseDB").click(function(){
                    $("#formMaUseDB").slideToggle();
                })
                
                $("#toggleRsiUseYahoo").click(function(){
                    $("#formRsiUseYahoo").slideToggle();
                })
                $("#toggleRsiUseDB").click(function(){
                    $("#formRsiUseDB").slideToggle();
                })
            });
        </script>
        
        <style type="text/css">
            @import url("../CSS/OTTZ_general.css");
            @import url("../CSS/formwizard.css");
        </style>
    </head>
    <body> 
        
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %>    
        <hr style="size:4px">
 
        <h1 style="text-align: center;">Filter Selection</h1>

        <div id="toggleFilterUseYahoo" style="text-align: center;margin: 10px">YahooFinace資料來源</div>
        <div id="formFilterUseYahoo" class="selectionMain" style="display:none;">
            <form action="../FilterTraining_Servlet" method="post" name="filterForm" id="filterForm">
            <!--給servlet的資料來源參數-->
            <input type='hidden' name='datasource' value='yahoo'/>
            <!--第一頁-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>輸入建模參數</legend>
                <table border="0" class="selectionTable">  
                    <tr>
                        <td>建模股票代號</td>
                        <td><input type="text" name="trainStockSymbol" value="2330"></td>
                    </tr>
                    <tr>
                        <td>建模開始日期</td>
                        <td>                            
                            <input type = "date" name = "trainStartDate" value = "2015-01-01"/>
                        </td>
                    </tr>
                    <tr>
                        <td>建模開始日期</td>
                        <td>                            
                            <input type = "date" name = "trainEndDate" value = "2015-12-01"/>
                        </td>                       
                    </tr>
                </table>
            </fieldset>
            <!--最終頁 確認與送出-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>最終確認</legend>
                <table class="selectionTable">                   
                    <tr>
                        <td colspan="100">
                            <input type="submit" value="送出">
                            <input type="reset" name="Reset" id="button" value="清除">
                        </td>
                    </tr>                
                </table>
            </fieldset>
            </form>
        </div>        
        <div id="toggleFilterUseDB" style="text-align: center; margin: 10px">SQL Server資料來源</div>        
        <div id="formFilterUseDB" class="selectionMain" style="display:none;">
            <form action="../FilterTraining_Servlet" method="post" name="filterForm" id="filterForm">
            <!--給servlet的資料來源參數-->
            <input type='hidden' name='datasource' value='db'/>
            
            <!--第一頁-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>輸入建模參數</legend>
                <table border="0" class="selectionTable">  
                    <tr>
                        <td>建模股票代號</td>
                        <td><input type="text" name="trainStockSymbol" value="2330"></td>
                    </tr>
                    <tr>
                        <td>建模開始日期</td>
                        <td>                            
                            <input type = "date" name = "trainStartDate" value = "2013-08-01"/>
                            <input type="time" name="trainStartTime" value="08:30"/>                              
                        </td>                        
                    </tr>
                    <tr>
                        <td>建模結束日期</td>
                        <td>                            
                            <input type = "date" name = "trainEndDate" value = "2013-08-02"/>
                            <input type="time" name="trainEndTime" value="13:00"/> 
                        </td>                       
                    </tr>
                </table>
            </fieldset>
            <!--最終頁 確認與送出-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>最終確認</legend>
                <table class="selectionTable">                   
                    <tr>
                        <td colspan="100">
                            <input type="submit" value="送出">
                            <input type="reset" name="Reset" id="button" value="清除">
                        </td>
                    </tr>                
                </table>
            </fieldset>
            </form>
        </div>
        
        <h1 style="text-align: center">MA Selection</h1>
        
        <div id="toggleMaUseYahoo" style="text-align: center;margin: 10px">YahooFinace資料來源</div>
        <div id="formMaUseYahoo" class="selectionMain" style="display:none;">
            <form action="../MaTraining_Servlet" method="post" name="MaForm" id="maForm">
            <!--給servlet的資料來源參數-->
            <input type='hidden' name='datasource' value='yahoo'/>
            <!--第一頁-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>輸入建模參數</legend>
                <table border="0" class="selectionTable">  
                    <tr>
                        <td>建模股票代號</td>
                        <td><input type="text" name="trainStockSymbol" value="2330"></td>
                    </tr>
                    <tr>
                        <td>建模開始日期</td>
                        <td>                            
                            <input type = "date" name = "trainStartDate" value = "2015-01-01"/>
                        </td>
                    </tr>
                    <tr>
                        <td>建模結束日期</td>
                        <td>                            
                            <input type = "date" name = "trainEndDate" value = "2015-05-01"/>
                        </td>                       
                    </tr>
                </table>
            </fieldset>

            <!--最終頁 確認與送出-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>最終確認</legend>
                <table class="selectionTable">                   
                    <tr>
                        <td colspan="100">
                            <input type="submit" value="送出">
                            <input type="reset" name="Reset" id="button" value="清除">
                        </td>
                    </tr>                
                </table>
            </fieldset>
            </form>
        </div>        
        <div id="toggleMaUseDB" style="text-align: center;margin: 10px">SQL Server資料來源</div>
        <div id="formMaUseDB" class="selectionMain" style="display:none;">
            <form action="../MaTraining_Servlet" method="post" name="MaForm" id="maForm">
            <!--給servlet的資料來源參數-->
            <input type='hidden' name='datasource' value='db'/>
            <!--第一頁-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>輸入建模參數</legend>
                <table border="0" class="selectionTable">  
                    <tr>
                        <td>建模股票代號</td>
                        <td><input type="text" name="trainStockSymbol" value="2330"></td>
                    </tr>
                    <tr>
                        <td>建模開始日期</td>
                        <td>                            
                            <input type = "date" name = "trainStartDate" value = "2013-08-01"/>
                            <input type="time" name="trainStartTime" value="08:30"/>  
                        </td>
                    </tr>
                    <tr>
                        <td>建模結束日期</td>
                        <td>                            
                            <input type = "date" name = "trainEndDate" value = "2013-08-02"/>
                            <input type="time" name="trainEndTime" value="13:00"/> 
                        </td>                       
                    </tr>
                </table>
            </fieldset>

            <!--最終頁 確認與送出-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>最終確認</legend>
                <table class="selectionTable">                   
                    <tr>
                        <td colspan="100">
                            <input type="submit" value="送出">
                            <input type="reset" name="Reset" id="button" value="清除">
                        </td>
                    </tr>                
                </table>
            </fieldset>
            </form>
        </div>
        
        <h1 style="text-align: center">RSI Selection</h1>
        <div id="toggleRsiUseYahoo" style="text-align: center;margin: 10px">YahooFinace資料來源</div>
        <div id="formRsiUseYahoo" class="selectionMain" style="display:none;">
            <form action="../RsiTraining_Servlet" method="post" name="RsiForm" id="RsiForm">
            <input type='hidden' name='datasource' value='yahoo'/>
            <!--第一頁-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>輸入建模參數</legend>
                <table border="0" class="selectionTable">  
                    <tr>
                        <td>建模股票代號</td>
                        <td><input type="text" name="trainStockSymbol" value="2330"></td>
                    </tr>
                    <tr>
                        <td>建模開始日期</td>
                        <td>                            
                            <input type = "date" name = "trainStartDate" value = "2015-01-01"/>
                        </td>
                    </tr>
                    <tr>
                        <td>建模結束日期</td>
                        <td>                            
                            <input type = "date" name = "trainEndDate" value = "2015-06-01"/>
                        </td>                       
                    </tr>
                </table>
            </fieldset>

            <!--最終頁 確認與送出-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>最終確認</legend>
                <table class="selectionTable">                   
                    <tr>
                        <td colspan="100">
                            <input type="submit" value="送出">
                            <input type="reset" name="Reset" id="button" value="清除">
                        </td>
                    </tr>                
                </table>
            </fieldset>
            </form>
        </div>
        
        <div id="toggleRsiUseDB" style="text-align: center;margin: 10px">SQL Server資料來源</div>
        <div id="formRsiUseDB" class="selectionMain" style="display:none;">
            <form action="../RsiTraining_Servlet" method="post" name="RsiForm" id="RsiForm">    
            <input type='hidden' name='datasource' value='db'/>
            <!--第一頁-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>輸入建模參數</legend>
                <table border="0" class="selectionTable">  
                    <tr>
                        <td>建模股票代號</td>
                        <td><input type="text" name="trainStockSymbol" value="2330"></td>
                    </tr>
                    <tr>
                        <td>建模開始日期</td>
                        <td>                            
                            <input type = "date" name = "trainStartDate" value = "2013-08-01"/>
                            <input type="time" name="trainStartTime" value="08:30"/>  
                        </td>
                    </tr>
                    <tr>
                        <td>建模結束日期</td>
                        <td>                            
                            <input type = "date" name = "trainEndDate" value = "2013-08-02"/>
                            <input type="time" name="trainEndTime" value="13:00"/> 
                        </td>                       
                    </tr>
                </table>
            </fieldset>

            <!--最終頁 確認與送出-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>最終確認</legend>
                <table class="selectionTable">                   
                    <tr>
                        <td colspan="100">
                            <input type="submit" value="送出">
                            <input type="reset" name="Reset" id="button" value="清除">
                        </td>
                    </tr>                
                </table>
            </fieldset>
            </form>
        </div>
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
