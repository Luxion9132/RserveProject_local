/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzServlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.luxion.ottzRserve.BeanMa_test;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public class MaTesting_Servlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        ServletContext sc = this.getServletConfig().getServletContext();
        String localPath = sc.getInitParameter("ottzRimagePath");
        
        HttpSession session =request.getSession();        
        BeanMa_test bm_test = null;
        try {
            bm_test = new BeanMa_test();
            bm_test.setBestS(request.getParameter("bestS"));
            bm_test.setBestL(request.getParameter("bestL"));
            bm_test.setRplotUrl(localPath + "/web/Rimage/MA/");
            if("yahoo".equals(request.getParameter("datasource"))){ 
                bm_test.setRDataSource(request.getParameter("testStockSymbol"), request.getParameter("testStartDate"), request.getParameter("testEndDate"));            
                request.setAttribute("testStartDatetime", request.getParameter("testStartDate"));
                request.setAttribute("testEndDatetime", request.getParameter("testEndDate"));
            }else if("db".equals(request.getParameter("datasource"))){
                bm_test.setRDataSource(sc,request.getParameter("testStockSymbol"),request.getParameter("testStartDate"),request.getParameter("testStartTime"),request.getParameter("testEndDate"),request.getParameter("testEndTime"));
                request.setAttribute("testStartDatetime", request.getParameter("testStartDate") + " " + request.getParameter("testStartTime"));
                request.setAttribute("testEndDatetime", request.getParameter("testEndDate") + " " + request.getParameter("testEndTime"));
            }        
            bm_test.excute();
            
            if(bm_test != null){
                request.setAttribute("BestS", bm_test.getBestS());
                request.setAttribute("BestL", bm_test.getBestL());
                request.setAttribute("testStockSymbol", request.getParameter("testStockSymbol"));

                request.setAttribute("hasTrade", bm_test.getHasTrade());
                request.setAttribute("testingCode", bm_test.getTestingCode());
                request.setAttribute("revenue", bm_test.getRevenuTrade());
                //trade list物件直接放入session 供之後operatorDB使用
                session.setAttribute("trade", bm_test.getRecordTrade());
                request.setAttribute("RplotUrl", bm_test.getMaPictureURL());
                request.getRequestDispatcher("/WEB-INF/MaPages/testing/displayMa_test.jsp").forward(request, response);
            }
        }  catch (Exception ex) {
            throw new ServletException(ex);                
        }finally{
            //關閉Rconnection連結
            if(bm_test!=null) try {
                bm_test.closeRconn();
            } catch (RserveException ex) {
                throw new ServletException(ex); 
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
