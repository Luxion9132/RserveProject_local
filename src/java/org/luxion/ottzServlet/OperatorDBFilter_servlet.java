/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzServlet;

import org.luxion.ottzDAO.FilterDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Leon
 */
public class OperatorDBFilter_servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");        
        //由context parameter取得JDBC需要的資訊
        ServletContext sc = this.getServletConfig().getServletContext();        
        String dbUrl = sc.getInitParameter("ottzDBurl");
        String ottzUser = sc.getInitParameter("ottzUser");
        String ottzPassword = sc.getInitParameter("ottzPassword");
        String jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        
        if("deleteFilterTrain".equals(action)){
            String dataKey = request.getParameter("id");
            
            FilterDAO daoFT_delete = null;
            String sResultDelete ="";
            try{
                daoFT_delete = new FilterDAO(jdbcDriver, dbUrl,ottzUser,ottzPassword);
                sResultDelete = daoFT_delete.deleteTraining(dataKey);
            }catch(Exception e){
                sResultDelete = "取消刪除: " + e;
            }finally{
                if(daoFT_delete != null) try {
                    daoFT_delete.closeConn();
                } catch (SQLException ex) {
                    throw new ServletException("關閉sql connection階段發生例外:<br/> " + ex);
                }
            }            
            request.setAttribute("ResultInsert", sResultDelete);
            request.getRequestDispatcher("/WEB-INF/FilterPages/training/ShowDdTradeResult.jsp").forward(request, response);
        }	
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");        
        //由context parameter取得JDBC需要的資訊
        ServletContext sc = this.getServletConfig().getServletContext();        
        String dbUrl = sc.getInitParameter("ottzDBurl");
        String ottzUser = sc.getInitParameter("ottzUser");
        String ottzPassword = sc.getInitParameter("ottzPassword");
        String jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        
        if("addFilterTrain".equals(action)){
                //取得qureyString的bestN與bestK
                String bestN_temp = request.getParameter("bestN");
                String bestK_temp = request.getParameter("bestK");
                String trainStartDate = request.getParameter("trainStartDate");
                String trainEndDate = request.getParameter("trainEndDate");
                String trainStockSymbol = request.getParameter("trainStockSymbol");
                
                //建立寫入FilterTrain物件                
                FilterDAO daoFT_insert = null;
                String sResultInsert ="";
                try{
                    daoFT_insert = new FilterDAO(jdbcDriver, dbUrl,ottzUser,ottzPassword);
                    sResultInsert = daoFT_insert.insertTraining(bestN_temp, bestK_temp,trainStartDate, trainEndDate, trainStockSymbol);
                }catch(Exception e){
                    sResultInsert = "取消寫入紀錄: " + e;
                }finally{
                    if(daoFT_insert != null) try {
                        daoFT_insert.closeConn();
                    } catch (SQLException ex) {
                        throw new ServletException("關閉sql connection階段發生例外:<br/> " + ex);
                    }
                }
                //輸出結果
                request.setAttribute("bestn", bestN_temp);
                request.setAttribute("bestk", bestK_temp);             
                request.setAttribute("ResultInsert", sResultInsert);
                request.getRequestDispatcher("/WEB-INF/FilterPages/training/ShowDdTradeResult.jsp").forward(request, response);                
            }
        if("addFilterTest".equals(action)){
            List<TradeRecord> listTrade = (List<TradeRecord>)request.getSession().getAttribute("trade");
            
            String bestN = request.getParameter("bestN");
            String bestK = request.getParameter("bestK");
            String testStockSymbol = request.getParameter("testStockSymbol");
            String testStartDatetime = request.getParameter("testStartDatetime");
            String testEndDatetime = request.getParameter("testEndDatetime");
            String RplotUrl = request.getParameter("RplotUrl");
            String revenue = request.getParameter("revenue");
            String testingCode = request.getParameter("testingCode"); 
            String hasTrade = request.getParameter("hasTrade");
            
            
            FilterDAO daoFT_insert = null;
            String resultInsertTesting = "";
            String resultInsertTestingTrade="";
               
            try{
                daoFT_insert = new FilterDAO(jdbcDriver, dbUrl,ottzUser,ottzPassword);
                //note: insertTestingTrade因為沒有pk, 此處無法用例外排除重複寫入 故放在insertTesting下方 
                //當insertTesting失敗即會跳過insertTestingTrade進入例外處理
                resultInsertTesting = daoFT_insert.insertTesting(bestN,bestK,testStartDatetime,testEndDatetime,testStockSymbol,revenue,testingCode,RplotUrl);
                if("true".equals(hasTrade))
                    resultInsertTestingTrade = daoFT_insert.insertTestingTrade(listTrade, testingCode);
            }catch(Exception e){
                resultInsertTesting = "取消寫入紀錄: " + e;
            }finally{
                    if(daoFT_insert != null) try {
                        daoFT_insert.closeConn();
                    } catch (SQLException ex) {
                        throw new ServletException("關閉sql connection階段發生例外:<br/> " + ex);
                    }
                }                
                //輸出結果                
                request.setAttribute("resultInsertTesting", resultInsertTesting);
                request.setAttribute("resultInsertTestingTrade", resultInsertTestingTrade);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/FilterPages/testing/ShowDbTradeResult.jsp");
                rd.forward(request, response);
                System.out.println(resultInsertTesting);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
