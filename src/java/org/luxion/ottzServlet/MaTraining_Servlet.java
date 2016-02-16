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
import org.luxion.ottzRserve.BeanMa_train;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public class MaTraining_Servlet extends HttpServlet {


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
        BeanMa_train bm_train = null;      
        try {
            bm_train = new BeanMa_train(); 
            if("yahoo".equals(request.getParameter("datasource"))){ 
                //使用yahoofinance資料來源
                bm_train.setRDataSource(request.getParameter("trainStockSymbol"),request.getParameter("trainStartDate"),request.getParameter("trainEndDate"));
                request.setAttribute("trainStartDatetime", request.getParameter("trainStartDate"));
                request.setAttribute("trainEndDatetime", request.getParameter("trainEndDate"));
            }else if("db".equals(request.getParameter("datasource"))){
                ServletContext sc = this.getServletConfig().getServletContext();
                bm_train.setRDataSource(sc,request.getParameter("trainStockSymbol"),request.getParameter("trainStartDate"),request.getParameter("trainStartTime"),request.getParameter("trainEndDate"),request.getParameter("trainEndTime"));
                request.setAttribute("trainStartDatetime", request.getParameter("trainStartDate") + " " + request.getParameter("trainStartTime"));
                request.setAttribute("trainEndDatetime", request.getParameter("trainEndDate") + " " + request.getParameter("trainEndTime"));
            }
            bm_train.excute();
        }  catch (Exception ex) {
            throw new ServletException(ex);                
        }finally{
            //關閉Rconnection連結
            if(bm_train!=null) try {
                bm_train.closeRconn();
            } catch (RserveException ex) {
                throw new ServletException(ex); 
            }
        }
        String bests = bm_train.getBestS();
        String bestl = bm_train.getBestL();
        request.setAttribute("Bests", bests);
        request.setAttribute("Bestl", bestl);
        request.getRequestDispatcher("/WEB-INF/MaPages/training/displayMa_train.jsp").forward(request, response);
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
