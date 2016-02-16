/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzServlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Leon
 */
public class TestingSelectGuide extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getParameter("method");
        String data = request.getParameter("data");
        
        request.setAttribute("data", request.getParameter("data"));
            
        if("Filter".equals(request.getParameter("method"))){
            if("yahooFinance".equals(request.getParameter("data")))
                request.getRequestDispatcher("/testing/filterTesting_yahoofinance.jsp").forward(request, response);
            else if("SqlServer".equals(request.getParameter("data")))
                request.getRequestDispatcher("/testing/filterTesting_db.jsp").forward(request, response);
        } 
        if("MA".equals(request.getParameter("method"))){
            if("yahooFinance".equals(request.getParameter("data")))
                request.getRequestDispatcher("/testing/maTesting_yahoofinance.jsp").forward(request, response);
            else if("SqlServer".equals(request.getParameter("data")))
                request.getRequestDispatcher("/testing/maTesting_db.jsp").forward(request, response);
        }
        if("RSI".equals(request.getParameter("method"))){
            if("yahooFinance".equals(request.getParameter("data")))
                request.getRequestDispatcher("/testing/rsiTesting_yahoofinance.jsp").forward(request, response);
            else if("SqlServer".equals(request.getParameter("data")))
                request.getRequestDispatcher("/testing/rsiTesting_db.jsp").forward(request, response);
            ;
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
