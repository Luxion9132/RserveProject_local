/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.luxion.ottzRserve.BeanFilter_train;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public class FilterTraining_Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        BeanFilter_train bf_train = null;      
        try {
            bf_train = new BeanFilter_train(); 
            if("yahoo".equals(request.getParameter("datasource"))){ 
                //使用yahoofinance資料來源
                bf_train.setRDataSource(request.getParameter("trainStockSymbol"),request.getParameter("trainStartDate"),request.getParameter("trainEndDate"));
                request.setAttribute("trainStartDatetime", request.getParameter("trainStartDate"));
                request.setAttribute("trainEndDatetime", request.getParameter("trainEndDate"));
            }else if("db".equals(request.getParameter("datasource"))){
                ServletContext sc = this.getServletConfig().getServletContext();
                bf_train.setRDataSource(sc,request.getParameter("trainStockSymbol"),request.getParameter("trainStartDate"),request.getParameter("trainStartTime"),request.getParameter("trainEndDate"),request.getParameter("trainEndTime"));
                request.setAttribute("trainStartDatetime", request.getParameter("trainStartDate") + " " + request.getParameter("trainStartTime"));
                request.setAttribute("trainEndDatetime", request.getParameter("trainEndDate") + " " + request.getParameter("trainEndTime"));
            }
            bf_train.excute();
        }  catch (Exception ex) {
            throw new ServletException(ex);                
        }finally{
            //關閉Rconnection連結
            if(bf_train!=null) try {
                bf_train.closeRconn();
            } catch (RserveException ex) {
                throw new ServletException(ex); 
            }
        }
        String bestn = bf_train.getBestN();
        String bestk = bf_train.getBestK();
        request.setAttribute("Bestn", bestn);
        request.setAttribute("Bestk", bestk);
        request.getRequestDispatcher("/WEB-INF/FilterPages/training/displayFilter_train.jsp").forward(request, response);

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
