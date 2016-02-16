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
import org.luxion.ottzRserve.BeanRsi_test;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public class RsiTesting_Servlet extends HttpServlet {

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
        BeanRsi_test br_test = null;
        try {
            br_test = new BeanRsi_test();
            br_test.setBestS(request.getParameter("bestS"));
            br_test.setBestL(request.getParameter("bestL"));
            br_test.setRplotUrl(localPath + "/web/Rimage/RSI/");
            if("yahoo".equals(request.getParameter("datasource"))){ 
                br_test.setRDataSource(request.getParameter("testStockSymbol"), request.getParameter("testStartDate"), request.getParameter("testEndDate"));            
                request.setAttribute("testStartDatetime", request.getParameter("testStartDate"));
                request.setAttribute("testEndDatetime", request.getParameter("testEndDate"));
            }else if("db".equals(request.getParameter("datasource"))){
                br_test.setRDataSource(sc,request.getParameter("testStockSymbol"),request.getParameter("testStartDate"),request.getParameter("testStartTime"),request.getParameter("testEndDate"),request.getParameter("testEndTime"));
                request.setAttribute("testStartDatetime", request.getParameter("testStartDate") + " " + request.getParameter("testStartTime"));
                request.setAttribute("testEndDatetime", request.getParameter("testEndDate") + " " + request.getParameter("testEndTime"));
            }        
            br_test.excute();
            
            if(br_test != null){
                request.setAttribute("BestS", br_test.getBestS());
                request.setAttribute("BestL", br_test.getBestL());
                request.setAttribute("testStockSymbol", request.getParameter("testStockSymbol"));

                request.setAttribute("hasTrade", br_test.getHasTrade());
                request.setAttribute("testingCode", br_test.getTestingCode());
                request.setAttribute("revenue", br_test.getRevenuTrade());
                //trade list物件直接放入session 供之後operatorDB使用
                session.setAttribute("trade", br_test.getRecordTrade());
                request.setAttribute("RplotUrl", br_test.getRsiPictureURL());
                request.getRequestDispatcher("/WEB-INF/RsiPages/testing/displayRsi_test.jsp").forward(request, response);
            }
        }  catch (Exception ex) {
            throw new ServletException(ex);                
        }finally{
            //關閉Rconnection連結
            if(br_test!=null) try {
                br_test.closeRconn();
            } catch (RserveException ex) {
                throw new ServletException(ex); 
            }
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
