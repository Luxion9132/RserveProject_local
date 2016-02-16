/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.luxion.ottzDAO.RsiDAO;

/**
 *
 * @author Leon
 */
public class OperatorDBRSI_servlet extends HttpServlet {


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
        
        if("deleteRsiTrain".equals(action)){
            String dataKey = request.getParameter("id");
            
            RsiDAO daoFT_delete = null;
            String sResultDelete ="";
            try{
                daoFT_delete = new RsiDAO(jdbcDriver, dbUrl,ottzUser,ottzPassword);
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
            request.getRequestDispatcher("/WEB-INF/RsiPages/training/ShowDdTradeResult.jsp").forward(request, response);
        }	
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
        
         if("addRsiTrain".equals(action)){
                //取得qureyString的bestN與bestK
                String bestS = request.getParameter("bestS");
                String bestL = request.getParameter("bestL");
                String trainStartDate = request.getParameter("trainStartDate");
                String trainEndDate = request.getParameter("trainEndDate");
                String trainStockSymbol = request.getParameter("trainStockSymbol");
                
                //建立寫入FilterTrain物件                
                RsiDAO daoRT_insert = null;
                String sResultInsert ="";
                try{
                    daoRT_insert = new RsiDAO(jdbcDriver, dbUrl,ottzUser,ottzPassword);
                    sResultInsert = daoRT_insert.insertTraining(bestS, bestL,trainStartDate, trainEndDate, trainStockSymbol);
                }catch(Exception e){
                    sResultInsert = "取消寫入紀錄: " + e;
                }finally{
                    if(daoRT_insert != null) try {
                        daoRT_insert.closeConn();
                    } catch (SQLException ex) {
                        throw new ServletException("關閉sql connection階段發生例外:<br/> " + ex);
                    }
                }
                //輸出結果
                request.setAttribute("bestS", bestS);
                request.setAttribute("bestL", bestL);             
                request.setAttribute("ResultInsert", sResultInsert);
                request.getRequestDispatcher("/WEB-INF/MaPages/training/ShowDdTradeResult.jsp").forward(request, response);                
         }
         if("addRsiTest".equals(action)){
            List<TradeRecord> listTrade = (List<TradeRecord>)request.getSession().getAttribute("trade");
            
            String bestS = request.getParameter("bestS");
            String bestL = request.getParameter("bestL");
            String testStockSymbol = request.getParameter("testStockSymbol");
            String testStartDatetime = request.getParameter("testStartDatetime");
            String testEndDatetime = request.getParameter("testEndDatetime");
            String RplotUrl = request.getParameter("RplotUrl");
            String revenue = request.getParameter("revenue");
            String testingCode = request.getParameter("testingCode"); 
            String hasTrade = request.getParameter("hasTrade");
            
            
            RsiDAO daoRT_insert = null;
            String resultInsertTesting = "";
            String resultInsertTestingTrade="";
               
            try{
                daoRT_insert = new RsiDAO(jdbcDriver, dbUrl,ottzUser,ottzPassword);
                //note: insertTestingTrade因為沒有pk, 此處無法用例外排除重複寫入 故放在insertTesting下方 
                //當insertTesting失敗即會跳過insertTestingTrade進入例外處理
                resultInsertTesting = daoRT_insert.insertTesting(bestS,bestL,testStartDatetime,testEndDatetime,testStockSymbol,revenue,testingCode,RplotUrl);
                if("true".equals(hasTrade))
                    resultInsertTestingTrade = daoRT_insert.insertTestingTrade(listTrade, testingCode);
            }catch(Exception e){
                resultInsertTesting = "取消寫入紀錄: " + e;
            }finally{
                    if(daoRT_insert != null) try {
                        daoRT_insert.closeConn();
                    } catch (SQLException ex) {
                        throw new ServletException("關閉sql connection階段發生例外:<br/> " + ex);
                    }
                }                
                //輸出結果                
                request.setAttribute("resultInsertTesting", resultInsertTesting);
                request.setAttribute("resultInsertTestingTrade", resultInsertTestingTrade);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/RsiPages/testing/ShowDbTradeResult.jsp");
                rd.forward(request, response);
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
