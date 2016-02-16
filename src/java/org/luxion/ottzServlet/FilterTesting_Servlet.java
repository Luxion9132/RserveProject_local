/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.luxion.ottzRserve.BeanFilter_test;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Leon
 */
public class FilterTesting_Servlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {     
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext sc = this.getServletConfig().getServletContext();
        String localPath = sc.getInitParameter("ottzRimagePath");
        
        HttpSession session =request.getSession();
        
        BeanFilter_test bf_test = null;
        try {
            bf_test = new BeanFilter_test();
            bf_test.setBestK(request.getParameter("bestK"));
            bf_test.setBestN(request.getParameter("bestN"));
            bf_test.setRplotUrl(localPath + "/web/Rimage/Filter/");
            if("yahoo".equals(request.getParameter("datasource"))){ 
                bf_test.setRDataSource(request.getParameter("testStockSymbol"), request.getParameter("testStartDate"), request.getParameter("testEndDate"));            
                request.setAttribute("testStartDatetime", request.getParameter("testStartDate"));
                request.setAttribute("testEndDatetime", request.getParameter("testEndDate"));
            }else if("db".equals(request.getParameter("datasource"))){
                bf_test.setRDataSource(sc,request.getParameter("testStockSymbol"),request.getParameter("testStartDate"),request.getParameter("testStartTime"),request.getParameter("testEndDate"),request.getParameter("testEndTime"));
                request.setAttribute("testStartDatetime", request.getParameter("testStartDate") + " " + request.getParameter("testStartTime"));
                request.setAttribute("testEndDatetime", request.getParameter("testEndDate") + " " + request.getParameter("testEndTime"));
            }        
            bf_test.excute();
        }  catch (Exception ex) {
            throw new ServletException(ex);                
        }finally{
            //關閉Rconnection連結
            if(bf_test!=null) try {
                bf_test.closeRconn();
            } catch (RserveException ex) {
                throw new ServletException(ex); 
            }
        }
        //test show
        if(bf_test != null){
            request.setAttribute("BestN", bf_test.getBestN());
            request.setAttribute("BestK", bf_test.getBestK());
            request.setAttribute("testStockSymbol", request.getParameter("testStockSymbol"));
            
            request.setAttribute("hasTrade", bf_test.getHasTrade());
            request.setAttribute("testingCode", bf_test.getTestingCode());
            request.setAttribute("revenue", bf_test.getRevenuTrade());
            //trade list物件直接放入session 供之後operatorDB使用
            session.setAttribute("trade", bf_test.getRecordTrade());
            request.setAttribute("RplotUrl", bf_test.getFilterPictureURL());
            request.getRequestDispatcher("/WEB-INF/FilterPages/testing/displayFilter_test.jsp").forward(request, response);
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
