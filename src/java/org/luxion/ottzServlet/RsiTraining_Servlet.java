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
import org.luxion.ottzRserve.BeanRsi_train;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public class RsiTraining_Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BeanRsi_train br_train = null;      
        try {
            br_train = new BeanRsi_train(); 
            if("yahoo".equals(request.getParameter("datasource"))){ 
                //使用yahoofinance資料來源
                br_train.setRDataSource(request.getParameter("trainStockSymbol"),request.getParameter("trainStartDate"),request.getParameter("trainEndDate"));
                request.setAttribute("trainStartDatetime", request.getParameter("trainStartDate"));
                request.setAttribute("trainEndDatetime", request.getParameter("trainEndDate"));
            }else if("db".equals(request.getParameter("datasource"))){
                ServletContext sc = this.getServletConfig().getServletContext();
                br_train.setRDataSource(sc,request.getParameter("trainStockSymbol"),request.getParameter("trainStartDate"),request.getParameter("trainStartTime"),request.getParameter("trainEndDate"),request.getParameter("trainEndTime"));
                request.setAttribute("trainStartDatetime", request.getParameter("trainStartDate") + " " + request.getParameter("trainStartTime"));
                request.setAttribute("trainEndDatetime", request.getParameter("trainEndDate") + " " + request.getParameter("trainEndTime"));
            }
            br_train.excute();
        }  catch (Exception ex) {
            throw new ServletException(ex);                
        }finally{
            //關閉Rconnection連結
            if(br_train!=null) try {
                br_train.closeRconn();
            } catch (RserveException ex) {
                throw new ServletException(ex); 
            }
        }
        String bests = br_train.getBestS();
        String bestl = br_train.getBestL();
        request.setAttribute("Bests", bests);
        request.setAttribute("Bestl", bestl);
        request.getRequestDispatcher("/WEB-INF/RsiPages/training/displayRsi_train.jsp").forward(request, response);
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
